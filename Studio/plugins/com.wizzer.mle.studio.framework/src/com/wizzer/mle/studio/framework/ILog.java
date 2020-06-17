/**
 * @file ILog.java
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
