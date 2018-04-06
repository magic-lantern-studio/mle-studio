/**
 * @file ILog.java
 * Created on Nov 5, 2003
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
 * This interface is used to log information to various destinations. The destination depends upon
 * the class implementation. The implementation is also responsible for the behavior of the application,
 * such as throwing an exception or printing a stack trace.
 * 
 * @author Mark S. Millard
 * @created Nov 5, 2003
 */
public interface ILog
{
	/**
	 * Log an error.
	 * 
	 * @param ex The exception object.
	 * @param message The message to log.
	 */
	public void error(Exception ex, String message);

	/**
	 * Log a warning.
	 * 
	 * @param message The message to log.
	 */
	public void warning(String message);

	/**
	 * Log an informative message.
	 * 
	 * @param message The informative message to log.
	 */
	public void info(String message);

	/**
	 * Log a debug message.
	 * 
	 * @param message The debug message to log.
	 */
	public void debug(String message);

}
