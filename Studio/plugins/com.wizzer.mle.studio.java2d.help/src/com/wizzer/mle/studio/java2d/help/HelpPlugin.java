// Declare package
package com.wizzer.mle.studio.java2d.help;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio.java2d.help
 * plug-in from Wizzer Works.
 */
public class HelpPlugin extends AbstractUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.java2d.help";

	// The shared instance
	private static HelpPlugin m_plugin;
	
	/**
	 * The constructor
	 */
	public HelpPlugin()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception 
	{
		// We use System.out.println here instead of MleLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.java2d.help plug-in.");
		super.start(context);
		m_plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception 
	{
		m_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static HelpPlugin getDefault() 
	{
		return m_plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) 
	{
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
