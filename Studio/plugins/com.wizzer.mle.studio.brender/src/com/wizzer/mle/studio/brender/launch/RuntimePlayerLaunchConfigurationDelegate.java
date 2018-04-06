/*
 * RuntimePlayerLaunchConfigurationDelegate.java
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
package com.wizzer.mle.studio.brender.launch;

// Import Eclispe classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;

// Import Digital Playprint classes.
import com.wizzer.mle.studio.brender.Activator;
//import com.wizzer.mle.studio.dpp.DppPlugin;

/**
 * This class is the launch configuration delegate for the Magic Lantern
 * Runtime Execution.
 * 
 * @author Mark S. Millard
 */
public class RuntimePlayerLaunchConfigurationDelegate extends
    LaunchConfigurationDelegate
{
	// The Runtime Player executable.
	private String m_titleExecutable = null;
	// The Digital Playprint being launched.
	private String m_digitalPlayprint = null;
	// The working directory.
	private String m_workingDirectory = null;

	/**
	 * The default constructor.
	 */
	public RuntimePlayerLaunchConfigurationDelegate()
	{
		super();
	}

	/**
	 * Launches the application with the given configuration in the specified mode,
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
	    ILaunch launch, IProgressMonitor monitor) throws CoreException
	{
        // Check for a null configuration.
		if (configuration == null)
		    throw new CoreException(new Status(Status.ERROR,Activator.getID(),
			    -1,"Launch configuration for runtime player is null.",null));

		// Verify that we have a proper launch configuration.
		if (! (configuration.getType().getIdentifier().equals(IRuntimePlayerLaunchConfigurationConstants.ID_RUNTIMEPLAYER_CONFIGURATION)))
			throw new CoreException(new Status(Status.ERROR,Activator.getID(),
			    -1,"Invalid launch configuration for runtime player.",null));
		
		// Validate that there is a project associated with the launch configuration.
		IProject project = getProject(configuration);
		if (project == null)
		{
			throw new CoreException(new Status(Status.ERROR,Activator.getID(),
				-1,"Invalid launch configuration for runtime player.",null));
		}
		
		// Extract parameters from launch configuration.
		m_digitalPlayprint = configuration.getAttribute(
			IRuntimePlayerLaunchConfigurationConstants.ATTR_DIGITAL_PLAYPRINT,
			new String(project.getName() + ".dpp"));
		m_workingDirectory = configuration.getAttribute(
			IRuntimePlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
			new String(project.getLocation().toOSString()));
		if (mode.equals(ILaunchManager.RUN_MODE))
		    m_titleExecutable = configuration.getAttribute(
			    IRuntimePlayerLaunchConfigurationConstants.ATTR_RUNTIME_EXECUTABLE,
			    new String(project.getLocation().toOSString() + "\\src\\Release\\" + project.getName() + ".exe"));
		else
		    m_titleExecutable = configuration.getAttribute(
				    IRuntimePlayerLaunchConfigurationConstants.ATTR_RUNTIME_EXECUTABLE,
				    new String(project.getLocation().toOSString() + "\\src\\Debug\\" + project.getName() + ".exe"));

		// Make sure only one session of the Runtime Player is running.
		if (! RuntimePlayerLaunchListener.isCurrentLaunch(launch))
		{
			// Remove the launch from the manager.
			ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		    manager.removeLaunch(launch);
		    
			return;
		}

		// Determine what mode we will launch the runtime player in.
		if (mode.equals(ILaunchManager.RUN_MODE))
		{
            // Set up the monitor.
            if (monitor == null)
                monitor = new NullProgressMonitor();
            
			// Use a dummy process so that the launch manager has something to hold on to.
			RuntimePlayerProcess process = new RuntimePlayerProcess(launch);
			launch.addProcess(process);

            // Start progress monitor.
			monitor.beginTask("Launching Runtime Player.",IProgressMonitor.UNKNOWN);

			// Begin Runtime Player execution. This can be asynchronous.
			Activator.getDisplay().asyncExec(new Runnable()
			{
				public void run()
				{
					Activator.getDefault().openPlayer(
						m_titleExecutable, m_digitalPlayprint, m_workingDirectory);
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
			IRuntimePlayerLaunchConfigurationConstants.ATTR_PROJECT_NAME,
			"__UNKNOWN__");
		if ((! location.equals("__UNKNOWN__")) && (! location.equals("")))
		{
			IWorkspaceRoot root = Activator.getWorkspace().getRoot();
			project = root.getProject(location);
		}

	    return project;
	}
	
}
