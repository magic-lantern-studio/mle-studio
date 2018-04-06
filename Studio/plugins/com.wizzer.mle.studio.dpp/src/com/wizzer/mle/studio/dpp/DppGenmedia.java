/*
 * DppGenmedia.java
 * Created on Jul 26, 2005
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
import java.util.ArrayList;
import java.util.Vector;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to execute the <b>genmedia</b> Magic Lantern mastering program.
 * 
 * @author Mark S. Millard
 */
public class DppGenmedia extends DppCommand
{
    /** Verbose mode: [-v]. */
    protected boolean m_verbose = false;
    /** Endianess of chunk generation: [-b|-l]. */
    protected boolean m_bigEndian = true;
    
    /** The digital workprint. */
    protected File m_workprint = null;
    /** The output directory. */
    protected File m_outputDir = new File("gen");
    /** The media inventory file. */
    protected String m_bom = "MediaBom.txt";
    /** The tags. */
    Vector m_tags = new Vector();
    
    // Hide the default constructor.
    private DppGenmedia()
    {}

    /**
     * A constructor that specifies the Digital Workprint.
     * 
     * @param workprint The DWP to use with the <b>genmedia</b> command.
     * 
     * @throws DppException This exception is thrown if the
     * Digital Workprint is not specified.
     */
    public DppGenmedia(File workprint) throws DppException
    {
        super();
        if (workprint == null)
            throw new DppException("Digital Workprint not specified.");
        m_workprint = workprint;
    }
    
    /**
     * Set the verbose mode.
     * 
     * @param value Set <b>true</b> if the <b>genmedia</b> command should be
     * verbose. Otherwise, set this parameter to <b>false</b>.
     */
    public void setVerbose(boolean value)
    {
        m_verbose = value;
    }
    
    /**
     * Set the endianess of the chunk file generation.
     * 
     * @param value Set <b>true</b> if the <b>genmedia</b> command should
     * generate chunk files using Big Endian byte ordering. Otherwise,
     * set this parameter to <b>false</b> to establish Little Endian byte
     * ordering.
     */
    public void setBigEndian(boolean value)
    {
        m_bigEndian = value;
    }
    
    /**
     * Set the output directory.
     * 
     * @param dir The output directory where the generated code and chunk
     * files will be generated. This value may be set to <b>null</b> to use
     * the default directory.
     */
    public void setOutputDir(File dir)
    {
        m_outputDir = dir;
    }
    
    /**
     * Set the name of the generated media inventory file.
     * 
     * @param name The name of the file to generate.
     * 
     * @throws DppException This exception is thrown if the
     * name of the media inventory file is not specified.
     */
    public void setBomFilename(String name) throws DppException
    {
        if (name == null)
            throw new DppException("Media inventory file name not specified.");
        m_bom = name;
    }
    
    /**
     * Add a discriminator tag.
     * 
     * @param tag The tag to set.
     * 
     * @throws DppException This exception is thrown if the
     * name of the tag is not specified.
     */
    public void addTag(String tag) throws DppException
    {
        if (tag == null)
            throw new DppException("Tag not specified.");
        m_tags.add(tag);
    }

    /**
     * Execute the <b>gengroup</b> command.
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
        
        argList.add(new String("genmedia"));
        
        if (m_bigEndian)
            argList.add(new String("-b"));
        else
            argList.add(new String("-l"));
        
        // XXX - Add a verbose mode to the genmedia command.
        /*
        if (m_verbose)
            argList.add(new String("-v"));
        */
        
        if (m_outputDir != null)
        {
        	DppLog.logInfo("DppGenmedia: output directory is " + m_outputDir.toString() + ".");
            if (m_outputDir.exists() == false) {
            	DppLog.logInfo("DppGenmedia: creating " + m_outputDir.getAbsolutePath());
                if (m_outputDir.mkdirs() == false)
                	throw new DppException("Unable to create " + m_outputDir.toString() + " directory.");
            }
            argList.add(new String("-d"));
            argList.add(new String(m_outputDir.getAbsolutePath().trim()));
        }
        
        if (m_tags.size() > 0)
        {
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < m_tags.size(); i++)
            {
                if (i != 0)
                    buffer.append(":");
                buffer.append((String)m_tags.elementAt(i));
            }
            argList.add(buffer.toString());
        } else
            argList.add("rehearsal");

        if (! m_workprint.exists())
            throw new DppException("Digital Workprint does not exist: " + m_workprint.getName());
        else
            argList.add(new String(m_workprint.getAbsolutePath().trim()));
        
        argList.add(new String(m_bom.trim()));
        
        String[] args = (String[])argList.toArray(new String[0]);
        
        String[] output;
        try
        {
        	if (DppPlugin.isWindows())
        		// Windows needs command shell.
                output = runCommand(args, true, true);
        	else
        		// Linux does not need command shell.
        		output = runCommand(args, true, false);
        } catch (IOException ex)
        {
            throw new DppException("IO Error while running genmedia command.",ex);
        }
        
        // Dump output if in verbose mode.
        if (m_verbose)
        {
            for (int i = 0; i < output.length; i++)
                DppLog.getInstance().debug(output[i]);
        }
        
        return getResult();
    }

}
