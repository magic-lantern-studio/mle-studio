/*
 * DwpFileSaveAction.java
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
 * This class executes the Digital Workprint Table File Save Action.
 *
 * @author Mark S. Millard
 */
public class DwpFileSaveAction extends Action implements IToolAction
{
	// The unique identifer for the DWP File Save Action.
	static final private String DWP_FILE_SAVE_ACTION = "com.wizzer.mle.studio.dwp.action.DwpFileSaveAction";
	// The name for the DWP File Save Action.
	static final private String DWP_FILE_SAVE_NAME = "Save";
	
	// The Eclipse IDE editor associated with this action.
	private IEditorPart m_editor = null;

	/**
	 * A constructor that associates the <code>IEditorPart</code> with th DWP File Save Action.
	 * 
	 * @param editor The <code>IEditorPart</code> to associate.
	 */
	public DwpFileSaveAction(IEditorPart editor)
	{
		super(DWP_FILE_SAVE_NAME + "...");
		setId(DWP_FILE_SAVE_ACTION);
		m_editor = editor;
	}
	
	/**
	 * Get the unique identifier for the File Save action.
	 * 
	 * @return A String is returned representing the uniquie identifier for the File Save action.
	 * 
	 * @see com.wizzer.mle.studio.dwp.action.IToolAction#getID()
	 */
	public String getID()
	{
		return DWP_FILE_SAVE_ACTION;
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
	 * Get the name of the File Save Action.
	 * 
	 * @return A String is returned.
	 */
	public String getName()
	{
		return DWP_FILE_SAVE_NAME;
	}
	
	/**
	 * Execute the File Save Action.
	 * 
	 * @param event The Event that caused this Action to be triggered.
	 */
	public void runWithEvent(Event event)
	{
		DwpLog.logInfo("Executing the File Save Action.");
		((DwpTableEditor)m_editor).getTool().handleFileSaveAction(event);
	}
}
