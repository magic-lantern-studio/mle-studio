/*
 * DwpFileDumpToConsoleAction.java
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
package com.wizzer.mle.studio.dwp.action;

// Import Eclipse packages.
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IEditorPart;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IWorkbenchPart;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.ui.IToolAction;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.editor.DwpTableEditor;

/**
 * This class executes the Digital Workprint Table File DumpToConsole Action.
 *
 * @author Mark S. Millard
 */
public class DwpFileDumpToConsoleAction extends Action implements IToolAction
{
	// The unique identifer for the DWP File DumpToConsole Action.
	static final private String DWP_FILE_DUMPTOCONSOLE_ACTION = "com.wizzer.mle.studio.dwp.action.DwpFileDumpToConsoleAction";
	// The name for the DWP File DumpToConsole Action.
	static final private String DWP_FILE_DUMPTOCONSOLE_NAME = "Dump To Console";
	
	// The Eclipse IDE editor associated with this action.
	private IEditorPart m_editor = null;

	
	/**
	 * A constructor that associates the <code>IEditorPart</code> with the
	 * DWP File DumpToConsole Action.
	 * 
	 * @param view The <code>IEditorPart</code> to associate.
	 */
	public DwpFileDumpToConsoleAction(IEditorPart editor)
	{
		super(DWP_FILE_DUMPTOCONSOLE_NAME);
		setId(DWP_FILE_DUMPTOCONSOLE_ACTION);
		m_editor = editor;
	}
	
	/**
	 * Get the unique identifier for the File DumpToConsole action.
	 * 
	 * @return A String is returned representing the uniquie identifier for the
	 * File DumpToConsole action.
	 * 
	 * @see com.wizzer.mle.studio.dwp.ui.IToolAction#getID()
	 */
	public String getID()
	{
		return DWP_FILE_DUMPTOCONSOLE_ACTION;
	}

	/**
	 * Get the <code>IEditorPart</code> associated with this Action.
	 * 
	 * @return An <code>IEditorPart</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.ui.IToolAction#getPart()
	 */
	public IWorkbenchPart getPart()
	{
		return m_editor;
	}

	/**
	 * Get the name of the File DumpToConsole Action.
	 * 
	 * @return A String is returned.
	 */
	public String getName()
	{
		return DWP_FILE_DUMPTOCONSOLE_NAME;
	}
	
	/**
	 * Execute the File DumpToConsole Action.
	 * 
	 * @param event The Event that caused this Action to be triggered.
	 */
	public void runWithEvent(Event event)
	{
		DwpLog.logInfo("Executing the File DumpToConsole Action.");
		((DwpTableEditor)m_editor).getTool().handleFileDumpToConsoleAction(event);
	}

}
