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

/**
 * This class is a base exception class for Magic Lantern
 * project meta-data exceptions.
 * 
 * @author Mark S. Millard
 */
public class MetaDataException extends Exception
{
    // Serialization identifier.
	private static final long serialVersionUID = -8107201146899782104L;

	/**
     * Creates a new <code>MetaDataException</code>.
     */
    public MetaDataException()
    {
        super();
    }

    /**
     * Creates a new <code>MetaDataException</code> with
     * an error message.
     * 
     * @param arg0 An error message.
     */
    public MetaDataException(String arg0)
    {
        super(arg0);
    }

    /**
     * Creates a new <code>MetaDataException</code> with
     * a root exception.
     * 
     * @param arg0 A root cause.
     */
    public MetaDataException(Throwable arg0)
    {
        super(arg0);
    }

    /**
     * Creates a new <code>MetaDataException</code> with
     * an error message and root cause.
     * 
     * @param arg0 An error message.
     * @param arg1 The cause exception.
     */
    public MetaDataException(String arg0, Throwable arg1)
    {
        super(arg0, arg1);
    }

}
