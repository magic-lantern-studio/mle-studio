// Declare package.
package com.wizzer.mle.studio.branding;

// Import Eclipse classes.
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle.
 */
public class BrandingPlugin extends AbstractUIPlugin
{
	// The plug-in ID.
	public static final String PLUGIN_ID = "com.wizzer.mle.studio.branding";

	// The shared instance.
	private static BrandingPlugin m_plugin;
	
	/**
	 * The constructor.
	 */
	public BrandingPlugin()
	{
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception
	{
		// We use System.out.println here instead of DppLog.logInfo because
	    // the plug-in activation will fail otherwise. This happens because
	    // the plug-in has not yet been completely constructed and initialized.
	    System.out.println("Activating com.wizzer.mle.studio.branding plug-in.");
		super.start(context);
		m_plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception
	{
	    System.out.println("Deactivating com.wizzer.mle.studio.branding plug-in.");
		m_plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 *
	 * @return The shared instance is returned.
	 */
	public static BrandingPlugin getDefault()
	{
		return m_plugin;
	}

}
