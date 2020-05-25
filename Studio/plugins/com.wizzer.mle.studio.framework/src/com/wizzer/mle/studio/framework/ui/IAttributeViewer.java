// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework.ui;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;


/**
 * An interface for an Attribute Viewer.
 */
public interface IAttributeViewer
{	
	/**
	 * Update the view to reflect the fact that an attribute was added 
	 * to the model.
	 * 
	 * @param attr The attribute to add to the view.
	 */
	public void addAttribute(Attribute attr);
	
	/**
	 * Update the view to reflect the fact that an attribute was removed 
	 * from the model.
	 * 
	 * @param attr The attribet to remove from the view.
	 */
	public void removeAttribute(Attribute attr);
	
	/**
	 * Update the view to reflect the fact that one of the attributes
	 * was modified.
	 * 
	 * @param attr The attr that was modified.
	 */
	public void updateAttribute(Attribute attr);
}
