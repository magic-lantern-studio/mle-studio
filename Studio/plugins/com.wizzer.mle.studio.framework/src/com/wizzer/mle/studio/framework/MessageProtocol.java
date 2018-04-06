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
 * This class specifies the known range of message protocols used by the
 * Magic Lantern Studio Framework.
 * 
 * @author Mark S. Millard
 */
public class MessageProtocol
{
	public static final byte FIRST_MSGTYPE = (byte)128;
	public static final byte PLUGIN_STATUS_MSGTYPE = (byte)255;
	public static final byte LAST_MSGTYPE = (byte)255;
	
	/**
	 * The default constructor.
	 */
	public MessageProtocol()
	{
		super();
	}

}
