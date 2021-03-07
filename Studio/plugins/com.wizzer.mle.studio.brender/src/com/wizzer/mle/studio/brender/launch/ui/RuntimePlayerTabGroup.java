/*
 * RuntimePlayerTabGroup.java
 * Created on Nov 1, 2006
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
package com.wizzer.mle.studio.brender.launch.ui;

// Import Eclipse classes.
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.core.ILaunchManager;

/**
 * This class is used to create and manage the Runtime Player launch configuration
 * tab group.
 * 
 * @author Mark S. Millard
 */
public class RuntimePlayerTabGroup
    extends AbstractLaunchConfigurationTabGroup
{
	/**
	 * The default constructor.
	 */
	public RuntimePlayerTabGroup()
	{
		super();
	}

	/**
	 * Creates the tabs for the Runtime Player launch configuration tab group.
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
		ILaunchConfigurationTab[] tabs = null;
		
		if (mode.equals(ILaunchManager.RUN_MODE))
		{
            // Create the tabs for the Runtime Player using the Run configuration parameters.
			tabs = new ILaunchConfigurationTab[]
			{
			    new RuntimePlayerConfigurationTab(mode),
				new CommonTab()
			};
		}

		// Set the tabs.
		setTabs(tabs);
	}

}
