/*
 * DwpExporter.java
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
