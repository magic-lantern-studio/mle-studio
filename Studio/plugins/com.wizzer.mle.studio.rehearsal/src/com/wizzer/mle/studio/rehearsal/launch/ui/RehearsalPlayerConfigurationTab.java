/*
 * RehearsalPlayerConfigurationTab.java
 * Created on Jun 6, 2006
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
package com.wizzer.mle.studio.rehearsal.launch.ui;

// Import standard Java classes.
import java.io.File;

// Import Eclipse classes.
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.rehearsal.RehearsalLog;
import com.wizzer.mle.studio.rehearsal.launch.IRehearsalPlayerLaunchConfigurationConstants;

/**
 * This class implements a tab for the Magic Lantern
 * Rehearsal Player launch configuration.
 * 
 * @author Mark S. Millard
 */
public class RehearsalPlayerConfigurationTab
	extends AbstractRehearsalPlayerConfigurationTab
{
	/** The name of the configruation tab. */
	public static final String REHEARSAL_PLAYER_CONFIG_NAME = "Rehearsal Player Configuration";
	
	// The project associated with the launch configuration.
	private IProject m_project = null;
	// The Text widget for the digital workprint.
	private Text m_dwpText = null;
	// The Button widget for the digital workprint.
	private Button m_dwpButton = null;
	// The Text widget for the working directory.
	private Text m_workingDirText = null;
	// The Button widget for the working directory.
	private Button m_workingDirButton = null;
	
	/**
	 * A constructor that initializes the context of the project.
	 * 
	 * @param mode The launch configuration mode. Valid values include:
	 * <ul>
	 * <li>run</li>
	 * <li>debug</li>
	 * </ul>
	 */
	public RehearsalPlayerConfigurationTab(String mode)
	{
		super(mode);
		m_project = getContext();
		setDirty(false);
	}

	/**
	 * Creates the top level control for this launch configuration tab under
	 * the given parent composite. This method is called once on tab creation,
	 * after <code>setLaunchConfigurationDialog</code> is called.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 */
	public void createControl(Composite parent)
	{
		Composite composite = new Composite(parent,SWT.NULL);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		composite.setLayout(layout);
		
		Label dwpLabel = new Label(composite,SWT.NULL);
		dwpLabel.setText("Digital Workprint:");
		
		m_dwpText = new Text(composite,SWT.BORDER);
		GridData dwpTextData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		dwpTextData.grabExcessHorizontalSpace = true;
		m_dwpText.setLayoutData(dwpTextData);
		if (m_project != null)
			m_dwpText.setText(m_project.getName() + ".dwp");
		m_dwpText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent evt)
			{
				handleDwpTextModified();
			}
		});
		
		m_dwpButton = new Button(composite,SWT.NONE);
		m_dwpButton.setText("&Browse...");
		m_dwpButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleDwpButtonSelected();
			}
		});
		
		Label workingDirLabel = new Label(composite,SWT.NULL);
		workingDirLabel.setText("Working Directory:");
		
		m_workingDirText = new Text(composite,SWT.BORDER);
		GridData workingDirData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		workingDirData.grabExcessHorizontalSpace = true;
		m_workingDirText.setLayoutData(workingDirData);
		if (m_project != null)
		{
			m_workingDirText.setText(m_project.getLocation().toOSString());
		} else
		{
			m_workingDirText.setText("!!!Unknown!!!");
		}
		m_workingDirText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent evt)
			{
				handleWorkingDirTextModified();
			}
		});
		
		m_workingDirButton = new Button(composite,SWT.NONE);
		m_workingDirButton.setText("&Browse...");
		m_workingDirButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleWorkingDirButtonSelected();
			}
		});
		
		setControl(composite);
	}

	/**
	 * Initializes the given launch configuration with default values for this tab.
	 * This method is called when a new launch configuration is created such that
	 * the configuration can be initialized with meaningful values.
	 * This method may be called before this tab's control is created. 
	 * 
	 * @param configuration The launch configuration.
	 */
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
	{
		try
		{
			if (m_mode.equals(ILaunchManager.RUN_MODE))
			{
				if (m_project != null)
				{
					initializeProject(m_project, configuration);
					
					IResource resource = findWorkprint(new String(m_project.getName() + ".dwp"),
						m_project);
					if (resource != null)
					{
						configuration.setAttribute(
							IRehearsalPlayerLaunchConfigurationConstants.ATTR_DIGITAL_WORKPRINT,
							resource.getLocation().toOSString());
					} else
					{
						configuration.setAttribute(
							IRehearsalPlayerLaunchConfigurationConstants.ATTR_DIGITAL_WORKPRINT,
							"");						
					}
					configuration.setAttribute(
						IRehearsalPlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
						m_project.getLocation().toOSString());
				} else
				{
					// We set empty attributes for project & main type so that when one config is
					// compared to another, the existence of empty attributes doesn't cause an
					// incorrect result (the performApply() method can result in empty values
					// for these attributes being set on a config if there is nothing in the
					// corresponding text boxes)
					configuration.setAttribute(
						IRehearsalPlayerLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
					configuration.setAttribute(
						IRehearsalPlayerLaunchConfigurationConstants.ATTR_PROJECT, "");
					configuration.setAttribute(
						IRehearsalPlayerLaunchConfigurationConstants.ATTR_DIGITAL_WORKPRINT, "");
					configuration.setAttribute(
						IRehearsalPlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "");
				}
			}
		} catch (CoreException ex)
		{
			RehearsalLog.logError(ex,"Unable to set defaults on Rehearsal Player launch configuration.");
		}
		
		// Set the name of the launch configuration.
		if (m_mode.equals(ILaunchManager.RUN_MODE))
			if (m_project == null)
				initializeName(configuration,null);
			else
				initializeName(configuration,m_project.getName());
	}

	/**
	 * Initializes this tab's controls with values from the given launch configuration.
	 * This method is called when a configuration is selected to view or edit,
	 * after this tab's control has been created.
	 * 
	 * @param configuration The launch configuration.
	 */
	public void initializeFrom(ILaunchConfiguration configuration)
	{
		try
		{
			String digitalWorkprint = configuration.getAttribute(
				IRehearsalPlayerLaunchConfigurationConstants.ATTR_DIGITAL_WORKPRINT, "");
			m_dwpText.setText(digitalWorkprint);
			
			String workingDir = configuration.getAttribute(
				IRehearsalPlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "");
			m_workingDirText.setText(workingDir);
		} catch (CoreException ex)
		{
			RehearsalLog.logError(ex,"Unable to initialize Rehearsal Player launch configuration.");
		}

	}

	/**
	 * Copies values from this tab into the given launch configuration.
	 * 
	 * @param configuration The launch configuration.
	 */
	public void performApply(ILaunchConfigurationWorkingCopy configuration)
	{
		configuration.setAttribute(
			IRehearsalPlayerLaunchConfigurationConstants.ATTR_DIGITAL_WORKPRINT,
			m_dwpText.getText());
		configuration.setAttribute(
			IRehearsalPlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
			m_workingDirText.getText());
	}
	
    // Initialize the name of the configuration.
	private void initializeName(ILaunchConfigurationWorkingCopy config, String name)
	{
		if (name == null) {
			name = "";
		}
		if (name.length() > 0) {
			int index = name.lastIndexOf('.');
			if (index > 0) {
				name = name.substring(index + 1);
			}
			name = getLaunchConfigurationDialog().generateName(name);
			config.rename(name);
		}
	}
	
	// Check the project hierarchy for a Digital Workprint matching the specified
	// name. If no resource is found matching the specified name, null will be
	// returned.
	private IResource findWorkprint(String dwpName,IContainer conatiner)
		throws CoreException
	{
		IResource dwp = null;
		
		dwp = conatiner.findMember(dwpName);
		if (dwp != null)
			return dwp;
		
		IResource[] members = conatiner.members();
		if ((members != null) && (members.length > 0))
		{
			for (int i = 0; i < members.length; i++)
			{
				if (members[i] instanceof IContainer)
				{
					dwp = findWorkprint(dwpName,(IContainer)members[i]);
					if (dwp != null)
						return dwp;
				}
			}
		}
		
		return dwp;
	}

	/**
	 * Get the name of the configuration tab.
	 * 
	 * @return The name of the configuration tab is returned as a <code>String</code>.
	 */
	public String getName()
	{
		return REHEARSAL_PLAYER_CONFIG_NAME;
	}

	/**
	 * Determine whether the information in the tab is valid.
	 * 
	 * @param configuration The launch configuration.
	 */
	public boolean isValid(ILaunchConfiguration configuration)
	{
		boolean retValue = super.isValid(configuration);
		if ((m_dwpText.getText() == null) || (m_dwpText.getText().equals("")))
		{
			setErrorMessage("Specify a Digital Workprint file.");
			retValue = false;
		} else if (! validateDwp(m_dwpText.getText()))
		{
			setErrorMessage("Invalid Digital Workprint file.");
			retValue = false;
		} else if ((m_workingDirText.getText() == null) || (m_workingDirText.getText().equals("")))
		{
			setErrorMessage("Specify a working directory.");
			retValue = false;
		} else if (! validateDir(m_workingDirText.getText()))
		{
			setErrorMessage("Invalid working directory.");
			retValue = false;
		}

		if (retValue == true)
			setErrorMessage(null);

		return retValue;
	}

	// Validate that the file is a Digital Workprint file.
	private boolean validateDwp(String filepath)
	{
		boolean retValue = true;
		
		// Determine that the file exists.
		File path = new File(filepath);
		if (! path.exists())
			retValue = false;

		// XXX - Perform format validation here. Make sure that the file
		// really is a Digital Workprint.
		
		return retValue;
	}
	
	// Post a dialog message for an invalid DWP.
	private void postInvalidDwpDialog(String filename)
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();

		new MessageDialog(display.getActiveShell(),
                "Rehearsal Player Configuration Information",null,
                filename + " is not a valid Digital Workprint file.",
                MessageDialog.INFORMATION,new String[] {"Ok"},0).open();
	}
	
	// Validate that the directory is valid.
	private boolean validateDir(String dirpath)
	{
		File path = new File(dirpath);
		if (path.exists())
			return true;
		else
			return false;
	}
	
	// Post a dialog message for an invalid directory.
	private void postInvalidDirDialog(String path)
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();

		new MessageDialog(display.getActiveShell(),
                "Rehearsal Player Configuration Information",null,
                path + " is not a valid directory.",
                MessageDialog.INFORMATION,new String[] {"Ok"},0).open();
	}
	
	/**
	 *  Handle location of Digital Workprint file.
	 */
	protected void handleDwpTextModified()
	{
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Handle the user's selection for finding a Digital Workprint
	 * file.
	 * <p>
	 * Show a dialog that lets the user select an Digital Workprint file.
	 * </p>
	 */
	protected void handleDwpButtonSelected()
	{
		// Provide a dialog by which the user can select the Digital Workprint file.
        FileDialog fileChooser = new FileDialog(getControl().getShell());
        String filepath =  fileChooser.open();
        if (filepath != null)
        {        	
        	// Validate that the file is a Digital Workprint file.
	        if (validateDwp(filepath))
	        {
	        	m_dwpText.setText(filepath);
	        } else
	        	postInvalidDwpDialog(filepath);
        }
	}

	/**
	 *  Handle location of working directory.
	 */
	protected void handleWorkingDirTextModified()
	{
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Handle the user's selection for specifying the working
	 * directory.
	 * <p>
	 * Show a dialog that lets the user select a directory.
	 * </p>
	 */
	protected void handleWorkingDirButtonSelected()
	{
		// Provide a dialog by which the user can select the directory.
        DirectoryDialog dirChooser = new DirectoryDialog(getControl().getShell());
        String dirpath =  dirChooser.open();
        if (dirpath != null)
        {        	
        	// Validate that the file is a Digital Workprint file.
	        if (validateDir(dirpath))
	        {
	        	m_workingDirText.setText(dirpath);
	        } else
	        	postInvalidDirDialog(dirpath);
        }
	}
}
