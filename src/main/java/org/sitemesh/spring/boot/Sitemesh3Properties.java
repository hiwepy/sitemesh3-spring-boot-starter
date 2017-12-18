package org.sitemesh.spring.boot;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(Sitemesh3Properties.PREFIX)
public class Sitemesh3Properties {

	public static final String PREFIX = "sitemesh3";

	protected Boolean enabled = false;
	
	/**
	 * 处理器链定义
	 */
	private String urlPatterns;
	private String definitions = null;
	private Map<String /* rule */, String /* handler names */> definitionMap = new LinkedHashMap<String, String>();
	
	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public String getUrlPatterns() {
		return urlPatterns;
	}

	public void setUrlPatterns(String urlPatterns) {
		this.urlPatterns = urlPatterns;
	}

	public String getDefinitions() {
		return definitions;
	}

	public void setDefinitions(String definitions) {
		this.definitions = definitions;
	}

	public Map<String, String> getDefinitionMap() {
		return definitionMap;
	}

	public void setDefinitionMap(Map<String, String> definitionMap) {
		this.definitionMap = definitionMap;
	}
	
}
