/*
 * MleTargetConfigurationPage.java
 * Created on Apr 6, 2005
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
package com.wizzer.mle.studio.project;

// Import Eclipse classes.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

/**
 * This class implements a Wizard page for configuring a
 * Magic Lantern target platform.
 * 
 * @author Mark S. Millard
 */
public class MleTargetConfigurationPage extends WizardPage
{
    /** Default setting for DWP and DPP file names. */
    public final String FILENAME_NOT_SPECIFIED = "__UNKNOWN_FILENAME_";

    // The name of the Digital Workprint.
    private String m_dwpFilename = FILENAME_NOT_SPECIFIED;
    // The name of the Digital Playprint.
    private String m_dppFilename = FILENAME_NOT_SPECIFIED;
    // Flag indicating whether DWP name specified.
    private boolean m_dwpFilenameSpecified = false;
    // Flag indicating whether DPP name specified.
    private boolean m_dppFilenameSpecified = false;
    
	// The button for selecting the target platform.
	private Button m_addRehearsalPlayerButton = null;
	// The label for the DPP text widget.
	private Label m_dppFilenameLabel = null;
	// The text widget for the name of the DWP.
	private Text m_dwpFilenameText = null;
	// The text widget for the name of the DPP.
	private Text m_dppFilenameText = null;
	
	// The collection of Mastering configuration widgets.
	private Group m_masteringGroup = null;
	// The button for enabling mastering.
	private Button m_enableMasteringButton = null;
	// The button for generating Runtime Tables.
	private Button m_genRuntimeTablesButton = null;
	// The button for generating Cast Chunks.
	private Button m_genCastChunksButton = null;
	// The button for generating Scene Chunks.
	private Button m_genSceneChunksButton = null;
	// The button for generating MediaRef Chunks.
	private Button m_genMediaRefChunksButton = null;
	// The button for generating DPP Layout Script.
	private Button m_genDppLayoutScriptButton = null;
	// The button for generating DPP.
	private Button m_genDppButton = null;
	
	TabFolder m_folder = null;
	TabItem m_dwpTab = null;
	TabItem m_dppTab = null;
	
	// Label for comments.
	private Label m_comment = null;
	private String DWP_CONFIG_COMMENT = "Configure the Digital Workprint options.";
	private String DPP_CONFIG_COMMENT = "Configure the Digital Playprint options.";

    /**
     * Creates a new wizard page with the given name, and with no title or image.
     * 
     * @param pageName The name of the page.
     */
    public MleTargetConfigurationPage(String pageName)
    {
        super(pageName);
    }

    /**
     * Creates a new wizard page with the given name, title, and image. 
     * 
     * @param pageName The name of the page.
     * @param title The title for this wizard page, or <b>null</b> if none.
     * @param titleImage The image descriptor for the title of this wizard page,
     * or <b>null</b> if none.
     */
    public MleTargetConfigurationPage(String pageName, String title,
            ImageDescriptor titleImage)
    {
        super(pageName, title, titleImage);
    }

	/**
	 * Creates the top level control for this dialog page under the given parent composite. 
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 * 
	 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
    public void createControl(Composite parent)
    {
		// Create a new composite.
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		// Add a widget to offer hints.
		m_comment = new Label(composite, SWT.NONE);
		m_comment.setText(DWP_CONFIG_COMMENT);
		
		// Create a dummy label for spacing.
		new Label(composite, SWT.NONE);

		// Create a folder for the tabs.
		m_folder = new TabFolder(composite, SWT.NONE);
		GridData folderData = new GridData(SWT.FILL,SWT.FILL,true,true);
		m_folder.setLayoutData(folderData);
		m_folder.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				TabItem item = m_folder.getItem(m_folder.getSelectionIndex());
				if (item.equals(m_dwpTab))
				    m_comment.setText(DWP_CONFIG_COMMENT);
				else if (item.equals(m_dppTab))
				    m_comment.setText(DPP_CONFIG_COMMENT);
			}
		});
		
		// Create a tab for the Digital Workprint configuration.
		m_dwpTab = new TabItem(m_folder, SWT.NONE);
		m_dwpTab.setText("Digital Workprint");
		
		// Configure the dwpTab layout.
		Composite dwpTabComposite = new Composite(m_folder, SWT.NONE);
		dwpTabComposite.setLayout(new GridLayout());
		dwpTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		m_dwpTab.setControl(dwpTabComposite);
				
		// Create the Project DWP components.
		createDigitalWorkprintGroup(dwpTabComposite);
		
		// Create a tab for the Digital Playprint configuration.
		m_dppTab = new TabItem(m_folder, SWT.NONE);
		m_dppTab.setText("Digital Playprint");

		// Configure the dppTab layout.
		Composite dppTabComposite = new Composite(m_folder, SWT.NONE);
		dppTabComposite.setLayout(new GridLayout());
		dppTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		m_dppTab.setControl(dppTabComposite);

		// Create the Project DWP components.
		createDigitalPlayprintGroup(dppTabComposite);

		// Make composite the main control point.
		setControl(composite);
    }

	/*
	 * Create the GUI elements for DWP configuration.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 * 
	 * @return A <code>Group</code> widget is returned.
	 */
    private Group createDigitalWorkprintGroup(Composite parent)
    {
		// Create a new Group.
		Group dwpGroup = new Group(parent, SWT.RESIZE);
		
		// Configure Group layout.
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		dwpGroup.setLayout(layout);
		dwpGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		dwpGroup.setText("Digital Workprint");
		
		// Create the text for naming the DWP file.
		Label dwpFilenameLabel = new Label(dwpGroup, SWT.NONE);
		dwpFilenameLabel.setText("DWP Filename:");
		
		m_dwpFilenameText = new Text(dwpGroup, SWT.BORDER | SWT.RESIZE);
		if (m_dwpFilename != null)
		    m_dwpFilenameText.setText(m_dwpFilename);
		m_dwpFilenameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent evt)
			{
				handleDwpFilenameModified();
			}
		});
		m_dwpFilenameText.addKeyListener(new KeyAdapter()
		{
		    public void keyReleased(KeyEvent evt)
		    {
		        m_dwpFilenameSpecified = true;
		    }
		});
		
		// Configure Text layout.
		GridData textData = new GridData();
		textData.grabExcessHorizontalSpace = true;
		m_dwpFilenameText.setLayoutData(textData);
				
		// Add a dummy widget to space widgets.
		new Label(dwpGroup, SWT.NONE);

		// Create the button for adding a Rehearsal Player.
		m_addRehearsalPlayerButton = new Button(dwpGroup, SWT.CHECK);
		m_addRehearsalPlayerButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleRehearsalPlayerButtonSelected();
			}
		});
		m_addRehearsalPlayerButton.setText("Add Rehearsal Player");
		m_addRehearsalPlayerButton.setSelection(true);

		// Configure Button layout.
		GridData buttonData = new GridData();
		buttonData.horizontalSpan = 2;
		m_addRehearsalPlayerButton.setLayoutData(buttonData);

		return dwpGroup;
    }
    
	/*
	 * Create the GUI elements for DPP configuration.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 * 
	 * @return A <code>Group</code> widget is returned.
	 */
    private Group createDigitalPlayprintGroup(Composite parent)
    {
		// Create a new Group.
		Group dppGroup = new Group(parent, SWT.NONE);
		dppGroup.setText("Digital Playprint");
		
		// Configure Group layout.
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		dppGroup.setLayout(layout);
		dppGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// Create the button for enabling/disabling Mastering a DPP.
		m_enableMasteringButton = new Button(dppGroup, SWT.CHECK);
		m_enableMasteringButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleEnableMasteringButtonSelected();
			}
		});
		m_enableMasteringButton.setText("Master Digital Playprint");
		m_enableMasteringButton.setSelection(true);

		// Configure button layout.
		GridData buttonData = new GridData();
		buttonData.horizontalSpan = 2;
		m_enableMasteringButton.setLayoutData(buttonData);

		// Add a dummy widget to space widgets.
		Label dummyLabel = new Label(dppGroup, SWT.NONE);
		GridData dummyLabelData = new GridData();
		dummyLabelData.horizontalSpan = 2;
		dummyLabel.setLayoutData(dummyLabelData);

		// Create the text for naming the DWP file.
		m_dppFilenameLabel = new Label(dppGroup, SWT.NONE);
		m_dppFilenameLabel.setText("DPP Filename:");
		
		m_dppFilenameText = new Text(dppGroup, SWT.BORDER | SWT.RESIZE);
		if (m_dppFilename != null)
		    m_dppFilenameText.setText(m_dppFilename);
		m_dppFilenameText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent evt)
			{
				handleDppFilenameModified();
			}
		});
		m_dppFilenameText.addKeyListener(new KeyAdapter()
		{
		    public void keyReleased(KeyEvent evt)
		    {
		        m_dppFilenameSpecified = true;
		    }
		});
		
		// Configure Text layout.
		GridData textData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		//textData.horizontalSpan = 1;
		//textData.grabExcessHorizontalSpace = true;
		m_dppFilenameText.setLayoutData(textData);

		// Create a sub-Group for Mastering Configuration widgets.
		m_masteringGroup = new Group(dppGroup, SWT.RESIZE);
		m_masteringGroup.setText("Mastering Configuration");
		
		layout = new GridLayout();
		layout.numColumns = 1;
		m_masteringGroup.setLayout(layout);
		GridData layoutData = new GridData(GridData.FILL_HORIZONTAL);
		layoutData.horizontalSpan = 2;
		m_masteringGroup.setLayoutData(layoutData);
				
		// Create the button for generating the Runtime Tables.
		m_genRuntimeTablesButton = new Button(m_masteringGroup, SWT.CHECK);
		m_genRuntimeTablesButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleGenTablesButtonSelected();
			}
		});
		m_genRuntimeTablesButton.setText("Generate Runtime Tables");
		m_genRuntimeTablesButton.setSelection(true);

		// Configure button layout.
		buttonData = new GridData();
		buttonData.horizontalSpan = 1;
		m_genRuntimeTablesButton.setLayoutData(buttonData);

		// Create the button for generating the Cast Chunks.
		m_genCastChunksButton = new Button(m_masteringGroup, SWT.CHECK);
		m_genCastChunksButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleGenCastsButtonSelected();
			}
		});
		m_genCastChunksButton.setText("Generate Cast Chunks");
		m_genCastChunksButton.setSelection(true);

		// Configure button layout.
		buttonData = new GridData();
		buttonData.horizontalSpan = 1;
		m_genCastChunksButton.setLayoutData(buttonData);

		// Create the button for generating the Scene Chunks.
		m_genSceneChunksButton = new Button(m_masteringGroup, SWT.CHECK);
		m_genSceneChunksButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleGenScenesButtonSelected();
			}
		});
		m_genSceneChunksButton.setText("Generate Scene Chunks");
		m_genSceneChunksButton.setSelection(true);

		// Configure button layout.
		buttonData = new GridData();
		buttonData.horizontalSpan = 1;
		m_genSceneChunksButton.setLayoutData(buttonData);

		// Create the button for generating the MediaRef Chunks.
		m_genMediaRefChunksButton = new Button(m_masteringGroup, SWT.CHECK);
		m_genMediaRefChunksButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleGenMediaRefsButtonSelected();
			}
		});
		m_genMediaRefChunksButton.setText("Generate Media Reference Chunks");
		m_genMediaRefChunksButton.setSelection(true);

		// Configure button layout.
		buttonData = new GridData();
		buttonData.horizontalSpan = 1;
		m_genMediaRefChunksButton.setLayoutData(buttonData);

		// Create the button for generating the DPP Layout Script.
		m_genDppLayoutScriptButton = new Button(m_masteringGroup, SWT.CHECK);
		m_genDppLayoutScriptButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleGenScenesButtonSelected();
			}
		});
		m_genDppLayoutScriptButton.setText("Generate DPP Layout Script");
		m_genDppLayoutScriptButton.setSelection(true);

		// Configure button layout.
		buttonData = new GridData();
		buttonData.horizontalSpan = 1;
		m_genDppLayoutScriptButton.setLayoutData(buttonData);

		// Create the button for generating the Digital Playprint.
		m_genDppButton = new Button(m_masteringGroup, SWT.CHECK);
		m_genDppButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleGenDppButtonSelected();
			}
		});
		m_genDppButton.setText("Generate Digital Playprint");
		m_genDppButton.setSelection(true);

		// Configure button layout.
		buttonData = new GridData();
		buttonData.horizontalSpan = 1;
		m_genDppButton.setLayoutData(buttonData);

		return dppGroup;
    }    

    /**
     * Get the Digital Workprint file name.
     * 
     * @return The name of the DWP is returned.
     */
    public String getDwpFilename()
    {
        return m_dwpFilename;
    }
    
    /**
     * Set the Digital Workprint file name.
     * 
     * @param filename The name of the DWP.
     */
    public void setDwpFilename(String filename)
    {
       m_dwpFilename = filename;
       if (m_dwpFilenameText != null)
           m_dwpFilenameText.setText(filename);
    }

    /**
     * Get the Digital Playprint file name.
     * 
     * @return The name of the DPP is returned.
     */
    public String getDppFilename()
    {
        return m_dppFilename;
    }
    
    /**
     * Set the Digital Playprint file name.
     * 
     * @param filename The name of the DPP.
     */
    public void setDppFilename(String filename)
    {
       m_dppFilename = filename;
       if (m_dppFilenameText != null)
           m_dppFilenameText.setText(filename);
    }
    
    /**
     * Determine whether the Rehearsal Player is selected.
     * 
     * @return <b>true</b> will be returned if the rehearsal player is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isRehearsalPlayerSelected()
    {
    	return m_addRehearsalPlayerButton.getSelection();
    }
    
    /**
     * Determine whether mastering is enabled.
     * 
     * @return <b>true</b> will be returned if mastering properties are enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isMasteringSelected()
    {
        return m_enableMasteringButton.getSelection();
    }
    
    /**
     * Determine whether cast generation is selected.
     * 
     * @return <b>true</b> will be returned if cast generation is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isGencastSelected()
    {
        if (isMasteringSelected())
            return m_genCastChunksButton.getSelection();
        else
            return false;
    }
    
    /**
     * Determine whether scene generation is selected.
     * 
     * @return <b>true</b> will be returned if scene generation is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isGensceneSelected()
    {
        if (isMasteringSelected())
            return m_genSceneChunksButton.getSelection();
        else
            return false;
    }
    
    /**
     * Determine whether media reference generation is selected.
     * 
     * @return <b>true</b> will be returned if media reference generation is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isGenmrefSelected()
    {
        if (isMasteringSelected())
            return m_genMediaRefChunksButton.getSelection();
        else
            return false;
    }
    
    /**
     * Determine whether table generation is selected.
     * 
     * @return <b>true</b> will be returned if table generation is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isGentablesSelected()
    {
        if (isMasteringSelected())
            return m_genRuntimeTablesButton.getSelection();
        else
            return false;
    }
    
    /**
     * Determine whether DPP Layout Script generation is selected.
     * 
     * @return <b>true</b> will be returned if script generation is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isGendppscriptSelected()
    {
        if (isMasteringSelected())
            return m_genDppLayoutScriptButton.getSelection();
        else
            return false;
    }
    
    /**
     * Determine whether DPP generation is selected.
     * 
     * @return <b>true</b> will be returned if DPP generation is enabled.
     * Otherwise <b>false</b> will be returned.
     */
    public boolean isGendppSelected()
    {
        if (isMasteringSelected())
            return m_genDppButton.getSelection();
        else
            return false;
    }

    /**
     * Determine if the page is complete enough to finish.
     * 
     * @return <b>true</b> will be returned if the page is compelete.
     * Otherwise, <b>false</b> will be returned.
     */
    public boolean isPageComplete()
    {
        if ((m_dwpFilename == null) || (m_dwpFilename.equals("")))
            return false;
        else if ((m_masteringGroup.isEnabled() &&
                 ((m_dppFilename == null) || (m_dppFilename.equals("")))))
            return false;
        else
            return true;
    }

	/**
	 *  Handle whether to provide a Rehearsal Player or not.
	 */
	protected void handleRehearsalPlayerButtonSelected()
	{
		System.out.println("Rehearsal Player button toggled.");
	}

	/**
	 *  Handle whether to generate Runtime Tables or not.
	 */
	protected void handleEnableMasteringButtonSelected()
	{
		//System.out.println("Enabling Mastering button toggled.");
		if (m_masteringGroup != null)
		{
			if (m_masteringGroup.isEnabled())
			{
			    // Toggle off.
			    m_masteringGroup.setEnabled(false);
			    m_dppFilenameText.setEnabled(false);
			    m_dppFilenameLabel.setEnabled(false);
			    m_genRuntimeTablesButton.setEnabled(false);
			    m_genCastChunksButton.setEnabled(false);
			    m_genSceneChunksButton.setEnabled(false);
			    m_genMediaRefChunksButton.setEnabled(false);
			    m_genDppLayoutScriptButton.setEnabled(false);
			    m_genDppButton.setEnabled(false);
			} else
			{
			    // Toggle on.
			    m_masteringGroup.setEnabled(true);
			    m_dppFilenameText.setEnabled(true);
			    m_dppFilenameLabel.setEnabled(true);
			    m_genRuntimeTablesButton.setEnabled(true);
			    m_genCastChunksButton.setEnabled(true);
			    m_genSceneChunksButton.setEnabled(true);
			    m_genMediaRefChunksButton.setEnabled(true);
			    m_genDppLayoutScriptButton.setEnabled(true);
			    m_genDppButton.setEnabled(true);
			}
			
			setPageComplete(true);
		}
	}

	/**
	 *  Handle whether to generate Runtime Tables or not.
	 */
	protected void handleGenTablesButtonSelected()
	{
		System.out.println("Generate Runtime Tables button toggled.");
	}
	
	/**
	 *  Handle whether to generate Cast Chunks or not.
	 */
	protected void handleGenCastsButtonSelected()
	{
		System.out.println("Generate Cast Chunks button toggled.");
	}

	/**
	 *  Handle whether to generate Scene Chunks or not.
	 */
	protected void handleGenScenesButtonSelected()
	{
		System.out.println("Generate Scene Chunks button toggled.");
	}

	/**
	 *  Handle whether to generate Media Reference Chunks or not.
	 */
	protected void handleGenMediaRefsButtonSelected()
	{
		System.out.println("Generate Media Reference Chunks button toggled.");
	}

	/**
	 *  Handle whether to generate Digital Playprint Layout Script or not.
	 */
	protected void handleGenDppLayoutScriptButtonSelected()
	{
		System.out.println("Generate DPP Layout Script button toggled.");
	}

	/**
	 *  Handle whether to generate Digital Playprint or not.
	 */
	protected void handleGenDppButtonSelected()
	{
		System.out.println("Generate DPP button toggled.");
	}

	/**
	 *  Handle updates to the name of the Digital Workprint file.
	 */
	protected void handleDwpFilenameModified()
	{
		//System.out.println("Digital Workprint filename modified: " + m_dwpFilenameText.getText());
	    m_dwpFilename = m_dwpFilenameText.getText();
	    if ((m_dwpFilename != null) && (! m_dwpFilename.equals("")))
	        setPageComplete(true);
	    else
	        setPageComplete(false);
	}

	/**
	 *  Handle updates to the name of the Digital Playprint file.
	 */
	protected void handleDppFilenameModified()
	{
		//System.out.println("Digital Playprint filename modified: " + m_dppFilenameText.getText());
	    m_dppFilename = m_dppFilenameText.getText();
	    if ((m_dppFilename != null) && (! m_dppFilename.equals("")))
	        setPageComplete(true);
	    else
	        setPageComplete(false);
	}
	
	/**
	 * Determine whether the DWP has been specified by this configuration page.
	 * 
	 * @return <b>true</b> is returned if the DWP filename was specified using
	 * the controls on this page. Otherwise, <b>false</b> will be returned.
	 */
	public boolean isDwpSpecified()
	{
	    return m_dwpFilenameSpecified;
	}

	/**
	 * Determine whether the DPP has been specified by this configuration page.
	 * 
	 * @return <b>true</b> is returned if the DPP filename was specified using
	 * the controls on this page. Otherwise, <b>false</b> will be returned.
	 */	
	public boolean isDppSpecified()
	{
	    return m_dppFilenameSpecified;
	}

	public void performFinish(IProgressMonitor monitor) throws CoreException, InterruptedException
	{
		// Do nothing for now.
	}
}
