package org.sitemesh.spring.boot.ext.content;

import java.io.IOException;

import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.spring.boot.utils.TagRuleBundleUtils;

public class ChildrenContentProperty implements ContentProperty {

	private ContentProperty current;
	private String propertyPath;
	
	public ChildrenContentProperty(ContentProperty property,String propertyPath) {
		this.current = property;
		this.propertyPath = propertyPath;
	}
	
	@Override
	public String getName() {
		return current.getName();
	}

	@Override
	public ContentProperty[] getFullPath() {
		return current.getFullPath();
	}

	@Override
	public ContentProperty getParent() {
		return current.getParent();
	}

	@Override
	public boolean hasChildren() {
		return current.hasChildren();
	}

	@Override
	public boolean hasChild(String name) {
		return current.hasChild(name);
	}

	@Override
	public ContentProperty getChild(String name) {
		return current.getChild(name);
	}

	@Override
	public Iterable<ContentProperty> getChildren() {
		return TagRuleBundleUtils.getChildren(current, propertyPath);
	}

	@Override
	public Iterable<ContentProperty> getDescendants() {
		return current.getDescendants();
	}

	@Override
	public boolean hasValue() {
		return current.hasValue();
	}

	@Override
	public String getValue() {
		return current.getValue();
	}
 
	@Override
	public String getNonNullValue() {
		return current.getNonNullValue();
	}

	@Override
	public void writeValueTo(Appendable out) throws IOException {
		current.writeValueTo(out);
	}

	@Override
	public void setValue(CharSequence value) {
		current.setValue(value);
	}

	@Override
	public Content getOwningContent() {
		return current.getOwningContent();
	}

	/**
	 * @return the current ContentProperty
	 */
	public ContentProperty getCurrentProperty() {
		return current;
	}

}
