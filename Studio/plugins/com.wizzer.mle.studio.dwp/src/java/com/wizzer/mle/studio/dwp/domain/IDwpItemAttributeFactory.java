/*
 * IDwpItemAttributeFactory.java
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
package com.wizzer.mle.studio.dwp.domain;

// Import the Magic Lantern Studio Framework packages.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;


/**
 * This interface is used to provide a contract for classes that can create
 * new Magic Lantern Digital Workprint <code>IAttribute</code> items.
 * 
 * @author Mark S. Millard
 */
public interface IDwpItemAttributeFactory
{
	/**
	 * Create an instance of a Digital Workprint <code>IAttribute</code> item.
	 * 
	 * @param vla The Variable List Attribute managing the list of items.
	 * @param type The type of DWP <code>IAttribute</code> item to create.
	 * 
	 * @return A reference to an <code>IAttribute</code> tree is returned containing
	 * the required fields.
	 */
	public IAttribute createDwpItemAttribute(VariableListAttribute vla, Object type);

}
