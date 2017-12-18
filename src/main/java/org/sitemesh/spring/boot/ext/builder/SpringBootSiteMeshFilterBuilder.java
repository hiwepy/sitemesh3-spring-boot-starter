package org.sitemesh.spring.boot.ext.builder;

import javax.servlet.Filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.spring.boot.ext.SpringBootSiteMeshFilter;

public class SpringBootSiteMeshFilterBuilder extends SiteMeshFilterBuilder {

	/**
	 * Create the SiteMesh Filter.
	 */
	public Filter create() {
		return new SpringBootSiteMeshFilter(getSelector(), getContentProcessor(), getDecoratorSelector(),
				isIncludeErrorPages());
	}

}
