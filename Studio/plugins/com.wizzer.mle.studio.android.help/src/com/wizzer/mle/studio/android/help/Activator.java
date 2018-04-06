// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.android.help;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the life cycle for the com.wizzer.mle.studio.android.help
 * plug-in.
 */
public class Activator extends AbstractUIPlugin
{
	// The plug-in ID
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.android.help"; //$NON-NLS-1$

	// The shared instance
	private static Activator g_plugin;
	
	/**
	 * The default constructor.
	 */
	public Activator()
	{
		// Do nothing extra.
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		super.start(context);
		g_plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
		g_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance of the plug-in.
	 *
	 * @return The shared instance is returned.
	 */
	public static Activator getDefault()
	{
		return g_plugin;
	}

}
