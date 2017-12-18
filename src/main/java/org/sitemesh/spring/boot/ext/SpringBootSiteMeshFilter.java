package org.sitemesh.spring.boot.ext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.SiteMeshFilter;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.sitemesh.webapp.contentfilter.Selector;

public class SpringBootSiteMeshFilter extends SiteMeshFilter {

	protected final Selector selector;
	protected final ContentProcessor contentProcessor;
	protected final DecoratorSelector<WebAppContext> decoratorSelector;
	protected final boolean includeErrorPages;

	public SpringBootSiteMeshFilter(Selector selector, ContentProcessor contentProcessor,
			DecoratorSelector<WebAppContext> decoratorSelector, boolean includeErrorPages) {
		super(selector, contentProcessor, decoratorSelector, includeErrorPages);
		this.selector = selector;
		this.contentProcessor = contentProcessor;
		this.decoratorSelector = decoratorSelector;
		this.includeErrorPages = includeErrorPages;
	}

	/**
	 * Create a context for the current request. This method can be overriden to
	 * allow for other types of context.
	 */
	protected WebAppContext createContext(String contentType, HttpServletRequest request, HttpServletResponse response,
			ResponseMetaData metaData) {
		return new SpringBootWebAppContext(contentType, request, response, request.getServletContext(),
				contentProcessor, metaData, includeErrorPages);
	}

}
