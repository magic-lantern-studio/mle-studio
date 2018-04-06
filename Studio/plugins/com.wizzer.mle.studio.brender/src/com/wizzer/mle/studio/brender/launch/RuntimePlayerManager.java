/*
 * RuntimePlayerManager.java
 * Created on Nov 3, 2006
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

// Import standard Java classes.
import java.util.Vector;

/**
 * This class is used to manage Runtime Player components.
 * 
 * @author Mark S. Millard
 */
public class RuntimePlayerManager
{
	// The singleton instance of the manager.
	private static RuntimePlayerManager m_theManager = null;
	
	// List of process listeners.
	private Vector m_processListeners;
	
	/**
	 * This class implements a <code>Thread</code> for waiting on a particular
	 * process.
	 * 
	 * @author Mark S. Millard
	 */
	protected class WaitForProcess extends Thread
	{
		// A process to wait on.
		private Process m_process;
		// The listeners to notify when the wait has completed.
		private IRuntimePlayerProcessListener m_listeners[] = null;
		// The exit status.
		private int m_exitValue;
		
		// Hide the default constructor.
		private WaitForProcess() {}
		
		/**
		 * A constructor identifying which process to wait for.
		 * 
		 * @param process The <code>Process</code> to wait for.
		 */
		public  WaitForProcess(Process process)
		{
			m_process = process;
		}
		
		/**
		 * Set the list of listeners to be notified when the process has
		 * been terminated.
		 * 
		 * @param listeners The listeners to notify of process termination.
		 */
		public void setListeners(IRuntimePlayerProcessListener listeners[])
		{
			m_listeners = listeners;
		}
		
		/**
		 * Execute the thread, waiting for the specified process to complete.
		 */
		public void run()
		{
			try
			{
				m_exitValue = m_process.waitFor();
			} catch (InterruptedException ex)
			{
				m_exitValue = -1;
			}
			
			// Notify registered listeners that the process has terminated.
			if (m_listeners != null)
			{
				for (int i = 0; i < m_listeners.length; i++)
				{
					m_listeners[i].processTerminated(m_process);
				}
			}
		}
		
		/**
		 * Get the exit value for the thread execution.
		 * 
		 * @return The results of <code>Process.waitfor()</code> is returned. If a
		 * <code>InterruptedException</code> is thrown while waiting for the process to
		 * terminate, then <b>-1</b> will be returned.
		 */
		public int getExitValue()
		{
			return m_exitValue;
		}
	}
	
	// Hide the default contstructor.
	private RuntimePlayerManager()
	{
		super();
		m_processListeners = new Vector();
	}
	
	/**
	 * Retrieve the Singleton instance of the Runtime Player Manager.
	 * 
	 * @return The manage is returned.
	 */
	static public RuntimePlayerManager getInstance()
	{
		if (m_theManager == null)
			m_theManager = new RuntimePlayerManager();
		return m_theManager;
	}
	
	/**
	 * Add a listener for Runtime Player process events.
	 * 
	 * @param listener The listener to add.
	 * 
	 * @return If the listener was added successfully, then <b>true</b> will be returned.
	 * Otherwise <b>false</b> will be returned.
	 */
	public boolean addProcessListener(IRuntimePlayerProcessListener listener)
	{
		return m_processListeners.add(listener);
	}
	
	/**
	 * Remove a listener from the Runtime Player process list.
	 * 
	 * @param listener The listener to remove.
	 * 
	 * @return If the listener was removed successfully, then <b>true</b> will be returned.
	 * Otherwise <b>false</b> will be returned.
	 */
	public boolean removeProcessListener(IRuntimePlayerProcessListener listener)
	{
		return m_processListeners.remove(listener);
	}
	
	/**
	 * Remove all Runtime Player process listeners.
	 */
	public void removeAllListeners()
	{
		m_processListeners.removeAllElements();
	}

	/**
	 * Wait for the specified process to terminate.
	 * 
	 * @param process The <code>Process</code> to wait for.
	 */
	public void waitFor(Process process)
	{
		// Create a Thread which will be used to wait.
		WaitForProcess wfp = new WaitForProcess(process);
		
		// Add the listeners that will be notified when the process has terminated.
		IRuntimePlayerProcessListener listeners[] = new IRuntimePlayerProcessListener[m_processListeners.size()];
		listeners = (IRuntimePlayerProcessListener[])m_processListeners.toArray(listeners);
		wfp.setListeners(listeners);
		
		// Execute the thread which will wait on the specified process.
		wfp.start();
	}
}
