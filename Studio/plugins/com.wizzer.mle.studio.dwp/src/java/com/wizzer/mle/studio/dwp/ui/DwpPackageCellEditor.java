/*
 * DwpPackageCellEditor.java
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
 * Magic Lanatern Digital Workprint <code>DwpPackageAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpPackageCellEditor extends DwpNameCellEditor
{
	/** The extension button for the editor. */
	protected Button m_button;

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 */
	public DwpPackageCellEditor(Composite parent)
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
	public DwpPackageCellEditor(Composite parent, int style)
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
	/*
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
	*/
	
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
