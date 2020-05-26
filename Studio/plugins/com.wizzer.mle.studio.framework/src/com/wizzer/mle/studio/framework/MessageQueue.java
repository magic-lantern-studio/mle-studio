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

// Import standard Java packages.
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.Iterator;

/**
 * This class implements a FIFO <code>Message</code> queue.
 * 
 * @author Mark S. Millard
 * @created Sep 9, 2003
 */
public class MessageQueue extends Observable
{
	// The queue.
    private Vector m_queue = null;
    
    /**
     * Constructs a <code>Message</code> queue of the specified size.
     * 
     * @param size The initial size of the queue.
     */
    public MessageQueue(int size)
    {
    	m_queue = new Vector(size);
    }
    
    /**
     * Add a Message to the front of the queue.
     * 
     * @param msg The <code>Message</code> to add.
     * 
     * @return <b>true</b> is returned if the <code>Message</code> is successfully
     * added to the front of the queue. Otherwise, <b>false</b> will be returned.
     */
    public synchronized boolean push(Message msg)
    {
    	boolean status = m_queue.add(msg);
    	if (status) {
			setChanged();          // Mark the Observable as being changed.
    		notifyObservers(msg);  // Notify the Observers of the change.
    	} 
    	return status;
    }
    
    /**
     * Remove a <code>Message</code> from the back of the queue.
     * 
     * @return A <code>Message</code> is returned if one exists on the queue.
     * Otherwise, <b>null</b> will be returned.
     */
    public synchronized Message pop()
    {
    	if (isEmpty()) return null;
    	else {
    		Message msg = (Message)m_queue.remove(m_queue.size());
    		if (msg != null) {
    			setChanged();          // Mark the Observable as being changed.
    			notifyObservers(msg);  // Notify the Observers of the change.
    		}
    		return msg;
    	}
    }

	/**
	 * Remove a <code>Message</code> from the queue.
	 * 
	 * @param msg The <code>Message</code> to remove from the queue.
	 * 
	 * @return A reference to the <code>Message</code> is returned.
	 * Otherwise, <b>null</b> will be returned if the <code>Message</code> was not
	 * successfully removed.
	 */
	public synchronized Message pop(Message msg)
	{
		if (isEmpty()) return null;
		else {
			boolean status = m_queue.remove(msg);
			if (status) {
				setChanged();          // Mark the Observable as being changed.
				notifyObservers(msg);  // Notify the Observers of the change.
			}
			return msg;
		}
	}
	
	/**
	 * Remove a <code>Message</code> of the specified type from the queue.
	 * 
	 * @param type The type of <code>Message</code> to remove from the queue.
	 * 
	 * @return A reference to the <code>Message</code> is returned.
	 * Otherwise, <b>null</b> will be returned if the <code>Message</code> was not
	 * successfully removed.
	 */
	public synchronized Message pop(byte type)
	{
		Message msg = peek(type);
		if (msg != null)
		    msg = pop(msg);
		return msg;
	}
    
    /**
     * Determine if the queue is empty or not.
     * 
     * @return <b>true</b> will be returned if the queue is empty. Otherwise,
     * <b>false</b> will be returned.
     */
    public boolean isEmpty()
    {
    	return (m_queue.isEmpty());
    }
    
	/**
	 * Peek to see if a <code>Message</code> of the specified type exists on the queue.
	 * <p>
	 * The <code>Message</code> is <b>not</b> removed from the queue.
	 * </p>
	 * 
	 * @param type The type of <code>Message</code> to look for.
	 * 
     * @return A reference to the <code>Message</code> in the queue will be returned
     * if the queue contains a message of the specified type. 
     * Otherwise, <b>null</b> will be returned.
	 */
    public synchronized Message peek(byte type)
    {
    	if (isEmpty()) return null;

        Iterator iter = m_queue.listIterator();
    	while (iter.hasNext()) {
    		Message msg = (Message)iter.next();
    		if (msg.getMessageType() == type)
    			return msg;
    	}
    	
    	return null;
    }
    
    /**
     * Get the number of elements on the queue.
     * 
     * @return The size of the queue is returned.
     */
    public int size()
    {
    	return m_queue.size();
    }
    
    /**
     * Flush all Messages from the queue.
     */
    public synchronized void flush()
    {
    	m_queue.removeAllElements();
		setChanged();                // Mark the Observable as being changed.
		notifyObservers(null);       // Notify the Observers of the change.
    }
    
	// A unit test for MessageQueue.
	static public void main(String[] args)
	{
		class MyObserver implements Observer
		{
			public void update(Observable o, Object arg)
			{
				MessageQueue theQueue = (MessageQueue)o;
				Message msg = (Message)arg;
				if ((msg != null) && (msg.getMessageType() == (byte)10))
					System.out.println("Observer update called for Message type 10.");
			}
		}
		
		// Create a new Message Queue.
		MessageQueue queue = new MessageQueue(10);
		
		// Add an Observer to the queue.
		MyObserver observer = new MyObserver();
		queue.addObserver(observer);
		
        // Create some Messages and place them on the queue.
        byte[] data = new byte[20];
        for (int i = 0; i < 15; i++) {
        	Message msg = new Message((byte)i,(short)data.length,data);
        	queue.push(msg);
        }
        System.out.println("Message queue has " + queue.size() + " elements.");
        
        // Flush the queue.
        queue.flush();
        if (queue.isEmpty())
            System.out.println("Mesage queue successfully flushed.");
        else
        	System.out.println("Message queue not flushed successfully.");
	}
}
