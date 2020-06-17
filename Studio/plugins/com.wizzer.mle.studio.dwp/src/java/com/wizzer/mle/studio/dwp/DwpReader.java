/*
 * DwpReader.java
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

// Import standard Java packages.

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.StringUtils;

// Import Digital Workprint packages.
import com.wizzer.mle.studio.dwp.domain.DwpTable;


/**
 * This class implements a reader for a Magic Lantern Digital Workprint.
 * 
 * @author Mark S. Millard
 */
public class DwpReader
{
	// The name of the DWP file.
	private String m_filename = null;
	// A byte array containing the DWP contents.
	private byte[] m_data = null;
	
	// Hide the default constructor.
	private DwpReader()
	{}

    /**
     * A constructor specifying the name of the file to read.
     * 
     * @param filename The name of the file.
     */
    public DwpReader(String filename)
    {
    	super();
    	
    	m_filename = filename;
    }

	/**
	 * A constructor specifying the data to read.
	 * 
	 * @param data The byte array containing the DWP contents.
	 */
	public DwpReader(byte[] data)
	{
		super();
    	
		m_data = data;
	}
    
    /**
     * Get the name of the file associated with this reader.
     * 
     * @return The name of the file is returned.
     */
    public String getFilename()
    {
    	return m_filename;
    }
    
    /**
     * Read the file, creating a Digital Workprint domain table.
     * <p>
     * This method uses a native C++ reader.
     * </p>
     * 
     * @param filename The name of the file to read.
     * 
     * @return A Digital Workprint domain table is returned.
     * <b>null</b> will be returned if the file can not be successfully read.
     */
    protected native DwpTable buildTableFromNativeDwp(String filename);

    /**
     * Read the file, storing the Digital Workprint in a byte array.
     * <p>
     * This method uses a native C++ reader.
     * </p>
     * 
     * @param filename The name of the file to read.
     * 
     * @return A <b>byte</b> array containing the contents of the Digital
     * Workprint will be returned if the file was successfully read.
     * Otherwise <b>null</b> will be returned.
     */
    protected native byte[] buildArrayFromNativeDwp(String filename);

	/**
	 * Consume the data, creating a Digital Workprint domain table.
	 * <p>
	 * This method uses a native C++ reader.
	 * </p>
	 * 
	 * @param data A byte array containing the contenst of the
	 * Digital Workprint.
	 * 
	 * @return A Digital Workprint domain table is returned.
	 * <b>null</b> will be returned if the file can not be successfully read.
	 */
	protected native DwpTable buildTableFromNativeData(byte[] data);

	/**
	 * Read the DWP, creating a Digital Workprint domain table.
	 * 
	 * @return A Digital Worprint domain table is returned.
	 * <b>null</b> will be returned if the DWP can not be successfully read.
	 */
    public DwpTable read()
    {
    	DwpTable table = null;
    	
    	if (m_filename != null)
    	    table = buildTableFromNativeDwp(m_filename);
    	else if (m_data != null)
    	{
    	    System.out.println(StringUtils.getStringBYTEArray(m_data));
		    table = buildTableFromNativeData(m_data);
    	}
    	
    	return table;
    }
    
	/**
	 * Read the file, dumping the Digital Workprint to stdout.
	 * 
	 * @return <b>true</b> will be returned if the file was successfully read.
	 * Otherwise <b>fasle</b> will be returned.
	 */
	public boolean dump()
	{
		boolean status = false;
		byte[] buffer = buildArrayFromNativeDwp(m_filename);
		if (buffer != null)
		{
			String dwp = new String(buffer);
			System.out.print(dwp);
			status = true;
		}
		
		return status;
	}

}
