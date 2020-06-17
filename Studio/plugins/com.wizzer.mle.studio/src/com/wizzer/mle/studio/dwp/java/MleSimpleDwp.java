/*
 * MleSimpleDwp.java
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
package com.wizzer.mle.studio.dwp.java;

// Import standard Java classes.
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;

// Import Magic Lantern code generation classes.

// Import Magic Lantern Environment classes.
import com.wizzer.mle.codegen.IDwpConfiguration;
import com.wizzer.mle.codegen.SimpleJavaDwpTemplate;
import com.wizzer.mle.studio.dwp.AbstractDigitalWorkprint;

/**
 * This class is used to generate the Magic Lantern Simple Java DWP
 * in the workprints directory.
 *  
 * @author Mark S. Millard
 */
public class MleSimpleDwp extends AbstractDigitalWorkprint
{
	// The Makefile template.
	private SimpleJavaDwpTemplate m_template;

	/**
	 * A constructor that associates the DWP Configuration with the specified
	 * project.
	 * 
	 * @param project The project resource.
	 */
	public MleSimpleDwp(IProject project)
	{
		super(project);
		m_template = new SimpleJavaDwpTemplate();
		init();
	}
	
    /*
     * Initialize the configuration.
     */
    private void init()
    {
    	String projectName = m_project.getName();
        setPropertyValue(IDwpConfiguration.PROJECT_NAME,projectName);
    }
    
    /**
     * Generate the Digital Workprint.
     * 
     * @return A <code>ByteArrayOutputStream</code> is returned.
     * 
     * @throws IOException This exception is thrown if the DWP can
     * not be generated.
     */
    public ByteArrayOutputStream generate() throws IOException
    {
    	String result = m_template.generate(this);
    	
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	byte[] data = result.getBytes();
    	out.write(data);
    	
    	return out;
    }

}
