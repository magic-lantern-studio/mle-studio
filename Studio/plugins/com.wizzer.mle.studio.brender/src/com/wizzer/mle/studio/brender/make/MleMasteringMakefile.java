/*
 * MleMasteringMakefile.java
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
import com.wizzer.mle.codegen.MasterMakefileTemplate;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.*;
import com.wizzer.mle.studio.make.AbstractMakefile;

/**
 * This class is used to generate the Magic Lantern C/C++ Makefile
 * in the source directory.
 *  
 * @author Mark S. Millard
 */
public class MleMasteringMakefile extends AbstractMakefile
{
	// The Makefile template.
	private MasterMakefileTemplate m_template;
	
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
	public MleMasteringMakefile(IProject project)
	    throws DppException
	{
		super(project);
		m_template = new MasterMakefileTemplate();
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
        setPropertyValue(IMasteringConfiguration.PROJECT_DESTINATION_DIRECTORY,destination);
        setPropertyValue(IMasteringConfiguration.PROJECT_BUILD_DIRECTORY,new String("build"));
        boolean verbose = MasteringProjectManager.getInstance().getVerboseValue(m_project);
        setPropertyValue(IMasteringConfiguration.VERBOSE,new Boolean(verbose));
        boolean bigEndian = MasteringProjectManager.getInstance().isBigEndian(m_project);
        setPropertyValue(IMasteringConfiguration.ENDIAN_MODE,new Boolean(bigEndian));
        String[] tags = MasteringProjectManager.getInstance().getTagsValue(m_project);
        StringBuffer tagList = new StringBuffer();
        if (tags != null)
        {
	        for (int i = 0; i < tags.length; i++)
	        {
	        	tagList.append(tags[i]);
	        	if (i < (tags.length - 1))
	        		tagList.append(",");
	        }
        } else
        {
        	tagList.append("brender");
        }
        setPropertyValue(IMasteringConfiguration.TAGS,new String(tagList.toString()));
        String dwpName = projectName + ".dwp";
        setPropertyValue(IMasteringConfiguration.DWP,new String(dwpName));
        String dppName = projectName + ".dpp";
        setPropertyValue(IMasteringConfiguration.DPP,new String(dppName));
        String actorId = GengroupPropertyManager.getInstance().getActorIdValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENGROUP_ACTORID,new String(actorId));
        String groupId = GengroupPropertyManager.getInstance().getGroupIdValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENGROUP_GROUPID,new String(groupId));
        boolean fixedPoint = GengroupPropertyManager.getInstance().getFixedPointValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENGROUP_FIXED_POINT,new Boolean(fixedPoint));
        String sceneId = GenscenePropertyManager.getInstance().getSceneIdValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENSCENE_SCENEID,new String(sceneId));
        fixedPoint = GenscenePropertyManager.getInstance().getFixedPointValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENSCENE_FIXED_POINT,new Boolean(fixedPoint));
        String bom = GenmediaPropertyManager.getInstance().getBomValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENMEDIA_INVENTORY,new String(bom));
        String script = GenppscriptPropertyManager.getInstance().getScriptValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENPPSCRIPT_SCRIPT,new String(script));
        String toc = GenppscriptPropertyManager.getInstance().getTocValue(m_project);
        setPropertyValue(IMasteringConfiguration.GENPPSCRIPT_TOC,new String(toc));
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
