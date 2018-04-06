/*
 * DwpDSOFileCellEditor.java
 * Created on Apr 20, 2005
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
import java.io.File;

// Import Eclipse classes.
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// Import Magic Lantern framework.
import com.wizzer.mle.studio.framework.ui.GuiConstants;
import com.wizzer.mle.studio.framework.ui.GuiUtilities;

/**
 * This class is a Eclipse SWT <code>CellEditor</code> for the Wizzer Works
 * Magic Lanatern Digital Workprint <code>DwpDSOFileAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpDSOFileCellEditor extends DwpNameCellEditor
{
	/** The extension button for the editor. */
	protected Button m_button;

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public DwpDSOFileCellEditor(Composite parent)
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
	public DwpDSOFileCellEditor(Composite parent, int style)
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
	    Control control = super.createControl(parent);
	    		
		// Create the extension button.
		m_button = new Button((Composite)control,SWT.NONE);
		m_button.setText("...");

		m_button.addSelectionListener(new SelectionAdapter() {
		    public void widgetSelected(SelectionEvent e)
		    {
			     handleExtensionButton(e);
			}
	    });

		GridData buttonLayoutData = new GridData();
		//buttonLayoutData.heightHint = GuiConstants.FIELD_HEIGHT_EDIT + 8;
		buttonLayoutData.heightHint = GuiConstants.PANEL_HEIGHT_EDIT;
		//buttonLayoutData.verticalAlignment = GridData.FILL | GridData.CENTER;
		buttonLayoutData.horizontalAlignment = GridData.END;
		m_button.setLayoutData(buttonLayoutData);

	    return control;
	}
	
	/**
	 * Handle the Selection event.
	 * 
	 * @param event The <code>SelectionEvent</code> that caused
	 * the event to occur.
	 */
	protected void handleExtensionButton(SelectionEvent event)
	{
        File f = GuiUtilities.openFile(this.getControl());
        if (f == null) {
            return;
        } else {
            String s = f.getAbsolutePath();
            m_text.setText(s);
            this.stopCellEditing();
        }

	}

}
