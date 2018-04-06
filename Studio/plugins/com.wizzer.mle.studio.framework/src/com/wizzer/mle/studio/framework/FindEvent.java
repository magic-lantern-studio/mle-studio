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

// Import standard Java classes.
import java.util.EventObject;
import java.io.File;

/**
 * The event for a found file system object.
 * 
 * @author Mark S. Millard
 */
public class FindEvent extends EventObject
{
    // The serialization unique identifier.
	private static final long serialVersionUID = 2981704975285161266L;

	/** The file that was found. */
    public File m_file = null;
    
    /**
     * Constructs an event for the specified source.
     * 
     * @param source The object on which the Event initially occurred.
     */
    public FindEvent(Object source)
    {
        super(source);
    }

}
