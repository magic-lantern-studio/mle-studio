/*
 * DwpItemTreeLabelProvider.java
 * Created on May 20, 2005
 */

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2007  Wizzer Works
//
//  Wizzer Works makes available all content in this file ("Content").
//  Unless otherwise indicated below, the Content is provided to you
//  under the terms and conditions of the Common Public License Version 1.0
//  ("CPL"). A copy of the CPL is available at
//
//      http://opensource.org/licenses/cpl1.0.php
//
//  For purposes of the CPL, "Program" will mean the Content.
//
//  For information concerning this Makefile, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
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
