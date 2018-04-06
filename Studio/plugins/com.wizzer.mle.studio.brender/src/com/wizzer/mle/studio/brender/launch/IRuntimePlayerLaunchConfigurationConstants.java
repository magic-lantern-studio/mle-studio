/*
 * IRuntimePlayerLaunchConfigurationConstants.java
 * Created on Jun 5, 2006
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

import com.wizzer.mle.studio.dpp.launch.IDppLaunchConfigurationConstants;

/**
 * Constant definitions for Runtime Player launch configurations. 
 * <p>
 * Constant definitions only; not to be implemented.
 * </p>
 * 
 * @author Mark S. Millard
 */
public interface IRuntimePlayerLaunchConfigurationConstants extends IDppLaunchConfigurationConstants
{
	/**
	 *  The unique identifier for the Runtime Player launch configuration type.
	 */
	static final public String ID_RUNTIMEPLAYER_CONFIGURATION =
	    "com.wizzer.mle.studio.brender.launch.RuntimePlayerLaunchConfigurationType";

	/**
	 * Launch configuration attribute key.
	 * <p>
	 * The title executable.
	 * </p>
	 */
	static final public String ATTR_RUNTIME_EXECUTABLE = "title";

	/**
	 * Launch configuration attribute key.
	 * <p>
	 * The Digital Playprint file.
	 * </p>
	 */
	static final public String ATTR_DIGITAL_PLAYPRINT = "digitalPlayprint";
	
	/**
	 * Launch configuration attribute key.
	 * <p>
	 * The working directory for the Runtime Player.
	 * </p>
	 */
	static final public String ATTR_WORKING_DIRECTORY = "workingDirectory";
}
