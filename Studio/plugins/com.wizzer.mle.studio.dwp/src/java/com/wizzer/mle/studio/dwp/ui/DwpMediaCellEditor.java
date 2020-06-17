/*
 * DwpMEdiaCellEditor.java
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
import java.io.File;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.ui.AttributeCellEditor;
import com.wizzer.mle.studio.framework.ui.GuiConstants;
import com.wizzer.mle.studio.framework.ui.GuiUtilities;

// Import Magic Lantern Digital Workprint packages.
import com.wizzer.mle.studio.dwp.DwpLog;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaAttribute;


/**
 * This class is a Eclipse SWT <code>CellEditor</code> for the Wizzer Works
 * Magic Lanatern Digital Workprint <code>DwpMediaAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpMediaCellEditor extends AttributeCellEditor
{
	// The Attribute.
	private DwpMediaAttribute m_attribute = null;
	// A label for the editor.
	private Label m_label;
	// The control for the label field.
	private Text m_labelText;
	// The control for the flags field.
	private Text m_flagsText;
	// The control for the url field.
	private Text m_urlText;


	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public DwpMediaCellEditor(Composite parent)
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
	public DwpMediaCellEditor(Composite parent, int style)
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
		m_flagsText = new Text(parent,SWT.BORDER);
		m_labelText = new Text(parent,SWT.BORDER | SWT.RESIZE);

		// Add a key listener for the flags Text control.		
		m_flagsText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the flags Text control
		m_flagsText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				DwpMediaCellEditor.this.focusLost();
			}
		});

		// Add a key listener for the label Text control.		
		m_labelText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the label Text control
		m_labelText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				DwpMediaCellEditor.this.focusLost();
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
		m_flagsText.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		
		m_labelText.setBackground(new Color(parent.getDisplay(), 255, 255, 255));

		GridData labelLayoutData = new GridData();
		//labelLayoutData.widthHint = GuiConstants.LABEL_WIDTH_EDIT;
		labelLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		labelLayoutData.horizontalAlignment = GridData.FILL;
		labelLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		labelLayoutData.grabExcessHorizontalSpace = true;
		//labelLayoutData.grabExcessVerticalSpace = true;
		m_label.setLayoutData(labelLayoutData);

		GridData flagsTextLayoutData = new GridData();
		//flagsTextLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		flagsTextLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		flagsTextLayoutData.horizontalAlignment = GridData.FILL;
		flagsTextLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		//flagsTextLayoutData.grabExcessHorizontalSpace = true;
		//flagsTextLayoutData.grabExcessVerticalSpace = true;
		m_flagsText.setLayoutData(flagsTextLayoutData);

		GridData labelTextLayoutData = new GridData();
		//labelTextLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		labelTextLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		labelTextLayoutData.horizontalAlignment = GridData.FILL;
		labelTextLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		labelTextLayoutData.grabExcessHorizontalSpace = true;
		//labelTextLayoutData.grabExcessVerticalSpace = true;
		m_labelText.setLayoutData(labelTextLayoutData);
		
		createUrlControl(parent);
	}
	
	// Create the Composite control for the URL
	private Composite createUrlControl(Composite parent)
	{
		Composite composite = new Composite(parent,SWT.RESIZE);
		composite.setBounds(0, 0, parent.getSize().x, parent.getSize().y);
		
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		composite.setLayout(layout);

		m_urlText = new Text(composite,SWT.BORDER | SWT.RESIZE);
		
		// Add a key listener for the url Text control.		
		m_urlText.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the url Text control
		m_urlText.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				DwpMediaCellEditor.this.focusLost();
			}
		});
		
		m_urlText.setBackground(new Color(parent.getDisplay(), 255, 255, 255));

		GridData urlTextLayoutData = new GridData();
		//urlTextLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		urlTextLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		urlTextLayoutData.horizontalAlignment = GridData.FILL;
		urlTextLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		urlTextLayoutData.grabExcessHorizontalSpace = true;
		//urlTextLayoutData.grabExcessVerticalSpace = true;
		m_urlText.setLayoutData(urlTextLayoutData);

		// Create the file button.
		Button fileButton = new Button(composite,SWT.NONE);
		fileButton.setText("...");

		fileButton.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e)
		    {
			     handlePushedButton();
			}
	    });

		GridData buttonLayoutData = new GridData();
		//buttonLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT + 8;
		buttonLayoutData.heightHint = GuiConstants.PANEL_HEIGHT_EDIT;
		//buttonLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		buttonLayoutData.horizontalAlignment = GridData.END;
		fileButton.setLayoutData(buttonLayoutData);

		return composite;
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
		m_urlText.selectAll();
		m_urlText.setFocus();
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
	 * and deactivates the cell editor. The <code>DwpMediaAttribute</doce> is also updated
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
		if (! ((String)m_flagsText.getText()).equals(m_attribute.getValueFlags().toString()))
			dirty = true;
		else if (! ((String)m_labelText.getText()).equals(m_attribute.getValueLabel()))
			dirty = true;
		else if (! ((String)m_urlText.getText()).equals(m_attribute.getValueURL()))
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
	 * Complete cell editing. The <code>DwpMediaAttribute</code> is updated.
	 * 
	 * @return <b>true</b> will alwaus be returned.
	 */
	public boolean stopCellEditing()
	{
		// Update the Attribute.
		Integer flags = new Integer(m_flagsText.getText());
		String label = (String)m_labelText.getText();
		String url = (String)m_urlText.getText();
		m_attribute.setValue(flags,label,url);
        
		// Select new string for editing.
		m_urlText.selectAll();

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
	 * Sets the <code>DwpMediaAttribute</code> for the editor.
	 * 
	 * @param attr A reference to the <code>DwpMediaAttribute</code> to use with the
	 * editor.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeCellEditor#setAttribute(com.wizzer.mle.studio.framework.attribute.Attribute)
	 */
	public void setAttribute(Attribute attr)
	{
		if (! (attr instanceof DwpMediaAttribute))
			System.out.println("XXX: Throw An Exception!");
		else
			m_attribute = (DwpMediaAttribute)attr;
        
		//String val  = attr.toString();
		String valueFlags  = (((DwpMediaAttribute)attr).getValueFlags()).toString();
		String valueLabel = ((DwpMediaAttribute)attr).getValueLabel();
		String valueURL  = ((DwpMediaAttribute)attr).getValueURL();
		String name = attr.getName();
		int bits    = attr.getBits();
        
		// Set the value of the CellEditor.
		m_flagsText.setVisible(bits != 0);
		m_flagsText.setText(valueFlags);
		m_labelText.setVisible(bits != 0);
		m_labelText.setText(valueLabel);
		m_urlText.setVisible(bits != 0);
		m_urlText.setText(valueURL);
       
		// Set read only state.
		if (attr.isReadOnly())
		{
			m_flagsText.setEnabled(false);
			m_labelText.setEnabled(false);
			m_urlText.setEnabled(false);
		}
		else
		{
			m_flagsText.setEnabled(true);
			m_labelText.setEnabled(true);
			m_urlText.setEnabled(true);
			m_urlText.selectAll();
			m_urlText.setFocus();
		}
        
		// Update label and tool tip.
		if (! m_label.isDisposed()) {
			m_label.setText(name);
			m_label.setToolTipText("String [" + attr.getName() + "] - bits = " +
				attr.getBits() + " - value = " + ((DwpMediaAttribute)attr).getValue());
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
	
    /**
     * Handle the Button Pushed event.
     */
    protected void handlePushedButton()
    {
        File f = GuiUtilities.openFile(this.getControl());
        if (f == null) {
            return;
        } else {
            String s = f.getAbsolutePath();
            m_urlText.setText(s);
            this.stopCellEditing();
        }
    }

}
