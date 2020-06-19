/*
 * DppGengroup.java
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
package com.wizzer.mle.studio.dpp;

// Import standard Java classes.
import java.util.ArrayList;
import java.util.Vector;
import java.io.File;
import java.io.IOException;

/**
 * This class is used to execute the <b>gengroup</b> Magic Lantern mastering program.
 * 
 * @author Mark S. Millard
 */
public class DppGengroup extends DppCommand
{
    /** Verbose mode: [-v]. */
    protected boolean m_verbose = false;
    /** Endianess of chunk generation: [-b|-l]. */
    protected boolean m_bigEndian = true;
    /** Indicate whether to use fixed or floating-point arithmetic [-f]. */
    protected boolean m_fixedPoint = false;
    /** Code generation mode: [-c|-j]. */
    protected boolean m_javaCodeGeneration = true;
    /** The default Java package. */
    protected String m_package = "gen";
    
    /** The digital workprint. */
    protected File m_workprint = null;
    /** The output directory. */
    protected File m_outputDir = new File("gen");
    /** The Actor ID file. */
    protected String m_actorId = "ActorID.java";
    /** The Group ID file. */
    protected String m_groupId = "GroupID.java";
    /** The tags. */
    Vector<String> m_tags = new Vector<String>();
    
    // Hide the default constructor.
    private DppGengroup()
    {}

    /**
     * A constructor that specifies the Digital Workprint.
     * 
     * @param workprint The DWP to use with the <b>gengroup</b> command.
     * 
     * @throws DppException This exception is thrown if the
     * Digital Workprint is not specified.
     */
    public DppGengroup(File workprint) throws DppException
    {
        super();
        if (workprint == null)
            throw new DppException("Digital Workprint not specified.");
        m_workprint = workprint;
    }
    
    /**
     * Set the verbose mode.
     * 
     * @param value Set <b>true</b> if the <b>gengroup</b> command should be
     * verbose. Otherwise, set this parameter to <b>false</b>.
     */
    public void setVerbose(boolean value)
    {
        m_verbose = value;
    }
    
    /**
     * Set the endianess of the chunk file generation.
     * 
     * @param value Set <b>true</b> if the <b>gengroup</b> command should
     * generate chunk files using Big Endian byte ordering. Otherwise,
     * set this parameter to <b>false</b> to establish Little Endian byte
     * ordering.
     */
    public void setBigEndian(boolean value)
    {
        m_bigEndian = value;
    }
    
    /**
     * Set the real value mode for the chunk file generation.
     * 
     * @param value Set <b>true</b> if the <b>gengroup</b> command should
     * generate chunk files using fixed-point arithmetic.  Otherwise,
     * set this parameter to <b>false</b> to establish floating-point
     * values.
     */
    public void setFixedPoint(boolean value)
    {
        m_fixedPoint = value;
    }
    
    /**
     * Set the code generation mode.
     * 
     * @param value Set <b>true</b> if the <b>gengroup</b> command should
     * generate Java code. Otherwise, set this parameter to <b>false</b> to
     * generate C++ code.
     */
    public void setJavaCodeGeneration(boolean value)
    {
        m_javaCodeGeneration = value;
    }
    
    /**
     * Set the package namespace for the generated code.
     * 
     * @param name The name of the package.
     * 
     * @throws DppException This exception is thrown if the
     * package name is not specified.
     */
    public void setPackage(String name) throws DppException
    {
        if (name == null)
            throw new DppException("Package name not specified.");
        m_package = name;
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
     * Set the name of the generated actor id file.
     * 
     * @param name The name of the file to generate.
     * 
     * @throws DppException This exception is thrown if the
     * name of the actor id file is not specified.
     */
    public void setActorIdFilename(String name) throws DppException
    {
        if (name == null)
            throw new DppException("Actor ID file name not specified.");
        m_actorId = name;
    }

    /**
     * Set the name of the generated group id file.
     * 
     * @param name The name of the file to generate.
     * 
     * @throws DppException This exception is thrown if the
     * name of the group id file is not specified.
     */
    public void setGroupIdFilename(String name) throws DppException
    {
        if (name == null)
            throw new DppException("Group ID file name not specified.");
        m_groupId = name;
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
        ArrayList<String> argList = new ArrayList<String>();
        
        argList.add(new String("gengroup"));
        
        if (m_bigEndian)
            argList.add(new String("-b"));
        else
            argList.add(new String("-l"));
        
        if (m_fixedPoint)
            argList.add(new String("-f"));
        
        if (m_javaCodeGeneration)
        {
            argList.add(new String("-j"));
            argList.add(new String(m_package.trim()));
        } else
            argList.add(new String("-c"));
            
        if (m_verbose)
            argList.add(new String("-v"));
        
        if (m_outputDir != null)
        {
        	DppLog.logInfo("DppGengroup: output directory is " + m_outputDir.toString() + ".");
            if (m_outputDir.exists() == false) {
            	DppLog.logInfo("DppGengroup: creating " + m_outputDir.getAbsolutePath());
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
        
        argList.add(new String(m_actorId.trim()));
        
        argList.add(new String(m_groupId.trim()));
        
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
            throw new DppException("IO Error while running gengroup command.",ex);
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
