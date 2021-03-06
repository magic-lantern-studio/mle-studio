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

//Declare package.
package com.wizzer.mle.studio.framework.ui;

// Import Eclipse packages.
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.FrameworkLog;
import com.wizzer.mle.studio.framework.Utilities;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.NumberAttribute;


/**
 * This class is a Eclipse SWT <code>CellEditor</code> for the Wizzer Works Framework
 * <code>NumberAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class NumberCellEditor extends AttributeCellEditor
{
    private NumberAttribute m_attribute = null;
	private Label m_label;
    private Text m_text;

    private long m_max = 0;

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public NumberCellEditor(Composite parent)
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
	public NumberCellEditor(Composite parent, int style)
	{
		super(parent,style);
		
		// Note: this.createControl(parent) is called from the super's constructor.
		// So, we don't need to do it here.
	}

	/**
	 * Create the control for the editor.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	protected Control createControl(Composite parent)
	{
		// Create a layout for the editor.
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = GuiConstants.BORDER_SIZE_EDIT;
		layout.marginHeight = GuiConstants.BORDER_SIZE_EDIT;

		// Initialize the components in a Composite container.
		Composite composite = new Composite(parent,SWT.RESIZE);
		//composite.setBounds(0, 0, GuiConstants.PANEL_WIDTH_EDIT, GuiConstants.PANEL_HEIGHT_EDIT); 
		composite.setBounds(0, 0, parent.getSize().x, parent.getSize().y);  	
		this.initGUI(composite);
        
		// Set the layout for the editor.
		composite.setLayout(layout);
		//composite.layout();
        
		return composite;
	}

	// Iniitialize the editor's GUI.
	private void initGUI(Composite parent)
	{
		m_label = new Label(parent,SWT.NONE);
		m_text = new Text(parent,SWT.BORDER);

		// Add a key listener for the Text control.		
		m_text.addKeyListener(new KeyAdapter() {
				public void keyReleased(KeyEvent e)
				{
					keyReleaseOccured(e);
				}
			});

		// Add a focus listener for the Text control
		m_text.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				NumberCellEditor.this.focusLost();
			}
		});
    	
		//m_label.setBackground(new Color(parent.getDisplay(), 255, 153, 204));
		//m_label.setForeground(new Color(parent.getDisplay(), 255, 51, 0));
		//m_label.setFont(new Font(parent.getDisplay(),"Comic Sans MS", GUIConstants.FONT_SIZE_EDIT, SWT.BOLD));
		Font labelFont = m_label.getFont();
		FontData[] labelFontData = labelFont.getFontData();
		for (int i = 0; i < labelFontData.length; i++) {
			labelFontData[i].setStyle(SWT.BOLD);
		}
		m_label.setFont(new Font(parent.getDisplay(),labelFontData));
         
		//m_text.setBackground(new Color(parent.getDisplay(), 153, 255, 255));
		//m_text.setForeground(new Color(parent.getDisplay(), 0, 0, 255));
		//m_text.setFont(new Font(parent.getDisplay(),"Comic Sans MS", GUIConstants.FONT_SIZE_EDIT, SWT.NORMAL));
		m_text.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		
		//m_label.setBounds(GUIConstants.BORDER_SIZE_EDIT, GUIConstants.BORDER_SIZE_EDIT,
		//    GUIConstants.LABEL_WIDTH_EDIT, GUIConstants.FIELD_HEIGHT_EDIT);
		//text.setBounds(GUIConstants.LABEL_WIDTH_EDIT + (2 * GUIConstants.BORDER_SIZE_EDIT),
		//	GUIConstants.BORDER_SIZE_EDIT, GUIConstants.EDIT_WIDTH_EDIT, GUIConstants.FIELD_HEIGHT_EDIT);

		GridData labelLayoutData = new GridData();
		//labelLayoutData.widthHint = GuiConstants.LABEL_WIDTH_EDIT;
		labelLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT + 6;
		labelLayoutData.horizontalAlignment = GridData.FILL;
		labelLayoutData.verticalAlignment = GridData.FILL;
		labelLayoutData.grabExcessHorizontalSpace = true;
		//labelLayoutData.grabExcessVerticalSpace = true;
		m_label.setLayoutData(labelLayoutData);

		GridData textLayoutData = new GridData();
		//textLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		textLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT + 6;
		textLayoutData.horizontalAlignment = GridData.FILL;
		textLayoutData.verticalAlignment = GridData.FILL;
		textLayoutData.grabExcessHorizontalSpace = true;
		//textLayoutData.grabExcessVerticalSpace = true;
		m_text.setLayoutData(textLayoutData);
	}

	/**
	 * Get the value of the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @return A reference to a <code>Long</code> object is returned.
	 */
	public Object doGetValue()
	{
		// Return the attribute's value.
		return new Long(m_attribute.getValue());
	}

	/**
	 * Set the value of the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @param attr A reference to a <code>Long</code> object.
	 */
	public void doSetValue(Object attr)
	{
		m_attribute.setValue(((Long)attr).toString());
	}
	
	/**
	 * Set the focus on the control.
	 */
	protected void doSetFocus()
	{
		m_text.selectAll();
		m_text.setFocus();
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
	 * and deactivates the cell editor. The <code>NumberAttribute</doce> is also updated
	 * if necessary.
	 * </p>
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	protected void focusLost()
	{
		//super.focusLost();
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
		if (! ((String)m_text.getText()).equals(m_attribute.getStringValue()))
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
	 * Complete cell editing. The <code>NumberAttribute</code> is updated.
	 * 
	 * @return <b>true</b> will alwaus be returned.
	 */
	public boolean stopCellEditing()
	{
		// Update the Framework NumberAttribute.
		String s = (String)m_text.getText();
		m_attribute.setValue(s);
        
		if (m_attribute.getValue() > m_max)
		{
			m_attribute.setValue(new Long(m_max));
		}
		this.setAttribute(m_attribute);

		// Select new string for editing.
		m_text.selectAll();

		// Update the tool tip.
		if (! m_label.isDisposed()) {
		    m_label.setToolTipText("Number [" + m_attribute.getName() + "] - bits = " +
			    m_attribute.getBits() + " - value = " + m_attribute.getValue());
		}

		// Print out some debugging inforamation.
		FrameworkLog.logInfo("Setting attribute to: " + s);
		FrameworkLog.getInstance().debug("Bits: " + m_attribute.getBitString());
        
		// Notify Cell listeners that the editor value is being applied.
		fireApplyEditorValue();

		return true;
	}

	/**
	 * Get the <code>Attribute</code> associated with the editor.
	 * 
	 * @return A reference to the <code>Attribute</code> is returned.
	 */
	public Attribute getAttribute()
	{
		return m_attribute;
	}
	
	/**
	 * Sets the <code>NumberAttribute</code> for the editor.
	 * 
	 * @param attr A reference to the <code>NumberAttribute</code> to use with the
	 * editor.
	 */
	public void setAttribute(Attribute attr)
	{
		if (! (attr instanceof NumberAttribute))
			System.out.println("XXX: Throw An Exception!");
		else
		    m_attribute = (NumberAttribute)attr;
		
		String val = ((NumberAttribute)attr).toValueString();
		m_max = Utilities.biggestIntegerWithNBits(attr.getBits());
		String name = attr.getName();
		
		// Set the value of the CellEditor.
		m_text.setText(val);

        // Set read-only state.
		if (attr.isReadOnly())
		{
			m_text.setEditable(false);
		}
		else
		{
			m_text.setEditable(true);
			m_text.selectAll();
		}

		// Update label and tool tip.
		if (! m_label.isDisposed()) {
		    m_label.setText(name);
		    m_label.setToolTipText("Number [" + attr.getName() + "] - bits = " +
		        attr.getBits() + " - value = " + ((NumberAttribute)attr).getValue());
		}
		
		// Activate the editor.
		//this.activate();
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
	 * Hide the label.
	 * <p>
	 * This routine will set the visibility of the label to be hidden. It will also dispose all the resources
	 * associated with the label. Thus, once the label is disposed, it can not be brought back to life.
	 * </p>
	 */
	public void hideLabel()
	{
		m_label.setVisible(false);
		m_label.dispose();
	}

}


