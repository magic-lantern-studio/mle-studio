/**
 * Activator.java
 * Created on January 28, 2008
 */

//COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2008  Wizzer Works
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
//COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.brender;

// Import standard Java classes.
import java.io.File;

//Import Eclipse classes.
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.dpp.DppException;
//import com.wizzer.mle.studio.brender.BRenderLog;
import com.wizzer.mle.studio.brender.launch.IRuntimePlayerProcessListener;
import com.wizzer.mle.studio.brender.launch.RuntimePlayerCommand;
import com.wizzer.mle.studio.brender.launch.RuntimePlayerLaunchListener;
import com.wizzer.mle.studio.brender.launch.RuntimePlayerManager;

/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio.brender
 * plug-in from Wizzer Works.
 */
public class Activator extends AbstractUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.brender";

	// The shared instance
	private static Activator m_plugin;
	// The runtime player.
	private RuntimePlayerCommand m_player = null;
	// The runtime player manager.
	private RuntimePlayerManager m_playerManager = null;
	// The launch listener.
	private RuntimePlayerLaunchListener m_launchListener = null;

	/**
	 * The constructor
	 */
	public Activator()
	{
		m_plugin = this;
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
		// We use System.out.println here instead of MleLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.brender plug-in.");
		super.start(context);

		// Initialize the launch listener.
		m_launchListener = new RuntimePlayerLaunchListener();
		
		// Get the launch manager from the Eclipse Debug Plugin and register our
		// launch listener with it.
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
        manager.addLaunchListener(m_launchListener);
        
        // Initialize the Runtime Player Manager.
        m_playerManager = RuntimePlayerManager.getInstance();
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception
	{
		m_plugin = null;
		System.out.println("Deactivating com.wizzer.mle.studio.brender plug-in.");
		super.stop(context);
	}

	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
	}
	
	/**
	 * Get the display.
	 * 
	 * @return The <code>Display</code> is returned.
	 */
	public static Display getDisplay()
	{
		Shell shell = getActiveWorkbenchShell();
		if (shell != null) {
			return shell.getDisplay();
		}
		
		Display display = Display.getCurrent();
		if (display == null) {
			display = Display.getDefault();
		}
		
		return display;
	}

	/**
	 * Get the active workbench shell.
	 * 
	 * @return A <code>Shell</code> is returned.
	 */
	public static Shell getActiveWorkbenchShell()
	{
		IWorkbenchWindow workBenchWindow = getActiveWorkbenchWindow();
		if (workBenchWindow == null)
			return null;
			
		return workBenchWindow.getShell();
	}
	
	/**
	 * Get the active workbench window.
	 * 
	 * @return The active workbench window is returned.
	 */
	static public IWorkbenchWindow getActiveWorkbenchWindow()
	{
		if (m_plugin == null)
			return null;

		IWorkbench workBench = m_plugin.getWorkbench();
		if (workBench == null)
			return null;

		return workBench.getActiveWorkbenchWindow();
	}
	
	/**
	 * Get the active page.
	 * 
	 * @return The active page is returned for the active workbench window.
	 */
	static public IWorkbenchPage getActivePage()
	{
		IWorkbenchWindow activeWorkbenchWindow = getActiveWorkbenchWindow();
		if (activeWorkbenchWindow == null)
			return null;
	
		return activeWorkbenchWindow.getActivePage();
	}

	/**
	 * Shutdown the Runtime Player.
	 */
	public void closePlayer()
	{
	    if (m_player != null)
	    	m_player.kill();
	    m_player = null;
	}
	
	/**
	 * Startup the Runtime Player.
	 * 
	 * @param title The title executable.
	 * @param dpp The Digital Playprint.
	 * @param workingDir The working directory.
	 */
	public void openPlayer(String title, String dpp, String workingDir)
	{
		try
		{
			// Determine whether executable exists.
			File titleExecutable = new File(title);
			if (! titleExecutable.isFile())
				throw new DppException("Title executable, " + title + ", is not a file.");
			
			// Determine whether digital playprint exists.
			File digitalPlayprint = new File(dpp);
			if (! digitalPlayprint.isFile())
				throw new DppException("Digital Playprint, " + digitalPlayprint + ", is not a file.");
			
			// Create the Runtime Player command.
		    m_player = new RuntimePlayerCommand(title,digitalPlayprint);
		    
		    // Set the current working directory.
		    File cwd = new File(workingDir);
		    if (! cwd.isDirectory())
		    	throw new DppException("Working directory, " + workingDir + ", is not a directory.");
		    m_player.setLocation(cwd);
		    
		    // XXX - Set the environment.
		    
		    // Configure the Runtime Player Manager.
		    IRuntimePlayerProcessListener listener = new IRuntimePlayerProcessListener()
		    	{
		    		public void processTerminated(Process process)
		    		{
		    			// Terminate the launch.
		    			RuntimePlayerLaunchListener.terminateCurrentLaunch();
		    		}
		    	};
		    m_playerManager.addProcessListener(listener);
		    
		    // Execute the title.
		    m_player.exec();
		    
		    // Notify the player manager to wait for the player to terminate.
		    m_playerManager.waitFor(m_player.getProcess());

		} catch (DppException ex)
		{
			BRenderLog.logError(ex, "Unable to execute title " + title + ".");
		}
	}

	/**
	 * Returns the shared instance of the plug-in.
	 *
	 * @return The shared instance.
	 */
	public static Activator getDefault()
	{
		return m_plugin;
	}

	/**
	 * Get the plug-in identifier.
	 * 
	 * @return A <code>String</code> is returned identifying the plug-in.
	 */
	public static String getID()
	{
		return getDefault().getBundle().getSymbolicName();
	}
}
