/*
 * GenmediaBuildCommand.java
 * Created on Jul 27, 2005
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
package com.wizzer.mle.studio.dpp.builder;

// Import standard Java classes.
import java.io.File;

// Import Eclipse classes.
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

// Import Magic Lantern Digital Playprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppGenmedia;
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;

/**
 * This class is used to manage the <i>genmedia</i> build command.
 * 
 * @author Mark S. Millard
 */
public class GenmediaBuildCommand
{
    // The genmedia command.
    private DppGenmedia m_command = null;

    /**
     * A constructor that builds the <i>genmedia</i> command line.
     * 
     * @param resource The Digital Workprint resource to build the
     * command for.
     * 
     * @throws DppException This exception is thrown if an error
     * occurs while constructing the command.
     */
    public GenmediaBuildCommand(IResource resource) throws DppException
    {
        super();

        // Create a genmedia command.
        IPath location = resource.getLocation();
        File dwp = location.toFile();;
        m_command = new DppGenmedia(dwp);
        m_command.setLocation(resource.getProject().getLocation().toFile());

        // Set the arguments.
        setArguments(resource);
    }
    
    /**
     * Get the command line object.
     * 
     * @return A <code>DppGenmedia</code> command is returned.
     */
    public DppGenmedia getCommand()
    {
        return m_command;
    }

    // Set the arguments on the genmedia command for generating C+++ compatible
    // artifacts.
    private void setArguments(IResource resource)
        throws DppException
    {
        // Set common arguments.
        if (MasteringProjectManager.getInstance().isBigEndian(resource))
            m_command.setBigEndian(true);
        else
            m_command.setBigEndian(false);
        
        String destDir = MasteringProjectManager.getInstance().getDestinationDirValue(resource);
        if (destDir != null)
            m_command.setOutputDir(getDestinationDirFile(resource.getProject(),destDir));
        else
            m_command.setOutputDir(getDestinationDirFile(resource.getProject(),"gen"));
        
        String[] tags = MasteringProjectManager.getInstance().getTagsValue(resource);
        if (tags != null)
        {
            for (int i = 0; i < tags.length; i++)
                m_command.addTag(tags[i]);
        }
        
        boolean verbose = MasteringProjectManager.getInstance().getVerboseValue(resource);
        if (verbose)
            m_command.setVerbose(true);
        else
            m_command.setVerbose(false);
        
        // Set genmedia arguments.        
        String bom = GenmediaPropertyManager.getInstance().getBomValue(resource);
        if (bom != null)
            m_command.setBomFilename(bom);
        else
            m_command.setBomFilename("MediaBom.txt");
    }
    
    // Get the destination dir File object relative to the specified project.
    private File getDestinationDirFile(IProject project,String dest)
    {
    	String fullpath;
    	if (DppPlugin.isWindows())
            fullpath = new String(project.getLocation() + "\\" + dest);
    	else
    		fullpath = new String(project.getLocation() + "/" + dest);
        return new File(fullpath);
    }

}
