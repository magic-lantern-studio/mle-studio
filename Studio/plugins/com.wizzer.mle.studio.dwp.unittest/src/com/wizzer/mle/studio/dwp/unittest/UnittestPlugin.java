/*
 * @file DwpPlugin.java
 * Created on Apr 12, 2004
 */

//COPYRIGHT_BEGIN
//COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.dwp.unittest;

// Import standard Java packages
import java.util.*;

// Import Eclipse packages.
import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;


/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio.dwp.unittest
 * plug-in from Wizzer Works.
 */
public class UnittestPlugin extends AbstractUIPlugin
{
	// The shared instance.
	private static UnittestPlugin m_plugin;
	// Resource bundle.
	private ResourceBundle m_resourceBundle;
	
	/**
	 * The constructor.
	 */
	public UnittestPlugin(IPluginDescriptor descriptor)
	{
		super(descriptor);
		m_plugin = this;
		try {
			m_resourceBundle= ResourceBundle.getBundle("com.wizzer.mle.studio.dwp.DwpPluginResources");
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
