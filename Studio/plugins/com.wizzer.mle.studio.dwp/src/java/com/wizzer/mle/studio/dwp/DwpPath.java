/*
 * DwpPath.java
 * Created on Oct 5, 2006
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

// Import Magic Lantern classes.
import com.wizzer.mle.studio.framework.Path;

/**
 * This class provides a common utility for handling hierarchial paths in
 * Magic Lantern.
 * 
 * @author Mark S. Millard
 */
public class DwpPath extends Path
{
    /**
     * Get the canonical form of the specified <code>path</code>.
     * The path separator will use unix-like slashes (<b>'/'</b>).
     * 
     * @param path The path to transform.
     * @param drive Flag indicating whether to convert drive component. If <b>true</b>,
	 * then the drive path will be converted to '//drive' (e.g. //C). If <b>false</b>,
	 * then the drive path will not be converted (e.g. remains C:).
     * 
     * @return A <code>String</code> is returned.
     */
    static public String canonicalPath(String path, boolean drive)
    {
    	String retValue = Path.canonicalPath(path,drive);
    	if (drive)
    		retValue = new String("/" + retValue);
    	return retValue;
    }
}
