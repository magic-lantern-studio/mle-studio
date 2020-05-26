/*
 * AbstractMakefile.java
 * Created on Jun 24, 2005
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
package com.wizzer.mle.studio.make;

// Import standard Java classes.
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;

// Import Magic Lantern code generation classes.

// Import Magic Lantern Digital Playprint classes.
import com.wizzer.mle.codegen.AbstractMasteringConfiguration;
import com.wizzer.mle.studio.dpp.DppException;

/**
 * This class provides a partial implementation of the Makefile configuration
 * API for Magic Lantern.
 * 
 * @author Mark S. Millard
 */
public abstract class AbstractMakefile extends AbstractMasteringConfiguration
{
	/** The Mastering Project associated with this configuration. */
	protected IProject m_project;

	// Hide the default constructor.
	private AbstractMakefile() {}
	
	/**
	 * A constructor that associates the Makefile Configuration with the specified
	 * project.
	 * 
	 * @param project The project resource. It must be a Magic Lantern Digital Playprint
	 * project.
	 * 
	 * @throws DppException This exception is thrown if the configuration for
	 * the Ismrules Makefile can not be constructed.
	 */
	public AbstractMakefile(IProject project)
	    throws DppException
	{
		m_project = project;
	}
	
    /**
     * Generate the Makefile.
     * 
     * @return A <code>ByteArrayOutputStream</code> is returned.
     * 
     * @throws IOException This exception is thrown if the Makefile can
     * not be generated.
     */
    public abstract ByteArrayOutputStream generate() throws IOException;
}
