package org.sitemesh.spring.boot;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(Sitemesh3Properties.PREFIX)
public class Sitemesh3Properties {

	public static final String PREFIX = "sitemesh3";

	/** Sitemesh3 是否可用 */
	protected boolean enabled = false;
	/** 处理器链定义 */
	protected String urlPatterns = "/*";
	/** 参数布局 是否可用 */
	protected boolean paramLayout = false;
	/** 用于获取装饰器名称参数的取值Key */
	protected String paramName = "layout";
	/** 是否每个请求都重新加载配置文件：默认 true,当值为true且配置文件有改动则会重新加载配置文件 */
	protected boolean autoReload = false;
	/** 基于XML的配置文件路径 */
	protected String configFile = "classpath:sitemesh3.xml";
	/** 不同参数对应的装饰页文件路径 */
	protected String layoutFile = "classpath:templates/layout/%s.ftl";
	/** 视图解析器名称 */
	protected String viewResolver = "viewResolver";
	/** Add a path to be excluded by SiteMesh. */
    protected String exclude = null;
    /** 
     * Set MIME types that the Filter should intercept. The default is {"text/html"}. 
	 * Note: The MIME types are ignored if setCustomSelector(Selector) is called.
     */
    protected String mimeTypes = "mimeTypes";
    /** Set if the error pages should be decorated as well. The default is false.  */
    protected boolean includeErrorPages = false;
    /** 
     * Set a custom DecoratorSelector. If called and decorator selector is not instance of PathBasedDecoratorSelector, 
     *  this will override any paths added with addDecoratorPath(String, String) and instead delegate to the custom DecoratorSelector.
     */
    protected String decoratorSelector = null;
    /** 装饰规则 */
    protected Map<String /* path */, String /* decorator */> mapping = new LinkedHashMap<String, String>();
    
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(String urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	public boolean isParamLayout() {
		return paramLayout;
	}

	public void setParamLayout(boolean paramLayout) {
		this.paramLayout = paramLayout;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public boolean isAutoReload() {
		return autoReload;
	}

	public void setAutoReload(boolean autoReload) {
		this.autoReload = autoReload;
	}

	public String getConfigFile() {
		return configFile;
	}

	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}

	public String getLayoutFile() {
		return layoutFile;
	}

	public void setLayoutFile(String layoutFile) {
		this.layoutFile = layoutFile;
	}

	public String getViewResolver() {
		return viewResolver;
	}

	public void setViewResolver(String viewResolver) {
		this.viewResolver = viewResolver;
	}

	public String getExclude() {
		return exclude;
	}

	public void setExclude(String exclude) {
		this.exclude = exclude;
	}

	public String getMimeTypes() {
		return mimeTypes;
	}

	public void setMimeTypes(String mimeTypes) {
		this.mimeTypes = mimeTypes;
	}

	public boolean isIncludeErrorPages() {
		return includeErrorPages;
	}

	public void setIncludeErrorPages(boolean includeErrorPages) {
		this.includeErrorPages = includeErrorPages;
	}

	public String getDecoratorSelector() {
		return decoratorSelector;
	}

	public void setDecoratorSelector(String decoratorSelector) {
		this.decoratorSelector = decoratorSelector;
	}

	public Map<String, String> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, String> mapping) {
		this.mapping = mapping;
	}

}