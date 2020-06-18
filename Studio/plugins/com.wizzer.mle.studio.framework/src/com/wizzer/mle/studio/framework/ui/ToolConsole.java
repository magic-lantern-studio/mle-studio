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
package com.wizzer.mle.studio.framework.ui;

// Import standard Java packages.
import java.io.PrintStream;
import java.util.GregorianCalendar;

// Import Eclipse packages.
import org.eclipse.ui.part.WorkbenchPart;
import org.eclipse.ui.PartInitException;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.ui.ConsoleView;
import com.wizzer.mle.studio.framework.ui.GuiUtilities;
import com.wizzer.mle.studio.framework.ui.IToolConsole;


/**
 * This class implements a generic Console that can be used for
 * displaying textual messages.
 * 
 * @author Mark S. Millard
 */
public class ToolConsole extends PrintStream implements IToolConsole
{
	// The associated Tool Workbench Part.
	private WorkbenchPart m_toolPart = null;
	// The managed Console View Part.
	private ConsoleView m_consoleView = null;
	// The ID of the Console View Part to manage.
	private String m_consoleID = null;
	// A flag indicating whether to append time stamps to console messages.
	private boolean m_showTimestamp = false;
	
	/**
	 * A constructor that associates a <code>WorkbenchPart</code> with the Console.
	 * 
	 * @param part The <code>WorkbenchPart</code> to associate with the console.
	 * @param id The Console ID that will be used identify the workbench part
	 * to show/hide.
	 */
	public ToolConsole(WorkbenchPart part,String id)
	{
		super(System.out);
		m_toolPart = part;
		m_consoleID = id;
	}
	
	/**
	 * Set the associated Tool Workbench Part.
	 * 
	 * @param part The <code>WorkbenchPart</code> that communicates with this instance of the
	 * ToolConsole.
	 */
	public void setPart(WorkbenchPart part)
	{
		m_toolPart = part;
	}
	
	/**
	 * Get the associated Tool Workbench Part.
	 * 
	 * @return The <code>WorkbenchPart</code> that communicates with this instance of the ToolConsole
	 * is returned.
	 */
	public WorkbenchPart getPart()
	{
		return m_toolPart;
	}

	/**
	 * Test to see if the console is open.
	 * 
	 * @return If the console is open, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#isOpen()
	 */
	public boolean isOpen()
	{
		// Always return true.
		return true;
	}

	/**
	 * Set the console to be visible.
	 * <p>
	 * By default, the console should be constructed in an invisible state.
	 * 
	 * @param isVisible If <b>true</b>, then the console window will become visible.
	 * If <b>false</b>, then the console will become invisible.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#setVisible(boolean)
	 */
	public void setVisible(boolean isVisible)
	{
		if (m_toolPart != null) {
			if (isVisible) {
				// Show the Console.
				try {
					m_consoleView = (ConsoleView)m_toolPart.getSite().getPage().showView(m_consoleID);
				} catch (PartInitException ex)
				{
					GuiUtilities.handleError("Unable to show Tool Console.",ex,m_toolPart.getSite().getShell());
				}
				
			} else {
				// Hide the Console.
				m_toolPart.getSite().getPage().hideView(m_consoleView);
				m_consoleView = null;
			}
		}
	}

	/**
	 * Test to see if the console is visible.
	 * 
	 * @return If the console is visible, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#isVisible()
	 */
	public boolean isVisible()
	{
		if (m_consoleView != null)
			return true;
		else
			return false;
	}

	/**
	 * Print a line of output to the console.
	 * 
	 * @param str The <code>String</code> to print.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#println(java.lang.String)
	 */
	public void println(String str)
	{
		// Make sure there is something to print to.
		if (m_consoleView == null)
			return;

		// Manage time stamp if necessary.
		String timestamp = "";
		if (m_showTimestamp)
		{
			GregorianCalendar timer = new GregorianCalendar();
			timestamp = timer.getTime().toString() + ": ";
		}

		// Display message in Console View.
		m_consoleView.append(timestamp + str + "\n");
	}

	/**
	 * Get the name of the console.
	 * 
	 * @return The name of the console is returned as a <code>String</code>.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#getName()
	 */
	public String getName()
	{
		if (m_consoleView != null)
			return m_consoleView.getTitle();
		else
			return null;
	}

	/**
	 * Specify the name of the console.
	 * 
	 * @param name The name of the console.
	 * @param debug If <b>true</b>, then a debug title will be generated.
	 * If <b>false</b>, then the title will be as specified.
	 *
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#setName(java.lang.String, boolean)
	 */
	public void setName(String name, boolean debug)
	{
		if (m_consoleView != null)
			m_consoleView.setTitle(((debug) ? "Debug" : "") + " Console For [" + name + "]");
	}

	/**
	 * Enable time stamping in print out.
	 * 
	 * @param state If <b>true</b>, then time stamping will be enabled.
	 * If <b>false</b>, then time stamping will be disabled.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#enableTimestamp(boolean)
	 */
	public void enableTimestamp(boolean state)
	{
		m_showTimestamp = state;
	}

	/**
	 * Represent the Tool Console as a <code>PrintStream</code> object.
	 * 
	 * @return A reference to a <code>PrintStream</code> object is returned that can be used
	 * as a channel to the Tool Console.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.IToolConsole#asStream()
	 */
	public PrintStream asStream()
	{
		return this;
	}

}
