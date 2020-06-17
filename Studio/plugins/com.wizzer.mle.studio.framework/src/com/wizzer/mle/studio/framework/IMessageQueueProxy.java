/**
 * @file IMessageQueueProxy.java
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
