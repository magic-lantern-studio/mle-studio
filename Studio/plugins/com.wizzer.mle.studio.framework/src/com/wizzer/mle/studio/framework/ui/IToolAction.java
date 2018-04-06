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
import org.eclipse.ui.IWorkbenchPart;

/**
 * This interface is used to provide a common API for Actions.
 * 
 * @author Mark S.Millard
 */
public interface IToolAction
{
	/**
	 * Get the unique action ID.
	 * 
	 * @return The unique action ID is returned as a <code>String</code>.
	 */
	public String getID();
	
	/**
	 * Get the part associated with the Action.
	 *
	 * @return A reference to an <code>IWorkbenchPart</code> is returned.
	 */
	public IWorkbenchPart getPart();
}
