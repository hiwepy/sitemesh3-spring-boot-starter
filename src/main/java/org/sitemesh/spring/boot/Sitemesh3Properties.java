package org.sitemesh.spring.boot;

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

}