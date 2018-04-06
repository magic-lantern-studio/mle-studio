// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework.unittest;

// Import standard Java packages.
import java.util.*;

// Import Eclipse packages.
import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;


/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio.framework.unittest
 * plug-in from Wizzer Works.
 */
public class UnittestPlugin extends AbstractUIPlugin
{
	//The shared instance.
	private static UnittestPlugin m_plugin;
	//Resource bundle.
	private ResourceBundle m_resourceBundle;
	
	/**
	 * The constructor.
	 */
	public UnittestPlugin(IPluginDescriptor descriptor)
	{
		super(descriptor);
		m_plugin = this;
		try {
			m_resourceBundle= ResourceBundle.getBundle("com.wizzer.mle.studio.framework.unittest.UnittestPluginResources");
		} catch (MissingResourceException x) {
			m_resourceBundle = null;
		}
	}

	/**
	 * Returns the shared instance.
	 */
	public static UnittestPlugin getDefault()
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
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle= UnittestPlugin.getDefault().getResourceBundle();
		try {
			return bundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		}
	}

	/**
	 * Returns the plugin's resource bundle,
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
		return getDefault().getDescriptor().getUniqueIdentifier();
	}
}
