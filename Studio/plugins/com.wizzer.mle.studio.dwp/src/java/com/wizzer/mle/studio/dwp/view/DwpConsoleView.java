/*
 * DwpViewConsole.java
 * Created on Jul 23, 2004
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
package com.wizzer.mle.studio.dwp.view;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.ui.ConsoleView;

/**
 * This class is an Eclipse IDE View used to present the Digital Workpint
 * Editor messages on a console.
 * 
 * @author Mark S. Millard
 */
public class DwpConsoleView extends ConsoleView
{
	// The unique identifer for the Console View.
	static final private String DWP_CONSOLE_VIEW = "com.wizzer.mle.studio.dwp.view.DwpConsoleView";

	/**
	 * Get the unique identifier for the DWP Console View.
	 * 
	 * @return A String is returned representing the ID of the Eclipse IDE view.
	 */
	static public String getID()
	{
		return DWP_CONSOLE_VIEW;
	}

	/**
	 * The default constructor.
	 */
	public DwpConsoleView()
	{
		super();
	}
}
