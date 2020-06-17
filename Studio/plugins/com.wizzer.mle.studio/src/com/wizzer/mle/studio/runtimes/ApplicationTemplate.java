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
package com.wizzer.mle.studio.runtimes;

/**
 * This class is used carry the information required for processing an
 * Application Template.
 * 
 * @author Mark Millard
 */
public class ApplicationTemplate implements IApplicationTemplateEntry
{
	// The template type.
    private String m_type = null;
    // The source location for the template package.
    private String m_source = null;
    // The destination directory for the generated source.
    private String m_destination = null;
    // The application template identifier.
    private String m_id = null;
    
	public String getDestination()
	{
		return m_destination;
	}
	
	public void setDestination(String destination)
	{
		m_destination = destination;
	}

	public String getSource()
	{
		return m_source;
	}
	
	public void setSource(String source)
	{
		m_source = source;
	}

	public String getType()
	{
		return m_type;
	}
	
	public void setType(String type)
	{
		m_type = type;
	}

	public String getId()
	{
		return m_id;
	}

	public void setId(String id)
	{
		m_id = id;
	}
}
