/*
 * DwpPath.java
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
