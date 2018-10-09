package org.sitemesh.spring.boot.ext.content.tagrules.decorate;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.content.tagrules.TagRuleBundle;
import org.sitemesh.tagprocessor.State;

/**
 * {@code <sitemesh:children property='body:script'/>}
 */
public class ExtendDecoratorTagRuleBundle implements TagRuleBundle {

	@Override
	public void install(State defaultState, ContentProperty contentProperty,
			SiteMeshContext siteMeshContext) {
		
		 defaultState.addRule("sitemesh:children", new SiteMeshChildrenRule(siteMeshContext));
		 defaultState.addRule("sitemesh:scripts", new SiteMeshScriptRule(siteMeshContext));
		 defaultState.addRule("sitemesh:links", new SiteMeshLinkRule(siteMeshContext));
		 defaultState.addRule("sitemesh:styles", new SiteMeshStyleRule(siteMeshContext));
		 
	}

	@Override
	public void cleanUp(State defaultState, ContentProperty contentProperty,
			SiteMeshContext siteMeshContext) {

	}

}
