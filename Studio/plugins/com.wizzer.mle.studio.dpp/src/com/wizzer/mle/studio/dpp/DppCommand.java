/*
 * DppCommand.java
 * Created on Jun 21, 2005
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
package com.wizzer.mle.studio.dpp;

// Import standard Java classes.
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.ArrayList;

/**
 * This class is used for executing commands related to mastering
 * a Magic Lantern Digital Workprint into its corresponding Digital
 * Playprint.
 * 
 * @author Mark S. Millard
 */
public class DppCommand
{
    /** The resulting process. */
    protected Process m_proc = null;
    /** The environment variable settings. */
    protected String[] m_envp = null;
    /** The working directory. */
    protected File m_dir = null;
    
    // Flag indicating whether to wait for command process to end or not.
    private boolean m_waiting = false;

    /**
     * This class provides utility for processing the <b>stdout</b> and
     * <b>stderr</b> stream IO.
     */
    class IOStreamHandler extends Thread
    {
        /** IO Stream Handler type for 'stderr' */
        public static final String HANDLER_TYPE_ERROR = "ERROR";
        /** IO Stream Handler type for 'stdout' */
        public static final String HANDLER_TYPE_OUTPUT = "OUTPUT";
        
        /** The input stream. */
        InputStream m_is;
        /** The stream results. */
        ArrayList m_list = new ArrayList();
        /** The type of handler. */
        String m_type;
        
        IOStreamHandler(InputStream is, String type)
        {
            m_is = is;
            m_type = type;
        }
        
        public String[] getResult()
        {
            return (String[])m_list.toArray(new String[0]);
        }
        
        public void run()
        {
            try
            {
                InputStreamReader isr = new InputStreamReader(m_is);
                BufferedReader br = new BufferedReader(isr);
                String line = null;
                while ((line = br.readLine()) != null)
                    m_list.add(line);    
            } catch (IOException ex)
            {
                DppLog.logError(ex,"Error occurred while processing "
                    + m_type + " stream.");
            }
        }
    }

    // Hide the default constructor.
    protected DppCommand()
    {
        super();
    }
    
    /**
     * Run the specified command as an external program/executable.
     * 
     * @param cmd The command array to execute.
     * @param wait Wait for execution to terminate.
     * @param useShell If <b>true</b> then prepend a command shell
     * to the command array.
     * 
     * @return The output from the command is returned as an array
     * of <code>String</code>.
     * 
     * @throws IOException This execption is thrown if an IO error occurs
     * while attempting to run the command.
     */
    protected String[] runCommand(String[] cmd, boolean wait, boolean useShell)
    	throws IOException
    {
    	String[] shellCmd;
        if (useShell)
    	    shellCmd = new String[cmd.length + 2];
        else
    	    shellCmd = new String[cmd.length];
    	
    	if (DppPlugin.isWindows())
    	{
            String osName = System.getProperty("os.name");

            if (useShell)
            {
	            if ((osName.equals("Windows NT")) ||
	                (osName.equals("Windows 2000")) ||
	                (osName.equals("Windows XP")) ||
	                (osName.equals("Windows Vista")) ||
	                (osName.equals("Windows 7")))
	            {
	                shellCmd[0] = "cmd.exe" ;
	                shellCmd[1] = "/C" ;
	            }
	            else if ((osName.equals( "Windows 95" )) ||
	        	         (osName.equals("Windows 98")))
	            {
	                shellCmd[0] = "command.com" ;
	                shellCmd[1] = "/C" ;
	            }
            }
    	} else if (DppPlugin.isLinux())
    	{
    		if (useShell)
    		{
    			shellCmd[0] = "bash";
    			shellCmd[1] = "-c";
    		}
    	}
    	
        for (int i = 0; i < cmd.length; i++)
        {
    	    if (useShell)
    		    shellCmd[i+2] = cmd[i];
    	    else
    		    shellCmd[i] = cmd[i];
        }
 
        // Print out command line.
        StringBuffer msg = new StringBuffer();
        for (int i = 0; i < shellCmd.length; i++)
        {
            msg.append(shellCmd[i]);
            msg.append(" ");
        }
        DppLog.getInstance().info("Running DPP command in " + m_dir);
        DppLog.getInstance().info(msg.toString().trim());
        
        // Execute a command and get its process handle.
        m_proc = Runtime.getRuntime().exec(shellCmd, m_envp, m_dir);
    
        try
        {
        	// Process output only if we are waiting for the command to complete.
        	if (wait)
        	{
        		// Set flag indicating we are going to wait for completion.
		        m_waiting = true;

		        // Get the handle for the processes.
		        InputStream istr = m_proc.getErrorStream();
		        // Create a BufferedReader and specify it reads from an input stream.
		        BufferedReader br = new BufferedReader(new InputStreamReader(istr));
		
		        // Any error message?
		        IOStreamHandler errorHandler = new 
		        	IOStreamHandler(m_proc.getErrorStream(), IOStreamHandler.HANDLER_TYPE_ERROR);            
		        
		        // Any output?
		        IOStreamHandler outputHandler = new 
		        	IOStreamHandler(m_proc.getInputStream(), IOStreamHandler.HANDLER_TYPE_OUTPUT);
		            
		        // Kick them off.
		        errorHandler.start();
		        outputHandler.start();

		        // Wait for process to terminate and catch any Exceptions.
                m_proc.waitFor();
                errorHandler.join();
                outputHandler.join();

		        // Note: proc.exitValue() returns the exit value. (Use if required)
		
		        br.close(); // Done.
		
		        // Convert the output to a string array and return.
		        if (m_proc.exitValue() == 0)
		        {
		            return outputHandler.getResult();
		        } else
		        {
		        	//m_proc.destroy();
		            return errorHandler.getResult();
		        }
        	}
        } catch (InterruptedException ex)
        {
            DppLog.logError(ex,"External process was interrupted.");
        }
        
        return null;
    }
    
    /**
     * Kill the command by killing the subprocess.
     */
    protected void killCommand()
    {
    	if (m_proc != null)
    	    m_proc.destroy();
    }

    /**
     * Get the exit value for the command.
     * 
     * @return An integer is returned representing the exit value
     * of the command. If the command has not yet been executed,
     * then <b>-1</b> will be returned.
     */
    protected int getResult()
    {
        if ((m_proc != null) && (m_waiting))
            return m_proc.exitValue();
        else if (! m_waiting)
        	return 0;
        else
            return -1;
    }
    
    /**
     * Get the working directory for the command's subprocess.
     * 
     * @return A <code>File</code> is returned. <b>null</b> may also be returned
     * if a location has not been previously set.
     */
    public File getLocation()
    {
    	return m_dir;
    }
    
    /**
     * Set the working directory for the command's subprocess.
     *
     * @param dir A <code>File</code> indicating the location of the working directory.
     * If dir is null, the subprocess inherits the current working directory of the current process.
     */
    public void setLocation(File dir)
    {
    	m_dir = dir;
    }
    
    /**
     * Get the environment variable settings for the command's subprocess.
     *
     * @return The environment variables are returned in an array of <code>String</code>.
     * <b>null</b> may be returned.
     */
    public String[] getEnvironment()
    {
    	return m_envp;
    }
    
    /**
     * Set the environment variable settings for the command's subprocess.
     * 
     * @param envp  An array of variables. If envp is null, the subprocess
     * inherits the environment settings of the current process.
     */
    public void setEnvironment(String[] envp)
    {
    	m_envp = envp;
    }
    
    /**
     * Get the process associated with this command.
     * 
     * @return A handle to the process is returned.
     */
    public Process getProcess()
    {
    	return m_proc;
    }
}
