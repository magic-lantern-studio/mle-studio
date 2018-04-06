/*
 * GendppPropertyManager.java
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
 * the gendpp mastering process.
 * 
 * @author Mark S. Millard
 */
public class GendppPropertyManager
{
	/** The gendpp TCL file property. */
	public static String DPP_GENDPP_SCRIPT_PROPERTY = "DPP_GENDPP_SCRIPT";
	/** The gendpp TCL file property value. */
	public static String DPP_GENDPP_SCRIPT_VALUE = "src/gen/playprint.tcl";
	/** The gendpp source directory property for generated code/components. */
	public static String DPP_GENDPP_SOURCE_DIR_PROPERTY = "DPP_GENDPP_SOURCE_DIR";
	/** The gendpp source directory. */
	public static String DPP_GENDPP_SOURCE_DIR_VALUE = "src/gen";

	// The singleton instance.
	private static GendppPropertyManager g_theManager = null;
	
    /// Hide the default constructor.
    private GendppPropertyManager() {}

	/**
	 * Get the singleton instance of the <code>GendppPropertyManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static GendppPropertyManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new GendppPropertyManager();
		return g_theManager;
	}
	
	/**
	 * Set the gendpp TCL property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the table-of-contents file.
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
					DPP_GENDPP_SCRIPT_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set TCL property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}

	    }
	}
	
	/**
	 * Set the default value for the gendpp TCL property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setScriptDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setScriptValue(resource,DPP_GENDPP_SCRIPT_VALUE);
	}
	
	/**
	 * Get the gendpp TCL property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the table-of-contents file will be returned.
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
					DPP_GENDPP_SCRIPT_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get TCL property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * Set the gendpp source directory property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the source directory.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setSourceDirValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENDPP_SOURCE_DIR_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set source dir property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Set the gendpp source directory property value on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setSourceDirDefaultValue(IResource resource) throws DppException
	{
	    setSourceDirValue(resource,DPP_GENDPP_SOURCE_DIR_VALUE);
	}
	
	/**
	 * Get the source directory from the specified resource.
	 * 
	 * @param resource The resource to get the property from.
	 * 
	 * @return The source directory is returned as a <code>String</code>.
	 * 
	 * @throws DppException  This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getSourceDirValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENDPP_SOURCE_DIR_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get source directory property from " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	    
	    return value;
	}
	
	/**
	 * A convenience method for setting the gendpp default properties.
	 * 
	 * @param resource The resource to set the properties on.
	 *
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the properties.
	 */
	public void setDefaults(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    setScriptDefaultValue(resource);
	    setSourceDirDefaultValue(resource);
	}

}
