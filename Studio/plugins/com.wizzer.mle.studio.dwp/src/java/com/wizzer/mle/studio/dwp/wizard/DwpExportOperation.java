/*
 * DwpExportOperation.java
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
package com.wizzer.mle.studio.dwp.wizard;

// Import standard Java classes.
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.*;

// Import Eclipse classes.
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.*;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.operation.ModalContext;
import org.eclipse.ui.dialogs.IOverwriteQuery;

// Import Magic Lantern Digital Workprint classes.
import com.wizzer.mle.studio.dwp.DwpPlugin;
import com.wizzer.mle.studio.dwp.DwpUtilities;

/**
 *	Operation for exporting the contents of a resource to the local file system.
 */
class DwpExportOperation implements IRunnableWithProgress
{
	private IPath m_path;
	private IProgressMonitor m_monitor;
	private DwpExporter m_exporter = new DwpExporter();
	private List m_resourcesToExport;
	private IOverwriteQuery m_overwriteCallback;
	private IResource m_resource;
	private List m_errorTable = new ArrayList(1);

	// The constants for the overwrite 3 state.
	private static final int OVERWRITE_NOT_SET = 0;
	private static final int OVERWRITE_NONE = 1;
	private static final int OVERWRITE_ALL = 2;
	private int m_overwriteState = OVERWRITE_NOT_SET;

	private boolean m_createLeadupStructure = true;
	private boolean m_createContainerDirectories = true;
	
	private int m_exportType = DwpExporter.ASCII_FILE_FORMAT;
	private static String m_asciiMagicNumber = "#DWP 1.0 ascii";
	
	/**
	 *  Create an instance of this class.  Use this constructor if you wish to
	 *  export specific resources without a common parent resource
	 */
	public DwpExportOperation(
		List resources,
		String destinationPath,
		IOverwriteQuery overwriteImplementor)
	{
		super();

		// Eliminate redundancies in list of resources being exported.
		Iterator elementsEnum = resources.iterator();
		while (elementsEnum.hasNext())
		{
			IResource currentResource = (IResource) elementsEnum.next();
			if (isDescendent(resources, currentResource))
				elementsEnum.remove(); //Remove currentResource
		}

		m_resourcesToExport = resources;
		m_path = new Path(destinationPath);
		m_overwriteCallback = overwriteImplementor;
	}
	
	/**
	 *  Create an instance of this class.  Use this constructor if you wish to
	 *  recursively export a single resource.
	 */
	public DwpExportOperation(
		IResource res,
		String destinationPath,
		IOverwriteQuery overwriteImplementor)
	{
		super();
		m_resource = res;
		m_path = new Path(destinationPath);
		m_overwriteCallback = overwriteImplementor;
	}
	
	/**
	 *  Create an instance of this class.  Use this constructor if you wish to
	 *  export specific resources with a common parent resource (affects container
	 *  directory creation).
	 */
	public DwpExportOperation(
		IResource res,
		List resources,
		String destinationPath,
		IOverwriteQuery overwriteImplementor)
	{
		this(res, destinationPath, overwriteImplementor);
		m_resourcesToExport = resources;
	}

	/**
	 * Add a new entry to the error table with the passed information.
	 */
	protected void addError(String message, Throwable e)
	{
		m_errorTable.add(
			new Status(IStatus.ERROR, DwpPlugin.getID(), 0, message, e));
	}

	/**
	 * Answer the total number of file resources that exist at or below self in the
	 * resources hierarchy.
	 *
	 * @param parentResource The resource to count.
	 * 
	 * @return The number of children is returned as an integer.
	 */
	protected int countChildrenOf(IResource parentResource) throws CoreException
	{
		if (parentResource.getType() == IResource.FILE)
			return 1;

		int count = 0;
		if (parentResource.isAccessible())
		{
			IResource[] children = ((IContainer) parentResource).members();
			for (int i = 0; i < children.length; i++)
				count += countChildrenOf(children[i]);
		}

		return count;
	}
	
	/**
	 *	Determine the number of file resources that were
	 *	specified for export.
	 *
	 *	@return The number of selected resources is returned.
	 */
	protected int countSelectedResources() throws CoreException
	{
		int result = 0;
		Iterator resources = m_resourcesToExport.iterator();

		while (resources.hasNext())
			result += countChildrenOf((IResource) resources.next());

		return result;
	}

	/**
	 *  Create the directories required for exporting the passed resource,
	 *  based upon its container hierarchy.
	 *
	 *  @param childResource The resource to create the directories for.
	 */
	protected void createLeadupDirectoriesFor(IResource childResource)
	{
		IPath resourcePath = childResource.getFullPath().removeLastSegments(1);

		for (int i = 0; i < resourcePath.segmentCount(); i++)
		{
			m_path = m_path.append(resourcePath.segment(i));
			m_exporter.createFolder(m_path);
		}
	}

	/**
	 *	Recursively export the previously-specified resource.
	 */
	protected void exportAllResources() throws InterruptedException
	{
		if (m_resource.getType() == IResource.FILE)
			exportFile((IFile) m_resource, m_path);
		else
		{
			try {
				exportChildren(((IContainer) m_resource).members(), m_path);
			} catch (CoreException ex)
			{
				// Not safe to show a dialog.
				// Should never happen because the file system export wizard ensures that the
				// single resource chosen for export is both existent and accessible.
				m_errorTable.add(ex);
			}
		}
	}
	
	/**
	 *	Export all of the resources contained in the passed collection.
	 *
	 *	@param children The collection of children to export.
	 *
	 *	@param currentPath IPath The m_path to export to.
	 */
	protected void exportChildren(IResource[] children, IPath currentPath)
		throws InterruptedException
    {
		for (int i = 0; i < children.length; i++)
		{
			IResource child = children[i];
			if (! child.isAccessible())
				continue;

			if (child.getType() == IResource.FILE)
				exportFile((IFile) child, currentPath);
			else {
				IPath destination = currentPath.append(child.getName());
				m_exporter.createFolder(destination);
				try
				{
					exportChildren(((IContainer) child).members(), destination);
				} catch (CoreException ex)
				{
					// Not safe to show a dialog.
					// Should never happen because:
					// i. this method is called recursively iterating over the result of #members,
					//    which only answers existing children,
					// ii. there is an #isAccessible check done before #members is invoked.
					m_errorTable.add(ex.getStatus());
				}
			}
		}
	}
	
	/**
	 *  Export the passed file to the specified location.
	 *
	 *  @param file The file to export.
	 *  @param location The m_path to export to.
	 */
	protected void exportFile(IFile file, IPath location)
		throws InterruptedException
	{
		IPath fullPath = location.append(file.getName());
		m_monitor.subTask(file.getFullPath().toString());
		String properPathString = fullPath.toOSString();
		File targetFile = new File(properPathString);
		
		// Verify that the file to transfer is a DWP.
		try
		{
			if (! DwpUtilities.isDigitalWorkprint(file))
			{
			    m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
					DwpExportWizardMessages.getFormattedString("DwpExportOperation.notDigitalWorkprint",
					new Object[] { file.getFullPath().toOSString() }), null));
			    m_monitor.worked(1);
				return;
			}
		} catch (FileNotFoundException ex)
		{
		    m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
				DwpExportWizardMessages.getFormattedString("DwpExportOperation.fileNotFound",
				new Object[] { file.getName() }), null));
		    return;
		} catch (IOException ex)
		{
		    m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
				DwpExportWizardMessages.getFormattedString("DwpExportOperation.ioError",
				new Object[] { file.getName() }), null));
			return;		    
		}

		// Determine if we can overwrite an existing file.
		if (targetFile.exists())
		{
			if (! targetFile.canWrite())
			{
				m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
				    DwpExportWizardMessages.getFormattedString("DwpExportOperation.cannotOverwrite",
				        new Object[] { targetFile.getAbsolutePath() }), null));
				m_monitor.worked(1);
				return;
			}

			if (m_overwriteState == OVERWRITE_NONE)
				return;
				
			if (m_overwriteState != OVERWRITE_ALL)
			{
				String overwriteAnswer =
					m_overwriteCallback.queryOverwrite(properPathString);

				if (overwriteAnswer.equals(IOverwriteQuery.CANCEL))
					throw new InterruptedException();

				if (overwriteAnswer.equals(IOverwriteQuery.NO))
				{
					m_monitor.worked(1);
					return;
				}

				if (overwriteAnswer.equals(IOverwriteQuery.NO_ALL))
				{
					m_monitor.worked(1);
					m_overwriteState = OVERWRITE_NONE;
					return;
				}

				if (overwriteAnswer.equals(IOverwriteQuery.ALL))
					m_overwriteState = OVERWRITE_ALL;
			}
		}

		try
		{
			m_exporter.write(file, fullPath, m_exportType);
		} catch (IOException ex)
		{
			m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
			    DwpExportWizardMessages.getFormattedString("DwpExportOperation.errorExporting",
			    new Object[] { fullPath, ex.getMessage()}), ex));
		} catch (CoreException ex)
		{
			m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
			    DwpExportWizardMessages.getFormattedString("DwpExportOperation.errorExporting",
			new Object[] { fullPath, ex.getMessage()}), ex));
		}

		m_monitor.worked(1);
		ModalContext.checkCanceled(m_monitor);
	}

	/**
	 *	Export the resources contained in the previously-defined
	 *	m_resourcesToExport collection
	 */
	protected void exportSpecifiedResources() throws InterruptedException
	{
		Iterator resources = m_resourcesToExport.iterator();
		IPath initPath = (IPath) m_path.clone();

		while (resources.hasNext())
		{
			IResource currentResource = (IResource) resources.next();
			if (! currentResource.isAccessible())
				continue;

			m_path = initPath;

			if (m_resource == null)
			{
				// No root resource specified and creation of containment directories
				// is required. Create containers from depth 2 onwards (ie.- project's
				// child inclusive) for each resource being exported.
				if (m_createLeadupStructure)
					createLeadupDirectoriesFor(currentResource);

			} else
			{
				// Root resource specified.  Must create containment directories
				// from this point onwards for each resource being exported
				IPath containersToCreate =
					currentResource
						.getFullPath()
						.removeFirstSegments(m_resource.getFullPath().segmentCount())
						.removeLastSegments(1);

				for (int i = 0; i < containersToCreate.segmentCount(); i++)
				{
					m_path = m_path.append(containersToCreate.segment(i));
					m_exporter.createFolder(m_path);
				}
			}

			if (currentResource.getType() == IResource.FILE)
				exportFile((IFile) currentResource, m_path);
			else
			{
				if (m_createContainerDirectories)
				{
					m_path = m_path.append(currentResource.getName());
					m_exporter.createFolder(m_path);
				}

				try
				{
					exportChildren(
						((IContainer) currentResource).members(),
						m_path);
				} catch (CoreException ex)
				{
					// Should never happen because #isAccessible is called before #members is invoked,
					// which implicitly does an existence check.
					m_errorTable.add(ex.getStatus());
				}
			}
		}
	}
	
	/**
	 * Returns the status of the export operation.
	 * <p>
	 * If there were any errors, the result is a status object containing
	 * individual status objects for each error.
	 * If there were no errors, the result is a status object with error code <code>OK</code>.
	 * </p.
	 *
	 * @return The status is returned.
	 */
	public IStatus getStatus()
	{
		IStatus[] errors = new IStatus[m_errorTable.size()];
		m_errorTable.toArray(errors);
		return new MultiStatus(DwpPlugin.getID(), IStatus.OK, errors,
		    DwpExportWizardMessages.getString("DwpExportOperation.problemsExporting"),
		    null);
	}
	
	/**
	 * Answer a boolean indicating whether the passed child is a descendent
	 * of one or more members of the passed resources collection.
	 *
	 * @param resources The collection of resources.
	 * @param child The child resource to test.
	 * 
	 * @return <b>true</b> will be returned if the child is a descendent
	 * of one or more members of the passed resources collection.
	 * If it isn't, then <b>false</b> will be returned.
	 */
	protected boolean isDescendent(List resources, IResource child)
	{
		if (child.getType() == IResource.PROJECT)
			return false;

		IResource parent = child.getParent();
		if (resources.contains(parent))
			return true;

		return isDescendent(resources, parent);
	}
	
	/**
	 * Determine whether the specified file is a Digital Workprint.
	 * 
	 * @param file The file resource to test.
	 * 
	 * @return <b>true</b> will be returned if the file is a
	 * Digital Workprint. <b>false</b> will be returned if it isn't.
	 */
	protected boolean isDigitalWorkprint(IFile file)
	{
	    boolean retValue = false;
	    
	    IPath fullPath = file.getLocation();
		String properPathString = fullPath.toOSString();
		File testFile = new File(properPathString);
		
		try
		{
		    FileReader reader = new FileReader(testFile);
		    
		    char[] cbuf = new char[m_asciiMagicNumber.length()];
		    int offset = 0;
		    int n;

		    while (!reader.ready())
		        ; // Wait until ready.
		    while (offset < m_asciiMagicNumber.length())
		    {
		        n = reader.read(cbuf,offset,m_asciiMagicNumber.length() - offset);
		        if (n == -1)
		        {
				    // Note: this is not an error.
		            reader.close();
					return false;
		        } else
		            offset += n;
		    }
		    
		    String magicNumber = new String(cbuf);
		    if (magicNumber.equals(m_asciiMagicNumber))
		        retValue = true;
		    else
		        retValue = false;
		    
			reader.close();
			
		} catch (FileNotFoundException ex)
		{
		    m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
				DwpExportWizardMessages.getFormattedString("DwpExportOperation.fileNotFound",
				new Object[] { file.getName() }), null));
		    return false;
		} catch (IOException ex)
		{
		    m_errorTable.add(new Status(IStatus.ERROR, DwpPlugin.getID(), 0,
				DwpExportWizardMessages.getFormattedString("DwpExportOperation.ioError",
				new Object[] { file.getName() }), null));
			return false;		    
		}
		
	    return retValue;
	}
	
	/**
	 * Export the resources that were previously specified for export
	 * (or if a single resource was specified then export it recursively).
	 */
	public void run(IProgressMonitor progressMonitor) throws InterruptedException
	{
		this.m_monitor = progressMonitor;

		if (m_resource != null)
		{
			if (m_createLeadupStructure)
				createLeadupDirectoriesFor(m_resource);

			if (m_createContainerDirectories
				&& m_resource.getType() != IResource.FILE)
			{
				// Ensure it's a container.
				m_path = m_path.append(m_resource.getName());
				m_exporter.createFolder(m_path);
			}
		}

		try
		{
			int totalWork = IProgressMonitor.UNKNOWN;
			try
			{
				if (m_resourcesToExport == null)
					totalWork = countChildrenOf(m_resource);
				else
					totalWork = countSelectedResources();
			} catch (CoreException ex)
			{
				// Should not happen.
				m_errorTable.add(ex.getStatus());
			}
			m_monitor.beginTask(DwpExportWizardMessages.getString("DwpExportOperation.exportingTitle"), totalWork);
			if (m_resourcesToExport == null)
			{
				exportAllResources();
			} else
			{
				exportSpecifiedResources();
			}
		} finally
		{
			m_monitor.done();
		}
	}
	
	/**
	 * Set this boolean indicating whether a directory should be created for
	 * Folder resources that are explicitly passed for export.
	 *
	 * @param value Set this <b>true</b> if the directories should be created.
	 * Otherwise set this <b>false</b>.
	 */
	public void setCreateContainerDirectories(boolean value)
	{
		m_createContainerDirectories = value;
	}
	
	/**
	 * Set this boolean indicating whether each exported resource's complete m_path should
	 * include containment hierarchies as dictated by its parents.
	 *
	 * @param value Set this <b>true</b> if the directories should be created.
	 * Otherwise set this <b>false</b>.
	 */
	public void setCreateLeadupStructure(boolean value)
	{
		m_createLeadupStructure = value;
	}
	
	/**
	 * Set this boolean indicating whether exported resources should automatically
	 * overwrite existing files when a conflict occurs. If not
	 * query the user.
	 *
	 * @param value Set this <b>true</b> if the user should be queried on overwrite.
	 * Otherwise set this <b>false</b>.
	 */
	public void setOverwriteFiles(boolean value)
	{
		if (value)	
			m_overwriteState = OVERWRITE_ALL;
	}
	
	/**
	 * Set the type of export to perform.
	 * 
	 * @param type The type of export to invoke; valid type include
	 * <code>DwpExporter.ASCII_FILE_FORMAT</code> and
	 * <code>DwpExporter.BINARY_FILE_FORMAT</code>
	 */
	public void setExportType(int type)
	{
	    m_exportType = type;
	}
}
