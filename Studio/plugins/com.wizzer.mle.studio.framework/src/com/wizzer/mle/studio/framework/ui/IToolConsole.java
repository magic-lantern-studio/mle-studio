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

// Import standard Java packages.
import java.io.PrintStream;

/**
 * This interface provides a common API for Tool Consoles.
 * 
 * @author Mark S. Millard
 */
public interface IToolConsole
{
	/**
	 * Test to see if the console is open.
	 * 
	 * @return If the console is open, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean isOpen();
	
	/**
	 * Set the console to be visible.
	 * <p>
	 * By default, the console should be constructed in an invisible state.
	 * 
	 * @param isVisible If <b>true</b>, then the console window will become visible.
	 * If <b>false</b>, then the console will become invisible.
	 */
	public void setVisible(boolean isVisible);
	
	/**
	 * Test to see if the console is visible.
	 * 
	 * @return If the console is visible, then <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean isVisible();

	/**
	 * Print some string output to the console.
	 * 
	 * @param str The <code>String</code> to display in the console.
	 */
	public void println(String str);
	
	/**
	 * Get the name of the console.
	 * 
	 * @return The name of the console is returned as a <code>String</code>.
	 */
	public String getName();
	
	/**
	 * Specify the name of the console.
	 * 
	 * @param name The name of the console.
	 * @param debug If <b>true</b>, then a debug title will be generated.
	 * If <b>false</b>, then the title will be as specified.
	 */
	public void setName(String name, boolean debug);

	/**
	 * Enable time stamping in print out.
	 * 
	 * @param state If <b>true</b>, then time stamping will be enabled.
	 * If <b>false</b>, then time stamping will be disabled.
	 */
	public void enableTimestamp(boolean state);
	
	/**
	 * Represent the Tool Console as a <code>PrintStream</code> object.
	 * 
	 * @return A reference to a <code>PrintStream</code> object is returned that can be used
	 * as a channel to the Tool Console.
	 */
	public PrintStream asStream();

}
