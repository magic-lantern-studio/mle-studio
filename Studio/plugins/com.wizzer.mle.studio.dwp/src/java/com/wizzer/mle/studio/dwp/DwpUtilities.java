/*
 * DwpUtilities.java
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
