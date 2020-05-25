// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2010  Wizzer Works
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
package com.wizzer.mle.studio.project.metadata;

/**
 * This class is a base exception class for Magic Lantern
 * project meta-data exceptions.
 * 
 * @author Mark S. Millard
 */
public class MetaDataException extends Exception
{
    // Serialization identifier.
	private static final long serialVersionUID = -8107201146899782104L;

	/**
     * Creates a new <code>MetaDataException</code>.
     */
    public MetaDataException()
    {
        super();
    }

    /**
     * Creates a new <code>MetaDataException</code> with
     * an error message.
     * 
     * @param arg0 An error message.
     */
    public MetaDataException(String arg0)
    {
        super(arg0);
    }

    /**
     * Creates a new <code>MetaDataException</code> with
     * a root exception.
     * 
     * @param arg0 A root cause.
     */
    public MetaDataException(Throwable arg0)
    {
        super(arg0);
    }

    /**
     * Creates a new <code>MetaDataException</code> with
     * an error message and root cause.
     * 
     * @param arg0 An error message.
     * @param arg1 The cause exception.
     */
    public MetaDataException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

}
