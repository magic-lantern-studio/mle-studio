/**
 * @file IMessageQueueProxy.java
 */

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

/**
 * This interface is used to specify a proxy for an Object that can process <code>Message</code>s
 * in a <code>MessageQueue</code>.
 * <p>
 * This interface is useful when messages must be sent between Java threads. For example,
 * a status message may be sent to the Eclipse thread that is handling the SWT GUI event
 * processing and display update. The Object that updates the display, using the data from
 * the status message, must be running in the context of the Eclipse SWT thread.
 * The Object sending the message may be running in another thread. Thus,
 * the Object that updates the display is acting as a proxy for the Object that is sending the
 * status message.
 * </p>
 * 
 * @author Mark S. Millard
 */
public interface IMessageQueueProxy
{
	/**
	 * Get the <code>MessageQueue</code> managed by this proxy.
	 * 
	 * @return A reference to the managed <code>MessageQueue</code> is returned.
	 *
	 * @see com.wizzer.mle.studio.framework.MessageQueue
	 */
	public MessageQueue getMessageQueue();
	
	/**
	 * Process all messages in a <code>MessageQueue</code>.
	 *
	 * @see com.wizzer.mle.studio.framework.MessageQueue
	 */
    public void processMessages();
}
