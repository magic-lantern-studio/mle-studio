// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2020 Wizzer Works
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.ui;

// Import Eclipse packages.
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.IColorProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * Label provider for the <code>AttributeTreeViewer</code>.
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class AttributeTreeLabelProvider extends LabelProvider implements ITableLabelProvider,IColorProvider
{
	// Names of images used to represent whether an attribute has been modified since
	// the last save.
	
	/** Used to denote that an image has been modified. */
	public static final String MODIFIED_IMAGE 	 = "modified";
	/** Used to denote that an image has not been modified. */
	public static final String UNMODIFIED_IMAGE  = "unmodified";

	// For the "attribute modified" images.
	private static ImageRegistry m_imageRegistry = new ImageRegistry();

	/*
	 * Note: An image registry owns all of the image objects registered with it,
	 * and automatically disposes of them when the SWT Display is disposed.
	 */ 
	static {
		String iconPath = "icons/"; 
		m_imageRegistry.put(MODIFIED_IMAGE, ImageDescriptor.createFromFile(
				AttributeTreeViewer.class, 
				iconPath + MODIFIED_IMAGE + ".gif"
				)
			);
		m_imageRegistry.put(UNMODIFIED_IMAGE, ImageDescriptor.createFromFile(
				AttributeTreeViewer.class, 
				iconPath + UNMODIFIED_IMAGE + ".gif"
				)
			);	
	}
	
	/*
	 * Get the image for the modified label.
	 * 
	 * @param isModified Determines which image to retrieve. If <b>true</c>, then
	 * the <b>MODIFIED_IMAGE</b> will be returned. Otherwise, the <b>UNMODIFIED_IMAGE</b> is 
	 * returned.
	 * 
	 * @return A reference to an <code>Image</code> with the given key is returned,
	 * or <code>null</code> if not found.
	 */
	private Image getImage(boolean isModified)
	{
		String key = isModified ? MODIFIED_IMAGE : UNMODIFIED_IMAGE;
		return m_imageRegistry.get(key);
	}

	/**
	 * Get the <code>String</code> associated with the specified column.
	 * 
	 * @param element The <code>Attribute</code>.
	 * @param columnIndex The column index.
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex)
	{
		String result = "";
		Attribute attr = (Attribute) element;
		
		switch (columnIndex) {
			case 0:  // ATTRIBUTE_NAME_COLUMN
				if (attr.getType() == Attribute.TYPE_VARIABLE_LIST) {
					result = "[" + attr.getName() + "]...";
				} else {
			    	result = attr.getName();
				}
			    break;
			case 1:  // ATTRIBUTE_VALUE_COLUMN
			    result = attr.getStringValue();
			    /*
			    if (attr.getChildCount() == 0) {
			        // A leaf node.
			        result = attr.getStringValue();
			    } else {
			    	if (attr.getType() == Attribute.TYPE_FILE) {
			    		result = attr.getStringValue();
			    	} else if (attr.getType() == Attribute.TYPE_STRING) {
						result = attr.getStringValue();
					}
			    }
			    */
				break;
			default :
				break; 	
		}
		
		return result;
	}

	/**
	 * Get the <code>Image</code> associated with the specified column.
	 * 
	 * @param element The <code>Attribute</code>.
	 * @param columnIndex The column index.
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex)
	{	
		Image image = null;
		Attribute attr = (Attribute) element;
		
		/*
		switch (columnIndex) {
			case 1:  // ATTRIBUTE_VALUE_COLUMN
			    if (attr.getChildCount() == 0)
			        // Must be a leaf node.
			        image = getImage(attr.isModified());
				break;
			default:
			    break;
		}
		*/

        return image;
	}

	/**
	 * Get the foreground <code>Color</code> associated with the specified element.
	 * 
	 * @param element The <code>Attribute</code>.
	 * 
	 * @return The foreground color for the specified element is returned.
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getForeground(java.lang.Object)
	 */
	public Color getForeground(Object element)
	{
		Attribute attr = (Attribute) element;
		Color color = null;

		if (attr.isReadOnly() && (attr.getType() != Attribute.TYPE_VARIABLE_LIST))
			color = new Color(Display.getDefault(),138,135,149);
		
		return color;
	}
	
	/**
	 * Get the background <code>Color</code> associated with the specified element.
	 * 
	 * @param element The <code>Attribute</code>.
	 * 
	 * @return The background color for the specified element is returned.
	 * 
	 * @see org.eclipse.jface.viewers.IColorProvider#getBackground(java.lang.Object)
	 */
	public Color getBackground(Object element)
	{
		Attribute attr = (Attribute) element;
		Color color = null;

		//if (attr.isReadOnly() && (attr.getType() != Attribute.TYPE_VARIABLE_LIST))
		//	color = new Color(Display.getDefault(),224,223,227);

		return color;
	}
}
