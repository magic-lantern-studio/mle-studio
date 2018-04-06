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
package com.wizzer.mle.studio.framework.unittest;

// Import JUnit packages.
import junit.framework.TestCase;

// Import Eclipse packages.
import org.eclipse.jface.window.Window;

// Import Magic Lantern Tool Framework packages.
import com.wizzer.mle.studio.framework.ui.Console;

/**
 * This class is a unit test for com.wizzer.mle.studio.framework.ui.Console.
 * 
 * @author Mark S. Millard
 */
public class ConsoleTest extends TestCase
{
	/**
	 * Constructor for ConsoleTest.
	 * 
	 * @param name
	 */
	public ConsoleTest(String name)
	{
		super(name);
	}

	/**
	 * Executes the test runner.
	 * 
	 * @param args Command line arguements.
	 */
	public static void main(String[] args)
	{
		junit.textui.TestRunner.run(ConsoleTest.class);
	}

	/**
	 * Set up the test case.
	 * 
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	/**
	 * Tear down the test case.
	 * 
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	// Note: This test will not allow the user to exit because the ConsoleWindow overrides
	// handleShellCloseEvent(). This is done on behalf of the Wizzer Works Plugin Framework.
	// Another way should be provided by the unit test to exit, e.g. pressing the ESC key.
	
	/**
	 * Test the <code>Console</code>.
	 */
	public void testConsole()
	{
		Console console = new Console();
		console.setVisible(true);
		console.setName("Console Unit Test",true);
		console.enableTimestamp(true);
		
		for (int i = 0; i < 100; i++) {
			console.println("Test output " + i);
		}

		// Dispatch events since we are not blocking.
		Window consoleWindow = console.getWindow();
		while (! consoleWindow.getShell().isDisposed()) {
			if (! consoleWindow.getShell().getDisplay().readAndDispatch()) {
				consoleWindow.getShell().getDisplay().sleep();
			}
		}
	}
	
}
