// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.java2d;

// Import Eclipse classes.
import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class Java2DPlugin extends AbstractUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.java2d";

	// The shared instance
	private static Java2DPlugin m_plugin;
	
	/**
	 * The constructor.
	 */
	public Java2DPlugin()
	{
	}

	/**
	 * This method is called upon plug-in activation.
	 */
	public void start(BundleContext context) throws Exception
	{
	    // We use System.out.println here instead of DwpLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
		System.out.println("Activating com.wizzer.mle.studio.java2d plug-in.");
		super.start(context);
		m_plugin = this;
	}

	/**
	 * This method is called when the plug-in is stopped.
	 */
	public void stop(BundleContext context) throws Exception
	{
		System.out.println("Deactivating com.wizzer.mle.studio.java2d plug-in.");
		m_plugin = null;
		super.stop(context);
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
	 * Returns the shared instance.
	 *
	 * @return The shared instance is returned.
	 */
	public static Java2DPlugin getDefault()
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
