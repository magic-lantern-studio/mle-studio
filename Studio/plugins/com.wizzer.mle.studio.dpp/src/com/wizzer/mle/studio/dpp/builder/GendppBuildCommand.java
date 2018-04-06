/*
 * GendppBuildCommand.java
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
import com.wizzer.mle.studio.dwp.DwpProjectManager;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppGendpp;
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GendppPropertyManager;

/**
 * This class is used to manage the <i>gendpp</i> build command.
 * 
 * @author Mark S. Millard
 */
public class GendppBuildCommand
{
    // The gendpp command.
    private DppGendpp m_command = null;

    /**
     * A constructor that builds the <i>gendpp</i> command line.
     * 
     * @param resource The Digital Workprint resource to build the
     * command for.
     * 
     * @throws DppException This exception is thrown if an error
     * occurs while constructing the command.
     */
    public GendppBuildCommand(IResource resource) throws DppException
    {
        super();

        // Create a gendpp command.
        IPath location = resource.getLocation();
        File dwp = location.toFile();;
        m_command = new DppGendpp(dwp);
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
     * @return A <code>DppGendpp</code> command is returned.
     */
    public DppGendpp getCommand()
    {
        return m_command;
    }

    // Set the arguments on the gendpp command for generating C+++ compatible
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
            //m_command.setOutputDir(expandFilePath(resource.getProject(),destDir));
            m_command.setOutputDir(new File(destDir));
        else
            //m_command.setOutputDir(expandFilePath(resource.getProject(),"src/gen"));
        	m_command.setOutputDir(new File("src/gen"));
        
        String srcDir = GendppPropertyManager.getInstance().getSourceDirValue(resource);
        if (srcDir != null)
            //m_command.setInputDir(expandFilePath(resource.getProject(),srcDir));
        	m_command.setInputDir(new File(srcDir));
        else
            //m_command.setInputDir(expandFilePath(resource.getProject(),"src/gen"));
        	m_command.setInputDir(new File("src/gen"));
        
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
        
        // Set gendpp arguments.
        String script = GendppPropertyManager.getInstance().getScriptValue(resource);
        if (script != null)
            //m_command.setScriptFilename(expandFilePath(resource.getProject(),script));
        	m_command.setScriptFilename(new File(script));
        else
            //m_command.setScriptFilename(expandFilePath(resource.getProject(),"src/gen/playprint.tcl"));
        	m_command.setScriptFilename(new File("src/gen/playprint.tcl"));

    }
    
    // Expand the specified path relative to the specified project.
    private File expandFilePath(IProject project,String path)
    {        
        String fullpath;
        if (DppPlugin.isWindows())
            fullpath = new String(project.getLocation() + "\\" + path);
        else
        	fullpath = new String(project.getLocation() + "/" + path);
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
            m_command.setOutputDir(expandFilePath(resource.getProject(),destDir));
        	//m_command.setOutputDir(new File(destDir));
        else
            m_command.setOutputDir(expandFilePath(resource.getProject(),"src/gen"));
        	//m_command.setOutputDir(new File("src/gen"));
        
        String srcDir = GendppPropertyManager.getInstance().getSourceDirValue(resource);
        if (srcDir != null)
            m_command.setInputDir(expandFilePath(resource.getProject(),srcDir));
        	//m_command.setInputDir(new File(srcDir));
        else
            m_command.setInputDir(expandFilePath(resource.getProject(),"src/gen"));
        	//m_command.setInputDir(new File("src/gen"));

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
        String script = GendppPropertyManager.getInstance().getScriptValue(resource);
        if (script != null)
            //m_command.setScriptFilename(expandFilePath(resource.getProject(),script));
        	m_command.setScriptFilename(new File(script));
        else
            //m_command.setScriptFilename(expandFilePath(resource.getProject(),"src/gen/playprint.tcl"));
        	m_command.setScriptFilename(new File("src/gen/playprint.tcl"));

    }

}
