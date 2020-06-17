/*
 * MasteringProjectManager.java
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
package com.wizzer.mle.studio.dpp.project;

// Import standard Java classes.
import java.util.StringTokenizer;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.CoreException;

// Import Magic Lantern Digital Workprint classes.
import com.wizzer.mle.studio.dwp.DwpProjectManager;

// Import Magic Lantern Digital Playprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppLog;


/**
 * This class is used to facilitate the Magic Lantern mastering process
 * for Digital Playprints.
 * <p>
 * It is used to associate mastering properties with Digital Workprint
 * resources.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class MasteringProjectManager
{
	/** The qualifier for DPP Project properties. */
	public static String DPP_PROJECT_QUALIFIER = "com.wizzer.mle.studio.dpp.project";
	/** The qualifier for the DPP resource properties. */
	public static String DPP_MASTERING_RESOURCE_QUALIFIER = "com.wizzer.mle.studio.dpp.resource";
	/** The DPP Project property type. */
	public static String DPP_PROJECT_TYPE_PROPERTY = "DPP_PROJECT_TYPE";
	/** The DPP Project property value. */
	public static String DPP_PROJECT_TYPE_VALUE = "DPP Project";
	/** The common byte order property. */
	public static String DPP_COMMON_BYTE_ORDER_PROPERTY = "DPP_COMMON_BYTE_ORDER";
	/** The Big Endian value for the common byte order property. */
	public static String DPP_COMMON_BYTE_ORDER_BE_VALUE = "Big Endian";
	/** The Little Endian value for the common byte order property. */
	public static String DPP_COMMON_BYTE_ORDER_LE_VALUE = "Little Endian";
	/** The common java package name property for generated code/components. */
	public static String DPP_COMMON_JAVA_PACKAGE_PROPERTY = "DPP_COMMON_JAVA_PACKAGE";
	/** The default destination directory. */
	public static String DPP_COMMON_JAVA_PACKAGE_DEFAULT_VALUE = "gen";
	/** The common destination directory property for generated code/components. */
	public static String DPP_COMMON_DESTINATION_DIR_PROPERTY = "DPP_COMMON_DESTINATION_DIR";
	/** The default destination directory. */
	public static String DPP_COMMON_DESTINATION_DIR_DEFAULT_VALUE = "src/gen";
	/** The common tags property. */
	public static String DPP_COMMON_TAGS_PROPERTY = "DPP_COMMON_TAGS";
	/** The common verbose property. */
	public static String DPP_COMMON_VERBOSE_PROPERTY = "DPP_COMMON_VERBOSE";
	
	/** The gengroup mastering property. */
	public static String DPP_GENGROUP_PROPERTY = "DPP_GENGROUP";
	/** The genscene mastering property. */
	public static String DPP_GENSCENE_PROPERTY = "DPP_GENSCENE";
	/** The genmedia mastering property. */
	public static String DPP_GENMEDIA_PROPERTY = "DPP_GENMEDIA";
	/** The gentables mastering property. */
	public static String DPP_GENTABLES_PROPERTY = "DPP_GENTABLES";
	/** The genppscript mastering property. */
	public static String DPP_GENPPSCRIPT_PROPERTY = "DPP_GENPPSCRIPT";
	/** The gendpp mastering property. */
	public static String DPP_GENDPP_PROPERTY = "DPP_GENDPP";
	
	public static String DPP_TARGET_TYPE_PROPERTY = "DPP_TARGET_TYPE";
	public static String DPP_TARGET_ID_PROPERTY = "DPP_TARGET_ID";
	public static String DPP_TARGET_DIGITAL_PLAYPRINT_PROPERTY = "DPP_TARGET_DIGITAL_PLAYPRINT";


	// The singleton instance.
	private static MasteringProjectManager g_theManager = null;
	
    /// Hide the default constructor.
    private MasteringProjectManager() {}

	/**
	 * Get the singleton instance of the <code>OcapProjectManager</code>.
	 * 
	 * @return The manager is returned.
	 */
	public static MasteringProjectManager getInstance()
	{
		if (g_theManager == null)
			g_theManager = new MasteringProjectManager();
		return g_theManager;
	}

	/**
	 * Initialize an DPP Project.
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
			QualifiedName key = new QualifiedName(DPP_PROJECT_QUALIFIER,
				DPP_PROJECT_TYPE_PROPERTY);
			project.setPersistentProperty(key,DPP_PROJECT_TYPE_VALUE);
		} catch (CoreException ex)
		{
			DppLog.logError(ex, "Unable to initialize DPP Project " +
			    project.getName() + ".");
			result = false;
		}
		
		return result;
	}
	
	/**
	 * Determine whether the specified project is a DPP Project or not.
	 * 
	 * @param project The project to test.
	 * 
	 * @return <b>true</b> is returned if the specified project is a DPP
	 * Project. Otherwise, <b>false</b> is returned.
	 */
	public boolean isDppProject(IProject project)
	{
		boolean result = false;

		try
		{
			QualifiedName key = new QualifiedName(DPP_PROJECT_QUALIFIER,
				DPP_PROJECT_TYPE_PROPERTY);
			String value = project.getPersistentProperty(key);
			if (value != null)
				result = true;
		} catch (CoreException ex)
		{
			DppLog.logError(ex, "Unable to determine whether " +
				project.getName() + " is a DPP Project.");
			result = false;
		}
		
		return result;
	}

	/**
	 * Set the common verbose mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to increase the verbosity of output
	 * messages. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setVerboseValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_VERBOSE_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set verbose property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the common verbose mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the output mode is verbose.
	 * Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getVerboseValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_VERBOSE_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get verbose property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if (value.equals("true"))
	        return true;
	    else
	        return false;
	}

	/**
	 * Set the common byte order property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>DPP_COMMON_BYTE_ORDER_BE_VALUE</b> to set the byte
	 * order to Big Endian.
	 * Otherwise, use <b>DPP_COMMON_BYTE_ORDER_LE_VALUE</b> for this parameter
	 * to set the byte order to LittleEndian.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setByteOrderValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_BYTE_ORDER_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set byte order property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Set the byte order to <b>DPP_COMMON_BYTE_ORDER_BE_VALUE</b>.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * testing the property.
	 */
	public void setBigEndian(IResource resource) throws DppException
	{
	    setByteOrderValue(resource,DPP_COMMON_BYTE_ORDER_BE_VALUE);
	}

	/**
	 * Set the byte order to <b>DPP_COMMON_BYTE_ORDER_LE_VALUE</b>.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * testing the property.
	 */
	public void setLittleEndian(IResource resource) throws DppException
	{
	    setByteOrderValue(resource,DPP_COMMON_BYTE_ORDER_LE_VALUE);
	}
	
	/**
	 * Test whether the specified resource has a byte order property
	 * value set to Big Endian.
	 * 
	 * @param resource The resrouce to test.
	 * 
	 * @return <b>true</b> is returned if the byte order property is set
	 * to Big Endian mode. Otherwise <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * settitestingng the property.
	 */
	public boolean isBigEndian(IResource resource) throws DppException
	{
	    boolean retValue = false;
	    
	    String value = getByteOrderValue(resource);
	    if (value != null)
		    if (value.equals(DPP_COMMON_BYTE_ORDER_BE_VALUE))
		        retValue = true;
	    
	    return retValue;
	}
	
	/**
	 * Test whether the specified resource has a byte order property
	 * value set to Little Endian.
	 * 
	 * @param resource The resrouce to test.
	 * 
	 * @return <b>true</b> is returned if the byte order property is set
	 * to Little Endian mode. Otherwise <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * testing the property.
	 */
	public boolean isLittleEndian(IResource resource) throws DppException
	{
	    boolean retValue = false;
	    
	    String value = getByteOrderValue(resource);
	    if (value != null)
		    if (value.equals(DPP_COMMON_BYTE_ORDER_LE_VALUE))
		        retValue = true;
	    
	    return retValue;
	}
	
	/**
	 * Get the common byte order property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the output mode is verbose.
	 * Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getByteOrderValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_BYTE_ORDER_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get byte order property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    return value;
	}

	/**
	 * Set the common java package property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the Java package.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setJavaPackageValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isJavaProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_COMMON_JAVA_PACKAGE_PROPERTY);
				project.setPersistentProperty(key,value);
			} catch (CoreException ex)
			{
				String message = new String("Unable to set java package property on " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    } else
	        throw new DppException("The resource must belong to a DPP Java Project.");
	}
		
	/**
	 * Set the default common java package property value on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setJavaPackageDefaultValue(IResource resource) throws DppException
	{
	    setJavaPackageValue(resource,DPP_COMMON_JAVA_PACKAGE_DEFAULT_VALUE);
	}

	/**
	 * Get the Java package name from the specified resource.
	 * 
	 * @param resource The resource to get the property from.
	 * 
	 * @return The name of the Java package is returned as a <code>String</code>.
	 * 
	 * @throws DppException  This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getJavaPackageValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
	    if (DwpProjectManager.getInstance().isJavaProject(project))
	    {
			try
			{
				QualifiedName key = new QualifiedName(
				    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
					DPP_COMMON_JAVA_PACKAGE_PROPERTY);
				value = project.getPersistentProperty(key);
			} catch (CoreException ex)
			{
				String message = new String("Unable to get java package property from " +
				    resource.getName() + ".");
				throw new DppException(message,ex);
			}
	    } else
	        throw new DppException("The resource must belong to a DPP Java Project.");
	    
	    return value;
	}

	/**
	 * Set the common destination directory property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value The name of the destination directory.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setDestinationDirValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_DESTINATION_DIR_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set destination dir property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Set the default common destination directory property value on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setDesitinationDirDefaultValue(IResource resource) throws DppException
	{
	    setDestinationDirValue(resource,DPP_COMMON_DESTINATION_DIR_DEFAULT_VALUE);
	}
	
	/**
	 * Get the destination directory from the specified resource.
	 * 
	 * @param resource The resource to get the property from.
	 * 
	 * @return The destination directory is returned as a <code>String</code>.
	 * 
	 * @throws DppException  This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String getDestinationDirValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_DESTINATION_DIR_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get destination directory property from " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	    
	    return value;
	}

	/**
	 * Set the common tags property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param values An array of tag values.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setTagsValue(IResource resource,String[] values) throws DppException
	{
	    StringBuffer buf = new StringBuffer();
	    for (int i = 0; i < values.length; i++)
	    {
	        buf.append(values[i]);
	        buf.append(" ");
	    }
	    String value = buf.toString().trim();
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_TAGS_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set tags property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}

	/**
	 * Get the tags from the specified resource.
	 * 
	 * @param resource The resource to get the property from.
	 * 
	 * @return The tags are returned as an array of <code>String</code>.
	 * If no tags are present, then <b>null</b> will be returned.
	 * 
	 * @throws DppException  This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public String[] getTagsValue(IResource resource) throws DppException
	{
	    String[] tags = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_COMMON_TAGS_PROPERTY);
			String value = project.getPersistentProperty(key);
			
			// Process the returned property value.
			if (value != null)
			{
			    StringTokenizer st = new StringTokenizer(value);
			    tags = new String[st.countTokens()];
			    int count = 0;
			    while (st.hasMoreElements())
			    {
			        tags[count] = new String(st.nextToken());
			        count++;
			    }
			}
		} catch (CoreException ex)
		{
			String message = new String("Unable to get tags property from " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	    
	    return tags;
	}
	
	/**
	 * Determine if the specified resource has tags associated with it.
	 * 
	 * @param resource The resource to test.
	 * 
	 * @return <b>true</b> will be returned if the resource has properties
	 * associated with it. <b>false</b> will be returned if it doesn't
	 * have any tags.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * testing the property.
	 */
	public boolean hasTags(IResource resource) throws DppException
	{
	    String[] tags = getTagsValue(resource);
	    if (tags == null)
	        return false;
	    else
	        return true;
	}
	
	/**
	 * Set the gengroup mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to indicate that gengroup properties
	 * exist on this resource. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGengroupValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENGROUP_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set gengroup property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the gengroup mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the resource contains gengroup
	 * properties. Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getGengroupValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENGROUP_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get gengroup property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if ((value != null) && (value.equals("true")))
	        return true;
	    else
	        return false;
	}
	
	/**
	 * Set the genscene mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to indicate that genscene properties
	 * exist on this resource. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGensceneValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENSCENE_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set genscene property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the genscene mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the resource contains genscene
	 * properties. Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getGensceneValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENSCENE_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get genscene property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if ((value != null) && (value.equals("true")))
	        return true;
	    else
	        return false;
	}
	
	/**
	 * Set the genmedia mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to indicate that genmedia properties
	 * exist on this resource. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGenmediaValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENMEDIA_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set genmedia property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the genmedia mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the resource contains genmedia
	 * properties. Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getGenmediaValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENMEDIA_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get genmedia property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if ((value != null) && (value.equals("true")))
	        return true;
	    else
	        return false;
	}
	
	/**
	 * Set the gentables mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to indicate that gentables properties
	 * exist on this resource. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGentablesValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENTABLES_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set gentables property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the gentables mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the resource contains gentables
	 * properties. Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getGentablesValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENTABLES_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get gentables property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if ((value != null) && (value.equals("true")))
	        return true;
	    else
	        return false;
	}

	/**
	 * Set the genppscript mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to indicate that genppscript properties
	 * exist on this resource. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGenppscriptValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENPPSCRIPT_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set genppscript property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the genppscript mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the resource contains genppscript
	 * properties. Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getGenppscriptValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENPPSCRIPT_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get genppscript property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if ((value != null) && (value.equals("true")))
	        return true;
	    else
	        return false;
	}
	
	/**
	 * Set the gendpp mode property on the specified resource.
	 * 
	 * @param resource The resource to set the property on.
	 * @param value Use <b>true</b> to indicate that gendpp properties
	 * exist on this resource. Otherwise, use <b>false</b> for this parameter.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * setting the property value.
	 */
	public void setGendppValue(IResource resource,boolean value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENDPP_PROPERTY);
			if (value)
			    project.setPersistentProperty(key,"true");
			else
			    project.setPersistentProperty(key,"false");
		} catch (CoreException ex)
		{
			String message = new String("Unable to set gendpp property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	/**
	 * Get the gendpp mode property from the specified resource.
	 * 
	 * @param resource The resource to retrieve the property from.
	 * 
	 * @return <b>true</b> is returned if the resource contains gendpp
	 * properties. Otherwise, <b>false</b> will be returned.
	 * 
	 * @throws DppException This exception is thrown if an error occurs while
	 * retrieving the property value.
	 */
	public boolean getGendppValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_GENDPP_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get gendpp property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    if ((value != null) && (value.equals("true")))
	        return true;
	    else
	        return false;
	}

	/**
	 * A convenience method for setting the common default properties for a
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
	        // Set the common properties.
		    setBigEndian(resource);
		    setDesitinationDirDefaultValue(resource);
		    setJavaPackageDefaultValue(resource);
		    setVerboseValue(resource,false);
	    } else
	        throw new DppException("Project is not a DPP Java Project.");
	}

	/**
	 * A convenience method for setting the common default properties for a
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
	        // Set the common properties.
		    setLittleEndian(resource);
		    setDesitinationDirDefaultValue(resource);
		    setVerboseValue(resource,false);
	    } else
	        throw new DppException("Project is not a DPP C++ Project.");
	}

	public void setTargetTypeValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_TARGET_TYPE_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set target type property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	public String getTargetTypeValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_TARGET_TYPE_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get target type property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    return value;
	}
	
	public void setTargetIdValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_TARGET_ID_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set target id property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	public String getTargetIdValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_TARGET_ID_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get target id property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    return value;
	}
	
	public void setTargetDigitalPlayprintValue(IResource resource,String value) throws DppException
	{
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_TARGET_DIGITAL_PLAYPRINT_PROPERTY);
			project.setPersistentProperty(key,value);
		} catch (CoreException ex)
		{
			String message = new String("Unable to set target digital playprint property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}
	}
	
	public String getTargetDigitalPlayprintValue(IResource resource) throws DppException
	{
	    String value = null;
	    
	    IProject project = resource.getProject();
		try
		{
			QualifiedName key = new QualifiedName(
			    MasteringProjectManager.DPP_MASTERING_RESOURCE_QUALIFIER,
				DPP_TARGET_DIGITAL_PLAYPRINT_PROPERTY);
			value = project.getPersistentProperty(key);
		} catch (CoreException ex)
		{
			String message = new String("Unable to get target type property on " +
			    resource.getName() + ".");
			throw new DppException(message,ex);
		}

	    return value;
	}

}
