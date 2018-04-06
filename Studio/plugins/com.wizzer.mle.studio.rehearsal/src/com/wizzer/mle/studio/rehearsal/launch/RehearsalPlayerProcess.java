/*
 * RehearsalPlayerProcess.java
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

// Import standard Java packages.
import java.util.Hashtable;

// Import Eclipse packages.
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.eclipse.core.runtime.Status;

// Import Magic Lantern packages.
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;


/**
 * This class is a a dummy or stub <code>IProcess</code> to be used with the
 * Rehearsal Player launcher. It is used only when in "run" mode.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPlayerProcess implements IProcess
{
	/** Set the process type. */
	static final public String ATTR_PROCESS_TYPE = "MAGIC_LANTERN";
	/** View configuration uses Magic LAntern Perspective. */
	static final public int LAUNCH_PERSPECTIVE = 0;
	/** View configuration uses separate window. */
	static final public int LAUNCH_WINDOW = 1;
	
	// The process label.
	static private String m_label = "Rehearsal Player Run Process";
	
	// The associcated launch.
	private ILaunch m_launch = null;
	// The process attribute list.
	private Hashtable m_attributes = null;
	// Flag indicating whether the process is terminated.
	private boolean m_terminated = false;
	// Flag indicating view configuration.
	private boolean m_usePerspective = true;
	
	// A proxy for input/output/error streams.
	private RehearsalPlayerStreamsProxy m_streamsProxy = new RehearsalPlayerStreamsProxy();
	
	// Hide the defatul constructor.
	private RehearsalPlayerProcess()
	{}

	/**
	 * A constructor that initializes with the specified <code>ILaunch</code> object.
	 * 
	 * @param launch The associated <code>ILaunch</code> object.
	 */
	public RehearsalPlayerProcess(ILaunch launch)
	{
		super();
		m_launch = launch;
		m_attributes = new Hashtable(16);
	}

	/**
	 * Get a human-readable label for this process.
	 * 
	 * @return A <code>String</code> is returned.
	 * 
	 * @see org.eclipse.debug.core.model.IProcess#getLabel()
	 */
	public String getLabel()
	{
		return m_label;
	}

	/**
	 * Returns the launch this element originated from.
	 * 
	 * @return The launch this process is contained in.
	 * 
	 * @see org.eclipse.debug.core.model.IProcess#getLaunch()
	 */
	public ILaunch getLaunch()
	{
		return m_launch;
	}

	/**
	 * Returns a proxy to the standard input, output, and error streams for this process,
	 * or null if not supported.
	 * 
	 * @return A streams proxy is returned.
	 * 
	 * @see org.eclipse.debug.core.model.IProcess#getStreamsProxy()
	 */
	public IStreamsProxy getStreamsProxy()
	{
		return m_streamsProxy;
	}

	/**
	 * Sets the value of a client defined attribute. 
	 * 
	 * @param key The attribute key.
	 * @param value The attribute value.
	 * 
	 * @see org.eclipse.debug.core.model.IProcess#setAttribute(java.lang.String, java.lang.String)
	 */
	public void setAttribute(String key, String value)
	{
		m_attributes.put(key,value);
	}

	/**
	 * Returns the value of a client defined attribute. 
	 * 
	 * @param key The attribute key.
	 * 
	 * @return The attribute's value is returned.
	 * 
	 * @see org.eclipse.debug.core.model.IProcess#getAttribute(java.lang.String)
	 */
	public String getAttribute(String key)
	{
		return (String)m_attributes.get(key);
	}

	/**
	 * Returns the exit value of this process. Conventionally,
	 * <b>0</b> indicates normal termination.
	 * 
	 * @return The exit value of this process is returned.
	 * 
	 * @throws org.eclipse.debug.core.DebugException This exception is thrown
	 * if this process has not yet terminated.
	 * 
	 * @see org.eclipse.debug.core.model.IProcess#getExitValue()
	 */
	public int getExitValue() throws DebugException
	{
		if (! m_terminated)
		    throw new DebugException(new Status(
		        Status.ERROR,RehearsalPlugin.getID(),
		        DebugException.INTERNAL_ERROR,"Process Not Terminated.",null));
		    
		return 0;
	}

	/**
	 * Returns an object which is an instance of the given class associated with this object.
	 * Returns null if no such object can be found.
	 * 
	 * @param adapter The adapter class to look up.
	 * 
	 * @return A object castable to the given class, or null if this object does not
	 * have an adapter for the given class.
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter)
	{
		return null;
	}

	/**
	 * Returns whether this element can be terminated.
	 * 
	 * @return <b>true</b> is returned if the process can be terminated.
	 * Otherwise <b>false</b> will be returned.
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate()
	{
		return true;
	}

	/**
	 * Returns whether this element is terminated.
	 * 
	 * @return <b>true</b> is returned if the process is terminated.
	 * Otherwise <b>false</b> will be returned.
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated()
	{
		return m_terminated;
	}

	/**
	 * Causes this element to terminate, generating a TERMINATE event.
	 * Implementations may be blocking or non-blocking.
	 * 
	 * @throws org.eclipse.debug.core.DebugException This exception is thrown
	 * if termination is unsuccessful.
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() throws DebugException
	{
	    // Close views if using Magic Lantern Perspective.
	    RehearsalPlugin.getDefault().closePlayer();
		m_terminated = true;
	}
	
	/**
	 * Set the view configuration.
	 * 
	 * @param config The type of view configuration for launching the ActiveX
	 * components of the Rehearsal Player.
	 */
	public void setViewConfiguration(int config)
	{
	    if (config == LAUNCH_PERSPECTIVE)
	        m_usePerspective = true;
	    else
	        m_usePerspective = false;
	}

}
