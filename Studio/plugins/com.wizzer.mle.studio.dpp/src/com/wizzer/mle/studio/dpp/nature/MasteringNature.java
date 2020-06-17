/*
 * MasteringNature.java
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
package com.wizzer.mle.studio.dpp.nature;

// Import Eclipse classes.
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * This class implements a Nature for the Magic Lantern Mastering
 * process.
 * 
 * @author Mark S. Millard
 */
public class MasteringNature implements IProjectNature
{
	/** The Digital Playprint Mastering Nature. */
	public static final String NATURE_ID = "com.wizzer.mle.studio.dpp.MasteringNature";

    /** The Build identifier for the gengroup command. */
    public static final String GENGROUP_BUILDER_ID = "com.wizzer.mle.studio.dpp.GengroupBuilder";
    /** The Build identifier for the genscene command. */
    public static final String GENSCENE_BUILDER_ID = "com.wizzer.mle.studio.dpp.GensceneBuilder";
    /** The Build identifier for the genmedia command. */
    public static final String GENMEDIA_BUILDER_ID = "com.wizzer.mle.studio.dpp.GenmediaBuilder";
    /** The Build identifier for the gentables command. */
    public static final String GENTABLES_BUILDER_ID = "com.wizzer.mle.studio.dpp.GentablesBuilder";
    /** The Build identifier for the genppscript command. */
    public static final String GENPPSCRIPT_BUILDER_ID = "com.wizzer.mle.studio.dpp.GenppscriptBuilder";
    /** The Build identifier for the gendpp command. */
    public static final String GENDPP_BUILDER_ID = "com.wizzer.mle.studio.dpp.GendppBuilder";
    
    // The project handle
    private IProject m_project;
    
    /**
     * The default constructor.
     */
    public MasteringNature()
    {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#configure()
     */
    public void configure() throws CoreException
    {
        initializeGengroupBuilder(m_project);
        initializeGensceneBuilder(m_project);
        initializeGenmediaBuilder(m_project);
        initializeGentablesBuilder(m_project);
        initializeGenppscriptBuilder(m_project);
        initializeGendppBuilder(m_project);
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#deconfigure()
     */
    public void deconfigure() throws CoreException
    {
        // TODO Auto-generated method stub

    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#getProject()
     */
    public IProject getProject()
    {
        return m_project;
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#setProject(org.eclipse.core.resources.IProject)
     */
    public void setProject(IProject project)
    {
        m_project = project;
    }

	// Initializes the gengroup builder on the specified project.
	private void initializeGengroupBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	       if (commands[i].getBuilderName().equals(GENGROUP_BUILDER_ID))
	       {
	          found = true;
	          break;
	       }
	    }
	    
	    if (! found)
	    { 
	       // Add builder to project.
	       ICommand command = desc.newCommand();
	       command.setBuilderName(GENGROUP_BUILDER_ID);
	       ICommand[] newCommands = new ICommand[commands.length + 1];

	       // Add it before other builders.
	       System.arraycopy(commands, 0, newCommands, 1, commands.length);
	       newCommands[0] = command;
	       desc.setBuildSpec(newCommands);
	       project.setDescription(desc, null);
	    }
	}

	// Initializes the genscene builder on the specified project.
	private void initializeGensceneBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	       if (commands[i].getBuilderName().equals(GENSCENE_BUILDER_ID))
	       {
	          found = true;
	          break;
	       }
	    }
	    
	    if (! found)
	    { 
	       // Add builder to project.
	       ICommand command = desc.newCommand();
	       command.setBuilderName(GENSCENE_BUILDER_ID);
	       ICommand[] newCommands = new ICommand[commands.length + 1];

	       // Add it after gengroup builder but before other builders.
	       System.arraycopy(commands, 1, newCommands, 2, commands.length-1);
	       newCommands[0] = commands[0];
	       newCommands[1] = command;
	       desc.setBuildSpec(newCommands);
	       project.setDescription(desc, null);
	    }
	}

	// Initializes the genmedia builder on the specified project.
	private void initializeGenmediaBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	       if (commands[i].getBuilderName().equals(GENMEDIA_BUILDER_ID))
	       {
	          found = true;
	          break;
	       }
	    }
	    
	    if (! found)
	    { 
	       // Add builder to project.
	       ICommand command = desc.newCommand();
	       command.setBuilderName(GENMEDIA_BUILDER_ID);
	       ICommand[] newCommands = new ICommand[commands.length + 1];

	       // Add it after genscene builder but before other builders.
	       System.arraycopy(commands, 2, newCommands, 3, commands.length-2);
	       newCommands[0] = commands[0];
	       newCommands[1] = commands[1];
	       newCommands[2] = command;
	       desc.setBuildSpec(newCommands);
	       project.setDescription(desc, null);
	    }
	}

	// Initializes the gentables builder on the specified project.
	private void initializeGentablesBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	       if (commands[i].getBuilderName().equals(GENTABLES_BUILDER_ID))
	       {
	          found = true;
	          break;
	       }
	    }
	    
	    if (! found)
	    { 
	       // Add builder to project.
	       ICommand command = desc.newCommand();
	       command.setBuilderName(GENTABLES_BUILDER_ID);
	       ICommand[] newCommands = new ICommand[commands.length + 1];

	       // Add it after genmedia builder but before other builders.
	       System.arraycopy(commands, 3, newCommands, 4, commands.length-3);
	       newCommands[0] = commands[0];
	       newCommands[1] = commands[1];
	       newCommands[2] = commands[2];
	       newCommands[3] = command;
	       desc.setBuildSpec(newCommands);
	       project.setDescription(desc, null);
	    }
	}

	// Initializes the genppscript builder on the specified project.
	private void initializeGenppscriptBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	       if (commands[i].getBuilderName().equals(GENPPSCRIPT_BUILDER_ID))
	       {
	          found = true;
	          break;
	       }
	    }
	    
	    if (! found)
	    { 
	       // Add builder to project.
	       ICommand command = desc.newCommand();
	       command.setBuilderName(GENPPSCRIPT_BUILDER_ID);
	       ICommand[] newCommands = new ICommand[commands.length + 1];

	       // Add it after gentables builder but before other builders.
	       System.arraycopy(commands, 4, newCommands, 5, commands.length-4);
	       newCommands[0] = commands[0];
	       newCommands[1] = commands[1];
	       newCommands[2] = commands[2];
	       newCommands[3] = commands[3];
	       newCommands[4] = command;
	       desc.setBuildSpec(newCommands);
	       project.setDescription(desc, null);
	    }
	}

	// Initializes the gendpp builder on the specified project.
	private void initializeGendppBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	       if (commands[i].getBuilderName().equals(GENDPP_BUILDER_ID))
	       {
	          found = true;
	          break;
	       }
	    }
	    
	    if (! found)
	    { 
	       // Add builder to project.
	       ICommand command = desc.newCommand();
	       command.setBuilderName(GENDPP_BUILDER_ID);
	       ICommand[] newCommands = new ICommand[commands.length + 1];

	       // Add it after genppscript builder but before other builders.
	       System.arraycopy(commands, 5, newCommands, 6, commands.length-5);
	       newCommands[0] = commands[0];
	       newCommands[1] = commands[1];
	       newCommands[2] = commands[2];
	       newCommands[3] = commands[3];
	       newCommands[4] = commands[4];
	       newCommands[5] = command;
	       desc.setBuildSpec(newCommands);
	       project.setDescription(desc, null);
	    }
	}

}
