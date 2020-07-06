/**
 * MlePlugin.java
 */

// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2020 Wizzer Works
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
// 
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
// 
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio;

// Import standard Java classes.
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

// Import Eclipse classes.
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;


/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio
 * plug-in from Wizzer Works.
 */
public class MlePlugin extends AbstractUIPlugin
{
	/** The application template extension point ID. */
	public static final String APPLICATION_TEMPLATE_EXTPOINT_ID = "applicationTemplates";

	// The shared instance.
	private static MlePlugin m_plugin;
	// Resource bundle.
	private ResourceBundle m_resourceBundle;
	
	/**
	 * The default constructor.
	 */
	public MlePlugin()
	{
		super();
		m_plugin = this;
		try {
			m_resourceBundle = ResourceBundle.getBundle("com.wizzer.mle.studio.MlePluginResources");
		} catch (MissingResourceException x) {
			m_resourceBundle = null;
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception
	{
	    // We use System.out.println here instead of MleLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio plug-in.");
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception
	{
		System.out.println("Deactivating com.wizzer.mle.studio plug-in.");
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static MlePlugin getDefault()
	{
		return m_plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = MlePlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
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
	 * Create the image descriptor associated with specified image name.
	 * 
	 * @param name The name of the image to retrieve a descriptor for.
	 * 
	 * @return A reference to the new <code>ImageDescriptor</code> is returned.
	 */
	public ImageDescriptor getImageDescriptor(String name)
	{
		try {
			URL url= new URL(getBundle().getEntry("/"), name);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
	
	/**
	 * Returns the workspace instance.
	 */
	public static IWorkspace getWorkspace()
	{
		return ResourcesPlugin.getWorkspace();
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
