/*
 * MleLog.java
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

// Import Eclipse packages.
import org.eclipse.core.runtime.Status;

// Import Magic Lantern Studio Framework packages.
import com.wizzer.mle.studio.framework.ILog;

/**
 * This class implements generic, application message logging for the Wizzer Works
 * Magic Lantern Studio plug-in.
 * <p>
 * Since all log messages should go through here, it's easy to redirect the messages somewhere 
 * other than the default location by subclassing this code.
 * </p><p>
 * The production plug-in sends all of these messages to the
 * Eclipse log file, but during development it is more convenient to
 * log to System.out or System.err. Thus, both techniques are employed by this API.
 * </p>
 * 
 * @author Mark S. Millard
 */
public class MleLog implements ILog
{
	// A singleton instance of the Magic Lantern Studio log.
	private static MleLog m_log = null;
	
	// Helper utility to log a error message and throw an exception. Note that the plugin may not be available
	// if the framework is being used in a standalone configuration (outside the Eclipse IDE).
	private static void logEclipseError(Exception ex, String message)
	{
		if (MlePlugin.getDefault() != null)
		    MlePlugin.getDefault().getLog().log(
				new Status(Status.ERROR, MlePlugin.getID(), Status.ERROR, message, ex));
	}

	// Helper utility to log a warning.  Note that the plugin may not be available
	// if the framework is being used in a standalone configuration (outside the Eclipse IDE).
	private static void logEclipseWarning(String message)
	{
		if (MlePlugin.getDefault() != null)
		    MlePlugin.getDefault().getLog().log(
				new Status(Status.WARNING, MlePlugin.getID(), Status.OK, message, null));
	}

	// Helper utility to log an informative message.  Note that the plugin may not be available
	// if the framework is being used in a standalone configuration (outside the Eclipse IDE).
	private static void logEclipseInfo(String message)
	{
		if (MlePlugin.getDefault() != null)
		    MlePlugin.getDefault().getLog().log(
				new Status(Status.INFO, MlePlugin.getID(), Status.OK, message, null));
	}
	
	/**
	 * Get the singleton instance of the Magic Lantern Studio Log. If the instance does not yet
	 * exist, it will be created.
	 * 
	 * @return A reference to a <code>MleLog</code> is returned.
	 */
	public static MleLog getInstance()
	{
		if (m_log == null)
			m_log = new MleLog();
		return m_log;
	}

	/**
	 * Log an error.
	 * <p>
	 * The message will be logged to <code>System.err</code> as well as the Eclipse log file.
	 * This method will also print a stack trace for the specified exception.
	 * </p>
	 * 
	 * @param ex The exception object.
	 * @param message The error message to log.
	 *
	 * @see com.wizzer.mle.studio.framework.ILog#error(java.lang.Exception, java.lang.String)
	 */
	public void error(Exception ex, String message)
	{
		System.err.println(message);
		ex.printStackTrace();
		logEclipseError(ex, message);
	}

	/**
	 * Log a warning message.
	 * <p>
	 * The message will be logged to <code>System.out</code> as well as the Eclipse log file.
	 * </p>
	 * 
	 * @param message The warning message to log.
	 * 
	 * @see com.wizzer.mle.studio.framework.ILog#info(java.lang.String)
	 */
	public void warning(String message)
	{
		System.out.println(message);
		logEclipseWarning(message);
	}

	/**
	 * Log an informative message.
	 * <p>
	 * The message will be logged to <code>System.out</code> as well as the Eclipse log file.
	 * </p>
	 * 
	 * @param message The informative message to log.
	 * 
	 * @see com.wizzer.mle.studio.framework.ILog#info(java.lang.String)
	 */
	public void info(String message)
	{
		System.out.println(message);
		logEclipseInfo(message);
	}

	/**
	 * Log a debug message.
	 * <p>
	 * The message will be logged only to <code>System.err</code>.
	 * </p>
	 * 
	 * @param message The debug message to log.
	 * 
	 * @see com.wizzer.mle.studio.framework.ILog#debug(java.lang.String)
	 */
	public void debug(String message)
	{
		System.err.println(message);
	}

	/**
	 * Log an error to the global Magic Lantern Studio log.
	 * <p>
	 * The message will be logged to <code>System.err</code> as well as the Eclipse log file.
	 * This method will also print a stack trace for the specified exception.
	 * </p>
	 * 
	 * @param ex The exception object.
	 * @param message The error message to log.
	 */
	static public void logError(Exception ex, String message)
	{
		MleLog log = getInstance();
		log.error(ex,message);
	}

	/**
	 * Log a warning message to the global Magic Lantern Studio log.
	 * <p>
	 * The message will be logged to <code>System.out</code> as well as the Eclipse log file.
	 * </p>
	 * 
	 * @param message The informative message to log.
	 */	
	static public void logWarning(String message)
	{
		MleLog log = getInstance();
		log.warning(message);
	}

	/**
	 * Log an informative message to the global Magic Lantern log.
	 * <p>
	 * The message will be logged to <code>System.out</code> as well as the Eclipse log file.
	 * </p>
	 * 
	 * @param message The informative message to log.
	 */ 
	static public void logInfo(String message)
	{
		MleLog log = getInstance();
		log.info(message);
	}

}
