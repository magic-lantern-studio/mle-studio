/*
 * MleException.java
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
