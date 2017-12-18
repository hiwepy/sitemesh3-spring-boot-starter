package org.sitemesh.spring.boot.ext.config.selector;

import java.io.IOException;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;

import org.sitemesh.DecoratorSelector;
import org.sitemesh.config.PathBasedDecoratorSelector;
import org.sitemesh.content.Content;
import org.sitemesh.webapp.WebAppContext;
import org.springframework.util.StringUtils;
 	
/**
 * 
 * @className	： ParamDecoratorSelector
 * @description	： 基于request参数decorator值进行动态定位装饰器的选择器;
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月18日 下午8:41:05
 * @version 	V1.0
 */
public class ParamDecoratorSelector extends PathBasedDecoratorSelector<WebAppContext> {
 
	protected DecoratorSelector<WebAppContext> defaultDecoratorSelector;
	protected FilterConfig filterConfig;
	protected String path = "/WEB-INF/layouts/%s.jsp";
	
    public ParamDecoratorSelector(FilterConfig filterConfig,DecoratorSelector<WebAppContext> defaultDecoratorSelector) {
        this.defaultDecoratorSelector = defaultDecoratorSelector;
        this.filterConfig = filterConfig;
        this.path = filterConfig.getInitParameter("request.decorator.path");
    }
 
    public String[] selectDecoratorPaths(Content content, WebAppContext context) throws IOException {
    	
    	// build decorator based on the request
        HttpServletRequest request = context.getRequest();
        // 根据取值参数取值
        String decorator = request.getParameter("request.decorator.name");
        //有参数的情况
    	if (StringUtils.hasText(decorator)) {
            //按照参数值返回对应路径下面的装饰模板页码
    		//"/WEB-INF/layouts/" + decorator + ".jsp"
    		return new String[] { String.format(this.path, decorator) };
        }
        // Otherwise, fallback to the standard configuration
        return defaultDecoratorSelector.selectDecoratorPaths(content, context);
    }
    
	/**
	 * Make the name of this filter available to subclasses.
	 * Analogous to GenericServlet's <code>getServletName()</code>.
	 * <p>Takes the FilterConfig's filter name by default.
	 * If initialized as bean in a Spring application context,
	 * it falls back to the bean name as defined in the bean factory.
	 * @return the filter name, or <code>null</code> if none available
	 * @see javax.servlet.GenericServlet#getServletName()
	 * @see javax.servlet.FilterConfig#getFilterName()
	 * @see #setBeanName
	 */
	protected final String getFilterName() {
		return (this.filterConfig != null ? this.filterConfig.getFilterName() : this.getClass().getSimpleName());
	}
	
}