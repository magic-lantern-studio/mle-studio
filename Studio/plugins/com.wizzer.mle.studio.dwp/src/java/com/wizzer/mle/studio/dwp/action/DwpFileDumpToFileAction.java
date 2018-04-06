/*
 * DwpFileDumpToFileAction.java
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
 * This class executes the Digital Workprint Table File DumpToFile Action.
 *
 * @author Mark S. Millard
 */
public class DwpFileDumpToFileAction extends Action implements IToolAction
{
	// The unique identifer for the DWP File DumpToFile Action.
	static final private String DWP_FILE_DUMPTOFILE_ACTION = "com.wizzer.mle.studio.dwp.action.DwpFileDumpToFileAction";
	// The name for the DWP File DumpToFile Action.
	static final private String DWP_FILE_DUMPTOFILE_NAME = "Dump To File";
	
	// The Eclipse IDE editor associated with this action.
	private IEditorPart m_editor = null;

	
	/**
	 * A constructor that associates the <code>IEditorPart</code> with the
	 * DWP File DumpToFile Action.
	 * 
	 * @param editor The <code>IEditorPart</code> to associate.
	 */
	public DwpFileDumpToFileAction(IEditorPart editor)
	{
		super(DWP_FILE_DUMPTOFILE_NAME);
		setId(DWP_FILE_DUMPTOFILE_ACTION);
		m_editor = editor;
	}
	
	/**
	 * Get the unique identifier for the File DumpToFile action.
	 * 
	 * @return A String is returned representing the uniquie identifier for the
	 * File DumpToFile action.
	 * 
	 * @see com.wizzer.mle.studio.dwp.action.IToolAction#getID()
	 */
	public String getID()
	{
		return DWP_FILE_DUMPTOFILE_ACTION;
	}

	/**
	 * Get the <code>IEditorPart</code> associated with this Action.
	 * 
	 * @return An <code>IEditorPart</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.action.IToolAction#getPart()
	 */
	public IWorkbenchPart getPart()
	{
		return m_editor;
	}

	/**
	 * Get the name of the File DumpToFile Action.
	 * 
	 * @return A String is returned.
	 */
	public String getName()
	{
		return DWP_FILE_DUMPTOFILE_NAME;
	}
	
	/**
	 * Execute the File DumpToFile Action.
	 * 
	 * @param event The Event that caused this Action to be triggered.
	 */
	public void runWithEvent(Event event)
	{
		DwpLog.logInfo("Executing the File DumpToFile Action.");
		((DwpTableEditor)m_editor).getTool().handleFileDumpToFileAction(event);
	}

}
