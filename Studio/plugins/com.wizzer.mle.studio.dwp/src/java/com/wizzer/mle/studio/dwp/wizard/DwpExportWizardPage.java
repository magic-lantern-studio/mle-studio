/*
 * DwpExportWizardPage.java
 * Created on Jun 13, 2005
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
package com.wizzer.mle.studio.dwp.wizard;

// Import standard Java classes.
import java.io.File;
import java.util.List;

// Import Eclipse classes.
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.ui.dialogs.WizardExportResourcesPage;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogSettings;
import java.lang.reflect.InvocationTargetException;

/**
 * This class implements a Wizard page for exporting
 * Digital Workprints to various file formats.
 * 
 * @author Mark S. Millard
 */
public class DwpExportWizardPage extends WizardExportResourcesPage implements Listener
{
	// Dialog store id constants.
	private static final String STORE_DESTINATION_NAMES_ID =
		"DwpExportWizardPage.STORE_DESTINATION_NAMES_ID";
	private static final String STORE_OVERWRITE_EXISTING_FILES_ID =
		"DwpExportWizardPage.STORE_OVERWRITE_EXISTING_FILES_ID";
	private static final String STORE_CREATE_STRUCTURE_ID =
		"DwpExportWizardPage.STORE_CREATE_STRUCTURE_ID";
	private static final String STORE_EXPORT_ASCII_FORMAT_ID =
		"DwpExportWizardPage.STORE_EXPORT_ASCII_FORMAT_ID";

    // The destination text widget.
    private Combo m_destinationNameField = null;
    // The destination browse button.
    private Button m_destinationBrowseButton = null;
    // The binary export format button.
    private Button m_binaryFormatButton = null;
    // The ASCII export format button.
    private Button m_asciiFormatButton = null;
    // The create directory structure button.
    private Button m_createDirectoryStructureButton = null;
    // The create selection directory structure button.
    private Button m_createSelectionOnlyButton = null;
    // The overwrite existing files button.
    private Button m_overwriteExistingFilesCheckbox = null;
    
    // Flag indicating whether page is complete.
    private boolean m_pageComplete = false;
    
    /**
     * A constructor that initializes the name of the page and the
     * curret resource selection.
     * 
     * @param pageName The name of the Wizard page.
     * @param selection The current resource selection.
     */
    public DwpExportWizardPage(String pageName,
    	IStructuredSelection selection)
    {
        super(pageName, selection);
        
        // Initialize the wizard.
        init();
    }
    
    /**
     * Initialize the wizard.
     */
    protected void init()
    {
        setTitle(DwpExportWizardMessages.getString("DwpExportWizardPage.title"));
        setDescription(DwpExportWizardMessages.getString("DwpExportWizardPage.destinationEmpty"));
    }

	/**
	 *	Add the passed value to self's destination widget's history.
	 *
	 *	@param value The value to add.
	 */
	protected void addDestinationItem(String value)
	{
		m_destinationNameField.add(value);
	}

    /* (non-Javadoc)
     * @see org.eclipse.ui.dialogs.WizardExportResourcesPage#createDestinationGroup(org.eclipse.swt.widgets.Composite)
     */
    protected void createDestinationGroup(Composite parent)
    {
        Font font = parent.getFont();

        Composite destGroup = new Composite(parent,SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        destGroup.setLayout(layout);
        GridData destGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destGroup.setLayoutData(destGroupData);
        
        Label destLabel = new Label(destGroup,SWT.NONE);
        destLabel.setText(DwpExportWizardMessages.getString("DwpExportWizardPage.toDirectory"));

		m_destinationNameField =
			new Combo(destGroup, SWT.SINGLE | SWT.BORDER);
		m_destinationNameField.addListener(SWT.Modify, this);
		m_destinationNameField.addListener(SWT.Selection, this);
		GridData data =
			new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		data.widthHint = SIZING_TEXT_FIELD_WIDTH;
		m_destinationNameField.setLayoutData(data);
		m_destinationNameField.setFont(font);

        m_destinationBrowseButton = new Button(destGroup,SWT.NONE);
        m_destinationBrowseButton.setText(DwpExportWizardMessages.getString("DwpExportWizardPage.browse"));
        m_destinationBrowseButton.addListener(SWT.Selection, this);
    }
    
    /*
     * (non-Javadoc)
     * @see org.eclipse.ui.dialogs.WizardDataTransferPage#createOptionsGroupButtons(org.eclipse.swt.widgets.Group)
     */
    protected void createOptionsGroupButtons(Group optionsGroup)
    {
        Font font = optionsGroup.getFont();
        
        // Create widgets for file overwrite options.
        createOverwriteExisting(optionsGroup,font);
        
        // Create widgets for directory structure creation options.
        createDirectoryStructureOptions(optionsGroup,font);
        
        // Create widgets for type of export options.
        createFormatOptions(optionsGroup,font);
    }
    
    /**
     * Create the buttons for the group that determine the output format.
     * 
     * @param optionsGroup The collection of options.
     * @param font The font to use.
     */
    protected void createFormatOptions(Group optionsGroup,Font font)
    {
        Group formatGroup = new Group(optionsGroup,SWT.NONE);
        formatGroup.setText("Available Formats");
        GridLayout layout = new GridLayout(1,false);
        formatGroup.setLayout(layout);
        GridData destGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        formatGroup.setLayoutData(destGroupData);

        m_binaryFormatButton = new Button(formatGroup,SWT.RADIO | SWT.LEFT);
        m_binaryFormatButton.setText(DwpExportWizardMessages.getString("DwpExportWizardPage.exportBinaryFormat"));
        m_binaryFormatButton.setFont(font);
        m_binaryFormatButton.setSelection(true);
        m_binaryFormatButton.addListener(SWT.Selection, this);
        
        m_asciiFormatButton = new Button(formatGroup,SWT.RADIO | SWT.LEFT);
        m_asciiFormatButton.setText(DwpExportWizardMessages.getString("DwpExportWizardPage.exportAsciiFormat"));
        m_asciiFormatButton.setFont(font);
        m_asciiFormatButton.addListener(SWT.Selection, this);
    }
    
	/**
	 * Create the buttons for the group that determine if the entire or
	 * selected directory structure should be created.
	 * 
	 * @param optionsGroup The collection of options.
	 * @param font The font to use.
	 */
	protected void createDirectoryStructureOptions(Group optionsGroup,Font font)
	{
		// Create directory structure radios.
		m_createDirectoryStructureButton =
			new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
		m_createDirectoryStructureButton.setText(DwpExportWizardMessages.getString("DwpExportWizardPage.createDirectoryStructure"));
		m_createDirectoryStructureButton.setSelection(false);
		m_createDirectoryStructureButton.setFont(font);

		// Create directory structure radios.
		m_createSelectionOnlyButton =
			new Button(optionsGroup, SWT.RADIO | SWT.LEFT);
		m_createSelectionOnlyButton.setText(
			DwpExportWizardMessages.getString("DwpExportWizardPage.createSelectedDirectories"));
		m_createSelectionOnlyButton.setSelection(true);
		m_createSelectionOnlyButton.setFont(font);
	}

	/**
	 * Create the button for checking if we should ask if we are going to
	 * overwrite existing files.
	 * 
	 * @param optionsGroup The collection of options.
	 * @param font The font to use.
	 */
	protected void createOverwriteExisting(Group optionsGroup,Font font)
	{
		// overwrite... checkbox
		m_overwriteExistingFilesCheckbox =
			new Button(optionsGroup, SWT.CHECK | SWT.LEFT);
		m_overwriteExistingFilesCheckbox.setText(DwpExportWizardMessages.getString("DwpExportWizardPage.overwriteExisting"));
		m_overwriteExistingFilesCheckbox.setFont(font);
	}

    /* (non-Javadoc)
     * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
     */
    public void handleEvent(Event event)
    {
        //System.out.println("Handle event called: " + event.toString())
		Widget source = event.widget;

		if (source == m_destinationBrowseButton)
			handleDestinationBrowseButtonPressed();

		updatePageCompletion();;
    }
   
    /**
     * Determine whether this page's destination specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the destination widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateDestinationGroup()
    {
        if ((m_destinationNameField.getText() != null) &&
            (! m_destinationNameField.getText().equals("")))
        {
            File file = new File(m_destinationNameField.getText());
            if (file.exists())
            {
                m_pageComplete = true;
                
                if (m_binaryFormatButton.getSelection())
                    setDescription(DwpExportWizardMessages.getString("DwpExportWizardPage.exportBinaryMessage"));
                else if (m_asciiFormatButton.getSelection())
                    setDescription(DwpExportWizardMessages.getString("DwpExportWizardPage.exportAsciiMessage"));
            } else
            {
                m_pageComplete = false;
                
                setErrorMessage(DwpExportWizardMessages.getFormattedString("DwpExportWizardPage.invalidDestination",
                    new Object[] {m_destinationNameField.getText()}));
            }
        } else
        {
            m_pageComplete = false;
            
            setMessage(DwpExportWizardMessages.getString("DwpExportWizardPage.destinationEmpty"));
        }
        
        return m_pageComplete;
    }
    
    /**
     * Handle the destination button being selected.
     */
    protected void handleDestinationBrowseButtonPressed()
    {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setMessage(DwpExportWizardMessages.getString("DwpExportWizardPage.selectDestinationMessage"));
        
        String directory = dialog.open();
        if (directory != null)
            m_destinationNameField.setText(directory);
        
        // Note: the updatePageCompletion() will be called via
        // the ModifyListener callback if the directory was set.
    }

	/**
	 *	Set the current input focus to self's destination entry field.
	 */
	protected void giveFocusToDestination()
	{
	    m_destinationNameField.setFocus();
	}

	/**
	 * Attempts to ensure that the specified directory exists on the local file system.
	 * Answers a boolean indicating success.
	 * 
	 * @param directory The directory to verify.
	 *
	 * @return <b>true</b> will be returned if the directory previously existed,
	 * or if it was successfully created. <b>false</b> will be returned if the
	 * directory was not successfully validated.
	 */
	protected boolean ensureDirectoryExists(File directory)
	{
		if (! directory.exists())
		{
			if (! queryYesNoQuestion(DwpExportWizardMessages.getString("DwpExportWizardPage.createTargetDirectory")))
				return false;

			if (! directory.mkdirs())
			{
				displayErrorDialog(DwpExportWizardMessages.getString("DwpExportWizardPage.directoryCreationError"));
				giveFocusToDestination();
				return false;
			}
		}

		return true;
	}

	/**
	 * If the target for export does not exist then attempt to create it.
	 * Answer a boolean indicating whether the target exists (ie.- if it
	 * either pre-existed or this method was able to create it).
	 *
	 * @return <b>true</b> is returned if the target is valid.
	 * Otherwise <b>false</b> will be returned.
	 */
	protected boolean ensureTargetIsValid(File targetDirectory)
	{
		if (targetDirectory.exists() && !targetDirectory.isDirectory())
		{
			displayErrorDialog(DwpExportWizardMessages.getString("DwpExportWizardPage.directoryExists")); //$NON-NLS-1$
			giveFocusToDestination();
			return false;
		}

		return ensureDirectoryExists(targetDirectory);
	}
	
	/**
	 *	Answer the contents of self's destination specification widget.
	 *
	 *	@return The destination is returned as a <code>String</code>.
	 */
	protected String getDestinationValue()
	{
		return m_destinationNameField.getText().trim();
	}

	/**
	 *	Set the contents of the receivers destination specification widget to
	 *	the passed value
	 */
	protected void setDestinationValue(String value)
	{
	    m_destinationNameField.setText(value);
	}

	/**
	 * Set up and execute the passed Operation. Answer a boolean indicating success.
	 *
	 * @return <b>true</b> will be returned if the operation was successful.
	 * Otherwise <b>false</b> will be returned.
	 */
	protected boolean executeExportOperation(DwpExportOperation op)
	{
		op.setCreateLeadupStructure(
			m_createDirectoryStructureButton.getSelection());
		op.setOverwriteFiles(m_overwriteExistingFilesCheckbox.getSelection());

		try
		{
			getContainer().run(true, true, op);
		} catch (InterruptedException ex)
		{
			return false;
		} catch (InvocationTargetException ex)
		{
			displayErrorDialog(ex.getTargetException());
			return false;
		}

		IStatus status = op.getStatus();
		if (! status.isOK())
		{
			ErrorDialog.openError(getContainer().getShell(),
			    DwpExportWizardMessages.getString("DwpExportWizardPage.exportProblems"),
			    null, // no special message
			    status);
			return false;
		}

		return true;
	}

    /**
     * Finish button was pressed. Try to do the required work now and answer
	 * a boolean indicating success. If false is returned then the wizard will
	 * not close.
     * 
     * @return <b>true</b> is returned if the finish was successfully
     * preformed. Otherwise <b>false</b> will be returned.
     */
    public boolean finish()
    {
		if (! ensureTargetIsValid(new File(getDestinationValue())))
			return false;

		List resourcesToExport = getWhiteCheckedResources();

		// Save dirty editors if possible but do not stop if not all are saved
		saveDirtyEditors();
		// about to invoke the operation so save our state
		saveWidgetValues();

		if (resourcesToExport.size() > 0)
		{
		    DwpExportOperation op = new DwpExportOperation(
					null,
					resourcesToExport,
					getDestinationValue(),
					this);
		    if (m_asciiFormatButton.getSelection())
		        op.setExportType(DwpExporter.ASCII_FILE_FORMAT);
		    else if (m_binaryFormatButton.getSelection())
		        op.setExportType(DwpExporter.BINARY_FILE_FORMAT);
			return executeExportOperation(op);
		}

		MessageDialog.openInformation(getContainer().getShell(),
		    DwpExportWizardMessages.getString("DwpExportWizardPage.information"),
		    DwpExportWizardMessages.getString("DwpExportWizardPage.noneSelected"));

		return false;
    }
    
	/**
	 *	Hook method for saving widget values for restoration by the next instance
	 *	of this class.
	 */
	protected void internalSaveWidgetValues()
	{
	    // Retrieve dialog settings.
		IDialogSettings settings = getDialogSettings();
		if (settings != null)
		{
			// Update directory names history.
			String[] directoryNames =
				settings.getArray(STORE_DESTINATION_NAMES_ID);
			if (directoryNames == null)
				directoryNames = new String[0];

			directoryNames =
				addToHistory(directoryNames, getDestinationValue());
			settings.put(STORE_DESTINATION_NAMES_ID, directoryNames);

			// Update options.
			settings.put(
				STORE_OVERWRITE_EXISTING_FILES_ID,
				m_overwriteExistingFilesCheckbox.getSelection());

			settings.put(
				STORE_CREATE_STRUCTURE_ID,
				m_createDirectoryStructureButton.getSelection());

			settings.put(
			    STORE_EXPORT_ASCII_FORMAT_ID,
			    m_asciiFormatButton.getSelection());
		}
	}
	
	/**
	 *	Hook method for restoring widget values to the values that they held
	 *	last time this wizard was used to completion.
	 */
	protected void restoreWidgetValues()
	{
	    // Retrieve dialog settings.
		IDialogSettings settings = getDialogSettings();
		if (settings != null)
		{
			String[] directoryNames =
				settings.getArray(STORE_DESTINATION_NAMES_ID);
			if (directoryNames == null)
				return; // ie.- no settings stored

			// Restore destination.
			setDestinationValue(directoryNames[0]);
			for (int i = 0; i < directoryNames.length; i++)
				addDestinationItem(directoryNames[i]);

			// Restore options.
			m_overwriteExistingFilesCheckbox.setSelection(
				settings.getBoolean(STORE_OVERWRITE_EXISTING_FILES_ID));

			boolean createDirectories =
				settings.getBoolean(STORE_CREATE_STRUCTURE_ID);
			m_createDirectoryStructureButton.setSelection(createDirectories);
			m_createSelectionOnlyButton.setSelection(!createDirectories);
			
			boolean asciiFormat =
			    settings.getBoolean(STORE_EXPORT_ASCII_FORMAT_ID);
			m_asciiFormatButton.setSelection(asciiFormat);
			m_binaryFormatButton.setSelection(!asciiFormat);
		}
	}

}
