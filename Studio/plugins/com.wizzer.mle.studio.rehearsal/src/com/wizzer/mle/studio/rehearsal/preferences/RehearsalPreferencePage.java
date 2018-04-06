/*
 * DwpPreferencePage.java
 * Created on Jun 9, 2006
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
package com.wizzer.mle.studio.rehearsal.preferences;

// Import Eclipse classes.
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.rehearsal.RehearsalPlugin;

/**
 * This class implements the global Preference Page for the Magic Lantern
 * Digital Workprint.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage
{
    /** Launch Client Simulator in Vidiom Tools Perspective. */
    public static final int LAUNCH_PERSPECTIVE = 0;
    /** Launch Client Simulator in separate window. */
    public static final int LAUNCH_WINDOW = 1;

	private Button m_perspectiveButton;
	private Button m_windowButton;
	private int m_launchState = LAUNCH_PERSPECTIVE;

	/**
	 * The default constructor.
	 */
	public RehearsalPreferencePage()
	{
		super();
	}

    /**
     * A constructor that initializes the title of the page.
     * 
     * @param title The name of the page.
     */
	public RehearsalPreferencePage(String title)
	{
		super(title);
	}

    /**
     * A constructor that initializes the title of the page and
     * accompanying image.
     * 
     * @param title The name of the page.
     * @param image An image for the page.
     */
	public RehearsalPreferencePage(String title, ImageDescriptor image)
	{
		super(title, image);
	}

    /**
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
	protected Control createContents(Composite parent)
	{
        Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
        
        TabFolder folder = new TabFolder(composite, SWT.NONE);
        
        createLaunchOptions(folder);

		return composite;
	}

	// Create the UI elements for the launch options.
	private void createLaunchOptions(TabFolder folder)
	{
		// Create tab for launch configuration UI.
        TabItem launchItem = new TabItem(folder, SWT.NONE);
        launchItem.setText("Launch Options");

        Composite launchItemComposite = new Composite(folder, SWT.RESIZE);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 1;
        launchItemComposite.setLayout(gridLayout);
        
        Label label = new Label(launchItemComposite, SWT.NONE);
        label.setText("Set global launch configuration parameters:");
        
        Label dummy = new Label(launchItemComposite, SWT.NONE);
        
        Group group = new Group(launchItemComposite, SWT.RESIZE);
        GridLayout groupLayout = new GridLayout();
        groupLayout.numColumns = 1;
        group.setLayout(groupLayout);
        group.setText("Configure rehearsal player views using");
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        group.setData(gridData);
        
        Listener listener = new Listener()
        {
            public void handleEvent(Event event)
            {
                Button button = (Button) event.widget;
                if (! button.getSelection()) return;
                if (m_perspectiveButton.getSelection())
                    m_launchState = LAUNCH_PERSPECTIVE;
                else
                    m_launchState = LAUNCH_WINDOW;
            }
        };
        
        m_perspectiveButton = new Button(group, SWT.RADIO | SWT.RESIZE);
        m_perspectiveButton.setText("Magic Lantern Perspective");
        m_perspectiveButton.setSelection(true);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        m_perspectiveButton.setLayoutData(gridData);
        m_perspectiveButton.addListener(SWT.Selection,listener);
        
        m_windowButton = new Button(group, SWT.RADIO | SWT.RESIZE);
        m_windowButton.setText("Separate Window");
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        gridData.grabExcessHorizontalSpace = true;
        m_windowButton.setLayoutData(gridData);
        m_windowButton.addListener(SWT.Selection,listener);
        
        int prefValue = RehearsalPlugin.getDefault().getPreferenceStore().getInt(
                ValueDefaults.VIEW_CONFIGURATION_KEY);
        if (prefValue == ValueDefaults.VIEW_CONFIGURATION_WINDOW)
        {
            m_perspectiveButton.setSelection(false);
            m_windowButton.setSelection(true);
            m_launchState = LAUNCH_WINDOW;
        }

        launchItem.setControl(launchItemComposite);

        return;
	}

    /**
     * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
	public void init(IWorkbench workbench)
	{
		// Do nothing for now.
	}

    /**
     * Performs special processing when this page's Defaults button has been pressed.
     * 
     * @see org.eclipse.jface.preference.PreferencePage#performDefaults()
     */
	public void performDefaults()
	{
        super.performDefaults();

        // Reset the view configuration settings.
        RehearsalPlugin.getDefault().getPreferenceStore().setValue(
                ValueDefaults.VIEW_CONFIGURATION_KEY,ValueDefaults.VIEW_CONFIGURATION_PERSPECTIVE);
        
        // Update UI.
        m_perspectiveButton.setSelection(true);
        m_windowButton.setSelection(false);
        m_launchState = LAUNCH_PERSPECTIVE;
	}
	
    /**
     * Notifies that the OK button of this page's container has been pressed.
     * 
     * @return <b>false</b> is returned to abort the container's OK processing
     * and <b>true</b> will be returned to allow the OK to happen.
     * 
     * @see org.eclipse.jface.preference.IPreferencePage#performOk()
     */
	public boolean performOk()
	{
        if (m_launchState == LAUNCH_PERSPECTIVE)
        {
        	RehearsalPlugin.getDefault().getPreferenceStore().setValue(
                ValueDefaults.VIEW_CONFIGURATION_KEY,ValueDefaults.VIEW_CONFIGURATION_PERSPECTIVE);
        } else
        {
        	RehearsalPlugin.getDefault().getPreferenceStore().setValue(
                ValueDefaults.VIEW_CONFIGURATION_KEY,ValueDefaults.VIEW_CONFIGURATION_WINDOW);
        }

		return true;
	}
}
