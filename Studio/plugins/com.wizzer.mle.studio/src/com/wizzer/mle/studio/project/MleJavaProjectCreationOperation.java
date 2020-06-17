/**
 * MleJavaProjectCreationOperation.java
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
package com.wizzer.mle.studio.project;

// Import standard Java packages.
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Vector;
import java.util.ArrayList;
import java.util.zip.ZipFile;

// Import Eclipse packages.
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;

import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

// Import Digital Playprint header files.

// Import Magic Lantern classes.
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.MlePlugin;
import com.wizzer.mle.codegen.IDwpConfiguration;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.nature.MasteringNature;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GendppPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GengroupPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenscenePropertyManager;
import com.wizzer.mle.studio.dpp.properties.GentablesPropertyManager;
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpProjectManager;
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.domain.DwpTableHandler;
import com.wizzer.mle.studio.dwp.domain.DwpTableHandlerFactory;
import com.wizzer.mle.studio.dwp.domain.IDwpTableHandler;
import com.wizzer.mle.studio.dwp.editor.DwpResource;
import com.wizzer.mle.studio.dwp.java.MleSimpleDwp;


/**
 * This class is used to perform the actual creation of a new Magic Lantern
 * Java project.
 * 
 * @author Mark S. Millard
 */
public class MleJavaProjectCreationOperation extends MleTargetCreationOperation
{
    public static final String MASTERING_NATURE_ID = "com.wizzer.mle.studio.dpp.MasteringNature";

	// The resource to open.
	private ArrayList m_elementsToOpen;
	// The main project page;
	private NewJavaProjectWizardPageOne m_projectPage;
	// The MLE Target configuration page.
	private MleTargetConfigurationPage m_configPage;
	// The MLE template page.
	private MleTemplatePage m_templatePage;
	// The Java capability configuration page.
	private MleJavaCapabilityConfigurationPage m_javaPage;
	// Dialog handler for query on overwrite.
	private IOverwriteQuery m_overwriteQuery;
	
	/**
	 * Constructor for <code>MleJavaProjectCreationOperation</code>
	 * 
	 * @param projectPage The main project creation page.
	 * @param configPage The Magic Lantern project configuration page.
	 * @param templatePage The Magic Lantern template creation page.
	 * @param javaPage The Java capability configuration page.
	 * @param The dialog handler for query on overwrite.
	 */
	public MleJavaProjectCreationOperation(
	  NewJavaProjectWizardPageOne projectPage,
	  MleTargetConfigurationPage configPage,
	  MleTemplatePage templatePage,
	  MleJavaCapabilityConfigurationPage javaPage,
	  IOverwriteQuery overwriteQuery)
	{
		super();
		m_elementsToOpen = new ArrayList();
		m_projectPage = projectPage;
		m_configPage = configPage;
		m_templatePage = templatePage;
		m_javaPage = javaPage;
		m_overwriteQuery = overwriteQuery;
	}
	
	/**
	 * Runs this operation.
	 * <p>
	 * Progress is reported to the given progress monitor. This method is usually
	 * invoked by the <code>MleProjectCreationWizard</code>, which supplies the
	 * progress monitor. A request to cancel the operation is honored and acknowledged
	 * by throwing <code>InterruptedException</code>.
	 * </p>
	 * 
	 * @param monitor The progress monitor to use to display progress and receive
	 * requests for cancelation.
	 * 
	 * @throws InvocationTargetException If the run method must propagate a checked exception,
	 * it will wrap it inside an <code>InvocationTargetException</code>; runtime
	 * exceptions are automatically wrapped in an <code>InvocationTargetException</code>
	 * by the calling context.
	 * 
	 * @throws InterruptedException If the operation detects a request to cancel,
	 * using <code>IProgressMonitor.isCanceled()</code>, it will exit by throwing
	 * <code>InterruptedException</code>.
	 * 
	 * @see IRunnableWithProgress#run(IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor)
	  throws InvocationTargetException, InterruptedException
	{
		if (monitor == null)
		{
			monitor = new NullProgressMonitor();
		}
		try
		{
			monitor.beginTask(MleProjectMessages.getString("MleProjectCreationOperation.op_desc"), 1);
			IWorkspaceRoot root = MlePlugin.getWorkspace().getRoot();
			
			createProject(root, new SubProgressMonitor(monitor, 1));
		} finally
		{
			monitor.done();
		}
	}		
	
	/**
	 * Get the elements to open.
	 * 
	 * @return An array of resources is returned.
	 */
	public IResource[] getElementsToOpen()
	{
		IResource[] resources = new IResource[m_elementsToOpen.size()];
		m_elementsToOpen.toArray(resources);
		return resources;
	}

	// Set the default DWP and DPP file names.
	private void setDefaultFilenames()
	{
	    if (m_configPage.getDwpFilename().equals(m_configPage.FILENAME_NOT_SPECIFIED))
	        m_configPage.setDwpFilename(m_projectPage.getProjectName() + ".dwp");
        if (m_configPage.getDppFilename().equals(m_configPage.FILENAME_NOT_SPECIFIED))
            m_configPage.setDppFilename(m_projectPage.getProjectName() + ".dpp");
	}

	// Build the default Magic Lantern project.
	private IFile buildDefaultProject(IProject project)
	{
	    IFile dwp;
	    
		// Place workprint files under a common folder.
		IFolder folder = project.getFolder("workprints");
		createFolder(folder);
		
        // Create a domain table for the DWP.
        DwpTable dwpDomainTable = new DwpTable();
        // Get a handler for the DWP table.
        IDwpTableHandler dwpHandler = DwpTableHandlerFactory.getInstance().createTableHandler(dwpDomainTable);
        // Create a workspace resource for the DWP.
        String dwpFilename = m_configPage.getDwpFilename();
		DwpResource dwpResource = new DwpResource(dwpFilename,(DwpTableHandler)dwpHandler);
		// Write the resource to the file system.
		dwp = dwpResource.createFileInFolder(folder);
		
		return dwp;
	}

	// Initialize the specified project's nature.
	private void initializeNature(IProject project) throws DppException
	{
	    try
	    {
	        IProjectDescription description = project.getDescription();
	        String[] natures = description.getNatureIds();
	        String[] newNatures = new String[natures.length + 1];
	        System.arraycopy(natures, 0, newNatures, 0, natures.length);
	        newNatures[natures.length] = MASTERING_NATURE_ID;
	        description.setNatureIds(newNatures);
	        project.setDescription(description, null);
	    } catch (CoreException ex)
	    {
	        throw new DppException("Unable to initialize project nature.",ex);
	    }

        // Update the project's nature.
        configureMasteringNature(project);
	}

	// Create the new project.
	private void createProject(IWorkspaceRoot root, IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{        
        // Set defaults, if necessary.
        setDefaultFilenames();

		// Get the configuration element for the the Java capability configuration
		// page.
		IConfigurationElement desc = m_javaPage.getConfigurationElement();
		
		// Get the "import" elements. Import elements are Zip files containing
		// resources that will become part of the new MLE Project.
		IConfigurationElement[] imports = desc.getChildren("import");
		int nImports = (imports == null) ? 0 : imports.length;
		
		// Begin monitoring the configuration of the new MLE Project.
		monitor.beginTask(MleProjectMessages.getString("MleProjectCreationOperation.op_desc_proj"), nImports + 1);

        // Configure the new MLE Project.
		IProject project = configureProject(monitor);
		
		// Initialize the Mastering nature.
		try
		{
		    initializeNature(project);
		} catch (DppException ex)
		{
		    MleLog.logError(ex,"Unable to initialize Mastering nature.");
		}
		
		// Add the resources from the specified Zip import files.
		if ((m_templatePage != null) && (m_templatePage.useTemplate()))
		{
			for (int i = 0; i < nImports; i++)
			{
				m_templatePage.doImports(project, imports[i], new SubProgressMonitor(monitor, 1), m_overwriteQuery);
				
				try {
					// Add Simple Java DWP.
					MleSimpleDwp dwp = new MleSimpleDwp(project);
					if (m_configPage.isRehearsalPlayerSelected())
					{
						dwp.setPropertyValue(IDwpConfiguration.INCLUDE_REHEARSAL_PLAYER,new Boolean(true));
					}
					ByteArrayOutputStream dwpOut = dwp.generate();
					ByteArrayInputStream dwpIn = new ByteArrayInputStream(dwpOut.toByteArray());
					IFolder folder = createFolderInProject(project,"workprints");
					createFileInFolder(folder,m_configPage.getDwpFilename(),dwpIn);
				} catch (IOException ex)
				{
					MleLog.logError(ex, "Unable to create a simple Java DWP.");
				}

				// Open the specified element in the new MLE Project.
				String open = desc.getAttribute("open");
				if (open != null && open.length() > 0)
				{
					IResource fileToOpen = project.findMember(new Path(open));
					if (fileToOpen != null)
					{
						m_elementsToOpen.add(fileToOpen);
					}
				}		
			}
		}
	}
	
	// Configure the mastering nature for the specified project.
	private void configureMasteringNature(IProject project) throws DppException
	{
	    try
	    {
    	    IProjectDescription desc = project.getDescription();
    	    ICommand[] commands = desc.getBuildSpec();

    	    Vector cmds = new Vector();
    	    for (int i = 0; i < commands.length; i++)
    	    {
	            if (commands[i].getBuilderName().equals(MasteringNature.GENGROUP_BUILDER_ID))
	            {
	                if (m_configPage.isGencastSelected())
	                {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENGROUP_BUILDER_ID);
		                cmds.add(command);
	                } else
	                    continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENSCENE_BUILDER_ID))
	            {
	                if (m_configPage.isGensceneSelected())
		                {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENSCENE_BUILDER_ID);
		                cmds.add(command);
	                } else
	                    continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENMEDIA_BUILDER_ID))
	            {
	                if (m_configPage.isGenmrefSelected())
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENMEDIA_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENTABLES_BUILDER_ID))
	            {
	                if (m_configPage.isGentablesSelected())
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENTABLES_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENPPSCRIPT_BUILDER_ID))
	            {
	                if (m_configPage.isGendppscriptSelected())
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENPPSCRIPT_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENDPP_BUILDER_ID))
	            {
	                if (m_configPage.isGendppSelected())		    
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENDPP_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else
	            {
	                ICommand command = desc.newCommand();
	                command.setBuilderName(commands[i].getBuilderName());
	                command.setArguments(commands[i].getArguments());
	                cmds.add(command);
	            }
    	    }
    	    
            // Create a new commnds array and set it on the project.
    	    if (! cmds.isEmpty())
    	    {
	            ICommand[] newCommands = new ICommand[cmds.size()];
	            for (int i = 0; i < cmds.size(); i++)
	                newCommands[i] = (ICommand)cmds.elementAt(i);
	 	        desc.setBuildSpec(newCommands);
		        project.setDescription(desc, null);
    	    }
	    } catch (CoreException ex)
	    {
	        throw new DppException("Unable to configure mastering nature.",ex);
	    }
	}

	// Configure the Java2d mastering target.
	private void configureJava2dTarget(IResource resource) throws DppException
	{
	    // Configure gengroup.
	    if (m_configPage.isGencastSelected())
	        GengroupPropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genscene.
	    if (m_configPage.isGensceneSelected())
	        GenscenePropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genmedia.
	    if (m_configPage.isGenmrefSelected())
	        GenmediaPropertyManager.getInstance().setDefaults(resource);
	    // Configure gentables.
	    if (m_configPage.isGentablesSelected())
	        GentablesPropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genppscript.
	    if (m_configPage.isGendppscriptSelected())
	        GenppscriptPropertyManager.getInstance().setDefaults(resource);
	    // Congigure gendpp.
	    if (m_configPage.isGendppSelected())
	        GendppPropertyManager.getInstance().setDefaults(resource);
	}

	// Configure the project state.
	private void configureNewProject(IResource resource)
	{
	    IProject project = resource.getProject();
	    
	    try
	    {
	        DwpProjectManager.getInstance().setJavaProject(project);
	        
	        // Set the mastering state.
	        //if (m_configPage.isMasteringSelected())
	        //{
	            MasteringProjectManager.getInstance().init(project);
	            if (DwpProjectManager.getInstance().isJavaProject(project))
	            {
			        MasteringProjectManager.getInstance().setJavaDefaults(resource);
			        // XXX - Check first to determine if Java2D target.
			        configureJava2dTarget(resource);
			    } else
	                throw new DppException("Unknown type of Mastering project.");

	            if (m_configPage.isGencastSelected())
	            	MasteringProjectManager.getInstance().setGengroupValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGengroupValue(resource,false);
	            
	            if (m_configPage.isGensceneSelected())
	            	MasteringProjectManager.getInstance().setGensceneValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGensceneValue(resource,false);
	            
	            if (m_configPage.isGenmrefSelected())
	            	MasteringProjectManager.getInstance().setGenmediaValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGenmediaValue(resource,false);
	            
	            if (m_configPage.isGentablesSelected())
	            	MasteringProjectManager.getInstance().setGentablesValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGentablesValue(resource,false);

		        if (m_configPage.isGendppscriptSelected())
		        {
		        	MasteringProjectManager.getInstance().setGenppscriptValue(resource,true);

				    String dpp = m_configPage.getDppFilename();
				    GenppscriptPropertyManager.getInstance().setDppValue(resource,dpp);
		        } else
		        {
		        	MasteringProjectManager.getInstance().setGenppscriptValue(resource,false);
		        }
		        
		        if (m_configPage.isGendppSelected())
		        	MasteringProjectManager.getInstance().setGendppValue(resource,true);
		        else
		        	MasteringProjectManager.getInstance().setGendppValue(resource,false);
	        //}
	    } catch (DppException ex)
	    {
	        MleLog.logError(ex,"Unable to configure mastering process for resource "
	            + resource.getName() + ".");
	    } catch (DwpException ex)
	    {
	        MleLog.logError(ex,"Unable to configure mastering process for resource "
		        + resource.getName() + ".");
	    }
	}

	// Configure the new project.
	private IProject configureProject(IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{
	    IFile dwp;
	    
		try {
			// Get the project and its location.
			IProject project = m_javaPage.getJavaProject().getProject();
			IPath locationPath = m_projectPage.getOutputLocation();
		
			// Create the project and open it.
			IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());
			/*
			if (! m_projectPage.useDefaults())
			{
				desc.setLocation(locationPath);
			}
			*/
			project.create(desc, new SubProgressMonitor(monitor, 1));
			project.open(new SubProgressMonitor(monitor, 1));
			
			// Build the default resources. The new Digital Workprint is returned.
			dwp = buildDefaultProject(project);
			
			// Initialize the meta-data.
			configureNewProject(dwp);
			
			// Configure the Java project by adding the Java nature and
			// configuring the build classpath.
			//m_javaPage.updatePage();
			m_javaPage.configureJavaProject(new SubProgressMonitor(monitor, 1));

			return project;
		} catch (CoreException ex)
		{
			throw new InvocationTargetException(ex);
		}
	}

}
