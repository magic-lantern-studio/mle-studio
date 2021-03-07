/*
 * MleLeafdefsMakefile.java
 * Create on May 2, 2006
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
package com.wizzer.mle.studio.brender.make;

// Import standard Java classes.
import java.io.ByteArrayOutputStream;
import java.io.IOException;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;

// Import Magic Lantern code generation classes.

// Import Magic Lantern Digital Playprint classes.

// Import Magic Lantern Environment classes.
import com.wizzer.mle.codegen.IMasteringConfiguration;
import com.wizzer.mle.codegen.LeafdefsTemplate;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;
import com.wizzer.mle.studio.make.AbstractMakefile;

/**
 * This class is used to generate the Magic Lantern C/C++ Leafdefs Makefile.
 *  
 * @author Mark S. Millard
 */
public class MleLeafdefsMakefile extends AbstractMakefile
{
	// The Makefile template.
	private LeafdefsTemplate m_template;

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
	public MleLeafdefsMakefile(IProject project)
	    throws DppException
	{
		super(project);
		m_template = new LeafdefsTemplate();
		init();
	}
	
    /*
     * Initialize the configuration.
     */
    private void init() throws DppException
    {
    	if (! MasteringProjectManager.getInstance().isDppProject(m_project))
    		throw new DppException("The project " + m_project.getName() +
    			" is not a Digital Playprint project.");

    	String projectName = m_project.getName();
        setPropertyValue(IMasteringConfiguration.PROJECT_NAME,projectName);
        setPropertyValue(IMasteringConfiguration.PROJECT_SOURCE_DIRECTORY,new String("src"));
        String destination = MasteringProjectManager.getInstance().getDestinationDirValue(m_project);
        if (destination.startsWith("src"))
        {
        	// Strip off the source directory.
        	int index = destination.indexOf("/");
        	destination = destination.substring(index +  1);
        }
        setPropertyValue(IMasteringConfiguration.PROJECT_DESTINATION_DIRECTORY,destination);
        setPropertyValue(IMasteringConfiguration.PROJECT_BUILD_DIRECTORY,new String("build"));
        String toc = GenppscriptPropertyManager.getInstance().getTocValue(m_project);
        if (toc != null)
        	setPropertyValue(IMasteringConfiguration.GENPPSCRIPT_TOC,new String(toc));
    }
    
    /**
     * Set the mastering configuration property.
     * 
     * @param useMastering If <b>true</b> then use the Mastering Configuration for
     * generating the leafdefs Makefile. Otherwise, don't generate Mastering
     * Configuration Make dependencies.
     */
    public void setConfiguration(boolean useMastering)
    {
    	if (useMastering)
    		setPropertyValue(IMasteringConfiguration.USE_MASTERING_CONFIGURATION,new Boolean(true));
    	else
    		setPropertyValue(IMasteringConfiguration.USE_MASTERING_CONFIGURATION,new Boolean(false));
    }
    
    /**
     * Generate the Makefile.
     * 
     * @return A <code>ByteArrayOutputStream</code> is returned.
     * 
     * @throws IOException This exception is thrown if the Makefile can
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
