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
package com.wizzer.mle.studio.project;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.zip.ZipFile;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.MlePlugin;

public class MleTemplatePage extends WizardPage
{
    /** No application template is selected. */
    public static final int NO_TEMPLATE = 0;
    /** The simple runtime template is selected. */
    public static final int SIMPLE_RUNTIME_TEMPLATE = 1;
    /** The HelloWorld runtime template is selected. */
    public static final int HELLOWORLD_RUNTIME_TEMPLATE = 2;
    /** The ImageViewer runtime template is selected. */
    public static final int IMAGEVIEWER_RUNTIME_TEMPLATE = 3;

    // Flag indicating whether template specified.
    private boolean m_useTemplate = false;
    // Type of template selected.
    private int m_templateSelected = NO_TEMPLATE;
    
	TabFolder m_folder = null;
	private Label m_comment = null;
	TabItem m_templateTab = null;
	
	private String TEMPLATE_CONFIG_COMMENT = "Configure the application template options.";

	/**
     * Creates a new wizard page with the given name, and with no title or image.
     * 
     * @param pageName The name of the page.
     */
    public MleTemplatePage(String pageName)
    {
        super(pageName);
    }

	public void createControl(Composite parent)
	{
		// Create a new composite.
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		// Add a widget to offer hints.
		m_comment = new Label(composite, SWT.NONE);
		m_comment.setText(TEMPLATE_CONFIG_COMMENT);
		
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
				if (item.equals(m_templateTab))
				    m_comment.setText(TEMPLATE_CONFIG_COMMENT);
			}
		});
		// Create a tab for the template configuration.
		m_templateTab = new TabItem(m_folder, SWT.NONE);
		m_templateTab.setText("Templates");

		// Configure the templateTab layout.
		Composite templateTabComposite = new Composite(m_folder, SWT.NONE);
		templateTabComposite.setLayout(new GridLayout());
		templateTabComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		m_templateTab.setControl(templateTabComposite);

		// Create the Project Templates layout.
		createTemplateGroup(templateTabComposite);

		// Make composite the main control point.
		setControl(composite);
	}

	/*
	 * Create the GUI elements for the template configuration.
	 * 
	 * @param parent The parent <code>Composite</code> widget.
	 * 
	 * @return A <code>Group</code> widget is returned.
	 */
    private Group createTemplateGroup(Composite parent)
    {
		// Create a new Group.
		Group templateGroup = new Group(parent, SWT.RESIZE);
		
		// Configure Group layout.
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		templateGroup.setLayout(layout);
		templateGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		templateGroup.setText("Templates");
		
		// Add a button for specifying no template.
		Button noTemplate = new Button(templateGroup,SWT.RADIO);
		noTemplate.setText("None");
		noTemplate.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleNoTemplateButtonSelected();
			}
		});
		
		// Add a button for specifying the simple runtime template.
		Button simpleTemplate = new Button(templateGroup,SWT.RADIO);
		simpleTemplate.setText("Simple");
		simpleTemplate.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				handleSimpleTemplateButtonSelected();
			}
		});
		simpleTemplate.setSelection(true);
		m_useTemplate = true;
		m_templateSelected = SIMPLE_RUNTIME_TEMPLATE;

		// Add a button for specifying the helloworld runtime template.
		Button helloWorldTemplate = new Button(templateGroup,SWT.RADIO);
		helloWorldTemplate.setText("Hello World");
		helloWorldTemplate.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				helloWorldTemplateButtonSelected();
			}
		});
		
		// Add a button for specifying the imageViewer runtime template.
		Button imageViewerTemplate = new Button(templateGroup,SWT.RADIO);
		imageViewerTemplate.setText("Image Viewer");
		imageViewerTemplate.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent evt)
			{
				imageViewerTemplateButtonSelected();
			}
		});

    	return templateGroup;
    }

	/**
	 *  Handle whether to generate a template or not.
	 */
	protected void handleNoTemplateButtonSelected()
	{
		//System.out.println("No template button toggled.");
		m_templateSelected = NO_TEMPLATE;
		m_useTemplate = false;
	}
	
	/**
	 *  Handle whether to generate a simple runtime template or not.
	 */
	protected void handleSimpleTemplateButtonSelected()
	{
		//System.out.println("Simple runtime template button toggled.");
		m_templateSelected = SIMPLE_RUNTIME_TEMPLATE;
		m_useTemplate = true;
	}
	
	/**
	 *  Handle whether to generate a hello world runtime template or not.
	 */
	protected void helloWorldTemplateButtonSelected()
	{
		//System.out.println("Hello World runtime template button toggled.");
		m_templateSelected = HELLOWORLD_RUNTIME_TEMPLATE;
		m_useTemplate = true;
	}

	/**
	 *  Handle whether to generate a hello world runtime template or not.
	 */
	protected void imageViewerTemplateButtonSelected()
	{
		//System.out.println("Image Viewer runtime template button toggled.");
		m_templateSelected = IMAGEVIEWER_RUNTIME_TEMPLATE;
		m_useTemplate = true;
	}

	/**
	 * Determine whether to use a specified template.
	 * 
	 * @return <b>true</b> will be returned if a template is to be used.
	 * <b>false</b> will be returned if no template has been specified.
	 */
	public boolean useTemplate()
	{
	    return m_useTemplate;
	}
	
	/**
	 * Get which application template is currently selected.
	 * 
	 * @return An integer value is returned indicating the currently
	 * selected template. Valid values include:
	 * <ul>
	 * <li>NO_TEMPLATE_SELECTED</li>
	 * <li>SIMPLE_TEMPLATE_SELECTED</li>
	 * <li>HELLOWORLD_TEMPLATE_SELECTED</li>
	 * <li>IMAGEVIEWER_TEMPLATE_SELECTED</li>
	 * </ul>
	 */
	public int getTemplateType()
	{
		return m_templateSelected;
	}
	
	/**
	 *  Unzip the Zip file.
	 *  
	 * @param project The project to import to.
	 * @param element A configuration element from the plug-in description.
	 * @param monitor The progress monitor.
	 * @param overwriteQuery A dialog query.
	 * 
	 * @throws InvocationTargetException
	 * @throws InterruptedException
	 */
	public void doImports(IProject project, IConfigurationElement element, IProgressMonitor monitor, IOverwriteQuery overwriteQuery)
	    throws InvocationTargetException, InterruptedException
	{
		try {
			IPath destPath;

			// Get the type of import.
			String type = element.getAttribute("type");
			if (type == null)
			{
				MleLog.logWarning("<import/> descriptor: type missing.");
				return;
			}

			if ((type.equals("SimpleJavaTemplate") &&
				(getTemplateType() == SIMPLE_RUNTIME_TEMPLATE)))
			{
				// Set up the destination directory.
				String name = element.getAttribute("dest");
				if (name == null || name.length() == 0) {
					destPath= project.getFullPath();
				} else {
					IFolder folder = project.getFolder(name);
					if (! folder.exists()) {
						folder.create(true, true, null);
					}
					destPath= folder.getFullPath();
				}
				
				// Determine the source of the Zip file.
				String importPath = element.getAttribute("src");
				if (importPath == null) {
					importPath = "";
					MleLog.logWarning("<import/> descriptor: import missing.");
					return;
				}
			    
			    // Retrieve the Zip file and import the resources.
				ZipFile zipFile = getZipFileFromPluginDir(importPath);
				importFilesFromZip(zipFile, destPath, new SubProgressMonitor(monitor, 1), overwriteQuery);
			}
		} catch (CoreException e)
		{
			throw new InvocationTargetException(e);
		}
	}
	
	// Retrieve the Zip file relative to the specified path.
	private ZipFile getZipFileFromPluginDir(String pluginRelativePath)
	    throws CoreException
	{
		try {
			URL starterURL = new URL(MlePlugin.getDefault().getBundle().getEntry("/"), pluginRelativePath);
			return new ZipFile(Platform.asLocalURL(starterURL).getFile());
		} catch (IOException e) {
			String message= pluginRelativePath + ": " + e.getMessage();
			Status status= new Status(IStatus.ERROR, MlePlugin.getID(), IStatus.ERROR, message, e);
			throw new CoreException(status);
		}
	}
	
	// Import resources from the specified Zip file
	private void importFilesFromZip(ZipFile srcZipFile, IPath destPath, IProgressMonitor monitor, IOverwriteQuery overwriteQuery)
	    throws InvocationTargetException, InterruptedException
	{		
		ZipFileStructureProvider structureProvider = new ZipFileStructureProvider(srcZipFile);
		ImportOperation op = new ImportOperation(destPath, structureProvider.getRoot(), structureProvider, overwriteQuery);
		op.run(monitor);
	}

	public void performFinish(IProgressMonitor monitor) throws CoreException, InterruptedException
	{
		// Do nothing for now.
	}
}
