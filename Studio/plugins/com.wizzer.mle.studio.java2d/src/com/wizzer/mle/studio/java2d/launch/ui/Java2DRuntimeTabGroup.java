/*
 * Java2DRuntimeTabGroup.java
 * Created on Nov 9, 2006
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
