package org.sitemesh.spring.boot.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.Content;
import org.sitemesh.content.ContentProperty;

public abstract class TagRuleBundleUtils {

	//div[id='222']
	private static Pattern pattern_tag = Pattern.compile("(?:(\\w+)\\[([^\\[\\]]*?)\\])+");
	
	public static boolean isMatch(ContentProperty property,String tagPath){
		boolean isMatch = false;
		Matcher matcher = pattern_tag.matcher(tagPath);
		if(matcher.matches()){
			String tagName = matcher.group(1);
			String selector = matcher.group(2);
			//property.getName()
			
		}
		return isMatch;
	}
	
	public static String getRootPath(String propertyPath) {
		//body:scripts
    	String[] propertyPaths = propertyPath.split(":");
    	//根节点路径取值
    	String rootPath = propertyPaths.length == 2 ? propertyPaths[0] : null ;
    	return rootPath;
    }
	
	public static String getTagName(String propertyPath) {
		//body:script
    	String[] propertyPaths = propertyPath.split(":");
    	//根节点路径取值
    	String tagName = propertyPaths.length == 2 ? propertyPaths[1] : propertyPaths[0] ;
    	return tagName;
    }
	
	/*
	 * 原意：The ContentProperty of the document being merged in to the decorator.
	 * 被合并到装饰模板的文档对象的ContentProperty对象实体：这里应指的是真实页面
	 */
	public static ContentProperty getTargetProperty(Content contentToMerge, String rootPath) {
		if (contentToMerge != null) {
			ContentProperty currentProperty = contentToMerge.getExtractedProperties();
	    	if ( rootPath != null) {
	    		for (String childPropertyName : rootPath.split("\\.")) {
		           currentProperty = currentProperty.getChild(childPropertyName);
		        }
	    	}
	        return currentProperty;
        }
		return null;
    }
	
	//html.body:scripts
	public static ContentProperty getTargetProperty(SiteMeshContext context, String rootPath) {
		/*
		 * 原意：The ContentProperty of the document being merged in to the decorator.
		 * 被合并到装饰模板的文档对象的ContentProperty对象实体：这里应指的是真实页面
		 */
		Content contentToMerge = context.getContentToMerge();
		if (contentToMerge != null) {
			ContentProperty currentProperty = contentToMerge.getExtractedProperties();
	    	if ( rootPath != null) {
	    		for (String childPropertyName : rootPath.split("\\.")) {
		           currentProperty = currentProperty.getChild(childPropertyName);
		        }
	    	}
	        return currentProperty;
        }
		return null;
    }
	
	
	public static Iterable<ContentProperty> getChildren(SiteMeshContext context,String propertyPath) {
		List<ContentProperty> resultIterable = new LinkedList<ContentProperty>();
		if (propertyPath != null) {
	    	String tagName = getTagName(propertyPath);
	    	//获取指定的根节点
	    	ContentProperty targetProperty = getTargetProperty(context, getRootPath(propertyPath));
	    	if(targetProperty != null){
	    		Iterator<ContentProperty> childIterator = targetProperty.getChildren().iterator();
			 	while (childIterator.hasNext()) {
					ContentProperty contentProperty = childIterator.next();
					if(contentProperty.getName().equalsIgnoreCase(tagName)){
						resultIterable.add(contentProperty);
					}
				}
	    	}
        }
		return resultIterable;
	}
	
	/*
	 * 获取类似body.div[id='222'] 当前节点下的指定根节点
	 */
	public static ContentProperty getTargetProperty(ContentProperty property, String rootPath) {
    	ContentProperty currentProperty = property;
    	if ( rootPath != null) {
    		for (String childPropertyName : rootPath.split("\\.")) {
	           currentProperty = currentProperty.getChild(childPropertyName);
	        }
    	}
        return currentProperty;
    }
    
	public static Iterable<ContentProperty> getChildren(ContentProperty property,String propertyPath) {
		List<ContentProperty> resultIterable = new LinkedList<ContentProperty>();
		if (propertyPath != null) {
			String tagName = getTagName(propertyPath);
	    	//获取指定的根节点
	    	ContentProperty targetProperty = getTargetProperty(property, getRootPath(propertyPath));
	    	if(targetProperty != null){
	    		Iterator<ContentProperty> childIterator = targetProperty.getChildren().iterator();
			 	while (childIterator.hasNext()) {
					ContentProperty contentProperty = childIterator.next();
					if(contentProperty.getName().equalsIgnoreCase(tagName)){
						resultIterable.add(contentProperty);
					}
				}
	    	}
		}
	 	return resultIterable;
	}
	
	
}
