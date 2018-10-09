package org.sitemesh.spring.boot.ext.content.tagrules.html;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.content.tagrules.html.ExportTagToContentRule;
import org.sitemesh.tagprocessor.State;
/**
 * 
 * 扩展sitemash标签规则支持
 * <pre>
 *  在BaseSiteMeshBuilder对象的setupDefaults可见如下代码：
 *  protected void setupDefaults() {
 *      addTagRuleBundles(new CoreHtmlTagRuleBundle(), new DecoratorTagRuleBundle());
 *  }
 *  通过CoreHtmlTagRuleBundle,DecoratorTagRuleBundle可得知：
 *  Sitemesh 3 默认只提供了 head,title,body,meta 等 tag 类型，我们可以通过实现 TagRuleBundle 扩展自定义的 tag 规则
 * </pre> 
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @see	org.sitemesh.content.tagrules.html.CoreHtmlTagRuleBundle
 */
public class ExtendHtmlTagRuleBundle implements TagRuleBundle {

	
	/*
	 * 这里的contentProperty等同于html元素的Document对象
	 */
	@Override
	public void install(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
		
		defaultState.addRule("header", new ExportTagToContentRule(siteMeshContext ,contentProperty.getChild("header"), true));
		defaultState.addRule("footer", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("footer"), true));
		defaultState.addRule("html", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("html"), false));
		 
		defaultState.addRule("script", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("script"), true));
		defaultState.addRule("link", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("link"), true));
		defaultState.addRule("style", new ExportTagToContentRule(siteMeshContext, contentProperty.getChild("style"), true));
		
	}

	@Override
	public void cleanUp(State defaultState, ContentProperty contentProperty, SiteMeshContext siteMeshContext) {
		if (!contentProperty.getChild("body").hasValue()) {
            contentProperty.getChild("body").setValue(contentProperty.getValue());
        }
	}

}
