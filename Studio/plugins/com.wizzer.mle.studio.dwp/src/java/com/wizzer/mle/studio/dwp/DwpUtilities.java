/*
 * DwpUtilities.java
 * Created on Jun 20, 2005
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
package com.wizzer.mle.studio.dwp;

// Import standard Java classes.
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;

/**
 * @author Mark S. Millard
 */
public class DwpUtilities
{
    // ASCII magic number.
    private static String g_asciiMagicNumber = "#DWP 1.0 ascii";
 
    // Hide default constructor.
    private DwpUtilities()
    {}

	/**
	 * Determine whether the specified file is a Digital Workprint.
	 * 
	 * @param file The file resource to test.
	 * 
	 * @return <b>true</b> will be returned if the file is a
	 * Digital Workprint. <b>false</b> will be returned if it isn't.
	 */
	public static boolean isDigitalWorkprint(IFile file)
	    throws FileNotFoundException, IOException
	{
	    boolean retValue = false;
	    
	    IPath fullPath = file.getLocation();
		String properPathString = fullPath.toOSString();
		File testFile = new File(properPathString);
		
	    FileReader reader = new FileReader(testFile);
	    
	    char[] cbuf = new char[g_asciiMagicNumber.length()];
	    int offset = 0;
	    int n;

	    while (!reader.ready())
	        ; // Wait until ready.
	    while (offset < g_asciiMagicNumber.length())
	    {
	        n = reader.read(cbuf,offset,g_asciiMagicNumber.length() - offset);
	        if (n == -1)
	        {
			    // Note: this is not an error.
	            reader.close();
				return false;
	        } else
	            offset += n;
	    }
	    
	    String magicNumber = new String(cbuf);
	    if (magicNumber.equals(g_asciiMagicNumber))
	        retValue = true;
	    else
	        retValue = false;
	    
		reader.close();
		
	    return retValue;
	}

}
