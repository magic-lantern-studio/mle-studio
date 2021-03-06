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
package com.wizzer.mle.studio.framework.ext;

// Import standard Java classes.
import java.util.HashMap;
import java.util.Vector;

// Import Eclipse classes.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.widgets.Composite;

public class ControlSiteFactory
{
	/** The application template extension point ID. */
	public static final String CONTROL_SITE_EXTPOINT_ID = "controlSite";
	
	// The singleton instance of the factory.
	private static ControlSiteFactory g_theInstance = null;
	// A registry of native control site adapters.
	private HashMap<String, IControlSiteAdapter> g_controlSiteRegistry;
	
	// Hide the default constructor.
    private ControlSiteFactory()
    {
    	g_controlSiteRegistry = new HashMap<String, IControlSiteAdapter>();
    	
    	// Load the control sites from the extension point.
    	IControlSiteAdapter[] controlSites = loadControlSites();

    	for (int i = 0; i < controlSites.length; i++)
    	{
    		IControlSiteAdapter site = controlSites[i];
    		g_controlSiteRegistry.put(site.getId(), site);
    	}
    }
    
    public static ControlSiteFactory getInstance()
    {
    	if (g_theInstance == null)
    		g_theInstance = new ControlSiteFactory();
    	return g_theInstance;
    }
    
    public IControlSite createControlSite(String id, Composite parent, int style, String progId)
    {
    	IControlSiteAdapter adapter = g_controlSiteRegistry.get(id);
    	return adapter.createControlSite(parent, style, progId);
    }
    
    public void registerControlSite(String id, IControlSiteAdapter site)
    {
    	g_controlSiteRegistry.put(id, site);
    }
    
    public void unregisterControlSite(String id)
    {
    	g_controlSiteRegistry.remove(id);
    }
    
	/*
	 * Load the control sites that have been contributed via
	 * extension points.
	 * 
	 * @return An array of control sites is returned.
	 */
	private IControlSiteAdapter[] loadControlSites()
	{
		Vector<IControlSiteAdapter> sites = new Vector<IControlSiteAdapter>();
	
		// Retrieve the extension point.
		IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(
			Activator.getID(), CONTROL_SITE_EXTPOINT_ID);
		
		// Process the known extensions.
		IExtension[] extensions = extension.getExtensions();
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++)
			{
				try
				{
				    Object controlSite = configElements[j].createExecutableExtension("class");
				    if (controlSite instanceof IControlSiteAdapter)
				    {
				    	IControlSiteAdapter site = (IControlSiteAdapter) controlSite;
				    	
				    	String id = configElements[j].getAttribute("id");
				    	site.setId(id);
				    	String name = configElements[j].getAttribute("name");
				    	site.setName(name);
				    	String version = configElements[j].getAttribute("version");
				    	site.setVersion(version);
				    	
				    	sites.add(site);
				    }
				} catch (CoreException ex)
				{
					FrameworkExtLog.logError(ex, "Unable to load control sites.");
				}
			}
		}
		
		if (! sites.isEmpty())
		{
			Object[] objs = sites.toArray();
			IControlSiteAdapter[] controlSites = new IControlSiteAdapter[objs.length];
			for (int i = 0; i < objs.length; i++)
				controlSites[i] = (IControlSiteAdapter) objs[i];
			return controlSites;
		} else
		    return null;
	}
}
