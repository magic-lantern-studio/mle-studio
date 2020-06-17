/*
 * AbstractMakefile.java
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
