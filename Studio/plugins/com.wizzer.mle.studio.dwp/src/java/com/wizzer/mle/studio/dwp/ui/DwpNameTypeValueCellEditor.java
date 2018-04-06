/*
 * DwpNameTypeValueCellEditor.java
 * Created on Aug 6, 2004
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

// Import Eclipse packages.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.ui.AttributeCellEditor;
import com.wizzer.mle.studio.framework.ui.GuiConstants;

// Import Magic Lantern Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.attribute.DwpNameTypeValueAttribute;


/**
 * This class is a Eclipse SWT <code>CellEditor</code> for the Wizzer Works
 * Magic Lanatern Digital Workprint <code>DwpNameTypeValueAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpNameTypeValueCellEditor extends AttributeCellEditor
{
	// The Attribute.
	protected DwpNameTypeValueAttribute m_attribute = null;
	// A label for the editor.
	protected Label m_label;
	// The control for the name field.
	protected Text m_nameText;
	// The control for the type field.
	protected Text m_typeText;
	// The control for the value field.
	protected Text m_valueText;


	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public DwpNameTypeValueCellEditor(Composite parent)
	{
		super(parent);
		
		// Note: this.createControl(parent) is called from the super's constructor.
		// So, we don't need to do it here.
	}

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent
	 * with the specified style.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * @param style The style to use for the new editor component.
	 */
	public DwpNameTypeValueCellEditor(Composite parent, int style)
	{
		super(parent,style);
		
		// Note: this.createControl(parent) is called from the super's constructor.
		// So, we don't need to do it here.
	}

	/**
	 * Create the control for the editor.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#createControl(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createControl(Composite parent)
	{
		// Create a layout for the editor.
		GridLayout layout = new GridLayout();
		layout.numColumns = 4;
		layout.marginWidth = GuiConstants.BORDER_SIZE_EDIT;
		layout.marginHeight = GuiConstants.BORDER_SIZE_EDIT;

		// Initialize the components in a Composite container.
		Composite composite = new Composite(parent,SWT.RESIZE);
		composite.setBounds(0, 0, parent.getSize().x, parent.getSize().y);
		this.initGUI(composite);
 
		// Set the layout for the editor.
		composite.setLayout(layout);
        
		return composite;
	}

	// Initialize the editor's GUI.
	private void initGUI(Composite parent)
	{
		m_label = new Label(parent,SWT.NONE);
		m_nameText = new Text(parent,SWT.BORDER | SWT.RESIZE);
		m_typeText = new Text(parent,SWT.BORDER | SWT.RESIZE);
		m_valueText = new Text(parent,SWT.BORDER | SWT.RESIZE);

		// Add a key listener for the name Text control.		
		m_nameText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the name Text control
		m_nameText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				DwpNameTypeValueCellEditor.this.focusLost();
			}
		});

		// Add a key listener for the type Text control.		
		m_typeText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the type Text control
		m_typeText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				DwpNameTypeValueCellEditor.this.focusLost();
			}
		});

		// Add a key listener for the value Text control.		
		m_valueText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the value Text control
		m_valueText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				DwpNameTypeValueCellEditor.this.focusLost();
			}
		});
    	
		//m_label.setBackground(new Color(parent.getDisplay(), 255, 153, 204));
		//m_label.setForeground(new Color(parent.getDisplay(), 255, 51, 0));
		//m_label.setFont(new Font(parent.getDisplay(),"Comic Sans MS", GUIConstants.FONT_SIZE_EDIT, SWT.BOLD));
		Font labelFont = m_label.getFont();
		FontData[] labelFontData = labelFont.getFontData();
		for (int i = 0; i < labelFontData.length; i++)
		{
			labelFontData[i].setStyle(SWT.BOLD);
		}
		m_label.setFont(new Font(parent.getDisplay(),labelFontData));
		//m_label.setAlignment(SWT.CENTER);
         
		//m_nameText.setBackground(new Color(parent.getDisplay(), 153, 255, 255));
		//m_nameText.setForeground(new Color(parent.getDisplay(), 0, 0, 255));
		//m_nameText.setFont(new Font(parent.getDisplay(),"Comic Sans MS", GUIConstants.FONT_SIZE_EDIT, SWT.NORMAL));
		m_nameText.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		
		m_typeText.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		
		m_valueText.setBackground(new Color(parent.getDisplay(), 255, 255, 255));

		GridData labelLayoutData = new GridData();
		//labelLayoutData.widthHint = GuiConstants.LABEL_WIDTH_EDIT;
		labelLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		labelLayoutData.horizontalAlignment = GridData.FILL;
		labelLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		labelLayoutData.grabExcessHorizontalSpace = true;
		//labelLayoutData.grabExcessVerticalSpace = true;
		m_label.setLayoutData(labelLayoutData);

		GridData nameTextLayoutData = new GridData();
		//nameTextLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		nameTextLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		nameTextLayoutData.horizontalAlignment = GridData.FILL;
		nameTextLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		nameTextLayoutData.grabExcessHorizontalSpace = true;
		//nameTextLayoutData.grabExcessVerticalSpace = true;
		m_nameText.setLayoutData(nameTextLayoutData);

		GridData typeTextLayoutData = new GridData();
		//typeTextLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		typeTextLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		typeTextLayoutData.horizontalAlignment = GridData.FILL;
		typeTextLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		typeTextLayoutData.grabExcessHorizontalSpace = true;
		//typeTextLayoutData.grabExcessVerticalSpace = true;
		m_typeText.setLayoutData(typeTextLayoutData);

		GridData valueTextLayoutData = new GridData();
		//valueTextLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		valueTextLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		valueTextLayoutData.horizontalAlignment = GridData.FILL;
		valueTextLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		valueTextLayoutData.grabExcessHorizontalSpace = true;
		//valueTextLayoutData.grabExcessVerticalSpace = true;
		m_valueText.setLayoutData(valueTextLayoutData);
	}

	/**
	 * Get the value of the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @return A reference to a <code>String</code> is returned.
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doGetValue()
	 */
	public Object doGetValue()
	{
		// Return the attribute.
		return m_attribute.getValue();
	}

	/**
	 * Set the value of the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @param attr A reference to a <code>String</code>.
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetValue(java.lang.Object)
	 */
	public void doSetValue(Object attr)
	{
		m_attribute.setValue(attr);
	}
	
	/**
	 * Set the focus on the control.
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#doSetFocus()
	 */
	protected void doSetFocus()
	{
		m_nameText.selectAll();
		m_nameText.setFocus();
	}

	/**
	 * Activate the main control for the editor.
	 */
	public void activate()
	{
		Control control = getControl();
		if (control != null)
			control.setVisible(true);
	}
    
	/**
	 * Processes a focus lost event that occurred in this cell editor. 
	 * <p>
	 * The default implementation of this framework method applies the current value
	 * and deactivates the cell editor. The <code>DwpNameTypeValueAttribute</doce> is also updated
	 * if necessary.
	 * </p>
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost()
	{
		if (this.isDirty())
			this.stopCellEditing();
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
		else if (! ((String)m_valueText.getText()).equals(m_attribute.getValueValue().toString()))
			dirty = true;

		return dirty;
	}
    
	/**
	 * Check to see if cell is editable.
	 * 
	 * @return <b>true</b> will be returned if the cell is editable.
	 * Otherwise, <b>false</b> will be returned.
	 */
	public boolean isCellEditable()
	{
		return ! m_attribute.isReadOnly();
	}

	/**
	 * Complete cell editing. The <code>DwpNameTypeValueAttribute</code> is updated.
	 * 
	 * @return <b>true</b> will alwaus be returned.
	 */
	public boolean stopCellEditing()
	{
		// Update the Attribute.
		String name = (String)m_nameText.getText();
		String type = (String)m_typeText.getText();
		String value = (String)m_valueText.getText();
		m_attribute.setValue(name,type,value);
        
		// Select new string for editing.
		m_nameText.selectAll();

		// Update the tool tip.
		if (! m_label.isDisposed())
			m_label.setToolTipText("String [" + m_attribute.getName() + "] - bits = " +
				m_attribute.getBits() + " - value = " + m_attribute.getValue());
		
		// Validate the entry.
		if (m_attribute.validate())
		{
			this.setValueValid(true);

			// Print out some debugging information.
			DwpLog.logInfo("Setting attribute to: " + getValue());
			DwpLog.getInstance().debug("Bits: " + m_attribute.getBitString());
	        
			// Notify Cell listeners that the editor value is being applied.
			fireApplyEditorValue();
	        
			return true;
		} else
			return false;
	}

	/**
	 * Get the <code>Attribute</code> associated with the editor.
	 * 
	 * @return A reference to the <code>Attribute</code> is returned.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeCellEditor#getAttribute()
	 */
	public Attribute getAttribute()
	{
		return m_attribute;
	}

	/**
	 * Sets the <code>DwpNameTypeValueAttribute</code> for the editor.
	 * 
	 * @param attr A reference to the <code>DwpNameTypeValueAttribute</code> to use with the
	 * editor.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeCellEditor#setAttribute(com.wizzer.mle.studio.framework.attribute.Attribute)
	 */
	public void setAttribute(Attribute attr)
	{
		if (! (attr instanceof DwpNameTypeValueAttribute))
			System.out.println("XXX: Throw An Exception!");
		else
			m_attribute = (DwpNameTypeValueAttribute)attr;
        
		//String val  = attr.toString();
		String valueName  = ((DwpNameTypeValueAttribute)attr).getValueName();
		String valueType  = ((DwpNameTypeValueAttribute)attr).getValueType();
		String valueValue  = ((DwpNameTypeValueAttribute)attr).getValueValue().toString();
		String name = attr.getName();
		int bits    = attr.getBits();
        
		// Set the value of the CellEditor.
		m_nameText.setVisible(bits != 0);
		m_nameText.setText(valueName);
		m_typeText.setVisible(bits != 0);
		m_typeText.setText(valueType);
		m_valueText.setText(valueValue);
       
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
	 * Hide the label.
	 * <p>
	 * This routine will set the visibility of the label to be hidden. It will also dispose all the resources
	 * associated with the label. Thus, once the label is disposed, it can not be brought back to life.
	 * </p>
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeCellEditor#hideLabel()
	 */
	public void hideLabel()
	{
		m_label.setVisible(false);
		m_label.dispose();
	}

	/**
	 * Handle the Key Release event.
	 * 
	 * @param e The <code>KeyEvent</code> that caused the event to occur.
	 */
	protected void keyReleaseOccured(KeyEvent e)
	{
		if (e.keyCode == SWT.CR)
		{
			// Stop cell editing on carriage return if the value has changed.
			if (this.isDirty())
				this.stopCellEditing();
		}
	}
}
