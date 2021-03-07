/*
 * MleSimpleDwp.java
 * Create on Feb 4, 2008
 */

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2008  Wizzer Works
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
package com.wizzer.mle.studio.android.dwp;

// Import standard Java classes.
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;

// Import Magic Lantern code generation classes.

// Import Magic Lantern Environment classes.
import com.wizzer.mle.codegen.IDwpConfiguration;
import com.wizzer.mle.codegen.SimpleAndroidDwpTemplate;
import com.wizzer.mle.studio.dwp.AbstractDigitalWorkprint;

/**
 * This class is used to generate the Magic Lantern Simple Android DWP
 * in the workprints directory.
 *  
 * @author Mark S. Millard
 */
public class MleSimpleDwp extends AbstractDigitalWorkprint
{
	// The Digital Workprint template.
	private SimpleAndroidDwpTemplate m_template;

	/**
	 * A constructor that associates the DWP Configuration with the specified
	 * project.
	 * 
	 * @param project The project resource.
	 */
	public MleSimpleDwp(IProject project)
	{
		super(project);
		m_template = new SimpleAndroidDwpTemplate();
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
