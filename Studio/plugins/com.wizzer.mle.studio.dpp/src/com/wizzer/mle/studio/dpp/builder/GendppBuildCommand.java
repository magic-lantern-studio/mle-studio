/*
 * GendppBuildCommand.java
 */

// COPYRIGHT_BEGIN
//
// The MIT License (MIT)
//
// Copyright (c) 2000-2022 Wizzer Works
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
        	m_command.setScriptFilename(new File("src/gen/playprint.py"));

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

        // Set gendpp arguments.
        String script = GendppPropertyManager.getInstance().getScriptValue(resource);
        if (script != null)
            //m_command.setScriptFilename(expandFilePath(resource.getProject(),script));
        	m_command.setScriptFilename(new File(script));
        else
            //m_command.setScriptFilename(expandFilePath(resource.getProject(),"src/gen/playprint.tcl"));
        	m_command.setScriptFilename(new File("src/gen/playprint.py"));

    }

}
