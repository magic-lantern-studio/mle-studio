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
package com.wizzer.mle.studio.framework;

/**
 * This class contains simple static methods that provide miscellaneous utility.
 * 
 * @author Mark S. Millard
 */
public class Utilities
{
    /**
     * Used for putting the largest possible numeric value into a numeric
     * attribute with N bits.
     * 
     * @param bits The number of bits.
     * 
     * @return An integer value is returned indicating the largest possible numeric value.
     */
    static public int biggestIntegerWithNBits(int bits)
    {
        return (int)Math.pow(2, bits) - 1;
    }

    /**
     * Takes a bit string (e.g., "1001010101") and converts it into a byte array
     * holding the equivalent numeric value (in 2's complement notation).
     * 
     * @param s The bit String to convert.
     *
     * @return A byte array is returned holding the bits in 2's complement notation.
     */
    static public byte [] bitStringToByteRepresentation(String s)
    {
        //System.out.println("In getByteRepresentation: " + s);
        int len = (s.length() / 8);
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++)
        {
            String sub = s.substring(i * 8, (i * 8) + 8);
            result[i] = (byte)Integer.parseInt(sub, 2);
        }
        return result;
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
    	
		// Resolve the element relative to where the tools have been installed.
		String toolrootEnv = System.getProperty(property);
		//System.out.println("Toolroot: " + toolrootEnv);
		if (toolrootEnv == null) {
			FrameworkLog.logWarning("The property, " + property + " must be set.");
			return path;
		}

        // Construct a path.
		String toolrootPath = new String(toolrootEnv + "/");
		//System.out.println("Toolroot Path: " + toolrootPath);

		path = new String(toolrootPath + element);
		
		// Structure path in it's canonical form.
		path = path.replace('\\','/');
		path = path.replaceAll("/+","/");
		
		FrameworkLog.logInfo("Resolved Path: " + path);
		return path;
    }
    
    /**
     * This class is used to convert the specified path to Microsoft DOS file system format.
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
	 * This class is used to convert the specified path to the UNIX-like file system format.
	 * 
	 * @param path The file system path or URL to convert.
	 * 
	 * @return The converted path is returned.
	 */
	static public String unixPath(String path)
	{
		return path.replace('\\','/');
	}

	/**
	 * Load a library, resolving the directory path for the specified library relative to the
     * specified property.
     * <p>
     * The path will be returned in a it's canonical form; path elements will be
     * separated by the forward slash ('/').
     * </p>
	 * @param property The java property representing the root of the directory path.
	 * @param library The name of the library to load.
	 * 
	 * @return <b>true</b> will be returned if the library is loaded. Otherwise, <b>false</b>
	 * will be returned.
	 */
	static public boolean loadLibrary(String property,String library)
	{
		boolean loaded = false;
		
		String libraryPath = resolvePath(property,library);
		if (libraryPath == null)
		{
			String message = "Unable to load library: " + property + "/" + library;
			FrameworkLog.logWarning(message);
			return loaded;
		}
		
		System.load(libraryPath);
		loaded = true;
		
		return loaded;
	}
}
