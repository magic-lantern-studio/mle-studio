/*
 * MleTargetCreationOperation.java
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
package com.wizzer.mle.studio.project;

// Import Eclipse classes.
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.operation.IRunnableWithProgress;

import com.wizzer.mle.studio.MleLog;

/**
 * This abstract class is used for Magic Lantern Mastering target
 * operations.
 * 
 * @author Mark S. Millard
 */
public abstract class MleTargetCreationOperation implements IRunnableWithProgress
{
    /**
     * The default constructor.
     */
    public MleTargetCreationOperation()
    {
        super();
    }
    
	/**
	 * Get the elements to open.
	 * 
	 * @return An array of resources is returned.
	 */
	public abstract IResource[] getElementsToOpen();
	
	/**
	 * Creates a folder if it does not exist, returns true if a folder
	 * was created.
	 */
	protected boolean createFolder(IFolder newFolder)
	{
		if (! newFolder.exists())
		{
			try {
				newFolder.create(true, true, null);
			} catch (CoreException ex) {
				MleLog.logWarning("Unable to create resource folder " + newFolder.getName());
				return false;
			};
		} else {
			return false;
	    };
	    
	    return true;
	}

	/**
	 * Create a folder in the specified project. If the folder already exists,
	 * then it will be returned.
	 *
	 * @param project The project to create the folder in.
	 * @param folderName The name of the folder to create.
	 * 
	 * @return A folder resource will be returned.
	 */
	protected IFolder createFolderInProject(IProject project,String folderName)
	{
		IFolder folder = project.getFolder(folderName);
		if (! folder.exists())
		{
			try
			{
				folder.create(true,true,null);
			} catch (CoreException ex)
			{
				MleLog.logError(ex,"Unable to create folder " + folderName
					+ " in project " + project.getName() + ".");
			}
		}
		
		return folder;
	}
	
	/**
	 * Create a file in the specified folder. If the file already exists,
	 * then its contents will be updated.
	 *
	 * @param folder The folder to create the file in.
	 * @param fileName The name of the file to create.
	 * @param source The data to create the contents of the file with.
	 * 
	 * @reutrn If successful, then a file resource will be returned.
	 */
	protected IFile createFileInFolder(IFolder folder,String fileName,InputStream source)
	{
		IFile file = folder.getFile(fileName);
		try
		{
			if (file.exists())
			{
				// Update the contents of the file.
				file.setContents(source,true,false,null);
			} else
			{
				java.io.File systemFile = file.getLocation().toFile();
				if (systemFile.exists())
				{
					// Skip create in file system.
					// XXX - could refreshLocal on parent at this point.
					//folder.getParent().refreshLocal(depth,monitor);
				} else
				{
					file.create(source,false,null);
				}
			}
		} catch (CoreException ex)
		{
			MleLog.logError(ex,"Unable to create file " + fileName
				+ " in folder " + folder.getLocation().toString() + ".");
		}
		
		return file;
	}

}
