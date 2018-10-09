package org.sitemesh.spring.boot.ext.content.tagrules.html;

import java.io.IOException;
import java.util.Iterator;

import org.sitemesh.SiteMeshContext;
import org.sitemesh.content.ContentProperty;
import org.sitemesh.spring.boot.ext.content.ChildrenContentProperty;
import org.sitemesh.tagprocessor.BasicBlockRule;
import org.sitemesh.tagprocessor.Tag;
import org.sitemesh.tagprocessor.util.CharSequenceList;

public class ExportChildTagToContentRule extends BasicBlockRule<Object> {

	protected final ChildrenContentProperty targetProperty;
	protected final boolean includeInContent;
	protected final SiteMeshContext context;

    /*
     * @param targetProperty   ContentProperty to export tag contents to.
     * @param includeInContent Whether the tag should be included in the content (if false, it will be stripped
     *                         from the current ContentProperty that is being written to.
     */
    public ExportChildTagToContentRule(SiteMeshContext context, ChildrenContentProperty targetProperty, boolean includeInContent) {
        this.targetProperty = targetProperty;
        this.includeInContent = includeInContent;
        this.context = context;
    }

    @Override
    protected Object processStart(Tag tag) throws IOException {
        // Some terminology:
        // Given a tag: '<foo>hello</foo>'
        // INNER contents refers to 'hello'
        // OUTER contents refers to '<foo>hello</foo>'
    	
    	String tagName = tag.getName();
    	String rewrite = tag.getAttributeValue("rewrite", true);
    	String compress = tag.getAttributeValue("compress", true);
    	
    	
        Tag t = tag;
        
        

        Iterator<ContentProperty> childPropertyIterator = targetProperty.getChildren().iterator();
        CharSequenceList charSequenceList = new CharSequenceList();
        while (childPropertyIterator.hasNext()) {
			ContentProperty contentProperty = childPropertyIterator.next();
			charSequenceList.append(contentProperty.getOwningContent().createDataOnlyBuffer());
		}

		// Push a buffer for the OUTER contents.
		if (!includeInContent) {
			// If the tag should NOT be included in the contents, we use a
			// data-only buffer,
			// which means that although the contents won't be written
			// back to the ContentProperty, they will be available in the main
			// Content data.
			// See Content.createDataOnlyBuffer()
			tagProcessorContext.pushBuffer(charSequenceList);
		} else {
			tagProcessorContext.pushBuffer();
		}
        // Write opening tag to OUTER buffer.
        t.writeTo(tagProcessorContext.currentBuffer());

        // Push a new buffer for storing the INNER contents.
        tagProcessorContext.pushBuffer();
        return null;
    }

    @Override
    protected void processEnd(Tag tag, Object data) throws IOException {
        // Get INNER content, and pop the buffer for INNER contents.
        CharSequence innerContent = tagProcessorContext.currentBufferContents();
        tagProcessorContext.popBuffer();

        // Write the INNER content and closing tag, to OUTER buffer and pop it.
        tagProcessorContext.currentBuffer().append(innerContent);
        if (tag.getType() != Tag.Type.EMPTY) { // if the tag is empty we have already written it in processStart().
            tag.writeTo(tagProcessorContext.currentBuffer());
        }
        CharSequence outerContent = tagProcessorContext.currentBufferContents();
        tagProcessorContext.popBuffer();

        // Write the OUTER contents to the current buffer, which is now the buffer before the
        // tag was processed. Note that if !includeInContent, this buffer will not be written
        // to the ContentProperty (though it will be available in Content.getData()).
        // See comment in processStart().
        tagProcessorContext.currentBuffer().append(outerContent);

        // Export the tag's inner contents to
        if (!targetProperty.hasValue()) {
            targetProperty.setValue(innerContent);
        }
    }
    
}