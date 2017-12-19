package org.sitemesh.spring.boot.ext.builder;

import javax.servlet.Filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.spring.boot.ext.SpringBootSiteMeshFilter;
import org.sitemesh.spring.boot.ext.SpringBootViewSiteMeshFilter;
import org.springframework.web.servlet.ViewResolver;

public class SpringBootSiteMeshFilterBuilder extends SiteMeshFilterBuilder {

	/**
	 * Create the SiteMesh Filter.
	 */
	public Filter create() {
		return new SpringBootSiteMeshFilter(
				getSelector(), 
				getContentProcessor(),
				getDecoratorSelector(), 
				isIncludeErrorPages());
	}

	/**
	 * Create the SiteMesh Filter.
	 */
	public Filter create(ViewResolver viewResolver) {
		return new SpringBootViewSiteMeshFilter(
				viewResolver, 
				getSelector(), 
				getContentProcessor(),
				getDecoratorSelector(), 
				isIncludeErrorPages());
	}

}
