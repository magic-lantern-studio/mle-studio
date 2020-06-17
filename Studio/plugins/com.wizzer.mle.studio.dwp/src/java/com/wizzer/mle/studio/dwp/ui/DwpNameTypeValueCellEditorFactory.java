/*
 * DwpNameTypeValueCellEditorFctory.java
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
package com.wizzer.mle.studio.dwp.ui;

// Import Eclipse packages.
import org.eclipse.swt.widgets.Composite;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.ui.AttributeCellEditor;
import com.wizzer.mle.studio.framework.ui.IAttributeCellEditorFactory;


/**
 * This class implements an <code>IAttributeCellEditorFactory</code> for a
 * Magic Lantern Digital Workprint <code>DwpNameTypeValueAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpNameTypeValueCellEditorFactory implements IAttributeCellEditorFactory 
{
	/**
	 * The default constructor.
	 */
	public DwpNameTypeValueCellEditorFactory()
	{
		super();
	}

	/**
	 * Create an instance of the <code>DwpNameTypeValueCellEditor</code>.
	 * 
	 * @param parent The parent <code>Composite</code> widget of the Cell Editor.
	 * 
	 * @return An instance of a <code>DwpNameTypeValueCellEditor</code> is returned.
	 * If one could not be created, then <b>null</b> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.attribute.IAttributeCellEditorFactory#createInstance()
	 */
	public AttributeCellEditor createInstance(Composite parent)
	{
		return new DwpNameTypeValueCellEditor(parent);
	}

}
