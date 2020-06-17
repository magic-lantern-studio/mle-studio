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
