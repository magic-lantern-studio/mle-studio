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
 * This interface is used to specify a specific application template implementation.
 * 
 * @author Mark Millard
 */
public interface IApplicationTemplateEntry
{
	/**
	 * Get an optional identifier for the application template.
	 * 
	 * @return The identifier is returned as a <code>String</code>.
	 */
	public String getId();

	/**
	 * Get the type of the template package.
	 * <p>
	 * Package types that are currently supported include <b>zip</b> files.
	 * </p>
	 * 
	 * @return The type of the application template is returned.
	 */
	public String getType();
	
	/**
	 * Get the source package for the application template.
	 * <p>
	 * This can be a relative path or a URL. It will be up to the actual template
	 * to determine how to resolve the location.
	 * </p>
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getSource();
	
	/**
	 * Get the destination directory for the generated application source.
	 * <p>
	 * The directory should be a relative path from the top of the OCAP Project
	 * that is using the template (i.e. src).
	 * </p>
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getDestination();
}
