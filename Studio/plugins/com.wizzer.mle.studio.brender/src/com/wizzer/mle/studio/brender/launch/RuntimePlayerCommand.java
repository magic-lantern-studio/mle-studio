/*
 * RuntimePlayerCommand.java
 * Created on Nov 1, 2006
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
package com.wizzer.mle.studio.brender.launch;

// Import standard Java classes.
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.dpp.DppCommand;
import com.wizzer.mle.studio.dpp.DppException;

import com.wizzer.mle.studio.brender.BRenderLog;

/**
 * This class is used to execute a title's runtime player. Essentially this is
 * executing the title.
 * 
 * @author Mark S. Millard
 */
public class RuntimePlayerCommand extends DppCommand
{
	/** The executable command. */
	protected String m_executable;
    /** The digital playprint. */
    protected File m_playprint = null;
    /** Flag indicating whether to be verbose or not. */
    protected boolean m_verbose = false;

	// Hide the default constructor.
	private RuntimePlayerCommand()
	{}

    /**
     * A constructor that specifies the Digital Playprint.
     * 
     * @param executable The title executable (e.g. HelloWorld.exe).
     * @param playprint The DPP to use with the title.
     * 
     * @throws DppException This exception is thrown if the title executable or
     * Digital Playprint is not specified.
     */
	public RuntimePlayerCommand(String executable,File playprint) throws DppException
	{
        super();
        if (executable == null)
        	throw new DppException("Executable not specified.");
        if (playprint == null)
            throw new DppException("Digital Playprint not specified.");
        m_executable = executable;
        m_playprint = playprint;
	}
	
   /**
     * Set the verbose mode.
     * 
     * @param value Set <b>true</b> if the command should be
     * verbose. Otherwise, set this parameter to <b>false</b>.
     */
    public void setVerbose(boolean value)
    {
        m_verbose = value;
    }
	
    /**
     * Execute the title.
     * 
     * @return If the command was successful, then <b>0</b> will be returned.
     * Otherwise a value of <b>-1</b> will be returned upon failure.
     * 
     * @throws DppException This exception is thrown if an error
     * occurs while processing the command.
     */
    public int exec() throws DppException
    {
        ArrayList argList = new ArrayList();
        
        argList.add(new String(m_executable));

        if (! m_playprint.exists())
            throw new DppException("Digital Playprint does not exist: " + m_playprint.getName());
        else
            argList.add(new String(m_playprint.getAbsolutePath().trim()));
        
        String[] args = (String[])argList.toArray(new String[0]);
        
        String[] output;
        try
        {
            output = runCommand(args, false, false);
        } catch (IOException ex)
        {
            throw new DppException("IO Error while running title.",ex);
        }
        
        // Dump output if in verbose mode.
        if (m_verbose)
        {
            for (int i = 0; i < output.length; i++)
                BRenderLog.getInstance().debug(output[i]);
        }
        
        return getResult();
    }
    
    /**
     * Kill the title.
     */
    public void kill()
    {
    	killCommand();
    }

    /**
     * Get the command process.
     * 
     * @return The <code>Process</code> is returned. Note that <>null<> may be returned if
     * no process is running.
     */
    public Process getProcess()
    {
    	return m_proc;
    }
}
