/*
 * GengroupPropertyManager.java
 * Created on Jun 24, 2005
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
import com.wizzer.mle.studio.dwp.DwpProjectManager;

// Import Magic Lantern Digital Playprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;

/**
 * This class provides utility for setting and retrieving properties on a
 * Digital Workprint resource. It is specifically used in conjunction with
 * the gengroup mastering process.
 * 
 * @author Mark S. Millard
 */
public class GengroupPropertyManager
{
	/** The gengroup actor id file property. */
	public static String DPP_GENGROUP_ACTORID_PROPERTY = "DPP_GENGROUP_ACTORID";
	/** The gengroup actor id file property value for java code generation. */
	public static String DPP_GENGROUP_ACTORID_JAVA_VALUE = "ActorID.java";
	/** The gengroup actor id file property value for C++ code generation. */
	public static String DPP_GENGROUP_ACTORID_CPP_VALUE = "actorid.h";
	/** The gengroup group id file property. */
	public static String DPP_GENGROUP_GROUPID_PROPERTY = "DPP_GENGROUP_GROUPID";
	/** The gengroup group id file property value for java code generation. */
	public static String DPP_GENGROUP_GROUPID_JAVA_VALUE = "GroupID.java";
	/** The gengroup group id file property value for C++ code generation. */
	public static String DPP_GENGROUP_GROUPID_CPP_VALUE = "groupid.h";
	/** The gengroup fixed-point arithmetic property. */
	public static String DPP_GENGROUP_FIXEDPOINT_PROPERTY = "DPP_GENGROUP_FIXEDPOINT";

	// The singleton instance.
	private static GengroupPropertyManager g_theManager = null;
	
    /// Hide the default constructor.
    private GengroupPropertyManager() {}

	/**
	 * Get the singleton instance of the <code>GengroupPropertyManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static GengroupPropertyManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new GengroupPropertyManager();
		return g_theManager;
	}
	
	/**
	 * Set the gengroup Actor ID property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the Actor ID file.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setActorIdValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENGROUP_ACTORID_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set Actor ID property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}

	    }
	}
	
	/**
	 * Set the default value for the gengroup Actor ID property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setActorIdDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isJavaProject(project))
        {
	        setActorIdValue(resource,DPP_GENGROUP_ACTORID_JAVA_VALUE);
        } else if (DwpProjectManager.getInstance().isCppProject(project))
        {
	        setActorIdValue(resource,DPP_GENGROUP_ACTORID_CPP_VALUE);
        } else
        {
            String message = new String("Unknown resource type, " +
                resource.getName() + ".");
            throw new DppException(message);
        }
	}
	
	/**
	 * Get the gengroup Actor Id property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the Actor Id file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getActorIdValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENGROUP_ACTORID_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get Actor ID property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * Set the gengroup Group ID property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the Group ID file.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGroupIdValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENGROUP_GROUPID_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set Group ID property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}

	    }
	}
	
	/**
	 * Set the default value for the gengroup Group ID property.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGroupIdDefaultValue(IResource resource) throws DppException
	{
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isJavaProject(project))
        {
	        setGroupIdValue(resource,DPP_GENGROUP_GROUPID_JAVA_VALUE);
        } else if (DwpProjectManager.getInstance().isCppProject(project))
        {
	        setGroupIdValue(resource,DPP_GENGROUP_GROUPID_CPP_VALUE);
        } else
        {
            String message = new String("Unknown resource type, " +
                resource.getName() + ".");
            throw new DppException(message);
        }
	}
	
	/**
	 * Get the gengroup Group Id property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return The name of the Group Id file will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getGroupIdValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (MasteringProjectManager.getInstance().isDppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENGROUP_GROUPID_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get Group ID property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    }

	    return value;
	}
	
	/**
	 * Set the gengroup fixed-point arithmetic property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> if the gengroup command should be generating
	 * values using fixed-point arithmetic values. Otherwise use <b>false</b> for
	 * floating-point values.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setFixedPointValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isCppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENGROUP_FIXEDPOINT_PROPERTY);
				if (value)
				    project.setPersistentProperty(key,"true");
				else
				    project.setPersistentProperty(key,"false");
			} catch (CoreException ex)
			{
				String message = new String("Unable to set fixed-point arithmetic property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    } else
	        throw new DppException("Project is not a DPP C++ Project.");
	}
	
	/**
	 * Get the gengroup fixed-point arithmetic property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the arithmetic mode is fixed-point.
	 * Otherwise, <b>false</b> will be returned for floating-point mode.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getFixedPointValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isCppProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_GENGROUP_FIXEDPOINT_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get fixed-point arithmetic property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    } else
	        throw new DppException("Project is not a DPP C++ Project.");

	    if (value.equals("true"))
	        return true;
	    else
	        return false;
	}
	
	/**
	 * A convenience method for setting the gengroup default properties for a
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
	        // Set the gengroup properties.
	        setActorIdDefaultValue(resource);
	        setGroupIdDefaultValue(resource);
	    } else
	        throw new DppException("Project is not a DPP Java Project.");
	}

	
	/**
	 * A convenience method for setting the gengroup default properties for a
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
	        // Set the gengroup properties.
	        setActorIdDefaultValue(resource);
	        setGroupIdDefaultValue(resource);
	        setFixedPointValue(resource,false);
	    } else
	        throw new DppException("Project is not a DPP C++ Project.");
	}

}
