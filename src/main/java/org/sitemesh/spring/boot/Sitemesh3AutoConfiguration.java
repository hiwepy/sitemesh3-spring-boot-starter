package org.sitemesh.spring.boot;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.spring.boot.ext.config.ParamConfigurableSiteMeshFilter;
import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({ SiteMeshContext.class })
@ConditionalOnProperty(prefix = Sitemesh3Properties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ Sitemesh3Properties.class })
public class Sitemesh3AutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
	@Bean
	public FilterRegistrationBean siteMeshFilter() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		registrationBean.setFilter(new ParamConfigurableSiteMeshFilter());
		registrationBean.addUrlPatterns("/apimgr/*"); // 设置初始参数

		// 是否每个请求都重新加载配置文件：默认 true,当值为true且配置文件有改动则会重新加载配置文件
		registrationBean.addInitParameter("autoReload", "true"); // 基于XML的配置文件路径
		registrationBean.addInitParameter("configFile", "classpath:sitemesh3.xml");
		// 用于获取装饰器名称参数的取值Key
		registrationBean.addInitParameter("request.decorator.name", "layout"); //
		// 不同参数对应的装饰页信息
		registrationBean.addInitParameter("request.decorator.path", "/templates/layout/%s.ftl");

		return registrationBean;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}
}
