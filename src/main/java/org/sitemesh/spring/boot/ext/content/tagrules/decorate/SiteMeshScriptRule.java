package org.sitemesh.spring.boot.ext.content.tagrules.decorate;

import java.io.IOException;
import java.util.Iterator;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.spring.boot.utils.TagRuleBundleUtils;
import org.sitemesh.tagprocessor.BasicBlockRule;
import org.sitemesh.tagprocessor.Tag;

/**
 * 
 * @className	： SiteMeshScriptRule
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年12月18日 下午8:39:23
 * @version 	V1.0
 * @see	org.sitemesh.content.tagrules.html.MetaTagRule
 */
public class SiteMeshScriptRule extends BasicBlockRule<String>{
	
	private final SiteMeshContext siteMeshContext;

    public SiteMeshScriptRule(SiteMeshContext siteMeshContext) {
        this.siteMeshContext = siteMeshContext;
    }

    /*
     * <sitemesh:children property='body.footer:script'/>
     * <sitemesh:children property='body:script'/>
     */
    @Override
    protected String processStart(Tag tag) throws IOException {
        String propertyPath = tag.getAttributeValue("property", true);
        Content contentToMerge = siteMeshContext.getContentToMerge();
        if (contentToMerge != null) {
        	//根节点路径取值
        	String rootPath = TagRuleBundleUtils.getRootPath(propertyPath);
        	String tagName = TagRuleBundleUtils.getTagName(propertyPath);
        	//获取指定的根节点
        	ContentProperty targetProperty = TagRuleBundleUtils.getTargetProperty(contentToMerge, rootPath);
	    	if(targetProperty != null){
	    		//循环子节点
	    		Iterator<ContentProperty> childIterator = targetProperty.getChildren().iterator();
			 	while (childIterator.hasNext()) {
					ContentProperty contentProperty = childIterator.next();
					if(contentProperty.getName().equalsIgnoreCase(tagName)){
						contentProperty.writeValueTo(tagProcessorContext.currentBuffer());
					}
				}
	    	}
        }
        tagProcessorContext.pushBuffer();
        return null;
    }

    @Override
    protected void processEnd(Tag tag, String property) throws IOException {
        CharSequence defaultContents = tagProcessorContext.currentBufferContents();
        tagProcessorContext.popBuffer();
        if (siteMeshContext.getContentToMerge() == null) {
            tagProcessorContext.currentBuffer().append(defaultContents);
        }
    }

}
