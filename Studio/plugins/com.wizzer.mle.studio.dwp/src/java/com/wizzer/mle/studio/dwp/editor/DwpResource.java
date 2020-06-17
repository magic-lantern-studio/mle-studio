/*
 * DwpResource.java
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
package com.wizzer.mle.studio.dwp.editor;

// Import standard Java packages.
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Observer;
import java.util.Observable;

// Import Eclipse packages.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.QualifiedName;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.domain.IDwpTableHandler;

/**
 * This class is used to manage a DwpTable as an Eclipse workspace resource.
 * 
 * @author Mark S. Millard
 */
public class DwpResource implements IDwpResource, Observer
{
	/** The value of the DWP editor property. */
	public static String DWP_EDITOR_VALUE = "DWP.EDITOR.VALUE";
	
	// The containing folder for the DWP resource.
	private IFolder m_folder = null;
	// Flag indicating whether the resource needs to be synchronized with the file system.
	private boolean m_isDirty = false;
	
	/** The name of the resource. */
	protected String m_name = null;
	/** The Digital Workprint Table handler. */
	protected IDwpTableHandler m_dwp = null;
	
	// Hide the default constructor.
	private DwpResource() {}
	
	/**
	 * A constructor that associates the Digital Workprint Table handler
	 * with this reource.
	 * 
	 * @param table The Digital Workprint Table handler.
	 */
	public DwpResource(String name,IDwpTableHandler table)
	{
		super();
		
		m_name = name;
		m_dwp = table;
		
		// Add this resource as an Observer of the DWP Table handler.
		m_dwp.addObserver(this);
	}
	
	/**
	 * Get the name of the resource.
	 * 
	 * @return The name of the resource is returned as a <code>String</code>.
	 */
	public String getName()
	{
		return m_name;
	}
	
	/**
	 * Get the resource <code>IFolder</code> for the Digital Workprint Table.
	 * 
	 * @return A <code>IFolder</code> resource is returned indicating the location of
	 * the Digital Workprint Table within the context of a Magic Lantern project.
	 * 
	 * @see com.wizzer.mle.studio.dwp.editor.IDwpResource#getFolder()
	 */
	public IFolder getFolder()
	{
		return m_folder;
	}
	
	/**
	 * Set the <code>IFolder</code> resource for the Digital Workprint Table.
	 * 
	 * @param folder A <code>IFolder</code> resource indicating the location of
	 * the Digital Workprint Table.
	 *
	 * @see com.wizzer.mle.studio.dwp.editor.IDwpResource#setFolder()
	 */
	public void setFolder(IFolder folder)
	{
		m_folder = folder;
		createFileInFolder(m_folder);
	}

	/**
	 * Validate the <code>IFolder</code> resource for the Digital Workprint Table.
	 * 
	 * @return <b>true</b> will be returned if the resource is valid. Otherwise
	 * <b>false</b> shall be returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.editor.IDwpResource#validateFolder()
	 */
	public boolean validateFolder()
	{
		boolean valid = false;
		
		// Verify that the locator has been previously set.
		if (m_folder == null)
			return valid;
		   
		// Determine if the folder exists in a valid Magic Lantern project.
		if (m_folder.exists())
			valid = true;
		
		// Return the validation status.
		return valid;
	}
	
	// Build up an input stream of the DWP data. This method expects the data
	// to already be unmarshalled or previously created.
	private InputStream getDWP()
	{
		boolean marshalled = false;
		ByteArrayInputStream input = null;
		
		if (m_dwp == null) return null;
		
		// Marshall the DWP into an output stream.
		ByteArrayOutputStream dwpTableOutput = new ByteArrayOutputStream();
		marshalled = m_dwp.marshall(dwpTableOutput);
		
		// Create an input stream tha can be used to write the resource file.
		if (marshalled)
			input = new ByteArrayInputStream(dwpTableOutput.toByteArray());		    
		
		// Return the input stream containing the marshalled table.
		return input;
	}
	
	// Create the specified DWP.
	private void createFile(IFile dwp)
	{
		try
		{
			if (dwp.exists())
			{
				// If the IFile is already a resource in the workspace, replace the contents
				// with the current form of the DWP.
				InputStream dwpInput = getDWP();
				if (dwpInput != null)
					dwp.setContents(dwpInput,true,false,null);
				else
					DwpLog.logWarning("Unable to create resource: " + getName());
					// XXX - Should throw a DwpException here and replace it with logError().
			}
			else
			{
				// The file system is checked to see if the file already exists.
				// If the file is in the file system but not in the workspace,
				// the file creation is aborted.
				java.io.File systemFile = dwp.getLocation().toFile();
    			
				if (systemFile.exists())
				{
					// Skip creation, file already exists in the file system.
					// XXX - should refreshLocal on parent at this point, reloading
					// the table from the file.
    				
					DwpLog.logWarning("Unable to create resource: " + getName());
					// XXX - Should throw a DwpException here and replace it with logError().
				}
				else
				{
					// Create the new file in the file system.
					InputStream dwpInput = getDWP();
					if (dwpInput != null)
					{
						dwp.create(dwpInput,false,null);
    				    
						// Set a persistent property that can be recognized by the
						// DWP table editor.
						QualifiedName key = new QualifiedName(IDwpResource.DWP_QUALIFIER,
							IDwpResource.DWP_EDITOR_PROPERTY);
						String value = new String(DwpResource.DWP_EDITOR_VALUE);
						dwp.setPersistentProperty(key, value);
					}
					else
						DwpLog.logWarning("Unable to create resource: " + getName());
						// XXX - Should throw a DwpException here and replace it with logError().
				}
			}
		}
		catch (CoreException ex)
		{
			DwpLog.logError(ex,"Unable to create resource: " + getName());
		}
	}

	/**
	 * Create the file in the specified folder.
	 * 
	 * @param folder The folder to create the file resource under.
	 * 
	 * @return A file resource is returned for the newly created
	 * Digital Workprint.
	 */
	public IFile createFileInFolder(IFolder folder)
	{
		if (m_dwp == null)
		{
			DwpLog.logWarning("Unable to create resource: " + getName());
			// XXX - Should throw a DwpException here and replace this with logError().
			return null;
		}
    	
		// Get a handle to the IFile resource from the folder.
		IFile newDWP = folder.getFile(getName());
    	
		// Create the file using the handle.
		createFile(newDWP);
    	
		// Should be synchronized, but may not be if the system file previously existed
		// and this resource has not yet been saved.
		if (newDWP.isSynchronized(IResource.DEPTH_ZERO))
			m_isDirty = false;
		
		return newDWP;
	}

	/**
	 * Create the file in the specified project.
	 * 
	 * @param project The project to create the file resource under.
	 * 
	 * @return A file resource is returned for the newly created
	 * Digital Workprint.
	 */
	public IFile createFileInProject(IProject project)
	{
		if (m_dwp == null)
		{
			DwpLog.logWarning("Unable to create resource: " + getName());
			// XXX - Should throw a DwpException here and replace this with logError().
			return null;
		}
    	
		// Get a handle to the IFile resource from the folder.
		IFile newDWP = project.getFile(getName());

		// Create the file using the handle.
		createFile(newDWP);
     	
		// Should be synchronized, but may not be if the system file previously existed
		// and this resource has not yet been saved.
		if (newDWP.isSynchronized(IResource.DEPTH_ZERO))
			m_isDirty = false;
		
		return newDWP;
	}

	/**
	 * Unmarshall the Digital Workprint contents from the specified file resource.
	 * 
	 * @param file The <code>IFile</code> resource.
	 * 
	 * @return If the contents of the Digital Workprint are successfully retrieved,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> will be returned.
	 */
	public boolean unmarshall(IFile file)
	{
		boolean status = false;
    	
		if ((file != null) && (m_dwp != null))
		{
			if (file.exists())
			{
				try
				{
					InputStream input = file.getContents();
					status = m_dwp.unmarshall(input);
				} catch (CoreException ex)
				{
					DwpLog.logError(ex,"Unable to unmarshall contents from " + file.getName());
				}
			}
		}
    	
		return status;
	}
    
	/**
	 * Marshall the Digital Workprint contents to the specified file resource.
	 * 
	 * @param file The <code>IFile</code> resource.
	 * 
	 * @return If the contents of the Digital Workprint are successfully written,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> will be returned.
	 */
	public boolean marshall(IFile file)
	{
		boolean status = false;
    	
		if ((file != null) && (m_dwp != null))
		{
			if (file.exists())
			{
				try
				{
					InputStream dwpInput = getDWP();
					if (dwpInput != null)
					{
						file.setContents(dwpInput,true,false,null);
						status = true;
					}
					else
						DwpLog.logWarning("Unable to marshall resource: " + getName());
						// XXX - Should throw a DwpException here and replace it with logError().
				} catch (CoreException ex)
				{
					DwpLog.logError(ex,"Unable to marshall contents to " + file.getName());
				}
			}
		}
    	
		return status;
	}

    
	/**
	 * This method is called whenever the observed object is changed.
	 * <p>
	 * The <code>DwpResource</code> observes modifications made to the
	 * <code>DwpTableHandler</code>.
	 * </p>
	 * 
	 * @param obs The oberservable object.
	 * @param obj An argument passed by the observed object when
	 * <code>notifyObservers</code> is called.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable obs, Object obj)
	{
		// Only interested in observing DWP Tables.
		if (obs instanceof IDwpTableHandler)
		{
			// Make sure the folder is valid.
			if (validateFolder())
			{
				if (((Boolean)obj).booleanValue() == true)
					// Update or create contents of the file.
					createFileInFolder(m_folder);
				else
					// Mark resource as being dirty.
					m_isDirty = true;
			}
		}
	}
}
