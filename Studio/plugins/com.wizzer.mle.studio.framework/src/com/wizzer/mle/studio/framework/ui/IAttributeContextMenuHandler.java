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

// Import Eclipse packages.
import org.eclipse.swt.widgets.Menu;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.IAttribute;


/**
 * This interface is used to provide a handler for a context menu for each
 * Attribute in an <code>AttributetreeViewer</code>.
 * 
 * @author Mark S. Millard
 */
public interface IAttributeContextMenuHandler
{
	/**
	 * Get a popup <code>Menu</code> for the specified <code>IAttribute</code>.
	 * 
	 * @param attribute The <code>IAttribute</code> to create a context menu for.
	 * 
	 * @return A reference to a <code>Menu</code> is returned.
	 */
	public Menu getContextMenu(IAttribute attribute,AttributeTreeViewer viewer);
}
