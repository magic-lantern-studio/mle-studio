// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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

//Declare package.
package com.wizzer.mle.studio.framework.ui;

// Import Eclipse packages.
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;

// Import Wizzer Works packages.
import com.wizzer.mle.studio.framework.FrameworkLog;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.ChoiceAttribute;


/**
 * This class is a Eclipse SWT <code>CellEditor</code> for the Wizzer Works Framework
 * <code>ChoiceAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class ChoiceCellEditor extends AttributeCellEditor
{
    private ChoiceAttribute m_attribute = null;
    private Combo m_combo;
    private Label m_label;
    
    Attribute m_lastAttribute = null;
    boolean m_allowSelections = true;

	
	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public ChoiceCellEditor(Composite parent)
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
	public ChoiceCellEditor(Composite parent, int style)
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
		Composite composite = new Composite(parent,SWT.NONE);
		//composite.setBounds(0, 0, GuiConstants.PANEL_WIDTH_EDIT, GuiConstants.PANEL_HEIGHT_EDIT);
		composite.setBounds(0, 0, parent.getSize().x, parent.getSize().y);
		this.initGUI(composite);
        
		// Set the layout for the editor.
		composite.setLayout(layout);
        
		return composite;
	}

	// Iniitialize the editor's GUI.
	private void initGUI(Composite parent)
	{
		m_label = new Label(parent,SWT.NONE);
		m_combo = new Combo(parent,SWT.DROP_DOWN | SWT.READ_ONLY);

		// Add a selection listener for the Combo control.		
		m_combo.addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e)
				{
					if (m_allowSelections) {
						stopCellEditing();
					}
				}
			});

		// Add a focus listener for the Combo control
		m_combo.addFocusListener(new FocusAdapter() {
			public void focusLost(FocusEvent e) {
				ChoiceCellEditor.this.focusLost();
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
         
		//m_combo.setBackground(new Color(parent.getDisplay(), 153, 255, 255));
		//m_combo.setForeground(new Color(parent.getDisplay(), 0, 0, 255));
		//m_combo.setFont(new Font(parent.getDisplay(),"Comic Sans MS", GUIConstants.FONT_SIZE_EDIT, SWT.NORMAL));
		m_combo.setBackground(new Color(parent.getDisplay(), 255, 255, 255));
		
		//m_label.setBounds(GUIConstants.BORDER_SIZE_EDIT, GUIConstants.BORDER_SIZE_EDIT,
		//    GUIConstants.LABEL_WIDTH_EDIT, GUIConstants.FIELD_HEIGHT_EDIT);
		//text.setBounds(GUIConstants.LABEL_WIDTH_EDIT + (2 * GUIConstants.BORDER_SIZE_EDIT),
		//	GUIConstants.BORDER_SIZE_EDIT, GUIConstants.EDIT_WIDTH_EDIT, GUIConstants.FIELD_HEIGHT_EDIT);

		GridData labelLayoutData = new GridData();
		//labelLayoutData.widthHint = GuiConstants.LABEL_WIDTH_EDIT;
		labelLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		labelLayoutData.horizontalAlignment = GridData.FILL;
		labelLayoutData.verticalAlignment = GridData.FILL;
		labelLayoutData.grabExcessHorizontalSpace = true;
		//labelLayoutData.grabExcessVerticalSpace = true;
		m_label.setLayoutData(labelLayoutData);

		GridData comboLayoutData = new GridData();
		//comboLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		comboLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		comboLayoutData.horizontalAlignment = GridData.FILL;
		comboLayoutData.verticalAlignment = GridData.FILL;
		comboLayoutData.grabExcessHorizontalSpace = true;
		//comboLayoutData.grabExcessVerticalSpace = true;
		m_combo.setLayoutData(comboLayoutData);
	}

	/**
	 * Get the value of the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @return A reference to an <code>Integer</code> object is returned.
	 */
	public Object doGetValue()
	{
		// Return the attribute.
		return new Integer(m_attribute.getValue());
	}

	/**
	 * Set the value of the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @param attr A reference to an <code>Integer</code> object.
	 */
	public void doSetValue(Object attr)
	{
		m_attribute.setValue(attr);
	}
	
	/**
	 * Set the focus on the control.
	 */
	protected void doSetFocus()
	{
		m_combo.select(m_attribute.getIndex());
		m_combo.setFocus();
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
	 * and deactivates the cell editor. The <code>ChoiceAttribute</doce> is also updated
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
		if (! (m_combo.getSelectionIndex() == m_attribute.getValue()))
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
	 * Complete cell editing. The <code>ChoiceAttribute</code> is updated.
	 * 
	 * @return <b>true</b> will alwaus be returned.
	 */
	public boolean stopCellEditing()
	{        
		// Update the Framework ChoiceAttribute.
		if (m_attribute == null)
		{
			return false;
		}
		int index = m_combo.getSelectionIndex();
		m_attribute.setValue(new Integer(index));

		// Update the tool tip.
		if (! m_label.isDisposed()) {
			m_label.setToolTipText("Choice [" + m_attribute.getName() + "] - bits = " +
			    m_attribute.getBits() + " - value = " + m_attribute.getValue());
		}

		// Print out some debugging inforamation.   XXX - should be logged.
		FrameworkLog.logInfo("Item changed to " + index);
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
	 * Sets the <code>ChoiceAttribute</code> for the editor.
	 * 
	 * @param attr A reference to the <code>ChoiceAttribute</code> to use with the
	 * editor.
	 */
    public void setAttribute(Attribute attr)
    {
    	if (! (attr instanceof ChoiceAttribute))
		    System.out.println("XXX: Throw An Exception!");
		
    	// Disable selection.
        m_allowSelections = false;
        
        m_combo.removeAll();
        Object objects[] = ((ChoiceAttribute)attr).getChoices();
        for (int i = 0; i < objects.length; i++)
        {
            m_combo.add((String)objects[i]);
        }

        m_attribute = (ChoiceAttribute)attr;
        int index = ((ChoiceAttribute)attr).getIndex();
        String name = attr.getName();
        
		// Set read only state.
        m_combo.select(index);
        if (attr.isReadOnly())
        {
            m_combo.setEnabled(false);
        }
        else
        {
            m_combo.setEnabled(true);
        }
        
		// Update label and tool tip.
		if (! m_label.isDisposed()) {
            m_label.setText(name);
            m_label.setToolTipText("Choice [" + attr.getName() + "] - bits = " +
                attr.getBits() + " - value = " + ((ChoiceAttribute)attr).getValue());
		}
        
        // Enable selection.
        m_allowSelections = true;
 
        // Activate the editor.       
        //this.activate();
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
