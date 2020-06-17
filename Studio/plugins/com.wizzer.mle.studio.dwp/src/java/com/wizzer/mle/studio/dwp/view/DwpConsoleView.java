/*
 * DwpViewConsole.java
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
