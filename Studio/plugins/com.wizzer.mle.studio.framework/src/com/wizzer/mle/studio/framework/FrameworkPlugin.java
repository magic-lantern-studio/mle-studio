/**
 * @file FrameworkPlugin.java
 * Created on Nov. 8, 2017
 */

// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework;

// Import standard Java classes.
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

// Import Eclipse packages.
import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.*;
import org.eclipse.core.resources.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.ui.AttributeCellEditorFactory;
import com.wizzer.mle.studio.framework.ui.AttributeContextMenuHandlerRegistry;
import com.wizzer.mle.studio.framework.ui.IAttributeCellEditorFactory;
import com.wizzer.mle.studio.framework.ui.IAttributeContextMenuHandler;

/**
 * The class is the main Eclipse Plug-in class for the com.wizzer.mle.studio.framework
 * plug-in from Wizzer Works.
 */
public class FrameworkPlugin extends AbstractUIPlugin
{
	// The id for the Attribute Cell Editor extension point.
	public static final String ATTRIBUTES_EXTENSIONPOINT_ID =
        "com.wizzer.mle.studio.framework.attributes";
	
	// The shared instance.
	private static FrameworkPlugin m_plugin;
	// A list of AttributeCellEditors.
	private List m_attrEditors = null;
	// A list of IAttributeContextMenuHandlers.
	private List m_attrMenus = null;
	// Resource bundle.
	private ResourceBundle m_resourceBundle;
	
	/**
	 * The default constructor.
	 */
	public FrameworkPlugin()
	{
		super();
		m_plugin = this;
		try {
			m_resourceBundle = ResourceBundle.getBundle("com.wizzer.mle.studio.framework.FrameworkPluginResources");
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
	    System.out.println("Activating com.wizzer.mle.studio.framework plug-in.");
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception
	{
		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static FrameworkPlugin getDefault()
	{
		return m_plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key)
	{
		ResourceBundle bundle = FrameworkPlugin.getDefault().getResourceBundle();
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
	
	// Compute the list of AttributeCellEditor that have been specified as extension points.
	private List computeCellEditors()
	{
		List editors = null;
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		//IPluginRegistry registry = Platform.getPluginRegistry();
		IExtensionPoint extensionPoint =
			registry.getExtensionPoint(FrameworkPlugin.ATTRIBUTES_EXTENSIONPOINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		
		ArrayList results = new ArrayList();
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				try {
					// Get the type of Attribute the Cell Editor is used for.
					String type = elements[j].getAttribute("type");
					
					// Construct the AttributeCellEditor.
					Object factory = elements[j].createExecutableExtension("class");
					if (factory instanceof IAttributeCellEditorFactory)
					{
						AttributeCellEditorFactory.getInstance().addFactory(
							type, (IAttributeCellEditorFactory)factory);
					}
				} catch (CoreException ex)
				{
					FrameworkLog.logError(ex,"Unable to load Attribute Cell Editor: " +
						elements[j].getAttribute("class") + " .");
				}
			}
		}
		
		return editors;
	}
	
	// Compute the list of IAttributeMenuHandler that have been specified as extension points.
	private List computeMenuHandlers()
	{
		List editors = null;
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		//IPluginRegistry registry = Platform.getPluginRegistry();
		IExtensionPoint extensionPoint =
			registry.getExtensionPoint(FrameworkPlugin.ATTRIBUTES_EXTENSIONPOINT_ID);
		IExtension[] extensions = extensionPoint.getExtensions();
		
		ArrayList results = new ArrayList();
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] elements = extensions[i].getConfigurationElements();
			for (int j = 0; j < elements.length; j++)
			{
				try {
					// Get the type of Attribute the Cell Editor is used for.
					String type = elements[j].getAttribute("type");
					
					// Construct the AttributeCellEditor.
					Object handler = elements[j].createExecutableExtension("class");
					if (handler instanceof IAttributeContextMenuHandler)
					{
						AttributeContextMenuHandlerRegistry.getInstance().addHandler(
							type, (IAttributeContextMenuHandler)handler);
					}
				} catch (CoreException ex)
				{
					FrameworkLog.logError(ex,"Unable to load Attribute Context Menu Handler: " +
						elements[j].getAttribute("class") + " .");
				}
			}
		}
		
		return editors;
	}
	
	/**
	 * Get a list of Attribute Cell Editors.
	 * 
	 * @return A <code>List</code> of <code>AttributeCellEditor</code> is returned.
	 */
	public List getAttributeCellEditors()
	{
		if (m_attrEditors == null)
			m_attrEditors = computeCellEditors();
		return m_attrEditors;
	}

	/**
	 * Get a list of Attribute Context Menu handlers.
	 * 
	 * @return A <code>List</code> of <code>IAttributeContextMenuHandler</code> is returned.
	 */
	public List getAttributeContextMenuHandlers()
	{
		if (m_attrMenus == null)
			m_attrMenus = computeMenuHandlers();
		return m_attrMenus;
	}
}
