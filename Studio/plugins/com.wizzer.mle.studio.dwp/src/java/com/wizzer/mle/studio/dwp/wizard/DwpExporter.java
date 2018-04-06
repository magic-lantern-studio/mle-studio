/*
 * DwpExporter.java
 * Created on Jun 13, 2005
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
package com.wizzer.mle.studio.dwp.wizard;

// Import standard Java classes.
import java.io.*;

// Import Eclipse classes.
import org.eclipse.core.resources.*;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

// Import Magic Lantern Digital Workprint classes.
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.domain.DwpTableHandlerFactory;
import com.wizzer.mle.studio.dwp.domain.IDwpTableHandler;

/**
 * Helper class for exporting resources to the file system.
 */
class DwpExporter
{
    /** Export the file in ASCII format. */
    public static int ASCII_FILE_FORMAT  = 0x00;
    /** Export the file in binary format. */
    public static int BINARY_FILE_FORMAT = 0x01;
    
	/**
	 * Creates the specified file system directory at <code>destinationPath</code>.
	 * This creates a new file system directory.
	 * 
	 * @param destinationPath The destination to create.
	 */
	public void createFolder(IPath destinationPath)
	{
		new File(destinationPath.toOSString()).mkdir();
	}
	
	/**
	 *  Writes the passed resource to the specified location recursively.
	 * 
	 * @param resource The resource to write.
	 * @param destinationPath The path to write to.
	 * @param format The type of file to export.
	 */
	public void write(IResource resource, IPath destinationPath, int format)
	    throws CoreException, IOException
	{
		if (resource.getType() == IResource.FILE)
		    if (format == BINARY_FILE_FORMAT)
		        writeBinaryFile((IFile)resource,destinationPath);
		    else
		        writeAsciiFile((IFile)resource,destinationPath);
		else 
			writeChildren((IContainer)resource,destinationPath,format);
	}
	
	/**
	 * Writes the passed container's children.
	 * 
	 * @param folder The children to export.
	 * @param destinationPath The path to write to.
	 * @param format The type of file to export.
	 */
	protected void writeChildren(IContainer folder, IPath destinationPath, int format)
	    throws CoreException, IOException
	{
		if (folder.isAccessible())
		{
			IResource[] children = folder.members();
			for (int i = 0; i < children.length; i++)
			{
				IResource child = children[i];
				writeResource(
					child,
					destinationPath.append(child.getName()),
					format);
			}
		}
	}
	
	/**
	 * Writes the passed file resource to the specified destination on the local
	 * file system.
	 * 
	 * @param file The file to write.
	 * @param destinationPath The path to write to.
	 */
	protected void writeBinaryFile(IFile file, IPath destinationPath)
	    throws IOException, CoreException
	{
	    InputStream contentStream = null;
	    
	    // Create a domain table to hold the unmarshalled contents of the DWP file.
	    DwpTable table = new DwpTable();
	    IDwpTableHandler dwp = DwpTableHandlerFactory.getInstance().createTableHandler(table);
	    
	    // Retrieve the contents of the input DWP file
	    contentStream = file.getContents(false);
	    dwp.unmarshall(contentStream);
	    
	    // Save the contents to the destination DWP file.
	    try
	    {
		    File output = new File(destinationPath.toOSString());
		    table.save(output);
	    } catch (Exception ex)
	    {
	        // Unknown exception, pass it along as an IOException.
	        throw new IOException(ex.getMessage());
	    }
	}
	
	/**
	 * Writes the passed file resource to the specified destination on the local
	 * file system.
	 * 
	 * @param file The file to write.
	 * @param destinationPath The path to write to.
	 */
	protected void writeAsciiFile(IFile file, IPath destinationPath)
	    throws IOException, CoreException
	{
		FileOutputStream output = null;
		InputStream contentStream = null;
	
		try
		{
			contentStream = file.getContents(false);
			output = new FileOutputStream(destinationPath.toOSString());
			int chunkSize = contentStream.available();
			byte[] readBuffer = new byte[chunkSize];
			int n = contentStream.read(readBuffer);
			
			while (n > 0)
			{
				output.write(readBuffer);
				n = contentStream.read(readBuffer);
			}
		} finally
		{
			if (output != null)
				output.close();
			if (contentStream != null)
				contentStream.close();
		}
	}

	/**
	 *  Writes the passed resource to the specified location recursively.
	 * 
	 * @param resource The resource to write.
	 * @param destinationPath The path to write to.
	 * @param format The type of file to export.
	 */
	protected void writeResource(IResource resource,IPath destinationPath,int format)
	    throws CoreException, IOException
	{
		if (resource.getType() == IResource.FILE)
		    if (format == BINARY_FILE_FORMAT)
		        writeBinaryFile((IFile)resource,destinationPath);
		    else
		        writeAsciiFile((IFile)resource,destinationPath);
		else
		{
			createFolder(destinationPath);
			writeChildren((IContainer)resource,destinationPath,format);
		}
	}
}
