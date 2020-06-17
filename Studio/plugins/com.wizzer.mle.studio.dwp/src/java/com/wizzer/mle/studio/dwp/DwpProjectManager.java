/*
 * DwpProjectManager.java
 */

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
package com.wizzer.mle.studio.dwp;

// Import standard Java classes.

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.CoreException;

// Import Magic Lantern Digital Playprint classes.
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpLog;


/**
 * This class is used to facilitate the Magic Lantern project management.
 * 
 * @author Mark S. Millard
 */
public class DwpProjectManager
{
	/** The qualifier for DWP Project properties. */
	public static String DWP_PROJECT_QUALIFIER = "com.wizzer.mle.studio.dwp.project";
	/** The DWP Project property type. */
	public static String DWP_PROJECT_TYPE_PROPERTY = "DWP_PROJECT_TYPE";
	/** The DWP NULL Project property value. */
	public static String DWP_PROJECT_TYPE_NULL_VALUE = "DWP NULL Project";
	/** The DWP Java Project property value. */
	public static String DWP_PROJECT_TYPE_JAVA_VALUE = "DWP Java Project";
	/** The DWP C++ Project property value. */
	public static String DWP_PROJECT_TYPE_CPP_VALUE = "DWP C++ Project";
	/** The DWP Rehearsal Project property value. */
	public static String DWP_PROJECT_TYPE_REHEARSAL_VALUE = "DWP Rehearsal Project";

	// The singleton instance.
	private static DwpProjectManager g_theManager = null;
	
    /// Hide the default constructor.
    private DwpProjectManager() {}

	/**
	 * Get the singleton instance of the <code>DwpProjectManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static DwpProjectManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new DwpProjectManager();
		return g_theManager;
	}

	/**
	 * Initialize an DWP Project.
	 * 
	 * @param project The <code>IProject</code> to initialize.
	 * 
	 * @return <b>true</b> is returned if the specified project is successfully
	 * initialized. Otherwise, <b>false</b> is returned.
	 */
	public boolean init(IProject project)
	{
		boolean result = true;

		try
		{
			QualifiedName key = new QualifiedName(DWP_PROJECT_QUALIFIER,
			        DWP_PROJECT_TYPE_PROPERTY);
			project.setPersistentProperty(key,DWP_PROJECT_TYPE_NULL_VALUE);
		} catch (CoreException ex)
		{
			DwpLog.logError(ex, "Unable to initialize Magic Lantern Project " +
			    project.getName() + ".");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Determine whether the specified project is a DWP Project or not.
	 * 
	 * @param project The project to test.
	 * 
	 * @return <b>true</b> is returned if the specified project is a Magic
	 * Lantern Project. Otherwise, <b>false</b> is returned.
	 */
	public boolean isDWPProject(IProject project)
	{
		boolean result = false;

		try
		{
			QualifiedName key = new QualifiedName(DWP_PROJECT_QUALIFIER,
			        DWP_PROJECT_TYPE_PROPERTY);
			String value = project.getPersistentProperty(key);
			if (value != null)
				result = true;
		} catch (CoreException ex)
		{
			DwpLog.logError(ex, "Unable to determine whether " +
				project.getName() + " is a Magic Lantern Project.");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Determine whether the specified project is a DWP Java Project or not.
	 * 
	 * @param project The project to test.
	 * 
	 * @return <b>true</b> is returned if the specified project is a Magic
	 * Lantern Java Project. Otherwise, <b>false</b> is returned.
	 */
	public boolean isJavaProject(IProject project)
	{
		boolean result = false;

		try
		{
			QualifiedName key = new QualifiedName(DWP_PROJECT_QUALIFIER,
			        DWP_PROJECT_TYPE_PROPERTY);
			String value = project.getPersistentProperty(key);
			if (value != null)
			{
			    if (value.equals(DWP_PROJECT_TYPE_JAVA_VALUE))
			        result = true;
			}
		} catch (CoreException ex)
		{
			DwpLog.logError(ex, "Unable to determine whether " +
				project.getName() + " is a Magic Lantern Java Project.");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Determine whether the specified project is a DWP C++ Project or not.
	 * 
	 * @param project The project to test.
	 * 
	 * @return <b>true</b> is returned if the specified project is a Magic
	 * Lantern C++ Project. Otherwise, <b>false</b> is returned.
	 */
	public boolean isCppProject(IProject project)
	{
		boolean result = false;

		try
		{
			QualifiedName key = new QualifiedName(DWP_PROJECT_QUALIFIER,
			    DWP_PROJECT_TYPE_PROPERTY);
			String value = project.getPersistentProperty(key);
			if (value != null)
			{
			    if (value.equals(DWP_PROJECT_TYPE_CPP_VALUE))
			        result = true;
			}
		} catch (CoreException ex)
		{
			DwpLog.logError(ex, "Unable to determine whether " +
				project.getName() + " is a Magic Lantern C++ Project.");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Determine whether the specified project is a DWP Rehearsal Project or not.
	 * 
	 * @param project The project to test.
	 * 
	 * @return <b>true</b> is returned if the specified project is a Magic
	 * Lantern Rehearsal Project. Otherwise, <b>false</b> is returned.
	 */
	public boolean isRehearsalProject(IProject project)
	{
		boolean result = false;

		try
		{
			QualifiedName key = new QualifiedName(DWP_PROJECT_QUALIFIER,
			    DWP_PROJECT_TYPE_PROPERTY);
			String value = project.getPersistentProperty(key);
			if (value != null)
			{
			    if (value.equals(DWP_PROJECT_TYPE_REHEARSAL_VALUE))
			        result = true;
			}
		} catch (CoreException ex)
		{
			DwpLog.logError(ex, "Unable to determine whether " +
				project.getName() + " is a Magic Lantern Rehearsal Project.");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Set the project type property for the specified <code>IProject</code> resource.
	 * 
	 * @param project The project resource.
	 * @param value The DWP Project type. Valid values include:
	 * <ul>
	 * <li>DWP_PROJECT_TYPE_NULL_VALUE</li>
	 * <li>DWP_PROJECT_TYPE_JAVA_VALUE</li>
	 * <li>DWP_PROJECT_TYPE_CPP_VALUE</li>
	 * <li>DWP_PROJECT_TYPE_REHEARSAL_VALUE</li>
	 * </ul>
	 * 
	 * @throws DwpException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	protected void setProject(IProject project, String value) throws DwpException
	{
		try
		{
			QualifiedName key = new QualifiedName(
			    DwpProjectManager.DWP_PROJECT_QUALIFIER,
			    DWP_PROJECT_TYPE_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set DWP Project property on " +
			    project.getName() + ".");
			throw new DwpException(message,ex);
		}
	}
	
	/**
	 * Set the project type to <b>DWP_PROJECT_TYPE_NULL_VALUE</b>.
	 * <p>
	 * A null project type does not have any target associated with it.
	 * </p>
	 * 
	 * @param project The project resource.
	 * 
	 * @throws DwpException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setNullProject(IProject project) throws DwpException
	{
	    setProject(project,DWP_PROJECT_TYPE_NULL_VALUE);
	}

	/**
	 * Set the project type to <b>DWP_PROJECT_TYPE_JAVA_VALUE</b>.
	 * <p>
	 * A Java project type is used to associate properties for
	 * the Java programming language.
	 * </p>
	 * 
	 * @param project The project resource.
	 * 
	 * @throws DwpException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setJavaProject(IProject project) throws DwpException
	{
	    setProject(project,DWP_PROJECT_TYPE_JAVA_VALUE);
	}

	/**
	 * Set the project type to <b>DWP_PROJECT_TYPE_CPP_VALUE</b>.
	 * <p>
	 * A C++ project type is used to associate properties for
	 * the C++ programming language.
	 * </p>
	 * 
	 * @param project The project resource.
	 * 
	 * @throws DwpException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setCppProject(IProject project) throws DwpException
	{
	    setProject(project,DWP_PROJECT_TYPE_CPP_VALUE);
	}

	/**
	 * Set the project type to <b>DWP_PROJECT_TYPE_REHEARSAL_VALUE</b>.
	 * <p>
	 * A Rehearsal project type is used to associate properties for
	 * the Inventor platform.
	 * </p>
	 * 
	 * @param project The project resource.
	 * 
	 * @throws DwpException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setRehearsalProject(IProject project) throws DwpException
	{
	    setProject(project,DWP_PROJECT_TYPE_REHEARSAL_VALUE);
	}
}
