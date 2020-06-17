/*
 * DwpTableHandlerFactory.java
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
package com.wizzer.mle.studio.dwp.domain;

// Import standard Java packages.
import java.util.Hashtable;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.domain.Table;


/**
 * This class is used to create Digital Workprint tables as defined by the
 * Wizzer Works Digital Workprint specification.
 * <p>
 * This class is a singleton.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpTableHandlerFactory implements IDwpTableHandlerFactory
{
	// The Digital Workprint factory and table registry manager.
	static private DwpTableHandlerFactory m_profile = null;
	// The table registry.
	static private Hashtable m_registry = new Hashtable();
	
	/**
	 * Get the singleton instance.
	 * 
	 * @return The singleton instance of <code>DwpTableHandlerFactory</code> is returned.
	 */
	static public DwpTableHandlerFactory getInstance()
	{
		if (m_profile == null)
			m_profile = new DwpTableHandlerFactory();
		return m_profile;
	}
    
	/*
	 * Hide the default constructor.
	 */
	private DwpTableHandlerFactory()
	{
		super();
	}

	/**
	 * Create a handler for a Digital Workprint Table conforming to the
	 * Wizzer Works Digital Workprint specification.
	 * 
	 * @param domainTable The domain table.
	 * 
	 * @return A reference to a new Digital Workprint Table handler is returned.
	 * If the specified domain table is not recognized, then <b>null</b> will be returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandlerFactory#createTableHandler(com.wizzer.mle.studio.dwp.domain.Table)
	 */
	public IDwpTableHandler createTableHandler(Table domainTable)
	{
		IDwpTableHandler newHandler = null;
		
		// Determine what type of table is to be generated from the specified domain.
		if (domainTable instanceof DwpTable)
		{
			DwpTableHandler dwpTableHandler = new DwpTableHandler();
			dwpTableHandler.setTable(domainTable);
			newHandler = dwpTableHandler;
		}
		
		// Do NOT register the handler at this time. Registration should be explicit,
		// with full knowledge on how the Digital Workprint Table will be accessed
		// via the registry.
		
		// Return the new handler.
		return newHandler;
	}

	/**
	 * Register a Digital Workprint Table handler.
	 * 
	 * @param table The Digital Workprint Table handler to register.
	 * 
	 * @return <b>true</b> will be returned if the table is successfully registered.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean registerTable(IDwpTableHandler table)
	{
		boolean status = true;
		String key = table.getID();
    	
		// Make sure it hasn't already been registered.
		if (! m_registry.containsKey(key))
		{
			m_registry.put(key, table);
		}
    	
		return status;
	}
    
	/**
	 * Unregister a Digital Workprint Table handler.
	 * 
	 * @param table The Digital Workprint Table handler to unregister.
	 * 
	 * @return <b>true</b> will be returned if the table is successfully registered.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean unregisterTable(IDwpTableHandler table)
	{
		boolean status = false;
		String key = table.getID();
    	
		// Make sure it has been previously registered.
		if (m_registry.containsKey(key))
		{
			m_registry.remove(key);
			status = true;
		}
		
		return status;
	}
    
	/**
	 * Check to see if a Digital Workprint Table handler has been registered.
	 * 
	 * @param table The Digital Workprint Table handler to check.
	 * 
	 * @return <b>true</b> will be returned if the table has been registered.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean isRegistered(IDwpTableHandler table)
	{
		String key = table.getID();
		return m_registry.contains(key);
	}
}
