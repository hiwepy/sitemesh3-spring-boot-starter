package org.sitemesh.spring.boot.ext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProcessor;
import org.sitemesh.webapp.WebAppContext;
import org.sitemesh.webapp.contentfilter.ResponseMetaData;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class SpringBootContext extends WebAppContext {

	/*
	 * spring 资源路径匹配解析器;
	 * “classpath”	: 用于加载类路径（包括jar包）中的一个且仅一个资源；对于多个匹配的也只返回一个，所以如果需要多个匹配的请考虑“classpath*:”前缀
	 * “classpath*” : 用于加载类路径（包括jar包）中的所有匹配的资源。带通配符的classpath使用“ClassLoader”的“Enumeration<URL> getResources(String name)” 方法来查找通配符之前的资源，然后通过模式匹配来获取匹配的资源。
	 */
	private final ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
	private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ServletContext servletContext;
    private final Charset encoding;
    
	protected SpringBootContext(String contentType, HttpServletRequest request,
            HttpServletResponse response, ServletContext servletContext,
            ContentProcessor contentProcessor, ResponseMetaData metaData,
            boolean includeErrorPages) {
		super(contentType, request, response, servletContext, contentProcessor, metaData, includeErrorPages);
		this.request = request;
        this.response = response;
        this.servletContext = servletContext;
		this.encoding = Charset.forName(request.getCharacterEncoding());
	}
	
	@Override
	protected void decorate(String decoratorPath, Content content, Writer out) throws IOException {
		Resource resource = resolver.getResource(decoratorPath);
		if (resource.exists()) {
			out.append(load(resource));
		}
	}

	public CharBuffer load(Resource resource) throws IOException {
		Reader reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), encoding));
		try {
			CharBuffer contents = CharBuffer.allocate((int) resource.contentLength());
			reader.read(contents);
			contents.flip();
			return contents;
		} finally {
			reader.close();
		}
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
