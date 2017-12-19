package org.sitemesh.spring.boot.ext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.SiteMeshFilter;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.sitemesh.webapp.contentfilter.Selector;
import org.springframework.web.servlet.ViewResolver;

public class SpringBootViewSiteMeshFilter extends SiteMeshFilter {

	private final ViewResolver viewResolver;
	private final ContentProcessor contentProcessor;
	private final boolean includeErrorPages;

	public SpringBootViewSiteMeshFilter(ViewResolver viewResolver, Selector selector, ContentProcessor contentProcessor,
			DecoratorSelector<WebAppContext> decoratorSelector, boolean includeErrorPages) {
		super(selector, contentProcessor, decoratorSelector, includeErrorPages);
		this.viewResolver = viewResolver;
		this.contentProcessor = contentProcessor;
		this.includeErrorPages = includeErrorPages;
	}

	/**
	 * Create a context for the current request. This method can be overriden to
	 * allow for other types of context.
	 */
	protected WebAppContext createContext(String contentType, HttpServletRequest request, HttpServletResponse response,
			ResponseMetaData metaData) {
		return new SpringBootViewContext(viewResolver, contentType, request, response, request.getServletContext(),
				contentProcessor, metaData, includeErrorPages);
	}

}