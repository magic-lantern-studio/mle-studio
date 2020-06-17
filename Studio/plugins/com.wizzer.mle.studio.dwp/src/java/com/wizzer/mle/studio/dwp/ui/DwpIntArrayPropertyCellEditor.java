/*
 * DwpIntArrayPropertyCellEditor.java
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

// Import Eclipse classes.
import org.eclipse.swt.widgets.Composite;

import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIntArrayPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpNameTypeValueAttribute;

/**
 * @author Mark S. Millard
 */
public class DwpIntArrayPropertyCellEditor extends DwpNameTypeValueCellEditor
{
	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
    public DwpIntArrayPropertyCellEditor(Composite parent)
    {
        super(parent);
    }

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent
	 * with the specified style.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * @param style The style to use for the new editor component.
	 */
    public DwpIntArrayPropertyCellEditor(Composite parent, int style)
    {
        super(parent, style);
    }

	/**
	 * Sets the <code>DwpIntArrayPropertyAttribute</code> for the editor.
	 * 
	 * @param attr A reference to the <code>DwpIntArrayPropertyAttribute</code> to use with the
	 * editor.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeCellEditor#setAttribute(com.wizzer.mle.studio.framework.attribute.Attribute)
	 */
	public void setAttribute(Attribute attr)
	{
		if (! (attr instanceof DwpIntArrayPropertyAttribute))
			System.out.println("XXX: Throw An Exception!");
		else
			m_attribute = (DwpNameTypeValueAttribute)attr;
        
		//String val  = attr.toString();
		String valueName  = ((DwpIntArrayPropertyAttribute)attr).getValueName();
		String valueType  = ((DwpIntArrayPropertyAttribute)attr).getValueType();
		Integer[] valueValue = (Integer[])((DwpIntArrayPropertyAttribute)attr).getValueValue();
		String name = attr.getName();
		int bits    = attr.getBits();
        
		// Set the value of the CellEditor.
		m_nameText.setVisible(bits != 0);
		m_nameText.setText(valueName);
		m_typeText.setVisible(bits != 0);
		m_typeText.setText(valueType);
		StringBuffer strBuf = new StringBuffer();
		strBuf.append("[");
		for (int i = 0; i < valueValue.length; i++)
		{
			strBuf.append(" ");
			strBuf.append(valueValue[i]);
			if (i != (valueValue.length - 1))
				strBuf.append(" ,");
		}
		strBuf.append(" ]");
		m_valueText.setText(strBuf.toString());
       
		// Set read only state.
		if (attr.isReadOnly())
		{
			m_nameText.setEnabled(false);
			m_typeText.setEnabled(false);
			m_valueText.setEnabled(false);
		}
		else
		{
			m_nameText.setEnabled(true);
			m_nameText.selectAll();
			m_nameText.setFocus();
			m_typeText.setEnabled(true);
			m_valueText.setEnabled(true);
		}
        
		// Update label and tool tip.
		if (! m_label.isDisposed()) {
			m_label.setText(name);
			m_label.setToolTipText("String [" + attr.getName() + "] - bits = " +
				attr.getBits() + " - value = " + ((DwpNameTypeValueAttribute)attr).getValue());
		}
	}

	/**
	 * Returns whether the value of this cell editor has changed since the
	 * last call to <code>setValue</code>.
	 *
	 * @return <code>true</code> is returned if the value has changed,
	 * and <code>false</code> is returned if the value is unchanged.
	 */
	public boolean isDirty()
	{
		boolean dirty = false;
		if (! ((String)m_nameText.getText()).equals(m_attribute.getValueName()))
			dirty = true;
		else if (! ((String)m_typeText.getText()).equals(m_attribute.getValueType()))
			dirty = true;
		else
		{
		    Integer[] intValue = (Integer[])(m_attribute.getValueValue());
		    StringBuffer strBuf = new StringBuffer();
			strBuf.append("[");
			for (int i = 0; i < intValue.length; i++)
			{
				strBuf.append(" ");
				strBuf.append(intValue[i]);
				if (i != (intValue.length - 1))
					strBuf.append(" ,");
			}
			strBuf.append(" ]");

		    if (! ((String)m_valueText.getText()).equals(strBuf.toString()))
		        dirty = true;
		}

		return dirty;
	}

}
