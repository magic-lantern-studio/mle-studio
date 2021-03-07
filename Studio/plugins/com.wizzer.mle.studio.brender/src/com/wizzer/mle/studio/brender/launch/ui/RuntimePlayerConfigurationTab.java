/*
 * RuntimePlayerConfigurationTab.java
 * Created on Nov 1, 2006
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
package com.wizzer.mle.studio.brender.launch.ui;

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
//import org.eclipse.jdt.core.IJavaElement;
//import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.cdt.core.model.ICProject;
import org.eclipse.cdt.core.model.ICElement;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.brender.BRenderLog;
import com.wizzer.mle.studio.brender.Activator;
import com.wizzer.mle.studio.brender.launch.IRuntimePlayerLaunchConfigurationConstants;
import com.wizzer.mle.studio.dpp.launch.ui.AbstractRuntimePlayerConfigurationTab;

/**
 * This class implements a tab for the Magic Lantern
 * Runtime Player launch configuration.
 * 
 * @author Mark S. Millard
 */
public class RuntimePlayerConfigurationTab
	extends AbstractRuntimePlayerConfigurationTab
{
	/** The name of the configruation tab. */
	public static final String RUNTIME_PLAYER_CONFIG_NAME = "Runtime Player Configuration";
	
	// The project associated with the launch configuration.
	private IProject m_project = null;
	// The Text widget for the title executable.
	private Text m_titleText = null;
	// The Button widget for the title executable.
	private Button m_titleButton = null;
	// The Text widget for the digital playprint.
	private Text m_dppText = null;
	// The Button widget for the digital playprint.
	private Button m_dppButton = null;
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
	public RuntimePlayerConfigurationTab(String mode)
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
		
		Label titleLabel = new Label(composite,SWT.NULL);
		titleLabel.setText("Title Executable:");
		
		m_titleText = new Text(composite,SWT.BORDER);
		GridData titleTextData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		titleTextData.grabExcessHorizontalSpace = true;
		m_titleText.setLayoutData(titleTextData);
		if (m_project != null)
			m_titleText.setText(m_project.getName() + ".exe");
		m_titleText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent evt)
			{
				handleTitleTextModified();
			}
		});

		m_titleButton = new Button(composite,SWT.NONE);
		m_titleButton.setText("&Browse...");
		m_titleButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleTitleButtonSelected();
			}
		});
		
		Label dppLabel = new Label(composite,SWT.NULL);
		dppLabel.setText("Digital Playprint:");
		
		m_dppText = new Text(composite,SWT.BORDER);
		GridData dppTextData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		dppTextData.grabExcessHorizontalSpace = true;
		m_dppText.setLayoutData(dppTextData);
		if (m_project != null)
			m_dppText.setText(m_project.getName() + ".dpp");
		m_dppText.addModifyListener(new ModifyListener()
		{
			public void modifyText(ModifyEvent evt)
			{
				handleDppTextModified();
			}
		});
		
		m_dppButton = new Button(composite,SWT.NONE);
		m_dppButton.setText("&Browse...");
		m_dppButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleDppButtonSelected();
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
					
					configuration.setAttribute(
						IRuntimePlayerLaunchConfigurationConstants.ATTR_RUNTIME_EXECUTABLE,
						m_project.getLocation().toOSString() + "\\src\\Release\\" + m_project.getName() + ".exe");
					IResource resource = findPlayprint(new String(m_project.getName() + ".dpp"),
						m_project);
					if (resource != null)
					{
						configuration.setAttribute(
							IRuntimePlayerLaunchConfigurationConstants.ATTR_DIGITAL_PLAYPRINT,
							resource.getLocation().toOSString());
					} else
					{
						configuration.setAttribute(
							IRuntimePlayerLaunchConfigurationConstants.ATTR_DIGITAL_PLAYPRINT,
							"");						
					}
					configuration.setAttribute(
						IRuntimePlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
						m_project.getLocation().toOSString());
				} else
				{
					// We set empty attributes for project & main type so that when one config is
					// compared to another, the existence of empty attributes doesn't cause an
					// incorrect result (the performApply() method can result in empty values
					// for these attributes being set on a config if there is nothing in the
					// corresponding text boxes)
					configuration.setAttribute(
						IRuntimePlayerLaunchConfigurationConstants.ATTR_PROJECT_NAME, "");
					configuration.setAttribute(
						IRuntimePlayerLaunchConfigurationConstants.ATTR_PROJECT, "");
					configuration.setAttribute(
							IRuntimePlayerLaunchConfigurationConstants.ATTR_RUNTIME_EXECUTABLE, "");
					configuration.setAttribute(
						IRuntimePlayerLaunchConfigurationConstants.ATTR_DIGITAL_PLAYPRINT, "");
					configuration.setAttribute(
						IRuntimePlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "");
				}
			}
		} catch (CoreException ex)
		{
			BRenderLog.logError(ex,"Unable to set defaults on Runtime Player launch configuration.");
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
			String title = configuration.getAttribute(
				IRuntimePlayerLaunchConfigurationConstants.ATTR_RUNTIME_EXECUTABLE, "");
			m_titleText.setText(title);

			String digitalPlayprint = configuration.getAttribute(
				IRuntimePlayerLaunchConfigurationConstants.ATTR_DIGITAL_PLAYPRINT, "");
			m_dppText.setText(digitalPlayprint);
			
			String workingDir = configuration.getAttribute(
				IRuntimePlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY, "");
			m_workingDirText.setText(workingDir);
		} catch (CoreException ex)
		{
			BRenderLog.logError(ex,"Unable to initialize Runtime Player launch configuration.");
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
			IRuntimePlayerLaunchConfigurationConstants.ATTR_RUNTIME_EXECUTABLE,
			m_titleText.getText());
		configuration.setAttribute(
			IRuntimePlayerLaunchConfigurationConstants.ATTR_DIGITAL_PLAYPRINT,
			m_dppText.getText());
		configuration.setAttribute(
			IRuntimePlayerLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
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
	
	// Check the project hierarchy for a Digital Playprint matching the specified
	// name. If no resource is found matching the specified name, null will be
	// returned.
	private IResource findPlayprint(String dppName,IContainer conatiner)
		throws CoreException
	{
		IResource dpp = null;
		
		dpp = conatiner.findMember(dppName);
		if (dpp != null)
			return dpp;
		
		IResource[] members = conatiner.members();
		if ((members != null) && (members.length > 0))
		{
			for (int i = 0; i < members.length; i++)
			{
				if (members[i] instanceof IContainer)
				{
					dpp = findPlayprint(dppName,(IContainer)members[i]);
					if (dpp != null)
						return dpp;
				}
			}
		}
		
		return dpp;
	}

	/**
	 * Get the name of the configuration tab.
	 * 
	 * @return The name of the configuration tab is returned as a <code>String</code>.
	 */
	public String getName()
	{
		return RUNTIME_PLAYER_CONFIG_NAME;
	}

	/**
	 * Determine whether the information in the tab is valid.
	 * 
	 * @param configuration The launch configuration.
	 */
	public boolean isValid(ILaunchConfiguration configuration)
	{
		boolean retValue = super.isValid(configuration);
		if ((m_titleText.getText() == null) || (m_titleText.getText().equals("")))
		{
			setErrorMessage("Specify a title executable.");
			retValue = false;
		} else if (! validateTitle(m_titleText.getText()))
		{
			setErrorMessage("Invalid title executable.");
			retValue = false;
		} if ((m_dppText.getText() == null) || (m_dppText.getText().equals("")))
		{
			setErrorMessage("Specify a Digital Playprint file.");
			retValue = false;
		} else if (! validateDpp(m_dppText.getText()))
		{
			setErrorMessage("Invalid Digital Playprint file.");
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

	// Validate that the file is an executable file.
	private boolean validateTitle(String filepath)
	{
		boolean retValue = true;
		
		// Determine that the file exists.
		File path = new File(filepath);
		if (! path.exists())
			retValue = false;

		// XXX - Perform format validation here. Make sure that the file
		// really is executable.
		
		return retValue;
	}
	
	// Post a dialog message for an invalid title.
	private void postInvalidTitleDialog(String filename)
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();

		new MessageDialog(display.getActiveShell(),
                "Runtime Player Configuration Information",null,
                filename + " is not a valid title executable.",
                MessageDialog.INFORMATION,new String[] {"Ok"},0).open();
	}

	// Validate that the file is a Digital Playprint file.
	private boolean validateDpp(String filepath)
	{
		boolean retValue = true;
		
		// Determine that the file exists.
		File path = new File(filepath);
		if (! path.exists())
			retValue = false;

		// XXX - Perform format validation here. Make sure that the file
		// really is a Digital Playprint.
		
		return retValue;
	}
	
	// Post a dialog message for an invalid DPP.
	private void postInvalidDppDialog(String filename)
	{
		final Display display = PlatformUI.getWorkbench().getDisplay();

		new MessageDialog(display.getActiveShell(),
                "Runtime Player Configuration Information",null,
                filename + " is not a valid Digital Playprint file.",
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
                "Runtime Player Configuration Information",null,
                path + " is not a valid directory.",
                MessageDialog.INFORMATION,new String[] {"Ok"},0).open();
	}
	
	/**
	 *  Handle location of title executable.
	 */
	protected void handleTitleTextModified()
	{
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Handle the user's selection for finding a title executable
	 * file.
	 * <p>
	 * Show a dialog that lets the user select a title executable file.
	 * </p>
	 */
	protected void handleTitleButtonSelected()
	{
		// Provide a dialog by which the user can select the title executable.
        FileDialog fileChooser = new FileDialog(getControl().getShell());
        String filepath =  fileChooser.open();
        if (filepath != null)
        {        	
        	// Validate that the file is an executable file.
	        if (validateTitle(filepath))
	        {
	        	m_titleText.setText(filepath);
	        } else
	        	postInvalidTitleDialog(filepath);
        }
	}

	/**
	 *  Handle location of Digital Playprint file.
	 */
	protected void handleDppTextModified()
	{
		updateLaunchConfigurationDialog();
	}
	
	/**
	 * Handle the user's selection for finding a Digital Playprint
	 * file.
	 * <p>
	 * Show a dialog that lets the user select an Digital Playprint file.
	 * </p>
	 */
	protected void handleDppButtonSelected()
	{
		// Provide a dialog by which the user can select the Digital Playprint file.
        FileDialog fileChooser = new FileDialog(getControl().getShell());
        String filepath =  fileChooser.open();
        if (filepath != null)
        {        	
        	// Validate that the file is a Digital Playprint file.
	        if (validateDpp(filepath))
	        {
	        	m_dppText.setText(filepath);
	        } else
	        	postInvalidDppDialog(filepath);
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
        	// Validate that the file is a Digital Playprint file.
	        if (validateDir(dirpath))
	        {
	        	m_workingDirText.setText(dirpath);
	        } else
	        	postInvalidDirDialog(dirpath);
        }
	}
	
	/**
	 * Returns the current project context from which to initialize
	 * default settings, or <code>null</code> if none.
	 * 
	 * @return A project context is returned.
	 */
	protected IProject getContext()
	{
		IWorkbenchPage page = Activator.getActivePage();
		if (page != null)
		{
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection)
			{
				IStructuredSelection ss = (IStructuredSelection)selection;
				if (! ss.isEmpty())
				{
					Object obj = ss.getFirstElement();
					if (obj instanceof IProject)
					{
						return (IProject)obj;
					}
/*
					if (obj instanceof IJavaElement)
					{
						IJavaElement element = (IJavaElement)obj;
						IJavaProject jproject = element.getJavaProject();
						IProject project = jproject.getProject();
						return project;
					}
*/
					if (obj instanceof ICElement)
					{
						ICElement element = (ICElement)obj;
						ICProject cproject = element.getCProject();
						IProject project = cproject.getProject();
						return project;
					}
					if (obj instanceof IResource)
					{
						IProject project = ((IResource)obj).getProject();
						return project;
					}
				}
			}
			
			IEditorPart part = page.getActiveEditor();
			if (part != null)
			{
				IEditorInput input = part.getEditorInput();
				return (IProject) input.getAdapter(IProject.class);
			}
		}
		
		return null;
	}

}
