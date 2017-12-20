package org.sitemesh.spring.boot.ext.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ObjectFactory;
import org.sitemesh.config.properties.PropertiesFilterConfigurator;
import org.sitemesh.config.xml.XmlFilterConfigurator;
import org.sitemesh.spring.boot.Sitemesh3Properties;
import org.sitemesh.spring.boot.ext.builder.SpringBootSiteMeshFilterBuilder;
import org.sitemesh.spring.boot.ext.config.selector.ParamDecoratorSelector;
import org.sitemesh.spring.boot.utils.StringUtils;
import org.sitemesh.webapp.WebAppContext;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.ViewResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * 
 * @className ： ParamConfigurableSiteMeshFilter
 * @description ： 扩展实现注入基于request参数decorator值进行动态定位装饰器的选择器
 * @author ： <a href="https://github.com/vindell">vindell</a>
 * @date ： 2017年12月18日 下午8:33:38
 * @version V1.0
 */
public class ParamConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {

	/*
	 * spring 资源路径匹配解析器;
	 * “classpath”	: 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	 * “classpath*” : 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)” 方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	 */
	private ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	private ApplicationContext applicationContext;
	private Sitemesh3Properties properties;
	private Resource xmlConfigResource;
	
	public ParamConfigurableSiteMeshFilter(ApplicationContext applicationContext, Sitemesh3Properties properties) {
		this.applicationContext = applicationContext;
		this.properties = properties;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}

	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		
		// 优先初始化Spring Boot配置文件中的装饰映射配置
		if (!CollectionUtils.isEmpty(properties.getMapping())) {

			Iterator<Entry<String, String>> ite = properties.getMapping().entrySet().iterator();
			while (ite.hasNext()) {
				Entry<String, String> entry = ite.next();
				if (StringUtils.hasText(entry.getValue())) {
					String contentPath = StringUtils.hasText(entry.getKey()) ? entry.getKey() : "/*";
					builder.addDecoratorPaths(contentPath, StringUtils.tokenizeToStringArray(entry.getValue()));
				}
			}
		}
		
		// 扩展参数指定布局文件
		if (properties.isParamLayout()) {
			// 获取原有默认配置装饰选择器
			DecoratorSelector<WebAppContext> defaultDecoratorSelector = builder.getDecoratorSelector();
			// 赋给自定义装饰选择器，则自定义规则未匹配时调用默认选择器获取
			builder.setCustomDecoratorSelector(new ParamDecoratorSelector(filterConfig, defaultDecoratorSelector));
		}
		
		super.applyCustomConfiguration(builder);
	}

	protected Filter setup() throws ServletException {

		ObjectFactory objectFactory = getObjectFactory();
		SpringBootSiteMeshFilterBuilder builder = new SpringBootSiteMeshFilterBuilder();
		
		configProperties.put(PropertiesFilterConfigurator.EXCLUDE_PARAM, properties.getExclude());
		configProperties.put(PropertiesFilterConfigurator.MIME_TYPES_PARAM, properties.getMimeTypes());
		configProperties.put(PropertiesFilterConfigurator.INCLUDE_ERROR_PAGES_PARAM, Boolean.toString(properties.isIncludeErrorPages()));
		configProperties.put(PropertiesFilterConfigurator.DECORATOR_SELECTOR, properties.getDecoratorSelector());
		
		new PropertiesFilterConfigurator(objectFactory, configProperties).configureFilter(builder);

		new XmlFilterConfigurator(getObjectFactory(), loadConfigXml(filterConfig, getConfigFileName()))
				.configureFilter(builder);

		applyCustomConfiguration(builder);

		try {
			ViewResolver viewResolver = getApplicationContext().getBean(properties.getViewResolver(), ViewResolver.class);
			return builder.create(viewResolver);
		} catch (Exception e) {
			LOG.warning(e.getCause().getMessage());
			return builder.create();
		}
		
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		// 将前端参数全部传递到下一步请求
		Map<String, String[]> params = servletRequest.getParameterMap();
		for (String key : params.keySet()) {
			servletRequest.setAttribute(key, params.get(key));
		}
		super.doFilter(servletRequest, servletResponse, filterChain);
	}

	@Override
	protected boolean reloadRequired() {
		try {
			return xmlConfigResource != null && timestampOfXmlFileAtLastLoad != xmlConfigResource.lastModified();
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * Load the XML config file. Will try a number of locations until it finds the
	 * file.
	 * 
	 * <pre>
	 * - Will first search for a file on disk relative to the root of the web-app.
	 * - Then a file with the absolute path.
	 * - Then a file as a resource in the classpath.
	 * - If none of those find the file, null will be returned.
	 * </pre>
	 */
	@Override
	protected Element loadConfigXml(FilterConfig filterConfig, String configFilePath) throws ServletException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();

			xmlConfigResource = resolver.getResource(configFilePath);
			if (xmlConfigResource != null && xmlConfigResource.exists()) {
				timestampOfXmlFileAtLastLoad = xmlConfigResource.lastModified();
				try {
					xmlConfigFile = xmlConfigResource.getFile();
					LOG.config("Loading SiteMesh 3 config file: " + xmlConfigFile.getAbsolutePath());
					Document document = documentBuilder.parse(xmlConfigFile);
					return document.getDocumentElement();
				} catch (SAXException e) {
					throw new ServletException("Could not parse " + xmlConfigFile.getAbsolutePath(), e);
				}
			} else if ( xmlConfigResource != null && xmlConfigResource.isReadable()) {
				
				timestampOfXmlFileAtLastLoad = xmlConfigResource.lastModified();
				InputStream stream = xmlConfigResource.getInputStream();
				if (stream == null) {
					LOG.config("No config file present - using defaults and init-params. Tried: ServletContext:" + configFilePath);
					return null;
				}
				try {
					LOG.config("Loading SiteMesh 3 config file from Classpath: " + configFilePath);
					Document document = documentBuilder.parse(stream);
					return document.getDocumentElement();
				} catch (SAXException e) {
					throw new ServletException("Could not parse " + configFilePath + " (loaded by ServletContext)", e);
				} finally {
					stream.close();
				}
			}
			return null;
		} catch (IOException e) {
			throw new ServletException(e);
		} catch (ParserConfigurationException e) {
			throw new ServletException("Could not initialize DOM parser", e);
		}
	}

	public ResourcePatternResolver getResolver() {
		return resolver;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
