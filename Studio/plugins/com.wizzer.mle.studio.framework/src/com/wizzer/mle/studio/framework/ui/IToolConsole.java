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
