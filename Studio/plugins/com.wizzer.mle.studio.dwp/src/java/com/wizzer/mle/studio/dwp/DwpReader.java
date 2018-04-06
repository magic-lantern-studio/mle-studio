/*
 * DwpReader.java
 * Created on May 2, 2004
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
