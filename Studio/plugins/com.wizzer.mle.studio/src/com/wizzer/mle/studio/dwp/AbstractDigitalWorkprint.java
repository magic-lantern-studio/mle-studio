/*
 * AbstractDigitalWorkprint.java
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
