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
package com.wizzer.mle.studio.runtimes;

// Import standard Java classes.
import java.util.Vector;

// Import Eclipse classes.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;

// Import VISION Workbench classes.
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.MlePlugin;

/**
 * This class manages application templates.
 * 
 * @author Mark S. Millard
 */
public class ApplicationTemplateManager
{
	// The Singleton instance.
	private static ApplicationTemplateManager m_manager = null;
	
	// The collection of application templates.
	private IApplicationTemplate[] m_templates = null;
	
	// Hide the default constructor.
	private ApplicationTemplateManager()
	{
		// Load the templates.
		m_templates = loadApplicationTemplates();
	}
	
	/**
	 * Get the Singleton instance of the manager.
	 * 
	 * @return A reference to the manager is returned.
	 */
	public static ApplicationTemplateManager getInstance()
	{
		if (m_manager == null)
			m_manager = new ApplicationTemplateManager();
		return m_manager;
	}
	
	/**
	 * Get the registered application templates.
	 * 
	 * @return An array of <code>IApplicationTemplate</code> is returned.
	 */
	public IApplicationTemplate[] getTemplates()
	{
		return m_templates;
	}
	
	/**
	 * Retrieve the named template.
	 * 
	 * @param name The name of the template that should be retrieved.
	 * 
	 * @return If the named template is found, then a reference to an
	 * <code>IApplicationTemplate</code> is returned. Otherwise, <b>null</b>
	 * will be returned.
	 */
	public IApplicationTemplate getTemplate(String name)
	{
		IApplicationTemplate template = null;
		for (int i = 0; i < m_templates.length; i++)
		{
			if (m_templates[i].getName().equals(name))
			{
				template = m_templates[i];
				return template;
			}
		}
		return template;
	}

	/*
	 * Load the application templates that have been contributed via
	 * extension points.
	 * 
	 * @return An array of application templates is returned.
	 */
	private IApplicationTemplate[] loadApplicationTemplates()
	{
		Vector<IApplicationTemplate> templates = new Vector<IApplicationTemplate>();
	
		// Retrieve the extension point.
		IExtensionPoint extension = Platform.getExtensionRegistry().getExtensionPoint(
			MlePlugin.getID(), MlePlugin.APPLICATION_TEMPLATE_EXTPOINT_ID);
		
		// Process the known extensions.
		IExtension[] extensions = extension.getExtensions();
		for (int i = 0; i < extensions.length; i++)
		{
			IConfigurationElement[] configElements = extensions[i].getConfigurationElements();
			for (int j = 0; j < configElements.length; j++)
			{
				try
				{
				    Object appTemplate = configElements[j].createExecutableExtension("class");
				    if (appTemplate instanceof IApplicationTemplate)
				    {
				    	IApplicationTemplate template = (IApplicationTemplate) appTemplate;
				    	
				    	String name = configElements[j].getAttribute("name");
				    	template.setName(name);
				    	String version = configElements[j].getAttribute("version");
				    	template.setVersion(version);
				    	String category = configElements[j].getAttribute("category");
				    	template.setCategory(category);
				    	
				    	IConfigurationElement[] children = configElements[j].getChildren();
				    	for (int k = 0; k < children.length; k++)
				    	{
				    		if (children[k].getName().equals("templateEntry"))
				    		{
				    			ApplicationTemplate entry = new ApplicationTemplate();
				    			String type = children[k].getAttribute("type");
				    			entry.setType(type);
				    			String destination = children[k].getAttribute("destination");
				    			entry.setDestination(destination);
				    			String source = children[k].getAttribute("source");
				    			entry.setSource(source);
				    			String id = children[k].getAttribute("id");
				    			entry.setId(id);
				    			
				    			template.addTemplateEntry(entry);
				    		}
				    	}
				    	
				    	templates.add(template);
				    }
				} catch (CoreException ex)
				{
					MleLog.logError(ex, "Unable to load application templates.");
				}
			}
		}
		
		if (! templates.isEmpty())
		{
			Object[] objs = templates.toArray();
			IApplicationTemplate[] appTemplates = new IApplicationTemplate[objs.length];
			for (int i = 0; i < objs.length; i++)
				appTemplates[i] = (IApplicationTemplate) objs[i];
			return appTemplates;
		} else
		    return null;
	}

}
