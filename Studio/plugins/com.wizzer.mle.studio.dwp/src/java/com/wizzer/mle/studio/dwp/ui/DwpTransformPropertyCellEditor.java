/*
 * DwpTransformPropertyCellEditor.java
 * Created on Apr 28, 2005
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
package com.wizzer.mle.studio.dwp.ui;

// Import Eclipse classes.
import org.eclipse.swt.widgets.Composite;

import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.dwp.attribute.DwpNameTypeValueAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpTransformPropertyAttribute;

/**
 * @author Mark S. Millard
 */
public class DwpTransformPropertyCellEditor extends DwpNameTypeValueCellEditor
{
	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
    public DwpTransformPropertyCellEditor(Composite parent)
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
    public DwpTransformPropertyCellEditor(Composite parent, int style)
    {
        super(parent, style);
    }

	/**
	 * Sets the <code>DwpRotationPropertyAttribute</code> for the editor.
	 * 
	 * @param attr A reference to the <code>DwpRotationPropertyAttribute</code> to use with the
	 * editor.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeCellEditor#setAttribute(com.wizzer.mle.studio.framework.attribute.Attribute)
	 */
	public void setAttribute(Attribute attr)
	{
		if (! (attr instanceof DwpTransformPropertyAttribute))
			System.out.println("XXX: Throw An Exception!");
		else
			m_attribute = (DwpNameTypeValueAttribute)attr;
        
		//String val  = attr.toString();
		String valueName  = ((DwpTransformPropertyAttribute)attr).getValueName();
		String valueType  = ((DwpTransformPropertyAttribute)attr).getValueType();
		Float[][] valueValue = (Float[][])((DwpTransformPropertyAttribute)attr).getValueValue();
		String name = attr.getName();
		int bits    = attr.getBits();
        
		// Set the value of the CellEditor.
		m_nameText.setVisible(bits != 0);
		m_nameText.setText(valueName);
		m_typeText.setVisible(bits != 0);
		m_typeText.setText(valueType);
		StringBuffer strBuf = new StringBuffer();
		for (int i = 0; i < 4; i ++)
		{
			for (int j = 0; j < 3; j++)
			{
				//strBuf.append(" ");
				//strBuf.append(valueValue[j + (i * 3)]);
				strBuf.append(valueValue[i][j]);
				if (! ((i == 3) && (j == 2)))
				    strBuf.append(" ");
			}
		}
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
		    Float[][] floatValue = (Float[][])(m_attribute.getValueValue());
		    
		    StringBuffer strBuf = new StringBuffer();
			for (int i = 0; i < 4; i ++)
			{
				for (int j = 0; j < 3; j++)
				{
					//strBuf.append(" ");
					//strBuf.append(floatValue[j + (i * 3)]);
					strBuf.append(floatValue[i][j]);
					if (! ((i == 3) && (j == 2)))
					    strBuf.append(" ");
				}
			}

		    if (! ((String)m_valueText.getText()).equals(strBuf.toString()))
		        dirty = true;
		}

		return dirty;
	}

}
