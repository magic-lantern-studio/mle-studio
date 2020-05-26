// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework;

// Import Simulator Tool Framework packages.

/**
 * This class is a <code>Thread</code> that will process all Messages in a
 * <code>MessageQueue</code> held by a registered proxy.
 * <p>
 * This class is very useful for processing messages from non-UI threads in
 * the context of the Eclipse SWT GUI thread.
 * </p>
 * 
 * @author Mark S. Millard
 * @created Oct 8, 2003
 */
public class ProcessMessageQueue extends Thread
{
	// A proxy that will be used to pocess messages in a MessageQueue.
	private IMessageQueueProxy m_proxy = null;
	
	/**
	 * A constructor that establishes the <code>MessageQueue</code> proxy.
	 * 
	 * @param proxy The <code>MessageQueue</code> proxy.
	 */
	public ProcessMessageQueue(IMessageQueueProxy proxy)
	{
		m_proxy = proxy;
	}
	
	/**
	 * Process all messages beholding to the <code>MessageQueue</code> proxy.
	 */
	public void run()
	{
		// Process all pending messages in the MessageQueue.
		//System.out.println("Processing Queued Messages.");
		m_proxy.processMessages();
	}
}
