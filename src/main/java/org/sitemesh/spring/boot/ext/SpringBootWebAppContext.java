package org.sitemesh.spring.boot.ext;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringBootWebAppContext extends WebAppContext {

	//spring 资源路径匹配解析器
	//“classpath”： 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	//“classpath*”： 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)”
	//方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	protected static ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	
	protected final String contentType;
	protected final HttpServletRequest request;
	protected final HttpServletResponse response;
	protected final ServletContext servletContext;
	protected final ResponseMetaData metaData;
	protected final boolean includeErrorPages;
    
	public SpringBootWebAppContext(String contentType, HttpServletRequest request, HttpServletResponse response,
			ServletContext servletContext, ContentProcessor contentProcessor, ResponseMetaData metaData,
			boolean includeErrorPages) {
		super(contentType, request, response, servletContext, contentProcessor, metaData, includeErrorPages);
		 this.contentType = contentType;
	        this.request = request;
	        this.response = response;
	        this.servletContext = servletContext;
	        this.metaData = metaData;
	        this.includeErrorPages = includeErrorPages;
	}
	
	/**
     * Dispatch to the actual path. This method can be overriden to provide different ways of dispatching
     * (such as cross web-app).
     */
    protected void dispatch(HttpServletRequest request, HttpServletResponse response, String path)
            throws ServletException, IOException {
    	
        RequestDispatcher dispatcher = servletContext.getRequestDispatcher(resolver.getResource(path).getFile().getAbsolutePath());
        if (dispatcher == null) {
            throw new ServletException("Not found: " + path);
        }
        dispatcher.forward(request, response);
    }

}
