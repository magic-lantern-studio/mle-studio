/*
 * GenppscriptBuildCommand.java
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
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppGenppscript;
import com.wizzer.mle.studio.dpp.DppPlugin;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;

/**
 * This class is used to manage the <i>genppscript</i> build command.
 * 
 * @author Mark S. Millard
 */
public class GenppscriptBuildCommand
{
    // The genppscript command.
    private DppGenppscript m_command = null;

    /**
     * A constructor that builds the <i>genppscript</i> command line.
     * 
     * @param resource The Digital Workprint resource to build the
     * command for.
     * 
     * @throws DppException This exception is thrown if an error
     * occurs while constructing the command.
     */
    public GenppscriptBuildCommand(IResource resource) throws DppException
    {
        super();

        // Create a genppscript command.
        IPath location = resource.getLocation();
        File dwp = location.toFile();;
        m_command = new DppGenppscript(dwp);
        m_command.setLocation(resource.getProject().getLocation().toFile());

        // Set the arguments.
        setArguments(resource);
    }
    
    /**
     * Get the command line object.
     * 
     * @return A <code>DppGenppscript</code> command is returned.
     */
    public DppGenppscript getCommand()
    {
        return m_command;
    }

    // Set the arguments on the genppscript command for generating C+++ compatible
    // artifacts.
    private void setArguments(IResource resource)
        throws DppException
    {        
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
        
        // Set genppscript arguments.       
        String dpp = GenppscriptPropertyManager.getInstance().getDppValue(resource);
        if (dpp != null)
            m_command.setDpp(dpp);
        else
            m_command.setDpp("playprint.dpp");

        String script = GenppscriptPropertyManager.getInstance().getScriptValue(resource);
        if (script != null)
            m_command.setScript(script);
        else
            m_command.setScript("playprint.py");

        String toc = GenppscriptPropertyManager.getInstance().getTocValue(resource);
        if (toc != null)
            m_command.setToc(toc);
        else
            m_command.setToc("DppTOC");

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
