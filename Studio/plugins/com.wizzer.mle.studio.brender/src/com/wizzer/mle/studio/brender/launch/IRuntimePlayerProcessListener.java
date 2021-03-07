/*
 * IRuntimePlayerProcessListener.java
 * Created on Nov 3, 2006
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
package com.wizzer.mle.studio.brender.launch;

// Import standard Java classes.
import java.util.EventListener;

/**
 * This interface is used to register a listener with the <code>RuntimePlayerManager</code>.
 * 
 * @author Mark S. Millard
 */
public interface IRuntimePlayerProcessListener extends EventListener
{
	/**
	 * This method is called when a process is terminated.
	 * 
	 * @param process The <code>Process</code> that was terminated.
	 */
	public void processTerminated(Process process);
}
