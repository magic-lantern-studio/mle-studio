/*
 * RuntimePlugin.java
 * Created on Aug 2, 2005
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
package com.wizzer.mle.studio.runtime;

// Import standard Java classes.
import java.util.*;

// Import Eclipse classes.
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;


/**
 * The main plugin class for the com.wizzer.mle.studio.runtime plugin.
 */
public class RuntimePlugin extends Plugin
{
	// The shared instance.
	private static RuntimePlugin m_plugin;
	// Resource bundle.
	private ResourceBundle m_resourceBundle;
	
	/**
	 * The default constructor.
	 */
	public RuntimePlugin()
	{
		super();
		m_plugin = this;
		try
		{
			m_resourceBundle = ResourceBundle.getBundle("com.wizzer.mle.studio.runtime.RuntimePluginResources");
		} catch (MissingResourceException ex)
		{
			m_resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
	    // We use System.out.println here instead of RuntimeLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.runtime plug-in.");
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception
	{
	    System.out.println("Deactivating com.wizzer.mle.studio.runtime plug-in.");
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static RuntimePlugin getDefault()
	{
		return m_plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = RuntimePlugin.getDefault().getResourceBundle();
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

}
