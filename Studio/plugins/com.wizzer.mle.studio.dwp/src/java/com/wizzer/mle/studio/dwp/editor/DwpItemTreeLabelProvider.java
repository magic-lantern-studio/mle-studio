/*
 * DwpItemTreeLabelProvider.java
 */

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
package com.wizzer.mle.studio.dwp.editor;

// Import Eclipse classes.
import org.eclipse.jface.viewers.LabelProvider;

// Import Magic Lantern Tool Framework.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * Label provider for the <code>DwpItemTreeModel</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpItemTreeLabelProvider extends LabelProvider
{
    /**
     * The default constructor.
     */
    public DwpItemTreeLabelProvider()
    {
        super();
    }

    /**
     * Get the text for the label.
     * 
     * @param element The element for which to provide the label text.
     * 
     * @return The text string used to label the element,
     * or <b>null</b> if there is no text label for the given object.
     */
    public String getText(Object element)
    {
        Attribute attr = (Attribute)element;
        
        String label = new String(attr.getName() + " " + attr.getStringValue());
        return label;
    }
}
