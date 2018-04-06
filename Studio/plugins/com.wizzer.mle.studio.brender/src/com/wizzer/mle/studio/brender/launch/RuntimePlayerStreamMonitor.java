/*
 * RuntimePlayerStreamMonitor.java
 * Created on Nov 1, 2006
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
package com.wizzer.mle.studio.brender.launch;

// Import Eclipse packages.
import org.eclipse.debug.core.IStreamListener;
import org.eclipse.debug.core.model.IStreamMonitor;

/**
 * This class implements an <code>IStreamMonitor</code> that can be used by the
 * <code>RuntimePlayerStreamsProxy<.code>.
 * <p>
 * Currently it is just a placeholder that does nothing.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class RuntimePlayerStreamMonitor implements IStreamMonitor
{
	/**
	 * The default constructor.
	 */
	public RuntimePlayerStreamMonitor()
	{
		super();
	}

	/**
	 * Adds the given listener to this stream monitor's registered listeners.
	 * Has no effect if an identical listener is already registered.
	 * 
	 * @param listener The listener to add.
	 * 
	 * @see org.eclipse.debug.core.model.IStreamMonitor#addListener(org.eclipse.debug.core.IStreamListener)
	 */
	public void addListener(IStreamListener listener)
	{
	}

	/**
	 * Returns the entire current contents of the stream. An empty String is returned
	 * if the stream is empty.
	 * 
	 * @return The stream contents as a <code>String</code>.
	 * 
	 * @see org.eclipse.debug.core.model.IStreamMonitor#getContents()
	 */
	public String getContents()
	{
		return new String();
	}

	/**
	 * Removes the given listener from this stream monitor's registered listeners.
	 * Has no effect if the listener is not already registered.
	 * 
	 * @param listener The listener to remove.
	 * 
	 * @see org.eclipse.debug.core.model.IStreamMonitor#removeListener(org.eclipse.debug.core.IStreamListener)
	 */
	public void removeListener(IStreamListener listener)
	{
	}

}
