// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.java2d.project;

// Import standard Java classes.
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

//Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.IWizardPage;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.MleException;
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GendppPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GengroupPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenscenePropertyManager;
import com.wizzer.mle.studio.dpp.properties.GentablesPropertyManager;
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpProjectManager;
import com.wizzer.mle.studio.java2d.Java2DLog;
import com.wizzer.mle.studio.project.MleStudioImportProjectWizard;
import com.wizzer.mle.studio.project.MleStudioMetaDataPage;
import com.wizzer.mle.studio.project.MleStudioProjectManager;
import com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller;
import com.wizzer.mle.studio.project.metadata.MetaDataException;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;


public class MleJava2DImportProjectWizard extends MleStudioImportProjectWizard
{
	@Override
	public boolean performFinish()
	{
        // Get the IPoject handle.
        IProject project = m_projectPage.getProjectHandle();

        // Create the project in the workspace.
        NullProgressMonitor monitor = new NullProgressMonitor();
        try
        {
            IProjectDescription description = null;

            // Load the project description for the identified project.
            IPath location = m_projectPage.getLocationPath();
            location = location.append(IProjectDescription.DESCRIPTION_FILE_NAME);
            description = getWorkspace().loadProjectDescription(location);

            // Create the new project.
            project.create(description, monitor);

            // Open the project.
            project.open(monitor);

            // Initialize the meta-data. This may be overwritten in the next
            // step.
            String projectType = MleJava2DProjectManager.MLE_STUDIO_JAVA2D_PROJECT_TYPE_VALUE;
            MleStudioProjectManager.getInstance().init(project, projectType);
            
            // Check to see if the project has the correct properties associated
            // with it. If not, ask the user if the project should be modified
            // to be compatible with a Java2D Project state.
            MleTitleMetaData metadata = m_metaDataPage.getMetaData();
            updateMetaData(project, metadata);
            updateMetaDataFile(project, metadata);

        }
        catch (Exception ex)
        {
            Java2DLog.logError(ex, "Unable to complete the import of project " + m_projectPage.getProjectName() + " due to the following error: " + ex.getMessage());

            MessageDialog dialog = new MessageDialog(
                    getShell(),
                    "Magic Lantern Project Import Error",
                    null,
                    project.getName()
                            + " could not be imported due to the following error: " + ex.getMessage(),
                    MessageDialog.ERROR, new String[] { "Ok" }, 0);

            dialog.open();

            // Opens tray with message instead of the above dialog.
            // Left in here as code sample...
            // ((WizardPage) getContainer().getCurrentPage()).setErrorMessage(project.getName() + ": " + ex.getMessage());

            // Import failed so delete project from workspace
            try
            {
	            project.delete(false, false, monitor);
            }
            catch (CoreException ce)
            {
                Java2DLog.logError(ex, "Unable to delete aborted import of project " + m_projectPage.getProjectName() + " due to the following error: " + ce.getMessage());            	
            }
            
            return false;
        }

        return true;
	}

    // Update the meta-data on the project.
    private void updateMetaData(IProject project, MleTitleMetaData metadata)
    {
        Object[] projectData = metadata.getProjectData();
        if (projectData.length > 0)
        {
	        MleTitleMetaData.ProjectDataElement projectElement = (MleTitleMetaData.ProjectDataElement) projectData[0];
	        
	        // Set the project data on the MLE Studio Project Manager.
	        MleStudioProjectManager projectMgr = MleStudioProjectManager.getInstance();
	        String projectType = MleJava2DProjectManager.MLE_STUDIO_JAVA2D_PROJECT_TYPE_VALUE;
	        projectMgr.init(project, projectType);
	        
        	try {
        		MleStudioProjectManager.getInstance().setProjectIdValue(project, projectElement.getId());
        		MleStudioProjectManager.getInstance().setProjectVersionValue(project, projectElement.getVersion());
        		MleStudioProjectManager.getInstance().setDigitalWorkprintValue(project, projectElement.getDwpFile());
        		
        		// Set the project data on the DWP Project Manager.
		        DwpProjectManager.getInstance().init(project);
		        DwpProjectManager.getInstance().setJavaProject(project);
		        
		        Object[] targetData = projectElement.getMasterTargets();
		        if (targetData.length > 0)
		        {
		        	MleTitleMetaData.MasterTargetElement targetElement = (MleTitleMetaData.MasterTargetElement) targetData[0];

		        	// Set the project data on the DPP Project Managers.
		        	MasteringProjectManager targetMgr = MasteringProjectManager.getInstance();
		        	targetMgr.init(project);
		        	targetMgr.setTargetTypeValue(project, targetElement.getType());
		        	targetMgr.setTargetIdValue(project, targetElement.getId());
		        	targetMgr.setTargetDigitalPlayprintValue(project, targetElement.getDigitalPlayprint());
	
		        	// Set the common properties for the Java target.
		        	targetMgr.setDestinationDirValue(project, targetElement.getDestinationDirectroy());
		        	targetMgr.setVerboseValue(project, targetElement.isVerbose());
		        	targetMgr.setBigEndian(project);
		        	targetMgr.setJavaPackageValue(project, targetElement.getHeaderPackage());
		        	
		        	// Set the gengroup properties.
					targetMgr.setGengroupValue(project, targetElement.hasGengroup());
					GengroupPropertyManager.getInstance().setActorIdValue(project, targetElement.getActorIdFile());
					GengroupPropertyManager.getInstance().setGroupIdValue(project, targetElement.getGroupIdFile());
					
					// Set the genscene properties.
					targetMgr.setGensceneValue(project, targetElement.hasGenscene());
					GenscenePropertyManager.getInstance().setSceneIdValue(project, targetElement.getSceneIdFile());
					
					// Set the genmedia properties.
					targetMgr.setGenmediaValue(project, targetElement.hasGenmedia());
					GenmediaPropertyManager.getInstance().setBomValue(project, targetElement.getBomFile());
					
					// Set the gentables properties.
					targetMgr.setGentablesValue(project, targetElement.hasGentables());
					GentablesPropertyManager.getInstance().setJavaDefaults(project);
					
					// Set the genppscript properties.
					targetMgr.setGenppscriptValue(project, targetElement.hasGenppscript());
					String dpp = targetElement.getDigitalPlayprint();
					dpp = dpp.substring(dpp.lastIndexOf("/") + 1);
					GenppscriptPropertyManager.getInstance().setDppValue(project, dpp);
					GenppscriptPropertyManager.getInstance().setScriptValue(project, targetElement.getScriptFile());
					GenppscriptPropertyManager.getInstance().setTocValue(project, targetElement.getTocName());
					
					// Set the gendpp properties.
					targetMgr.setGendppValue(project, targetElement.hasGendpp());
					GendppPropertyManager.getInstance().setScriptValue(project, targetElement.getScriptPath());
					GendppPropertyManager.getInstance().setSourceDirValue(project, targetElement.getSourceDirectory());
					
					// Set tags.
					ArrayList<String> tags = targetElement.getTags();
					String tagValues[] = new String[0];
					tagValues = tags.toArray(tagValues);
					targetMgr.setTagsValue(project, tagValues);
		        }
			} catch (DwpException e) {
				Java2DLog.logError(e, "Unable to configure project " + project.getName() + ": "+ e.getMessage());
			} catch (DppException e) {
				Java2DLog.logError(e, "Unable to configure project " + project.getName() + ": " + e.getMessage());
			} catch (MleException e) {
				Java2DLog.logError(e, "Unable to configure project " + project.getName() + ": " + e.getMessage());
			}
        }
    }
    
    /**
     * Get the next wizard page.
     * 
     * @param page The current page.
     * 
     * @see org.eclipse.jface.wizard.IWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
     */
    public IWizardPage getNextPage(IWizardPage page)
    {
        IWizardPage nextPage = super.getNextPage(page);

        if (nextPage instanceof MleStudioMetaDataPage)
        {
            ((MleStudioMetaDataPage) nextPage).setProject(m_projectPage.getProjectHandle());
            ((MleStudioMetaDataPage) nextPage).setLocationPath(m_projectPage.getLocationPath());

            try
            {
                MleTitleMetaData metadata = null;
                File file = hasProjectMetaData();
                if (file != null)
                {
                    // Use the meta-data associated with the project.
                    InputStream input = new BufferedInputStream(new FileInputStream(file));

                    // Create a new JAXB marshaller.
                    JaxbMetaDataMarshaller marshaller = new JaxbMetaDataMarshaller();

                    // Retrieve the meta-data;
                    metadata = (MleTitleMetaData) marshaller.unmarshallMetaData(input);

                    input.close();

                    ((MleStudioMetaDataPage) nextPage).updateControl(metadata);
                }
                else
                {
                    m_projectPage.setMessage(
                        "Using default meta-data settings. Please continue to the next page to change default values.",
                        IMessageProvider.WARNING);
                }
            } catch (IOException ex)
            {
                Java2DLog.logError(ex, "Unable to process meta-data for " + m_projectPage.getProjectName() + ".");
            } catch (MetaDataException ex)
            {
            	Java2DLog.logError(ex, "Unable to process meta-data for " + m_projectPage.getProjectName() + ".");
            } catch (CoreException ex)
            {
            	Java2DLog.logError(ex, "Unable to process meta-data for " + m_projectPage.getProjectName() + ".");
            }
        }

        return nextPage;
    }
}
