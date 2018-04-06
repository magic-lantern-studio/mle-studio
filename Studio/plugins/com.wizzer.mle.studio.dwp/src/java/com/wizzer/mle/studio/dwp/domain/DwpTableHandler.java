/*
 * DwpTableHandler.java
 * Created on Jul 21, 2004
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
package com.wizzer.mle.studio.dwp.domain;

// Import standard Java packages.
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Observable;
import java.net.URL;
import java.net.URLConnection;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.UID;
import com.wizzer.mle.studio.framework.domain.Table;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.io.IOUtil;

// Import Magic Lantern Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.DwpReader;
import com.wizzer.mle.studio.dwp.DwpWriter;
import com.wizzer.mle.studio.dwp.domain.DwpTable;


/**
 * This class manages the Digital Workprint Table IO.
 * <p>
 * It is used to associate a Digital Workprint Table with its persistent data representation.
 * Utility is provided for marshalling and unmarshalling the data representation
 * to and from a Universal Resource Locator, or URL, in addition to/from an
 * <code>OutputStream</code>/<code>InputStream<.code>.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class DwpTableHandler extends Observable implements IDwpTableHandler
{
	// The DWP Table.
	private DwpTable m_table = null;
	/** The table's unique identifier. */
	protected String m_uid = null;

	/**
	 * Get the domain table corresponding to this Digital Workprint table.
	 * 
	 * @return A reference to a doamin table is returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#getTable()
	 */
	public Table getTable()
	{
		return m_table;
	}

	/**
	 * Set the domain table corresponding to this Digital Workprint table.
	 * 
	 * @param table A reference to the domain table to set.

	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#setTable(com.wizzer.mle.studio.framework.domain.Table)
	 */
	public void setTable(Table table)
	{
		if (table instanceof DwpTable)
			m_table = (DwpTable)table;
	}

	/**
	 * Unmarshall the Digital Workprint table from an input stream.
	 * 
	 * @param istream The <code>InputStream</code> to unmarshall the data from.
	 * 
	 * @return If the data from the istream was successfully unmarshalled,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#unmarshall(java.io.InputStream)
	 */
	public boolean unmarshall(InputStream istream)
	{
		boolean retValue = false;
		
		try
		{
			// Obtain a byte array from the InputStream.
			byte [] data = IOUtil.toByteArray(istream);
			
			// Create a native DWP reader.
			DwpReader reader = new DwpReader(data);
			
			// Read the DWP and update the DwpTable.
			if (reader != null)
			{
			    DwpTable table = reader.read();
			    if (table != null)
			    {
			    	// Update the domain table.
			    	updateDomainTable(table);

					// Notify observers that a new table has been read in.
					setChanged();
					notifyObservers(new Boolean(false));

				    retValue = true;
			    }				
			}
		} catch (IOException ex)
		{
			DwpLog.logError(ex,"Unable to unmarshall Digital Workprint.");
		}
		
		return retValue;
	}

	/**
	 * Unmarshall the Digital Workprint table from an URL.
	 * 
	 * @param locator The <code>URL</code> to locate the table to unmarshall.
	 * 
	 * @return If the data from the URL was successfully unmarshalled,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#unmarshall(java.net.URL)
	 */
	public boolean unmarshall(URL locator)
	{
		boolean retValue = false;

		try
		{
			// Get an InputStream for the URL.
			InputStream istream = locator.openStream();
			
			// Retrieve the DWP.
			retValue = unmarshall(istream);
		} catch (IOException ex)
		{
			DwpLog.logError(ex,"Unable to unmarshall Digital Workprint.");
		}

		return false;
	}

	/**
	 * Marshall the Digital Workprint table to an output stream.
	 * 
	 * @param ostream The <code>OutputStream</code> to marshall the data to.
	 * 
	 * @return If the DWP was successfully marshalled to the ostream,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#marshall(java.io.OutputStream)
	 */
	public boolean marshall(OutputStream ostream)
	{
		boolean retValue = false;
		
		try
		{
			// Create a DWP Writer.
			DwpWriter writer = new DwpWriter(m_table);
			
			// Obtain a byte array from the DwpTable.
			byte[] data = writer.write();
			
			// Write the data to the OutputStream.
			if (data != null)
			{
				ostream.write(data);
				ostream.flush();
				
				// Set the return value since we are successful.
				retValue = true;
			}
		} catch (DwpException ex)
		{
			DwpLog.logError(ex,"Unable to marshall Digital Workprint.");
		} catch (IOException ex)
		{
			DwpLog.logError(ex,"Unable to marshall Digital Workprint.");
		}
		
		return retValue;
	}

	/**
	 * Marshall the Digital Workprint table to an URL.
	 * 
	 * @param locator The <code>URL</code> to marshall the data to.
	 * 
	 * @return If the DWP was successfully marshalled to the URL,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#marshall(java.net.URL)
	 */
	public boolean marshall(URL locator)
	{
		boolean retValue = false;

		// Verify that the locator has been previously set.
		if (locator == null)
			return retValue;

		try
		{
			// Attempt to open the connection (same as validation of locator).
			URLConnection connection = locator.openConnection();
			if (connection != null)
			{
				// Update return value.
				retValue = this.marshall(connection.getOutputStream());;
			}
		}
		catch (IOException ex)
		{
			DwpLog.logWarning("Unable to marshall Digital Workprint: " + locator.toString() +
				": " + ex.getMessage());
		}
		
		// Return the status of marshalling the table.
		return retValue;
	}

	/**
	 * Get the unique identifier for the instance of the Digital Workprint table.
	 * 
	 * @return A unique identifier is returned.
	 * 
	 * @see com.wizzer.mle.studio.dwp.domain.IDwpTableHandler#getID()
	 */
	public String getID()
	{
		// Generate a unique ID if necessary.
		if (m_uid == null)
		    UID.generateUID();
		
		return m_uid;
	}

    // Update the domain table using the specifed table.
	private void updateDomainTable(DwpTable table)
	{
		// Copy the specified tables DWP items.
		m_table.copy(table);
		
		// Mark the domain table as being changed and notify its observers.
		m_table.externalSetChanged();
		//m_table.notifyObservers(m_table.getTopAttribute());
	}
		
	/**
	 * This method is called whenever the observed object is changed.
	 * <p>
	 * The <code>DwpTableHandler</code> observes modifications made to the
	 * <code>DwpTable</code> Table.
	 * </p>
	 * 
	 * @param obs The oberservable object.
	 * @param obj An argument passed by the observed object when
	 * <code>notifyObservers</code> is called.
	 * 
	 * @see java.util.Observer#update(java.util.Observable, java.lang.Object)
	 */
	public void update(Observable obs, Object obj)
	{
		if (obs instanceof Table)
		{
			if (obj instanceof Attribute)
			{
				String name = ((Attribute)obj).getName();

				// Notify observers that the table has been modified.
				setChanged();
				notifyObservers(new Boolean(false));
			}
		}
	}
}
