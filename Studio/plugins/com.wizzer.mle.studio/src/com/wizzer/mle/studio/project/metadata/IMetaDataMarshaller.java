// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2010  Wizzer Works
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
package com.wizzer.mle.studio.project.metadata;

 // Import standard Java classes.
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This interface is used to marshall/unmarshall data from/to a
 * Magic Lantern meta-data file.
 * 
 * @author Mark S. Millard
 */
public interface IMetaDataMarshaller
{
    /**
     * Persists project meta-data to an <code>OutputStream</code>.
     * 
     * @param os Any <code>OutputStream</code>.
     * @param data The project meta-data to persist.
     * 
     * @throws IOException This exception is thrown if an error occurs
     * during marshalling the project meta-data.
     */
    public void marshallMetaData(OutputStream os, ProjectMetaData data)
    	throws IOException;
    
    /**
     * Retrieves project meta-data from an <code>InputStream</code>.
     * 
     * @param is Any <code>InputStream<code>.
     * @return A reference to a <code>ProjectMetaData</code> instance is
     * returned.
     * 
     * @throws IOException This exception is thrown if an error occurs
     * during unmarshalling the project meta-data.
     * @throws MetaDataException This exception is thrown if there is
     * an error with the project meta-data.
     */
    public ProjectMetaData unmarshallMetaData(InputStream is)
    	throws IOException, MetaDataException;

}
