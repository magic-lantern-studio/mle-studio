/*
 * RehearsalPlayerStreamsProxy.java
 * Created on Jun 5, 2006
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
package com.wizzer.mle.studio.rehearsal.launch;

import java.io.IOException;

import org.eclipse.debug.core.model.IStreamMonitor;
import org.eclipse.debug.core.model.IStreamsProxy;

/**
 * This class implements an <code>IStreamsProxy</code> that can be used by the
 * <code>RehearsalPlayerProcess<.code>.
 * <p>
 * Currently it is just a placeholder that does nothing.
 * </p>
 * @author Mark S. Millard
 */
public class RehearsalPlayerStreamsProxy implements IStreamsProxy
{
	// The stream monitor for output messages.
    private RehearsalPlayerStreamMonitor m_outputMonitor = new RehearsalPlayerStreamMonitor();
    // The stream monitor for the error messages.
    private RehearsalPlayerStreamMonitor m_errorMonitor = new RehearsalPlayerStreamMonitor();
    
	/**
	 * The default constructor.
	 */
	public RehearsalPlayerStreamsProxy()
	{
		super();
	}

	/**
	 * Returns a monitor for the error stream of this proxy's process,
	 * or null if not supported. The monitor is connected to the error stream
	 * of the associated process. 
	 * 
	 * @return An error stream monitor is returned.
	 * 
	 * @see org.eclipse.debug.core.model.IStreamsProxy#getErrorStreamMonitor()
	 */
	public IStreamMonitor getErrorStreamMonitor()
	{
		return m_errorMonitor;
	}

	/**
	 * Returns a monitor for the output stream of this proxy's process,
	 * or null if not supported. The monitor is connected to the output stream
	 * of the associated process.
	 * 
	 * @return An output stream monitor is returned.
	 * 
	 * @see org.eclipse.debug.core.model.IStreamsProxy#getOutputStreamMonitor()
	 */
	public IStreamMonitor getOutputStreamMonitor()
	{
		return m_outputMonitor;
	}

	/**
	 * Writes the given text to the output stream connected to the standard input stream
	 * of this proxy's process.
	 * 
	 * @param input The text to be written.
	 * 
	 * @throws IOException When an error occurs writing to the underlying OutputStream.
	 * 
	 * @see org.eclipse.debug.core.model.IStreamsProxy#write(java.lang.String)
	 */
	public void write(String input) throws IOException
	{
		// Do nothing for now. Eventually we may want to write to something
		// like a registered OutputStream.
	}

}
