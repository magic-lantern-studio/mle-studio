/*
 * GenppscriptPropertyManager.java
 */

// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2022 Wizzer Works
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
package com.wizzer.mle.studio.dpp.properties;

// Import Eclipse classes.
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

// Import Magic Lantern Digital Workprint classes.

// Import Magic Lantern Digital Playprint classes.
import com.wizzer.mle.studio.dwp.DwpProjectManager;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;

/**
 * This class provides utility for setting and retrieving properties on a
 * Digital Workprint resource. It is specifically used in conjunction with
 * the genppscript mastering process.
 * 
 * @author Mark S. Millard
 */
public class GenppscriptPropertyManager
{
	/** The genppscript DPP file property. */
	public static String DPP_GENPPSCRIPT_DPP_PROPERTY = "DPP_GENPPSCRIPT_DPP";
	/** The genppscript DPP file property value. */
	public static String DPP_GENPPSCRIPT_DPP_VALUE = "playprint.dpp";
	/** The genppscript Python script file property. */
	public static String DPP_GENPPSCRIPT_SCRIPT_PROPERTY = "DPP_GENPPSCRIPT_SCRIPT";
	/** The genppscript Python script file property value. */
	public static String DPP_GENPPSCRIPT_SCRIPT_VALUE = "playprint.py";
	/** The genppscript TOC file property. */
	public static String DPP_GENPPSCRIPT_TOC_PROPERTY = "DPP_GENPPSCRIPT_TOC";
	/** The genppscript TOC file property value. */
	public static String DPP_GENPPSCRIPT_TOC_JAVA_VALUE = "DppTOC";
	/** The genppscript TOC file property value. */
	public static String DPP_GENPPSCRIPT_TOC_CPP_VALUE = "dpptoc";

	// The singleton instance.
	private static GenppscriptPropertyManager g_theManager = null;
	
    /// Hide the default constructor.
    private GenppscriptPropertyManager() {}

	/**
	 * Get the singleton instance of the <code>GenppscriptPropertyManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static GenppscriptPropertyManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new GenppscriptPropertyManager();
		return g_theManager;
	}
	
	/**
	 * Set the genppscript DPP property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the Digital Playprint file.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setDppValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENPPSCRIPT_DPP_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set DPP property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }
	}
	
	/**
	 * Set the default value for the genppscript DPP property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setDppDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setDppValue(resource,DPP_GENPPSCRIPT_DPP_VALUE);
	}
	
	/**
	 * Get the genppscript DPP property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the Digital Playprint file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getDppValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENPPSCRIPT_DPP_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get DPP property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * Set the genppscript script property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the Python script.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setScriptValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENPPSCRIPT_SCRIPT_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set Python script property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }
	}

	/**
	 * Set the default value for the genppscript script property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setScriptDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setScriptValue(resource,DPP_GENPPSCRIPT_SCRIPT_VALUE);
	}
	
	/**
	 * Get the genppscript script property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the Python script file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getScriptValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENPPSCRIPT_SCRIPT_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get Python script property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * Set the genppscript TOC property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the table-of-contents file.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setTocValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENPPSCRIPT_TOC_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set TOC property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }
	}
	
	/**
	 * Set the default value for the genppscript TOC property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setTocDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isJavaProject(project))
        {
	        setTocValue(resource,DPP_GENPPSCRIPT_TOC_JAVA_VALUE);
        } else if (DwpProjectManager.getInstance().isCppProject(project))
        {
            setTocValue(resource,DPP_GENPPSCRIPT_TOC_CPP_VALUE);
        } else
        {
            String message = new String("Unknown resource type, " +
                resource.getName() + ".");
            throw new DppException(message);
        }
	    
	}
	
	/**
	 * Get the genppscript TOC property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the Digital Playprint file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getTocValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENPPSCRIPT_TOC_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get TOC property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}

	/**
	 * A convenience method for setting the genppscript default properties.
	 * 
	 * @param resource The resource to set the properties on.
	 *
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the properties.
	 */
	public void setDefaults(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setDppDefaultValue(resource);
	    setScriptDefaultValue(resource);
	    setTocDefaultValue(resource);
	}

}
