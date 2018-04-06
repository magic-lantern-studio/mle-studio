/*
 * GentablesPropertyManager.java
 * Created on Aug 1, 2005
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
import com.wizzer.mle.studio.dwp.DwpProjectManager;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;

/**
 * This class provides utility for setting and retrieving properties on a
 * Digital Workprint resource. It is specifically used in conjunction with
 * the gentables mastering process.
 * 
 * @author Mark S. Millard
 */
public class GentablesPropertyManager
{
	/** The gentables tables file property. */
	public static String DPP_GENTABLES_TABLES_PROPERTY = "DPP_GENTABLES_TABLES";
	/** The gentables tables file property value for java code generation. */
	public static String DPP_GENTABLES_TABLES_JAVA_VALUE = "MleRuntimeTables.java";
	/** The gentables tables file property value for C++ code generation. */
	public static String DPP_GENTABLES_TABLES_CPP_VALUE = "mltables.cxx";

	// The singleton instance.
	private static GentablesPropertyManager g_theManager = null;
	
    /// Hide the default constructor.
    private GentablesPropertyManager() {}

	/**
	 * Get the singleton instance of the <code>GentablesPropertyManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static GentablesPropertyManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new GentablesPropertyManager();
		return g_theManager;
	}
	
	/**
	 * Set the gentables tables property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the tables file.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setTablesValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENTABLES_TABLES_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set tables property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}

	    }
	}
	
	/**
	 * Set the default value for the gengroup tables property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setTablesDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isJavaProject(project))
        {
	        setTablesValue(resource,DPP_GENTABLES_TABLES_JAVA_VALUE);
        } else if (DwpProjectManager.getInstance().isCppProject(project))
        {
	        setTablesValue(resource,DPP_GENTABLES_TABLES_CPP_VALUE);
        } else
        {
            String message = new String("Unknown resource type, " +
                resource.getName() + ".");
            throw new DppException(message);
        }
	}
	
	/**
	 * Get the gentables tables property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the tables file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getTablesValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENTABLES_TABLES_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get tables property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * A convenience method for setting the gentables default properties for a
	 * DPP Java Project.
	 * 
	 * @param resource The resource to set the properties on.
	 *
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the properties.
	 */
	public void setJavaDefaults(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    
	    if (DwpProjectManager.getInstance().isJavaProject(project))
	    {
	        // Set the gentables properties.
	        setTablesDefaultValue(resource);
	    } else
	        throw new DppException("Project is not a DPP Java Project.");
	}

	
	/**
	 * A convenience method for setting the gentables default properties for a
	 * DPP C++ Project.
	 * 
	 * @param resource The resource to set the properties on.
	 *
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the properties.
	 */
	public void setCppDefaults(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    
	    if (DwpProjectManager.getInstance().isCppProject(project))
	    {
	        // Set the gentables properties.
	        setTablesDefaultValue(resource);
	    } else
	        throw new DppException("Project is not a DPP C++ Project.");
	}

}
