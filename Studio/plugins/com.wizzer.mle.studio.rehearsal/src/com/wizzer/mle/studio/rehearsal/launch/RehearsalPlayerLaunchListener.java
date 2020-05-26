/*
 * RehearsalPlayerLaunchListener.java
 * Created on Apr 21, 2004
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
package com.wizzer.mle.studio.rehearsal.launch;

// Import standard Java packages.

// Import Eclipse packages.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchListener;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.widgets.Display;

// Import Tool Framework classes.
import com.wizzer.mle.studio.framework.ui.GuiUtilities;

// Import Rehearsal Player classes.
import com.wizzer.mle.studio.rehearsal.RehearsalLog;
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;


/**
 * A Rehearsal Player launch listener is notified of launches as they are added and removed
 * from the launch manager. Also, when a process or debug target is added to a launch,
 * this listener is notified of a change.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPlayerLaunchListener implements ILaunchListener
{
	// Track current launch. We need to do this since only one Rehearsal Player
	// launch configuration can be active at a time.
	static private ILaunch g_currentLaunch = null;

	/**
	 * The default constructor.
	 */
	public RehearsalPlayerLaunchListener()
	{
		super();
	}

	/**
	 * Notifies this listener that the specified launch has been removed.
	 * 
	 * @param launch The <code>ILaunch</code> being removed.
	 * 
	 * @see org.eclipse.debug.core.ILaunchListener#launchRemoved(org.eclipse.debug.core.ILaunch)
	 */
	public void launchRemoved(ILaunch launch)
	{
		// Verify that the launch is of the correct configuration type.
		try
		{
			ILaunchConfiguration config = launch.getLaunchConfiguration();
			if (! (config.getType().getIdentifier().equals(IRehearsalPlayerLaunchConfigurationConstants.ID_REHEARSALPLAYER_CONFIGURATION)))
				return;
		} catch (CoreException ex)
		{
			RehearsalLog.logError(ex,"Unable to query launch configuration.");
		}
		
		// Close views only if the launch is the current one.
		if (launch == g_currentLaunch)
		{
		    g_currentLaunch = null;
		
		    // Reset the Rehearsal Player components in order to begin another launch
		    // run/debug session.
		    RehearsalPlugin.getDefault().closePlayer();
		}
	}

	/**
	 * Notifies this listener that the specified launch has been added. 
	 * 
	 * @param launch The <code>ILaunch</code> being added.
	 * 
	 * @see org.eclipse.debug.core.ILaunchListener#launchAdded(org.eclipse.debug.core.ILaunch)
	 */
	public void launchAdded(ILaunch launch)
	{
		// Verify that the launch is of the correct configuration type.
		try
		{
			ILaunchConfiguration config = launch.getLaunchConfiguration();
			if (! (config.getType().getIdentifier().equals(IRehearsalPlayerLaunchConfigurationConstants.ID_REHEARSALPLAYER_CONFIGURATION)))
				return;
		} catch (CoreException ex)
		{
			RehearsalLog.logError(ex,"Unable to query launch configuration.");
		}

		if (g_currentLaunch == null)
		{
			g_currentLaunch = launch;
		} else
		{
			RehearsalLog.logWarning("Rehearsal Player is currently launched with configuration "
			    + g_currentLaunch.getLaunchConfiguration().getName() + ".");
			
			// Display a dialog 
			Display.getDefault().syncExec(new Runnable() {
				public void run() {
					GuiUtilities.popupMessage("Rehearsal Player is already launched with configuration "
					    + g_currentLaunch.getLaunchConfiguration().getName() + ".",
					   Display.getCurrent().getActiveShell());
				}
			});
		}
	}

	/**
	 * Notifies this listener that the specified launch has changed. For example,
	 * a process or debug target has been added to the launch.
	 * 
	 * @param launch The <code>ILaunch</code> that changed.
	 * 
	 * @see org.eclipse.debug.core.ILaunchListener#launchChanged(org.eclipse.debug.core.ILaunch)
	 */
	public void launchChanged(ILaunch launch)
	{
		// Make sure that the launch is the one we registered for.
		if (g_currentLaunch != launch)
			return;
	}

	/**
	 * If there is a current launch, terminate it.
	 */
	static public void terminateCurrentLaunch()
	{
		if (g_currentLaunch != null)
		{
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
			manager.removeLaunch(g_currentLaunch);
		}
	}
	
	/**
	 * Determine whether the Rehearsal Player is currently launched.
	 * 
	 * @return If the Rehearsal Player is currently running in a launch configuration,
	 * <b>true</b> is returned. Otherwise, <b>false</b> is returned.
	 */
	static public boolean isLaunched()
	{
		if (g_currentLaunch == null)
		    return false;
		else
		    return true;
	}

	/**
	 * Determine whether the currently launched player is the same as the one
	 * specified.
	 * 
	 * @param launch The launch to test.
	 * 
	 * @return If the Rehearsal Player is currently running in a launch configuration
	 * and it is the same one as the specified <code>ILaunch</code> parameter, then
	 * <b>true</b> is returned. Otherwise, <b>false</b> is returned.
	 */
	static public boolean isCurrentLaunch(ILaunch launch)
	{
		if ((g_currentLaunch == null) || (g_currentLaunch != launch))
		    return false;
		else
			return true;
	}

}
