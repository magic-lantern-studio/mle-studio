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
