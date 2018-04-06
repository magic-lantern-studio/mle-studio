/*
 * MleMakeProjectOptionPage.java
 * Created on May 11, 2006
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
package com.wizzer.mle.studio.brender.project;

// Import Eclipse classes.
import org.eclipse.core.runtime.IConfigurationElement;

// Import Eclipse CDT classes.
import org.eclipse.cdt.make.ui.wizards.MakeProjectWizardOptionPage;

/**
 * This class is an option page for the CDT Standard Makefile configuration.
 * 
 * @author Mark Millard
 */
public class MleMakeProjectOptionPage extends MakeProjectWizardOptionPage
{
	// The configuration element.
	private IConfigurationElement m_configurationElement = null;
	
	/**
	 * A constructor that sets the title, description and configuration element
	 * for the project's Makefile option page.
	 * 
	 * @param title The title for the option page.
	 * @param description A description of the option page.
	 * @param element The configuration element.
	 */
	public MleMakeProjectOptionPage(String title,String description, IConfigurationElement element)
	{
		super(title,description);
		m_configurationElement = element;
	}
	
	/**
	 * Get the configuration element of this page.
	 * 
	 * @return Returns the <code>IConfigurationElement</code> that was set
	 * during construction.
	 */
	public IConfigurationElement getConfigurationElement()
	{
		return m_configurationElement;
	}

}
