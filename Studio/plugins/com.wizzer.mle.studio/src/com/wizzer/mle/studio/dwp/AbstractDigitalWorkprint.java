/*
 * AbstractDigitalWorkprint.java
 * Created on May 22, 2006
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
package com.wizzer.mle.studio.dwp;

// Import standard Java classes.
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;

// Import Magic Lantern code generation classes.
import com.wizzer.mle.codegen.AbstractDwpConfiguration;

/**
 * This class provides a partial implementation of the Digital Workprint configuration
 * API for Magic Lantern.
 * 
 * @author Mark S. Millard
 */
public abstract class AbstractDigitalWorkprint extends AbstractDwpConfiguration
{
	/** The Project associated with this configuration. */
	protected IProject m_project;

	// Hide the default constructor.
	private AbstractDigitalWorkprint() {}
	
	/**
	 * A constructor that associates the DWP Configuration with the specified
	 * project.
	 * 
	 * @param project The project resource.
	 */
	public AbstractDigitalWorkprint(IProject project)
	{
		m_project = project;
	}
	
    /**
     * Generate the Makefile.
     * 
     * @return A <code>ByteArrayOutputStream</code> is returned.
     * 
     * @throws IOException This exception is thrown if the DWP can
     * not be generated.
     */
    public abstract ByteArrayOutputStream generate() throws IOException;
}
