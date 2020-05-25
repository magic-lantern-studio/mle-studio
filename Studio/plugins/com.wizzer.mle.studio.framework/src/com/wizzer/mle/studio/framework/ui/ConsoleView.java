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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.SWT;
import org.eclipse.ui.part.ViewPart;

/**
 * This class is an Eclipse IDE View used to present messages
 * on a console.
 * 
 * @author Mark S. Millard
 */
public class ConsoleView extends ViewPart
{
	// The unique identifer for the Console View.
	static final private String CONSOLE_VIEW = "com.wizzer.mle.studio.framework.ui.ConsoleView";

	/**
	 * Get the unique identifier for the Console View.
	 * 
	 * @return A String is returned representing the ID of the Eclipse IDE view.
	 */
	static public String getID()
	{
		return CONSOLE_VIEW;
	}

	// The console's SWT Control.
	private Text m_textArea = null;

	/**
	 * The default constructor.
	 */
	public ConsoleView()
	{
		super();
	}
	
	/**
	 * Creates the SWT controls for this workbench part.
	 * <p>
	 * Clients should not call this method (the workbench calls this method when it needs to, which may be never).
	 * </p>
	 * 
	 * @param parent The parent <code>Control</code>.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent)
	{
		// Create a text area that can be scrolled.
		m_textArea = new Text(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);

		// Set the text area attributes.      
		m_textArea.setSize(parent.getSize().x,parent.getSize().y);
		m_textArea.setEditable(false);
		m_textArea.setText("Informative messages will be sent here.\n\n");
	}

	/**
	 * Set the focus to the Console's Text area.
	 * 
	 * @see org.eclipse.ui.IWorkbenchPart#setFocus()
	 */
	public void setFocus()
	{
		m_textArea.setFocus();
	}

	/**
	 * Set the title of the Console.
	 * 
	 * @param name The name of the Console title.
	 */
	public void setTitle(String name)
	{
		super.setTitle(name);
	}
	
	/**
	 * Append a message to the Console.
	 * 
	 * @param msg The message to append.
	 */
	public void append(String msg)
	{
		m_textArea.append(msg);
	}
}
