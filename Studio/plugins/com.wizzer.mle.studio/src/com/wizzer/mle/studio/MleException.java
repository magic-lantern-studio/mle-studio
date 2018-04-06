/*
 * MleException.java
 * Created on Jun 21, 2005
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
package com.wizzer.mle.studio;

/**
 * This class is an <code>Exception</code> used to manage errors that occur
 * while working with Magic Lantern projects.
 * 
 * @author Mark S. Millard
 */
public class MleException extends Exception
{
	/**
	 * Constructs a new exception with <b>null</b> as its detail message.
	 * <p>
	 * The cause is not initialized, and may subsequently be initialized by a call to
	 * <code>Throwable.initCause(java.lang.Throwable)</code>.
	 * </p>
	 */
    public MleException()
    {
        super();
    }

	/**
	 * Constructs a new exception with the specified detail message.
	 * <p>
	 * The cause is not initialized, and may subsequently be initialized by a call to
	 * <code>Throwable.initCause(java.lang.Throwable)</code>.
	 * </p>
	 * 
	 * @param message The detail message. The detail message is saved for later retrieval
	 * by the <code>Throwable.getMessage()</code> method.

	 */
    public MleException(String message)
    {
        super(message);
    }

	/**
	 * Constructs a new exception with the specified cause and a detail message of
	 * (<code>cause==null ? null : cause.toString()</code>) which typically contains
	 * the class and detail message of <code>cause</code>. This constructor is useful
	 * for exceptions that are little more than wrappers for other throwables
	 * (for example, <code>PrivilegedActionException</code>).
	 * 
	 * @param cause The cause (which is saved for later retrieval by the
	 * <code>Throwable.getCause()</code> method). A <b>null</b> value is permitted,
	 * and indicates that the cause is nonexistent or unknown.
	 */
    public MleException(Throwable cause)
    {
        super(cause);
    }

	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * <p>
	 * Note that the detail message associated with <code>cause</code> is not
	 * automatically incorporated in this exception's detail message.
	 * </p>
	 * 
	 * @param message The detail message. The detail message is saved for later retrieval
	 * by the <code>Throwable.getMessage()</code> method.
	 * @param cause The cause (which is saved for later retrieval by the
	 * <code>Throwable.getCause()</code> method). A <b>null</b> value is permitted,
	 * and indicates that the cause is nonexistent or unknown.
	 */
    public MleException(String message, Throwable cause)
    {
        super(message, cause);
    }

}
