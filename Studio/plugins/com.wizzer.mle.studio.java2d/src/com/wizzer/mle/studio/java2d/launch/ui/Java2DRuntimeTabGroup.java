/*
 * Java2DRuntimeTabGroup.java
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
package com.wizzer.mle.studio.java2d.launch.ui;

// Import Eclipse classes.
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.*;

/**
 * This class is used to create and manage the Java2D Runtime Player
 * launch configuration tab group.
 * 
 * @author Mark S. Millard
 */
public class Java2DRuntimeTabGroup extends AbstractLaunchConfigurationTabGroup
{
	/**
	 * The default constructor.
	 */
	public Java2DRuntimeTabGroup()
	{
		super();
	}

	/**
	 * Creates the tabs for the Java2D Runtime Player launch configuration tab group.
	 * <p>
	 * Creates the tabs contained in this tab group for the specified launch mode.
	 * The tabs control's are not created. This is the fist method called in the lifecycle
	 * of a tab group.
	 * </p>
	 * 
	 * @param dialog The launch configuration dialog this tab group is contained in.
	 * @param mode The mode the launch configuration dialog was opened in.
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTabGroup#createTabs(org.eclipse.debug.ui.ILaunchConfigurationDialog, java.lang.String)
	 */
	public void createTabs(ILaunchConfigurationDialog dialog, String mode)
	{
	    ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[]
	    {
	        new JavaMainTab(),
	        new JavaArgumentsTab(),
	        new JavaJRETab(),
	        new JavaClasspathTab(),
	        new SourceLookupTab(),
	        new EnvironmentTab(),
	        new CommonTab()
	    };
	    setTabs(tabs);
	}

}
