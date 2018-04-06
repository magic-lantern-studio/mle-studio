/*
 * RehearsalPlayerLaunchConfigurationDelegate.java
 * Created on Jun 5, 2006
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

// Import standard Java classes.
import java.util.List;

// Import Eclipse classes.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;

/**
 * This class is the launch configuration delegate for the Magic Lantern
 * Rehearsal Player.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPlayerLaunchConfigurationDelegate extends
	LaunchConfigurationDelegate
{
	// The Digital Workprint being launched.
	private String m_digitalWorkprint = null;
	// The working directory.
	private String m_workingDirectory = null;
	
	/**
	 * The default constructor.
	 */
	public RehearsalPlayerLaunchConfigurationDelegate()
	{
		super();
	}

	/**
	 * Launches the Rehearsal Player with the given configuration in the specified mode,
	 * contributing debug targets and/or processes to the given launch object.
	 * The launch object has already been registered with the launch manager. 
	 * 
	 * @param configuration The configuration to launch.
	 * @param mode The mode in which to launch, one of the mode constants defined by
	 * ILaunchManager - RUN_MODE or DEBUG_MODE.
	 * @param launch The launch object to contribute processes and debug targets to.
	 * @param monitor The progress monitor, or null.
	 * 
	 * @throws org.eclipse.core.runtime.CoreException This exception is thrown if the
	 * launch configuration is invalid.
	 * 
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration, java.lang.String, org.eclipse.debug.core.ILaunch, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch(ILaunchConfiguration configuration, String mode,
		ILaunch launch, IProgressMonitor monitor)
		throws CoreException
	{
        // Check for a null configuration.
		if (configuration == null)
		    throw new CoreException(new Status(Status.ERROR,RehearsalPlugin.getID(),
			    -1,"Launch configuration for rehearsal player is null.",null));

		// Verify that we have a proper launch configuration.
		if (! (configuration.getType().getIdentifier().equals(IRehearsalPlayerLaunchConfigurationConstants.ID_REHEARSALPLAYER_CONFIGURATION)))
			throw new CoreException(new Status(Status.ERROR,RehearsalPlugin.getID(),
			    -1,"Invalid launch configuration for rehearsal player.",null));
		
		// Validate that there is a project associated with the launch configuration.
		IProject project = getProject(configuration);
		if (project == null)
		{
			throw new CoreException(new Status(Status.ERROR,RehearsalPlugin.getID(),
				-1,"Invalid launch configuration for rehearsal player.",null));
		}
		
		// Extract parameters from launch configuration.
		m_digitalWorkprint = configuration.getAttribute(
			IRehearsalPlayerLaunchConfigurationConstants.ATTR_DIGITAL_WORKPRINT,
			new String(project.getName() + ".dwp"));
		m_workingDirectory = configuration.getAttribute(
				IRehearsalPlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
				new String(project.getLocation().toOSString()));
		
		// Make sure only one session of the Rehearsal Player is running.
		if (! RehearsalPlayerLaunchListener.isCurrentLaunch(launch))
		{
			// Remove the launch from the manager.
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		    manager.removeLaunch(launch);
		    
			return;
		}

		// Determine what mode we will launch the rehearsal player in.
		if (mode.equals(ILaunchManager.RUN_MODE))
		{
            // Set up the monitor.
            if (monitor == null)
                monitor = new NullProgressMonitor();
            
			// Use a dummy process so that the launch manager has something to hold on to.
			RehearsalPlayerProcess process = new RehearsalPlayerProcess(launch);
			int viewConfig = RehearsalPlugin.getDefault().getPreferenceStore().getInt(
	            com.wizzer.mle.studio.rehearsal.preferences.ValueDefaults.VIEW_CONFIGURATION_KEY);
			if (viewConfig == 0)
			    process.setViewConfiguration(RehearsalPlayerProcess.LAUNCH_PERSPECTIVE);
			else
	            process.setViewConfiguration(RehearsalPlayerProcess.LAUNCH_WINDOW);
			launch.addProcess(process);

            // Start progress monitor.
			monitor.beginTask("Launching Rehearsal Player.",IProgressMonitor.UNKNOWN);

			// Begin Rehearsal Player execution. This can be asynchronous.
			RehearsalPlugin.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					// Inialize view configuration from global preferences.
					int viewConfigValue = RehearsalPlugin.getDefault().getPreferenceStore().getInt(
					    com.wizzer.mle.studio.rehearsal.preferences.ValueDefaults.VIEW_CONFIGURATION_KEY);

			        if (viewConfigValue == 0)
			        {
			            // Launch the ActiveX components in the Magic Lantern Perspective.
			        	RehearsalPlugin.getDefault().setViewConfiguration(
			        			RehearsalPlugin.LAUNCH_PERSPECTIVE);
			        	RehearsalPlugin.getDefault().openPlayer(m_digitalWorkprint, m_workingDirectory);
			        } else
			        {
			            // Launch the ActiveX components in a separate SWT window.
			        	RehearsalPlugin.getDefault().setViewConfiguration(
			        			RehearsalPlugin.LAUNCH_WINDOW);
			        	RehearsalPlugin.getDefault().openPlayer(m_digitalWorkprint, m_workingDirectory);
			        }
				}
			});
			
			// End progress monitor.
			monitor.done();
		}
	}
	
	// Get the project associated with the launch configuration.
	private IProject getProject(ILaunchConfiguration configuration)
		throws CoreException
	{
		IProject project = null;
		
		String location = configuration.getAttribute(
			IRehearsalPlayerLaunchConfigurationConstants.ATTR_PROJECT_NAME,
			"__UNKNOWN__");
		if ((! location.equals("__UNKNOWN__")) && (! location.equals("")))
		{
			IWorkspaceRoot root = RehearsalPlugin.getWorkspace().getRoot();
			project = root.getProject(location);
		}

	    return project;
	}

}
