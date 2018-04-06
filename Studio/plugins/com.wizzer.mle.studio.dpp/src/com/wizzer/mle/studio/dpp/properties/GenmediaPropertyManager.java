/*
 * GenmediaPropertyManager.java
 * Created on Jul 12, 2005
 */

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2007  Wizzer Works
//
//  Wizzer Works makes available all content in this file ("Content").
//  Unless otherwise indicated below, the Content is provided to you
//  under the terms and conditions of the Common Public License Version 1.0
//  ("CPL"). A copy of the CPL is available at
//
//      http://opensource.org/licenses/cpl1.0.php
//
//  For purposes of the CPL, "Program" will mean the Content.
//
//  For information concerning this Makefile, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
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
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;

/**
 * This class provides utility for setting and retrieving properties on a
 * Digital Workprint resource. It is specifically used in conjunction with
 * the genmedia mastering process.
 * 
 * @author Mark S. Millard
 */
public class GenmediaPropertyManager
{
	/** The genmedia BOM file property. */
	public static String DPP_GENMEDIA_BOM_PROPERTY = "DPP_GENMEDIA_BOM";
	/** The genmedia BOM file property value. */
	public static String DPP_GENMEDIA_BOM_VALUE = "MediaBom.txt";

	// The singleton instance.
	private static GenmediaPropertyManager g_theManager = null;
	
    /// Hide the default constructor.
    private GenmediaPropertyManager() {}

	/**
	 * Get the singleton instance of the <code>GenmediaPropertyManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static GenmediaPropertyManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new GenmediaPropertyManager();
		return g_theManager;
	}
	
	/**
	 * Set the genmedia BOM property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the bill-of-materials file.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setBomValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENMEDIA_BOM_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set BOM property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}

	    }
	}
	
	/**
	 * Set the default value for the genmedia BOM property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setBomDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setBomValue(resource,DPP_GENMEDIA_BOM_VALUE);
	}
	
	/**
	 * Get the genmedia BOM property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the bill-of-materials file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getBomValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENMEDIA_BOM_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get BOM property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * A convenience method for setting the genmedia default properties.
	 * 
	 * @param resource The resource to set the properties on.
	 *
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the properties.
	 */
	public void setDefaults(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setBomDefaultValue(resource);
	}

}
