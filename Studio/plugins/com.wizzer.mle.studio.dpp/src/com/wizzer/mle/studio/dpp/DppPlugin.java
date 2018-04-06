/*
 * DppPlugin.java
 * Created on Jun 25, 2005
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
package com.wizzer.mle.studio.dpp;

// Import standard Java classes.
import java.util.*;

// Import Eclipse classes.
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.plugin.*;
import org.osgi.framework.BundleContext;

/**
 * The class is the main Eclipse Plug-in class for the <b>com.wizzer.mle.studo.dpp</b>
 * plug-in from Wizzer Works.
 * 
 * @author Mark S. Millard
 */
public class DppPlugin extends AbstractUIPlugin
{
	// The shared instance.
	private static DppPlugin m_plugin;
	// Resource bundle.
	private ResourceBundle m_resourceBundle;

	/**
	 * The constructor.
	 */
	public DppPlugin()
	{
		super();
		m_plugin = this;
		try
		{
			m_resourceBundle = ResourceBundle.getBundle("com.wizzer.mle.studio.dpp.DppPluginResources");
		} catch (MissingResourceException x) {
			m_resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
	    // We use System.out.println here instead of DppLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.dpp plug-in.");
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception
	{
	    System.out.println("Deactivating com.wizzer.mle.studio.dpp plug-in.");
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static DppPlugin getDefault()
	{
		return m_plugin;
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
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = DppPlugin.getDefault().getResourceBundle();
		try
		{
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException ex)
		{
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle.
	 */
	public ResourceBundle getResourceBundle()
	{
		return m_resourceBundle;
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

	/**
	 * Determine if plug-in is running on the Linux operating system.
	 * 
	 * @return <code>true</code> is returned if the plug-in is operating on
	 * the Linux operating system. Otherwise, <code>false</code> will be
	 * returned.
	 */
	public static boolean isLinux()
	{
		if (Platform.getOS().equals(Platform.OS_LINUX))
			return true;
		else
			return false;
	}

	/**
	 * Determine if plug-in is running on the Windows operating system.
	 * 
	 * @return <code>true</code> is returned if the plug-in is operating on
	 * the Linux operating system. Otherwise, <code>false</code> will be
	 * returned.
	 */
	public static boolean isWindows()
	{
		if (Platform.getOS().equals(Platform.OS_WIN32))
			return true;
		else
			return false;
	}
}
