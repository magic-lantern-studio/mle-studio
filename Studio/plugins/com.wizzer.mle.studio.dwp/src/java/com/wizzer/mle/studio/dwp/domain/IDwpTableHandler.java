/*
 * IDwpTableHandler.java
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

// Import standard Java header files.
import java.net.URL;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Observer;

// Import Magic Lantern Framework packages.
import com.wizzer.mle.studio.framework.domain.Table;


/**
 * This interface is used to help manage Digital Workprint Tables as defined by the
 * Wizzer Works Digital Workprint specification.
 * <p>
 * It is used to associate a Digital Workprint Table with its persistent data representation.
 * Utility is provided for marshalling and unmarshalling the data representation
 * to and from a Universal Resource Locator, or URL, in addition to/from an
 * <code>OutputStream</code>/<code>InputStream<.code>.
 * </p>
 * 
 * @author Mark S. Millard
 */
public interface IDwpTableHandler extends Observer
{
	/**
	 * Get the domain table corresponding to this Digital Workprint table.
	 * 
	 * @return A reference to a doamin table is returned.
	 */
	public Table getTable();
    
	/**
	 * Set the domain table corresponding to this Digital Workprint table.
	 * 
	 * @param table A reference to the domain table to set.
	 */
	public void setTable(Table table);
        
	/**
	 * Unmarshall the Digital Workprint table from an input stream.
	 * 
	 * @param istream The <code>InputStream</code> to unmarshall the data from.
	 * 
	 * @return If the data from the input stream was successfully unmarshalled,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 */
	public boolean unmarshall(InputStream istream);

	/**
	 * Unmarshall the Digital Workprint table from an URL.
	 * 
	 * @param locator The <code>URL</code> to locate the table to unmarshall.
	 * 
	 * @return If the data from the URL was successfully unmarshalled,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 */
	public boolean unmarshall(URL locator);
    
	/**
	 * Marshall the Digital Workprint table to an output stream.
	 * 
	 * @param ostream The <code>OutputStream</code> to marshall the data to.
	 * 
	 * @return If the data was successfully marshalled to the ouput stream,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 */
	public boolean marshall(OutputStream ostream);

	/**
	 * Marshall the Digital Workprint table to an URL.
	 * 
	 * @param locator The <code>URL</code> to marshall the data to.
	 * 
	 * @return If the data was successfully marshalled to the URL,
	 * then <b>true</b> will be returned. Otherwise, <b>false</b> fill be returned.
	 */
	public boolean marshall(URL locator);
	
	/**
	 * Get the unique identifier for the instance of the Digital Workprint table.
	 * 
	 * @return A unique identifier is returned.
	 */
	public String getID();
	
	/**
	 * Adds an observer to the set of observers for this object,
	 * provided that it is not the same as some observer already in the set.
	 * The order in which notifications will be delivered to multiple observers
	 * is not specified.
	 * <p>
	 * Note that this implies that implementing classes should extend
	 * the <code>java.util.Observable</code> class.
	 * </p>
	 * 
	 * @param obs An <code>Observer</code> to be added.
	 */
	public void addObserver(Observer obs);
}
