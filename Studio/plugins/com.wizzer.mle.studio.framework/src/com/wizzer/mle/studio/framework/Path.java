// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework;

// Import standard Java packages.
import java.io.File;
import java.io.IOException;


/**
 * This class provides a common utility for handling hierarchial paths.
 * 
 * @author Mark S. Millard
 */
public class Path
{
    /**
     * Get the canonical form of the specified <code>path</code>
     * as it is returned by the underlying operating system.
     * 
     * @param path The path to transform.
     * 
     * @return A <code>String</code> is returned.
     */
    static public String osCanonicalPath(String path)
    {
    	String canonicalPath = null;
    	
    	try
		{
	    	File file = new File(path);
	    	canonicalPath = file.getCanonicalPath();
		} catch (IOException ex)
		{
			FrameworkLog.logError(ex,"Unable to determine canonical path for " +
				path);
		}
		
		return canonicalPath;
    }

    /**
     * Get the absolute form of the specified <code>path</code>
     * as it is returned by the underlying operating system.
     * 
     * @param path The path to transform.
     * 
     * @return A <code>String</code> is returned.
     */
    static public String osAbsolutePath(String path)
    {
    	String absolutePath = null;
    	
	    File file = new File(path);
	    absolutePath = file.getAbsolutePath();
		
		return absolutePath;
    }
    
    /**
     * Get the canonical form of the specified <code>path</code>.
     * The path separator will use unix-like slashes (<b>'/'</b>).
     * 
     * @param path The path to transform.
     * @param drive Flag indicating whether to convert drive component. If <b>true</b>,
	 * then the drive path will be converted to '/drive' (e.g. /C). If <b>false</b>,
	 * then the drive path will not be converted (e.g. remains C:).
     * 
     * @return A <code>String</code> is returned.
     */
    static public String canonicalPath(String path, boolean drive)
    {
    	String tmpPath = osCanonicalPath(path);
    	return unixPath(tmpPath,drive);
    }

    /**
     * Resolves the directory path for the specified element relative to the
     * specified property.
     * <p>
     * The path will be returned in a it's canonical form; path elements will be
     * separated by the forward slash ('/').
     * </p>
     * 
     * @param property The java property representing the root of the directory path.
     * @param element The path element to resolve.
     * 
     * @return The resolved path is returned in canonical form.
     */
    static public String resolvePath(String property,String element)
    {
    	String path = null;
    	
		// Resolve the element relative to the specified property.
		String root = System.getProperty(property);
		//System.out.println("Root: " + root);
		if (root == null)
		{
			FrameworkLog.logWarning("The property, " + property + " must be set.");
			return path;
		}

		path = new String(root + "/" + element);
		
		// Structure path in it's canonical form.
		//path = path.replace('\\','/');
		//path = path.replaceAll("/+","/");
		path = osCanonicalPath(path);
		
		FrameworkLog.getInstance().debug("Resolved Path: " + path);
		return path;
    }
    
    /**
     * This method is used to convert the specified path to Microsoft DOS file system format.
     * 
     * @param path The file system path or URL to convert.
     * 
     * @return The converted path is returned.
     */
    static public String dosPath(String path)
    {
    	String[] tokens = path.split("/");
    	StringBuffer buffer = new StringBuffer();
    	for (int i = 0; i < tokens.length; i++)
    	{
    		buffer.append(tokens[i]);
    		if (i != (tokens.length - 1))
    			// Not the last token in the path.
    			buffer.append("\\");
    	}
    	
    	return buffer.toString();
    }

	/**
	 * This method is used to convert the specified path to the UNIX-like file system format.
	 * 
	 * @param path The file system path to convert.
	 * @param drive Flag indicating whether to convert drive component. If <b>true</b>,
	 * then the drive path will be converted to '/drive' (e.g. /C). If <b>false</b>,
	 * then the drive path will not ve converted (e.g. remains C:).
	 * 
	 * @return The converted path is returned.
	 */
	static public String unixPath(String path, boolean drive)
	{
		StringBuffer buffer = new StringBuffer(path.length() + 1);
		
		// Process drive element.
		if (path.indexOf(':') != -1)
		{
			// A drive element exists.
			if (drive)
			{
				buffer.append('/');
				buffer.append(path);
				buffer.setCharAt(buffer.indexOf(":"),'/');
			} else
			{
				buffer.append(path);
			}
		} else
		{
			// No drive element exists.
			buffer.append(path);
		}

		// Replace all backslashes.
		for (int i = 0; i < buffer.length(); i++)
		{
			if (buffer.charAt(i) == '\\')
				buffer.setCharAt(i,'/');
		}

		// Replace sequential sequences of '/' with a single instance; then return.
		return buffer.toString().replaceAll("/+","/");
	}

}
