package org.sitemesh.spring.boot.ext;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

public class SpringBootViewContext extends WebAppContext {

	private final ViewResolver viewResolver;
	private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;
    private final Locale locale;
    
	protected SpringBootViewContext(ViewResolver viewResolver, String contentType, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext,
            ContentProcessor contentProcessor, ResponseMetaData metaData,
            boolean includeErrorPages) {
		super(contentType, request, response, servletContext, contentProcessor, metaData, includeErrorPages);
		this.viewResolver = viewResolver;
		this.request = request;
        this.response = response;
        this.servletContext = servletContext;
		this.locale = request.getLocale();
	}
	
	/**
	 * Dispatch to the actual path. This method can be overriden to provide
	 * different ways of dispatching (such as cross web-app).
	 */
	@Override
	protected void dispatch(HttpServletRequest request, HttpServletResponse response, String path)
			throws ServletException, IOException {
		try {
			View view = getViewResolver().resolveViewName(path, locale);
			if (view == null) {
				throw new ServletException("Not found: " + path);
			}
			view.render(null, request, response);

		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public ViewResolver getViewResolver() {
		return viewResolver;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public ServletContext getServletContext() {
		return servletContext;
	}

}
