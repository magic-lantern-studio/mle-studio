/*
 * GengroupBuildCommand.java
 * Created on Jul 5, 2005
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
import com.wizzer.mle.studio.dwp.DwpProjectManager;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppGengroup;
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GengroupPropertyManager;

/**
 * This class is used to manage the <i>gengroup</i> build command.
 * 
 * @author Mark S. Millard
 */
public class GengroupBuildCommand
{
    // The gengroup command.
    private DppGengroup m_command = null;

    /**
     * A constructor that builds the <i>gengroup</i> command line.
     * 
     * @param resource The Digital Workprint resource to build the
     * command for.
     * 
     * @throws DppException This exception is thrown if an error
     * occurs while constructing the command.
     */
    public GengroupBuildCommand(IResource resource) throws DppException
    {
        super();

        // Create a gengroup command.
        IPath location = resource.getLocation();
        File dwp = location.toFile();;
        m_command = new DppGengroup(dwp);
        m_command.setLocation(resource.getProject().getLocation().toFile());

        // Set the arguments.
        if (DwpProjectManager.getInstance().isCppProject(resource.getProject()))
            setCppArguments(resource);
        else if (DwpProjectManager.getInstance().isJavaProject(resource.getProject()))
            setJavaArguments(resource);

    }
    
    /**
     * Get the command line object.
     * 
     * @return A <code>DppGengroup</code> command is returned.
     */
    public DppGengroup getCommand()
    {
        return m_command;
    }

    // Set the arguments on the gengroup command for generating C+++ compatible
    // artifacts.
    private void setCppArguments(IResource resource)
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
        
        m_command.setJavaCodeGeneration(false);
        
        // Set gengroup arguments.
        boolean fixedPoint = GengroupPropertyManager.getInstance().getFixedPointValue(resource);
        if (fixedPoint)
            m_command.setFixedPoint(true);
        else
            m_command.setFixedPoint(false);
        
        String actorId = GengroupPropertyManager.getInstance().getActorIdValue(resource);
        if (actorId != null)
            m_command.setActorIdFilename(actorId);
        else
            m_command.setActorIdFilename("actorid.h");
        
        String groupId = GengroupPropertyManager.getInstance().getGroupIdValue(resource);
        if (groupId != null)
            m_command.setGroupIdFilename(groupId);
        else
            m_command.setGroupIdFilename("groupid.h");

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
    
    // Set the arguments on the gengroup command for generating Java compatible
    // artifacts.    
    private void setJavaArguments(IResource resource)
    	throws DppException
    {
        // Set common arguments.
        m_command.setBigEndian(true);
        
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
        
        String packageName = MasteringProjectManager.getInstance().getJavaPackageValue(resource);
        if (packageName != null)
            m_command.setPackage(packageName);
        else
            m_command.setPackage("gen");

        // Set gengroup arguments.
        String actorId = GengroupPropertyManager.getInstance().getActorIdValue(resource);
        if (actorId != null)
            m_command.setActorIdFilename(actorId);
        else
            m_command.setActorIdFilename("ActorID.java");
        
        String groupId = GengroupPropertyManager.getInstance().getGroupIdValue(resource);
        if (groupId != null)
            m_command.setGroupIdFilename(groupId);
        else
            m_command.setGroupIdFilename("GroupID.java");

    }

}
