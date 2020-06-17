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
package com.wizzer.mle.studio.project;

// Import standard Java classes.
import java.util.EventObject;

/**
 * The event object for a meta-data event;
 * 
 * @author Mark S. Millard
 */
public class MleStudioMetaDataEvent extends EventObject
{
    // The serialization unique identifier.
	private static final long serialVersionUID = 6254144720466749905L;
	
	/** An unknown state. */
    public static final int UNKNOWN = -1;
    /** The state of the meta-data is ok to process. */
    public static final int OK      = 0;
    /** The state of the meta-data may be problematic. */
    public static final int WARNING = 1;
    /** The state of the meta-data is in error. */
    public static final int ERROR   = 2;
    
    /** An event message. */
    public String m_msg = null;
    /** The event state. */
    public int m_state = UNKNOWN;

    /**
     * Constructs an event with the specified source.
     * 
     * @param source The object on which the Event initially occurred.
     */
    public MleStudioMetaDataEvent(Object source)
    {
        super(source);
    }

}
