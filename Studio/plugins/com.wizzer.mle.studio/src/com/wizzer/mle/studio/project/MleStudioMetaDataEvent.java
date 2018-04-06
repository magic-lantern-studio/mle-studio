// COPYRIGHT_BEGIN
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
