/**
 * DwpExportWizardMessages.java
 * Created on June 14, 2005
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
package com.wizzer.mle.studio.dwp.wizard;

// Import standard Java packages.
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// Import OCAP Plug-in packages.
import com.wizzer.mle.studio.dwp.DwpLog;

/**
 * This class is used to manage messages as a resource bundle.
 * 
 * @author Mark S. Millard
 */
public class DwpExportWizardMessages
{
    // The name of the resource bundle.
	private static final String RESOURCE_BUNDLE = DwpExportWizardMessages.class.getName();
	// The resource bundle.
	private static ResourceBundle g_resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

	// Hide the default constructor.
	private DwpExportWizardMessages()
	{
		// Do nothing for now.
	}

	/**
	 * Get the value of the specified key from the message resource bundle.
	 * 
	 * @param key The key identifying which resource to retrieve.
	 * 
	 * @return The value of the resource is returned as a <code>String</code>.
	 * If the resource is missing, a bogus string is returned identifying
	 * which key is the offender.
	 */
	public static String getString(String key)
	{
		try
		{
			return g_resourceBundle.getString(key);
		} catch (MissingResourceException ex)
		{
			DwpLog.logWarning("Missing Resource: " + key);
			return '!' + key + '!';
		}
	}
	
	/**
	 * Gets a string from the resource bundle and formats it with the argument.
	 * 
	 * @param key The string used to get the bundle value, must not be <b>null</b>.
	 */
	public static String getFormattedString(String key, Object arg)
	{
		return MessageFormat.format(getString(key), new Object[] { arg });
	}

	/**
	 * Gets a string from the resource bundle and formats it with arguments.
	 */	
	public static String getFormattedString(String key, Object[] args)
	{
		return MessageFormat.format(getString(key), args);
	}
}
