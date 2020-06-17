/*
 * GengroupBuildCommand.java
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
