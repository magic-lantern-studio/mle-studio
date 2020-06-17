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
package com.wizzer.mle.studio.project;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.MleException;
import com.wizzer.mle.studio.MleLog;

/**
 * This class is used to manage the Magic Lantern Studio Project
 * meta-data.
 * 
 * @author Mark S. Millard
 */
public class MleStudioProjectManager
{
    /** The qualifier for MLE Studio Project properties. */
    public static final String MLE_STUDIO_PROJECT_QUALIFIER = "com.wizzer.mle.studio.project";
    /** The MLE Studio Project type property. */
    public static final String MLE_STUDIO_PROJECT_TYPE_PROPERTY = "MLE_STUDIO_PROJECT_TYPE";
    /** The MLE Studio Application property type. */
    public static final String MLE_STUDIO_APPLICATION_TYPE_PROPERTY = "MLE_STUDIO_APPLICATION_TYPE";
    /** The name of the MLE Studio Project meta-data file. */
    public static final String MLE_STUDIO_PROJECT_METADATA_FILENAME = ".mleproject";
	/** The MLE Studio Project id property. */
	public static String MLE_STUDIO_PROJECT_ID_PROPERTY = "MLE_STUDIO_PROJECT_ID";
	/** The MLE Studio Project version property. */
	public static String MLE_STUDIO_PROJECT_VERSION_PROPERTY = "MLE_STUDIO_PROJECT_VERSION";
	/** The MLE Studio Digital Workprint property. */
	public static String MLE_STUDIO_DIGITAL_WORKPRINT_PROPERTY = "MLE_STUDIO_DIGITAL_WORKPRINT";

    
    // The singleton instance.
    private static MleStudioProjectManager g_theManager = null;

    // Hide the default constructor.
    private MleStudioProjectManager()
    {}

    /**
     * Get the singleton instance of the <code>MleStudioProjectManager</code>.
     * 
     * @return The manager is returned.
     */
    public static MleStudioProjectManager getInstance()
    {
        if (g_theManager == null) g_theManager = new MleStudioProjectManager();
        return g_theManager;
    }
    
    /**
     * Initialize an Mle Studio Project.
     * 
     * @param project The <code>IProject</code> to initialize.
     * 
     * @return <b>true</b> is returned if the specified project is successfully
     * initialized. Otherwise, <b>false</b> is returned.
     */
    public boolean init(IProject project, String projectType)
    {
        boolean result = true;

        try
        {
            // Flag the project as a Magic Lantern Studio Project.
            QualifiedName key = new QualifiedName(MLE_STUDIO_PROJECT_QUALIFIER, MLE_STUDIO_PROJECT_TYPE_PROPERTY);
            project.setPersistentProperty(key, projectType);

        } catch (CoreException ex)
        {
        	MleLog.logError(ex, "Unable to initialize Magic Lantern Project " + project.getName() + ".");
            result = false;
        }

        return result;
    }
    
    /**
     * Determine whether the specified project is a Magic Lantern
     * Studio Project or not.
     * 
     * @param project The project to test.
     * @param projectType The type of project to test against.
     * 
     * @return <b>true</b> is returned if the specified project is a
     * Magic Lantern Studio Project. Otherwise, <b>false</b> is returned.
     */
    public boolean isMleProject(IProject project, String projectType)
    {
        boolean result = false;

        try
        {
            QualifiedName key = new QualifiedName(MLE_STUDIO_PROJECT_QUALIFIER, MLE_STUDIO_PROJECT_TYPE_PROPERTY);
            String value = project.getPersistentProperty(key);
            if ((value != null) && (value.equals(projectType))) result = true;
        } catch (CoreException ex)
        {
        	MleLog.logError(ex, "Unable to determine whether " + project.getName() + " is a Magic Lantern Studio Project.");
            result = false;
        }

        return result;
    }
    
	public void setProjectIdValue(IResource resource, String value) throws MleException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MleStudioProjectManager.MLE_STUDIO_PROJECT_QUALIFIER,
				MLE_STUDIO_PROJECT_ID_PROPERTY);
			project.setPersistentProperty(key, value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set project id property on " +
			    resource.getName() + ".");
			throw new MleException(message,ex);
		}
	}
	
	public String getProjectIdValue(IResource resource) throws MleException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
				MleStudioProjectManager.MLE_STUDIO_PROJECT_QUALIFIER,
				MLE_STUDIO_PROJECT_ID_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get project id property on " +
			    resource.getName() + ".");
			throw new MleException(message,ex);
		}

	    return value;
	}
	
	public void setProjectVersionValue(IResource resource, String value) throws MleException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MleStudioProjectManager.MLE_STUDIO_PROJECT_QUALIFIER,
				MLE_STUDIO_PROJECT_VERSION_PROPERTY);
			project.setPersistentProperty(key, value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set project version property on " +
			    resource.getName() + ".");
			throw new MleException(message,ex);
		}
	}
	
	public String getProjectVersionValue(IResource resource) throws MleException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
				MleStudioProjectManager.MLE_STUDIO_PROJECT_QUALIFIER,
				MLE_STUDIO_PROJECT_VERSION_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get project version property on " +
			    resource.getName() + ".");
			throw new MleException(message,ex);
		}

	    return value;
	}
	
	public void setDigitalWorkprintValue(IResource resource, String value) throws MleException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MleStudioProjectManager.MLE_STUDIO_PROJECT_QUALIFIER,
				MLE_STUDIO_DIGITAL_WORKPRINT_PROPERTY);
			project.setPersistentProperty(key, value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set digital workprint property on " +
			    resource.getName() + ".");
			throw new MleException(message,ex);
		}
	}
	
	public String getDigitalWorkprintValue(IResource resource) throws MleException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
				MleStudioProjectManager.MLE_STUDIO_PROJECT_QUALIFIER,
				MLE_STUDIO_DIGITAL_WORKPRINT_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get digital workprint property on " +
			    resource.getName() + ".");
			throw new MleException(message,ex);
		}

	    return value;
	}
}
