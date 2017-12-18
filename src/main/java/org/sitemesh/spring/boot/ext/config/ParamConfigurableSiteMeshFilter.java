package org.sitemesh.spring.boot.ext.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ObjectFactory;
import org.sitemesh.config.properties.PropertiesFilterConfigurator;
import org.sitemesh.config.xml.XmlFilterConfigurator;
import org.sitemesh.spring.boot.ext.builder.SpringBootSiteMeshFilterBuilder;
import org.sitemesh.spring.boot.ext.config.selector.ParamDecoratorSelector;
import org.sitemesh.webapp.WebAppContext;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


/**
 * 
 * @className	： ParamConfigurableSiteMeshFilter
 * @description	： 扩展实现注入基于request参数decorator值进行动态定位装饰器的选择器
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月18日 下午8:33:38
 * @version 	V1.0
 */
public class ParamConfigurableSiteMeshFilter extends ConfigurableSiteMeshFilter {

	// spring 资源路径匹配解析器
	// “classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	// “classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL>getResources(String name)”
	// 方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	protected ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

	public void init(FilterConfig filterConfig) throws ServletException {
		super.init(filterConfig);
	}

	protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
		super.applyCustomConfiguration(builder);
		// 获取原有默认配置装饰选择器
		DecoratorSelector<WebAppContext> defaultDecoratorSelector = builder.getDecoratorSelector();
		// 赋给自定义装饰选择器，则自定义规则未匹配时调用默认选择器获取
		builder.setCustomDecoratorSelector(new ParamDecoratorSelector(filterConfig, defaultDecoratorSelector));
	}

	protected Filter setup() throws ServletException {

		ObjectFactory objectFactory = getObjectFactory();
		SiteMeshFilterBuilder builder = new SpringBootSiteMeshFilterBuilder();

		new PropertiesFilterConfigurator(objectFactory, configProperties).configureFilter(builder);

		new XmlFilterConfigurator(getObjectFactory(), loadConfigXml(filterConfig, getConfigFileName()))
				.configureFilter(builder);

		applyCustomConfiguration(builder);

		return builder.create();
	}

	/**
	 * Load the XML config file. Will try a number of locations until it finds the
	 * file.
	 * 
	 * <pre>
	 * - Will first search for a file on disk relative to the root of the web-app.
	 * - Then a file with the absolute path.
	 * - Then a file as a resource in the ServletContext (allowing for files embedded in a .war file).
	 * - If none of those find the file, null will be returned.
	 * </pre>
	 */
	protected Element loadConfigXml(FilterConfig filterConfig, String configFilePath) throws ServletException {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = factory.newDocumentBuilder();

			xmlConfigFile = resolver.getResource(configFilePath).getFile();

			ServletContext servletContext = filterConfig.getServletContext();

			if (servletContext.getRealPath(configFilePath) != null) {
				xmlConfigFile = new File(servletContext.getRealPath(configFilePath));
			}

			if (xmlConfigFile.canRead()) {
				try {
					timestampOfXmlFileAtLastLoad = xmlConfigFile.lastModified();
					LOG.config("Loading SiteMesh 3 config file: " + xmlConfigFile.getAbsolutePath());
					Document document = documentBuilder.parse(xmlConfigFile);
					return document.getDocumentElement();
				} catch (SAXException e) {
					throw new ServletException("Could not parse " + xmlConfigFile.getAbsolutePath(), e);
				}
			} else {
				InputStream stream = servletContext.getResourceAsStream(configFilePath);
				if (stream == null) {
					LOG.config("No config file present - using defaults and init-params. Tried: "
							+ xmlConfigFile.getAbsolutePath() + " and ServletContext:" + configFilePath);
					return null;
				}
				try {
					LOG.config("Loading SiteMesh 3 config file from ServletContext " + configFilePath);
					Document document = documentBuilder.parse(stream);
					return document.getDocumentElement();
				} catch (SAXException e) {
					throw new ServletException("Could not parse " + configFilePath + " (loaded by ServletContext)", e);
				} finally {
					stream.close();
				}
			}

		} catch (IOException e) {
			throw new ServletException(e);
		} catch (ParserConfigurationException e) {
			throw new ServletException("Could not initialize DOM parser", e);
		}
	}

}
