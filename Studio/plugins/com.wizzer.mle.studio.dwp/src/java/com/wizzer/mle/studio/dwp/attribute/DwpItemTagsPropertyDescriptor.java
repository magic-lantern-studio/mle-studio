/*
 * DwpItemTagsPropertyDescriptor.java
 * Created on May 10, 2005
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
package com.wizzer.mle.studio.dwp.attribute;

// Import Eclipse classes.
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * @author Mark S. Millard
 */
public class DwpItemTagsPropertyDescriptor extends TextPropertyDescriptor
{
	/**
	 * Creates an property descriptor with the given id and display name. 
	 * 
	 * @param id The id of the property.
	 * @param displayName The name to display for the property.
	 */
    public DwpItemTagsPropertyDescriptor(Object id, String displayName)
    {
        super(id, displayName);
    }

}
