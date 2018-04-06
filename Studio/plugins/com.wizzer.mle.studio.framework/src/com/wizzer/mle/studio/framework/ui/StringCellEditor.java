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

// Declare package.
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
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;


/**
 * This class is a Eclipse SWT <code>CellEditor</code> for the Wizzer Works Framework
 * <code>StringAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class StringCellEditor extends AttributeCellEditor
{
	// The Attribute.
    private StringAttribute m_attribute = null;
	// A label for the editor.
	private Label m_label;
	// The control for the editor.
    private Text m_text;

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public StringCellEditor(Composite parent)
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
    public StringCellEditor(Composite parent, int style)
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
        
        return composite;
    }

    // Initialize the editor's GUI.
    private void initGUI(Composite parent)
    {
     	m_label = new Label(parent,SWT.NONE);
		m_text = new Text(parent,SWT.BORDER | SWT.RESIZE);

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
				StringCellEditor.this.focusLost();
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
		//m_label.setAlignment(SWT.CENTER);
         
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
		labelLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		labelLayoutData.horizontalAlignment = GridData.FILL;
		labelLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		labelLayoutData.grabExcessHorizontalSpace = true;
		//labelLayoutData.grabExcessVerticalSpace = true;
		m_label.setLayoutData(labelLayoutData);

        GridData textLayoutData = new GridData();
		//textLayoutData.widthHint = GuiConstants.EDIT_WIDTH_EDIT;
		textLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT;
		textLayoutData.horizontalAlignment = GridData.FILL;
		textLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
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
     * @return A reference to a <code>String</code> is returned.
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
     * and deactivates the cell editor. The <code>StringAttribute</doce> is also updated
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
     * Complete cell editing. The <code>StringAttribute</code> is updated.
     * 
     * @return <b>true</b> will alwaus be returned.
     */
    public boolean stopCellEditing()
    {
    	// Update the Framework StringAttribute.
        String s = (String)m_text.getText();
        m_attribute.setValue(s);
        
        // Select new string for editing.
        m_text.selectAll();

        // Update the tool tip.
        if (! m_label.isDisposed())
		    m_label.setToolTipText("String [" + m_attribute.getName() + "] - bits = " +
		        m_attribute.getBits() + " - value = " + m_attribute.getValue());

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
     * Sets the <code>StringAttribute</code> for the editor.
     * 
     * @param attr A reference to the <code>StringAttribute</code> to use with the
     * editor.
     */
    public void setAttribute(Attribute attr)
    {
    	if (! (attr instanceof StringAttribute))
			System.out.println("XXX: Throw An Exception!");
        else
			m_attribute = (StringAttribute)attr;
        
        String val  = attr.toString();
        String name = attr.getName();
        int bits    = attr.getBits();
        
        // Set the value of the CellEditor.
        m_text.setVisible(bits != 0);
        m_text.setText(val);
        
        // Set read only state.
        if (attr.isReadOnly())
        {
            m_text.setEditable(false);;
        }
        else
        {
            m_text.setEditable(true);;
            m_text.selectAll();
            m_text.setFocus();
        }
        
        // Update label and tool tip.
        if (! m_label.isDisposed()) {
            m_label.setText(name);
            m_label.setToolTipText("String [" + attr.getName() + "] - bits = " +
                attr.getBits() + " - value = " + ((StringAttribute)attr).getValue());
        }

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

