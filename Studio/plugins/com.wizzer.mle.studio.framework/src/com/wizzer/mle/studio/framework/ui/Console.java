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
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.*;

// Import Simulator Tool Framework packages.
import com.wizzer.mle.studio.framework.ui.GuiUtilities;
import com.wizzer.mle.studio.framework.ui.IToolConsole;

/**
 * This package implements a console for displaying textual information.
 * 
 * @author Mark S. Millard
 */
public class Console extends PrintStream implements IToolConsole
{
	// A class that implements the Console's Window.
	private class ConsoleWindow extends Window
	{
		public int DEFAULT_WIDTH = 640;
		public int DEFAULT_HEIGHT = 240;
	
		public Text m_textArea = null;

        /*
         * A constructor that specifies the parent shell.
         * 
         * @param parentShell The parent <code>Shell</code>.
         */
		ConsoleWindow(Shell parentShell)
		{
			super(parentShell);
		}
		
		/*
		 * Creates and returns the contents of the Console's control
		 * hierarchy.
		 * 
		 * @param parent The parent widget.
		 * 
		 * @return The new Text control is returned.
		 */
		protected Control createContents(Composite parent)
		{
			// Add a Fill Layout so that the text area will fill the window.
			getShell().setLayout(new FillLayout());

            // Create a text area that can be scrolled.
			m_textArea = new Text(parent,SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI);

            // Set the text area attributes.      
			m_textArea.setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
			m_textArea.setEditable(false);
			m_textArea.setText("");

            // Set the shell's bounding area.        
            getShell().setSize(DEFAULT_WIDTH,DEFAULT_HEIGHT);
            
			return m_textArea;
		}
		
		/*
		 * Handle the shell's close event.
		 * <p>
		 * A dialog message is popped-up instructing the user that the <code>Console</code>
		 * should be closed via the simulator's menu.
		 */
		protected void handleShellCloseEvent()
		{
			GuiUtilities.popupMessage("Please close this console using the simulator's menu.", getShell());
			//System.exit(0);
		}
	}
	
    private ConsoleWindow m_frame = null;
    private boolean m_showTimestamp   = false;
    private boolean m_frameOpened = false;
    
	// Get the Window for unit testing purposes.
	public Window getWindow()
	{
		return m_frame;
	}

    // The default constructor.
    public Console()
    {
        super(System.out);
        
        // Create a new Window for the Console.
        m_frame = new ConsoleWindow(null);
        
        // Open the new Window.
        //m_frame.open();

        // Hide the window. This must be done after the window has been opened.
		//m_frame.getShell().setVisible(false);
    }

    /**
     * Set the console to be visible.
     * <p>
     * By default, the console is constructed in an invisible state.
     * </p>
     * 
     * @param isVisible If <b>true</b>, then the console window will become visible.
     * If <b>false</b>, then the console will become invisible.
     */
    public void setVisible(boolean isVisible)
    {
    	if (m_frameOpened) {
        	m_frame.getShell().setVisible(isVisible);
    	} else {
    		m_frame.open();
    		m_frameOpened = true;
    	}
    }
    
    /**
     * Determine if the <code>Console</code> is visible.
     * 
     * @return <b>true</b> will be returned if the console is visible.
     * Otherwise, <b>false</b> will be returned.
     */
    public boolean isVisible()
    {
    	return m_frame.getShell().isVisible();
    }
    
	/**
	 * Determine if the <code>Console</code> is open.
	 * 
	 * @return <b>true</b> will be returned if the console is open.
	 * Otherwise, <b>false</b> will be returned.
	 */
    public boolean isOpen()
    {
    	if (m_frameOpened && m_frame.getShell().isVisible())
    	    return true;
    	else
    	    return false;
    }

    /**
     * Enable time stamping in print out.
     * 
     * @param state If <b>true</b>, then time stamping will be enabled.
     * If <b>false</b>, then time stamping will be disabled.
     */
    public void enableTimestamp(boolean state)
    {
        m_showTimestamp = state;
    }

    /**
     * Print some string output to the console.
     * 
     * @param str The <code>String</code> to display in the console.
     */
    public void println(String str)
    {
        String timestamp = "";
        if (m_showTimestamp)
        {
            GregorianCalendar timer = new GregorianCalendar();
            timestamp = timer.getTime().toString() + ": ";
        }

        m_frame.m_textArea.append(timestamp + str + "\n");
    }

    /**
     * Specify the name of the console.
     * 
     * @param s The name of the console.
     * @param debug If <b>true</b>, then a debug title will be generated.
     * If <b>false</b>, then the title will be as specified.
     */
    public void setName(String s, boolean debug)
    {
    	if (m_frame.getShell() != null)
        	m_frame.getShell().setText(((debug) ? "Debug" : "") + " Console For [" + s + "]");
    }
    
    /**
     * Get the name of the console.
     * 
     * @return The name of the console is returned as a String. If no name has been previously
     * established, then <b>null</b> will be returned.
     */
    public String getName()
    {
		if (m_frame.getShell() != null)
    		return m_frame.getShell().getText();
    	else
    		return null;
    }
    
    /**
     * Represent the console as a PrintStream.
     */
    public PrintStream asStream()
    {
    	return this;
    }
    
}




