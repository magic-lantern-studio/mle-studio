/*
 * ResourceNature.java
 * Created on Feb 6, 2008
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
package com.wizzer.mle.studio.android.dpp;

// Import Eclipse classes.
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

/**
 * This class implements a Nature for the Magic Lantern Mastering
 * process targeting the Android platform.
 * 
 * @author Mark S. Millard
 */
public class ResourceNature implements IProjectNature
{
    /** The Build identifier for the Magic Lantern Android Resource builder. */
    public static final String RESOURCE_BUILDER_ID = "com.wizzer.mle.studio.android.ResourceBuilder";
    // The Build identifier for the gendpp command.
    private static final String GENDPP_BUILDER_ID = "com.wizzer.mle.studio.dpp.GendppBuilder";
    
    // The project handle
    private IProject m_project;
    
    /**
     * The default constructor.
     */
    public ResourceNature()
    {
        super();
    }

    /* (non-Javadoc)
     * @see org.eclipse.core.resources.IProjectNature#configure()
     */
    public void configure() throws CoreException
    {
        initializeResourceBuilder(m_project);
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

	// Initializes the Resource builder on the specified project.
	private void initializeResourceBuilder(IProject project) throws CoreException
	{
	    IProjectDescription desc = project.getDescription();
	    ICommand[] commands = desc.getBuildSpec();
	    boolean found = false;

	    for (int i = 0; i < commands.length; ++i)
	    {
	    	if (commands[i].getBuilderName().equals(RESOURCE_BUILDER_ID))
	        {
	            found = true;
	            break;
	        }
	    }
	    
	    if (! found)
	    { 
	        // Add builder to project.
	        ICommand resCommand = desc.newCommand();
	        resCommand.setBuilderName(RESOURCE_BUILDER_ID);
	        ICommand[] newCommands = new ICommand[commands.length + 1];

	        // Add it after the GenDpp builder.
	        for (int i = 0, j = 0; i < commands.length; ++i, ++j)
		    {
		    	if (commands[i].getBuilderName().equals(GENDPP_BUILDER_ID))
		        {
		    		newCommands[j++] = commands[i];
		    		newCommands[j] = resCommand;
		        } else
		        {
		        	newCommands[j] = commands[i];
		        }
		    }
	        
	        //System.arraycopy(commands, 0, newCommands, 1, commands.length);
	        //newCommands[0] = command;
	        desc.setBuildSpec(newCommands);
	        project.setDescription(desc, null);
	    }
	}

}
