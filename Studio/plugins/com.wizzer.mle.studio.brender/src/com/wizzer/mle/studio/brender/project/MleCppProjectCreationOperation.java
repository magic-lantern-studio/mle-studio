/**
 * MleCppProjectCreationOperation.java
 * Created on Aug. 5, 2005
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
package com.wizzer.mle.studio.brender.project;

// Import standard Java packages.
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Map.Entry;
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
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;

import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.ui.dialogs.IOverwriteQuery;

import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

import org.eclipse.cdt.core.model.CoreModel;
import org.eclipse.cdt.make.core.MakeCorePlugin;
import org.eclipse.cdt.ui.wizards.NewCProjectWizardPage;
import org.eclipse.cdt.make.ui.wizards.MakeProjectWizardOptionPage;
import org.eclipse.cdt.make.core.scannerconfig.IDiscoveredPathManager;
import org.eclipse.cdt.make.core.scannerconfig.IDiscoveredPathManager.IDiscoveredPathInfo;

// Import Digital Playprint header files.

// Import Magic Lantern classes.
import com.wizzer.mle.studio.MleLog;
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
import com.wizzer.mle.studio.brender.dwp.MleSimpleDwp;
import com.wizzer.mle.studio.brender.Activator;
import com.wizzer.mle.studio.brender.make.MleDebugMakefile;
import com.wizzer.mle.studio.brender.make.MleIsmdefsMakefile;
import com.wizzer.mle.studio.brender.make.MleIsmrulesMakefile;
import com.wizzer.mle.studio.brender.make.MleLeafdefsMakefile;
import com.wizzer.mle.studio.brender.make.MleMakefile;
import com.wizzer.mle.studio.brender.make.MleMasteringMakefile;
import com.wizzer.mle.studio.brender.make.MleReleaseMakefile;
import com.wizzer.mle.studio.brender.make.MleTopMakefile;
import com.wizzer.mle.studio.project.MleTargetCreationOperation;
import com.wizzer.mle.studio.project.MleTargetConfigurationPage;

/**
 * This class is used to perform the actual creation of a new Magic Lantern
 * C++ project.
 * 
 * @author Mark S. Millard
 */
public class MleCppProjectCreationOperation extends MleTargetCreationOperation
{
    public static final String MASTERING_NATURE_ID = "com.wizzer.mle.studio.dpp.MasteringNature";

	// The resource to open.
	private ArrayList m_elementsToOpen;
	// The main project page;
	private NewCProjectWizardPage m_projectPage;
	// The MLE Target configuration page.
	private MleTargetConfigurationPage m_configPage;
	// The C++ capability configuration page.
	private MleMakeProjectOptionPage m_cppPage;
	// Dialog handler for query on overwrite.
	private IOverwriteQuery m_overwriteQuery;
	// The project to configure.
	private IProject m_project;
	
	/**
	 * Constructor for <code>MleCppProjectCreationOperation</code>
	 * 
	 * @param projectPage The main project creation page.
	 * @param configPage The Magic Lantern project configuration page.
	 * @param javaPage The C++ capability configuration page.
	 * @param The dialog handler for query on overwrite.
	 */
	public MleCppProjectCreationOperation(
	    IProject project,
	    NewCProjectWizardPage projectPage,
	    MleTargetConfigurationPage configPage,
	    MleMakeProjectOptionPage cppPage,
	    IOverwriteQuery overwriteQuery)
	{
		m_elementsToOpen = new ArrayList();
		m_project = project;
		m_projectPage = projectPage;
		m_configPage = configPage;
		m_cppPage = cppPage;
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
			IWorkspaceRoot root = Activator.getWorkspace().getRoot();
			
			createProject(m_project, new SubProgressMonitor(monitor, 1));
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
	private void createProject(IProject project, IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{        
        // Set defaults, if necessary.
        setDefaultFilenames();

		// Get the configuration element for the the C++ capability configuration
		// page.
		IConfigurationElement desc = m_cppPage.getConfigurationElement();
		
		// Get the "import" elements. Import elements are Zip files containing
		// resources that will become part of the new MLE Project.
		IConfigurationElement[] imports = desc.getChildren("import");
		int nImports = (imports == null) ? 0 : imports.length;
		
		// Begin monitoring the configuration of the new MLE Project.
		monitor.beginTask(MleProjectMessages.getString("MleProjectCreationOperation.op_desc_proj"), nImports + 1);

        // Configure the new MLE Project.
		configureProject(project, monitor);
				
		// Initialize the Mastering nature.
		try
		{
		    initializeNature(project);
		} catch (DppException ex)
		{
		    MleLog.logError(ex,"Unable to initialize Mastering nature.");
		}
		
		// Add the resources from the specified Zip import files.
		if (m_configPage.useTemplate())
		{
			for (int i = 0; i < nImports; i++)
			{
				doImports(project, imports[i], new SubProgressMonitor(monitor, 1));
				
				// Open the specified element in the new MLE Project.
				String open = imports[i].getAttribute("open");
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
		
		// Create the Makefile hierarchy. This needs to follow configureProject() since
		// it relies on the project to have the properties already set.
		if (m_configPage.useTemplate())
			createMakefiles(project,new SubProgressMonitor(monitor, 1));
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

	// Configure the C++ mastering target.
	private void configureCppTarget(IResource resource) throws DppException
	{
	    // Configure gengroup.
	    if (m_configPage.isGencastSelected())
	        GengroupPropertyManager.getInstance().setCppDefaults(resource);
	    // Configure genscene.
	    if (m_configPage.isGensceneSelected())
	        GenscenePropertyManager.getInstance().setCppDefaults(resource);
	    // Configure genmedia.
	    if (m_configPage.isGenmrefSelected())
	        GenmediaPropertyManager.getInstance().setDefaults(resource);
	    // Configure gentables.
	    if (m_configPage.isGentablesSelected())
	        GentablesPropertyManager.getInstance().setCppDefaults(resource);
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
	        DwpProjectManager.getInstance().setCppProject(project);
	        
	        // Set the mastering state.
            MasteringProjectManager.getInstance().init(project);
	        //if (m_configPage.isMasteringSelected())
	        //{
	            if (DwpProjectManager.getInstance().isCppProject(project))
	            {
	                MasteringProjectManager.getInstance().setCppDefaults(resource);
	                configureCppTarget(resource);
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
	private IProject configureProject(IProject project,IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{
	    IFile dwp;
		
		// Build the default resources. The new Digital Workprint is returned.
		dwp = buildDefaultProject(project);
		
		// Initialize the meta-data.
		configureNewProject(dwp);

		return project;
	}
		
	// Create the Makefile hierarchy.
	private void createMakefiles(IProject project,IProgressMonitor monitor)
	{
		try
		{
			// Create the Ismdefs Makefile.
			MleIsmdefsMakefile ismdefs = new MleIsmdefsMakefile(project);
			ByteArrayOutputStream ismdefsOut = ismdefs.generate();
			ByteArrayInputStream ismdefsIn = new ByteArrayInputStream(ismdefsOut.toByteArray());
			IFile ismdefsFile = project.getFile("ismdefs");
			ismdefsFile.create(ismdefsIn,false,null);
			
			// Create the Ismrules Makefile.
			MleIsmrulesMakefile ismrules = new MleIsmrulesMakefile(project);
			ByteArrayOutputStream ismrulesOut = ismrules.generate();
			ByteArrayInputStream ismrulesIn = new ByteArrayInputStream(ismrulesOut.toByteArray());
			IFile ismrulesFile = project.getFile("ismrules");
			ismrulesFile.create(ismrulesIn,false,null);
			
			// Create the Top Makefile.
			MleTopMakefile topMakefile = new MleTopMakefile(project);
			ByteArrayOutputStream topMakefileOut = topMakefile.generate();
			ByteArrayInputStream topMakefileIn = new ByteArrayInputStream(topMakefileOut.toByteArray());
			IFile topMakefileFile = project.getFile("Makefile");
			topMakefileFile.create(topMakefileIn,false,null);
			
			// Create the Makefile in the source directory.
			MleMakefile makefile = new MleMakefile(project);
			ByteArrayOutputStream makefileOut = makefile.generate();
			ByteArrayInputStream makefileIn = new ByteArrayInputStream(makefileOut.toByteArray());
			IFolder srcFolder = createFolderInProject(project,"src");
			createFileInFolder(srcFolder,"Makefile",makefileIn);
			
			// Create the Leafdefs Makefile in the source directory.
			MleLeafdefsMakefile leafdefs = new MleLeafdefsMakefile(project);
			leafdefs.setConfiguration(m_configPage.isMasteringSelected());
			ByteArrayOutputStream leafdefsOut = leafdefs.generate();
			ByteArrayInputStream leafdefsIn = new ByteArrayInputStream(leafdefsOut.toByteArray());
			createFileInFolder(srcFolder,"leafdefs",leafdefsIn);
			
			// Create the Mastering Makefile.
			if (m_configPage.isMasteringSelected())
	        {
				MleMasteringMakefile masterMakefile = new MleMasteringMakefile(project);
				ByteArrayOutputStream masterMakefileOut = masterMakefile.generate();
				ByteArrayInputStream masterMakefileIn = new ByteArrayInputStream(masterMakefileOut.toByteArray());
				IFolder masterFolder = createFolderInProject(project,"src/gen");
				createFileInFolder(masterFolder,"Makefile",masterMakefileIn);
	        }

			// Create the Debug Target Makefile.
			MleDebugMakefile debugMakefile = new MleDebugMakefile(project);
			ByteArrayOutputStream debugMakefileOut = debugMakefile.generate();
			ByteArrayInputStream debugMakefileIn = new ByteArrayInputStream(debugMakefileOut.toByteArray());
			IFolder debugFolder = createFolderInProject(project,"src/Debug");
			createFileInFolder(debugFolder,"Makefile",debugMakefileIn);
			
			// Create the LocalDefs Makefile for the Debug Target.
			
			// Create the Release Target Makefile.
			MleReleaseMakefile releaseMakefile = new MleReleaseMakefile(project);
			ByteArrayOutputStream releaseMakefileOut = releaseMakefile.generate();
			ByteArrayInputStream releaseMakefileIn = new ByteArrayInputStream(releaseMakefileOut.toByteArray());
			IFolder releaseFolder = createFolderInProject(project,"src/Release");
			createFileInFolder(releaseFolder,"Makefile",releaseMakefileIn);

			// Create the LocalDefs Makefile for the Release Target.

		} catch (DppException ex)
		{
			MleLog.logError(ex,"Unable to create Makefile hierarchy.");
		} catch (IOException ex)
		{
			MleLog.logError(ex,"Unable to create Makefile hierarchy.");
		} catch (CoreException ex)
		{
			MleLog.logError(ex,"Unable to create Makefile hierarchy.");
		}
	}
	
	// Unzip the Zip file.
	private void doImports(IProject project, IConfigurationElement curr, IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{
		try {
			IPath destPath;
			
			// Get the type of import.
			String type = curr.getAttribute("type");
			if (type == null)
			{
				MleLog.logWarning("<import/> descriptor: type missing.");
				return;
			}

			if ((type.equals("SimpleBRenderTemplate") &&
				(m_configPage.getTemplateType() == MleTargetConfigurationPage.SIMPLE_RUNTIME_TEMPLATE)))
			{
				// Set up the destination directory.
				String name = curr.getAttribute("dest");
				if (name == null || name.length() == 0)
				{
					destPath= project.getFullPath();
				} else
				{
					IFolder folder = project.getFolder(name);
					if (! folder.exists())
					{
						folder.create(true, true, null);
					}
					destPath= folder.getFullPath();
				}
				
				// Determine the source of the Zip file.
				String importPath = curr.getAttribute("src");
				if (importPath == null)
				{
					importPath = "";
					MleLog.logWarning("<import/> descriptor: import missing.");
					return;
				}
			    
			    // Retrieve the Zip file and import the resources.
				ZipFile zipFile = getZipFileFromPluginDir(importPath);
				importFilesFromZip(zipFile, destPath, new SubProgressMonitor(monitor, 1));
				
				// Add Simple BRender DWP.
				MleSimpleDwp dwp = new MleSimpleDwp(project);
				if (m_configPage.isRehearsalPlayerSelected())
				{
					dwp.setPropertyValue(IDwpConfiguration.INCLUDE_REHEARSAL_PLAYER,new Boolean(true));
				}
				ByteArrayOutputStream dwpOut = dwp.generate();
				ByteArrayInputStream dwpIn = new ByteArrayInputStream(dwpOut.toByteArray());
				IFolder folder = createFolderInProject(project,"workprints");
				createFileInFolder(folder,m_configPage.getDwpFilename(),dwpIn);
			}
		} catch (CoreException e)
		{
			throw new InvocationTargetException(e);
		} catch (IOException e)
		{
			throw new InvocationTargetException(e);
		}
	}
	
	// Retrieve the Zip file relative to the specified path.
	private ZipFile getZipFileFromPluginDir(String pluginRelativePath)
	    throws CoreException
	{
		try
		{
			URL starterURL = new URL(Activator.getDefault().getBundle().getEntry("/"), pluginRelativePath);
			//return new ZipFile(Platform.asLocalURL(starterURL).getFile());
			URL localURL = FileLocator.toFileURL(starterURL);
			return new ZipFile(localURL.getFile());
		} catch (IOException e)
		{
			String message= pluginRelativePath + ": " + e.getMessage();
			Status status= new Status(IStatus.ERROR, Activator.getID(), IStatus.ERROR, message, e);
			throw new CoreException(status);
		}
	}
	
	// Import resources from the specified Zip file
	private void importFilesFromZip(ZipFile srcZipFile, IPath destPath, IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{		
		ZipFileStructureProvider structureProvider = new ZipFileStructureProvider(srcZipFile);
		ImportOperation op = new ImportOperation(destPath, structureProvider.getRoot(), structureProvider, m_overwriteQuery);
		op.run(monitor);
	}
}
