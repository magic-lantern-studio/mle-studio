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
package com.wizzer.mle.studio.properties;

// Import standard Java classes.

// Import Eclipse classes.
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.ui.dialogs.PropertyPage;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.MleException;
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.dwp.DwpException;
import com.wizzer.mle.studio.dwp.DwpProjectManager;

//Import Digital Playprint classes.
import com.wizzer.mle.studio.dpp.DppException;
import com.wizzer.mle.studio.dpp.DppLog;
import com.wizzer.mle.studio.dpp.project.MasteringMessages;
import com.wizzer.mle.studio.dpp.project.MasteringProjectManager;
import com.wizzer.mle.studio.dpp.properties.GendppPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GengroupPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenscenePropertyManager;
import com.wizzer.mle.studio.dpp.properties.GentablesPropertyManager;
import com.wizzer.mle.studio.framework.Find;
import com.wizzer.mle.studio.framework.FindEvent;
import com.wizzer.mle.studio.framework.FindFilter;
import com.wizzer.mle.studio.framework.IFindListener;
import com.wizzer.mle.studio.project.IMleStudioMetaDataEventListener;
import com.wizzer.mle.studio.project.MleStudioMetaDataControl;
import com.wizzer.mle.studio.project.MleStudioMetaDataEvent;
import com.wizzer.mle.studio.project.MleStudioProjectManager;
import com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;

/**
 * This class implements a property page for the Magic Lantern
 * mastering commands.
 * 
 * @author Mark S. Millard
 */
public class MasteringPropertyPage extends PropertyPage implements IMleStudioMetaDataEventListener
{
    // The page control.
    private MleStudioMetaDataControl m_control = null;
    
	// Recursively check the project hierarchy to determine whether the project
    // file system has a .mleproject file.
    private File m_foundFile = null;

    /**
     * The default constructor.
     */
    public MasteringPropertyPage()
    {
        super();
    }

    // Determine if the project has a .mleproject file.
    private File hasProjectMetaData() throws CoreException
    {
    	IResource resource = (IResource)this.getElement();
    	
        FindFilter filter = new FindFilter();
        filter.setNameFilter(MleStudioProjectManager.MLE_STUDIO_PROJECT_METADATA_FILENAME);

        Find finder = new Find(filter);
        finder.addListener(new IFindListener()
        {
            public void handleEvent(FindEvent event)
            {
                m_foundFile = event.m_file;
            }

        });

        // Search the project file location
        IPath location = resource.getProject().getLocation();
        finder.doName(location.toOSString());

        return m_foundFile;
    }
    
    private void initConfiguration(IResource resource, MleStudioMetaDataControl.MetaDataControlConfig config)
    {
    	try
    	{
    		config.m_dstDirectory = MasteringProjectManager.getInstance().getDestinationDirValue(resource);
    		config.m_isBigEndian = MasteringProjectManager.getInstance().isBigEndian(resource);
    		config.m_isVerbose = MasteringProjectManager.getInstance().getVerboseValue(resource);
    		config.m_javaPackage = MasteringProjectManager.getInstance().getJavaPackageValue(resource);
    	    config.m_hasGengroup = MasteringProjectManager.getInstance().getGengroupValue(resource);
    	    config.m_hasGenscene = MasteringProjectManager.getInstance().getGensceneValue(resource);
    	    config.m_hasGenmedia = MasteringProjectManager.getInstance().getGenmediaValue(resource);
    	    config.m_hasGentables = MasteringProjectManager.getInstance().getGentablesValue(resource);
    	    config.m_hasGenppscript = MasteringProjectManager.getInstance().getGenppscriptValue(resource);
    	    config.m_hasGendpp = MasteringProjectManager.getInstance().getGendppValue(resource);
    	    String[] tags = MasteringProjectManager.getInstance().getTagsValue(resource);
    	    if (tags != null)
    	    {
    	    	for (int i = 0; i < tags.length; i++)
    	            config.m_tags.add(new String(tags[i]));
    	    }
    	    
    	    if (config.m_hasGengroup)
    	    {
    	    	config.m_actorId = GengroupPropertyManager.getInstance().getActorIdValue(resource);
    	    	config.m_groupId = GengroupPropertyManager.getInstance().getGroupIdValue(resource);
    	    	if (DwpProjectManager.getInstance().isCppProject(resource.getProject()))
    	    	    config.m_isFixedPoint = GengroupPropertyManager.getInstance().getFixedPointValue(resource);
    	    }
    	    
    	    if (config.m_hasGenscene)
    	    {
    	    	config.m_sceneId = GenscenePropertyManager.getInstance().getSceneIdValue(resource);
    	    	if (DwpProjectManager.getInstance().isCppProject(resource.getProject()))
    	    	    config.m_isFixedPoint = GenscenePropertyManager.getInstance().getFixedPointValue(resource);
    	    }
    	    
    	    if (config.m_hasGenmedia)
    	    {
    	    	config.m_bom = GenmediaPropertyManager.getInstance().getBomValue(resource);
    	    }
    	    
    	    if (config.m_hasGenppscript)
    	    {
    	    	config.m_dpp = GenppscriptPropertyManager.getInstance().getDppValue(resource);
    	    	config.m_script = GenppscriptPropertyManager.getInstance().getScriptValue(resource);
    	    	config.m_toc = GenppscriptPropertyManager.getInstance().getTocValue(resource);
    	    }
    	    
    	    if (config.m_hasGendpp)
    	    {
    	    	config.m_srcDirectory = GendppPropertyManager.getInstance().getSourceDirValue(resource);
    	    	config.m_scriptPath = GendppPropertyManager.getInstance().getScriptValue(resource);
    	    }
    	} catch (DppException ex)
    	{
    		MleLog.logError(ex, "Unable to initialize meta-data configuration.");
    	}
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.preference.PreferencePage#createContents(org.eclipse.swt.widgets.Composite)
     */
    protected Control createContents(Composite parent)
    {
        IResource resource = (IResource)this.getElement();

        if (! MasteringProjectManager.getInstance().isDppProject(resource.getProject()))
        {
            Label infoLabel = new Label(parent, SWT.NONE);
            infoLabel.setText(MasteringMessages.getString("MasteringPropertyPage.notDppProject"));
            return parent;
        }

        // Create the control for setting the meta-data.
        m_control = new MleStudioMetaDataControl();
        MleStudioMetaDataControl.MetaDataControlConfig configuration = m_control.new MetaDataControlConfig();
        initConfiguration(resource, configuration);
        Control control = m_control.createControl(parent, SWT.NULL, configuration);
        try {
	        m_control.setProjectId(MleStudioProjectManager.getInstance().getProjectIdValue(resource));
	        m_control.setProjectVersion(MleStudioProjectManager.getInstance().getProjectVersionValue(resource));
	        m_control.setDigitalWorkprint(MleStudioProjectManager.getInstance().getDigitalWorkprintValue(resource));
	        m_control.setTargetType(MasteringProjectManager.getInstance().getTargetTypeValue(resource));
	        m_control.setTargetId(MasteringProjectManager.getInstance().getTargetIdValue(resource));
	        m_control.setTargetDigitalPlayprint(MasteringProjectManager.getInstance().getTargetDigitalPlayprintValue(resource));
        } catch (MleException ex)
        {
        	MleLog.logError(ex, "Unable to set project meta-data in control, using defaults.");
        } catch (DppException ex)
        {
        	MleLog.logError(ex, "Unable to set project meta-data in control, using defaults.");
        }
        m_control.addMetaDataListener(this);

        return control;
    }

    /**
     * Checks whether it is all right to leave this page.
     * 
     * @return <b>false</b> is returned to abort page flipping
     * and have the current page remain visible,
     * and <b>true</b> should be returned to allow the page flip.
     */
    public boolean performOk()
    {
        boolean retValue = true;
        
        IResource resource = (IResource)this.getElement();
        if (! MasteringProjectManager.getInstance().isDppProject(resource.getProject()))
            return retValue;
        
        try
        {
        	MleTitleMetaData metaData = m_control.getMetaData();
        	updateMetaData(resource.getProject(), metaData);
        	if (hasProjectMetaData() != null)
        		updateMetaDataFile(resource.getProject(), metaData);
        } catch (CoreException ex)
        {
            DppLog.logError(ex,"Unable to apply mastering properties.");
        } catch (FileNotFoundException ex)
        {
        	DppLog.logError(ex,"Unable to apply mastering properties.");
		} catch (IOException ex)
		{
			DppLog.logError(ex,"Unable to apply mastering properties.");
		}
        
        return retValue;
    }

    /**
     * Performs special processing when this page's Defaults button has been pressed.
     */
    protected void performDefaults()
    {
        super.performDefaults();
        
        IResource resource = (IResource)this.getElement();
        if (! MasteringProjectManager.getInstance().isDppProject(resource.getProject()))
            return;

        if (DwpProjectManager.getInstance().isJavaProject(resource.getProject()))
		{
            // Set common properties defaults.
            m_control.setDestinationDirectory(MasteringProjectManager.DPP_COMMON_DESTINATION_DIR_DEFAULT_VALUE);
            m_control.setBigEndian(true);
            m_control.setHeaderPackage(MasteringProjectManager.DPP_COMMON_JAVA_PACKAGE_DEFAULT_VALUE);
            m_control.setVerbose(false);
            m_control.clearTags();
            
            // Set gengroup properties.
            m_control.setActorID(GengroupPropertyManager.DPP_GENGROUP_ACTORID_JAVA_VALUE);
            m_control.setGroupID(GengroupPropertyManager.DPP_GENGROUP_GROUPID_JAVA_VALUE);

            // Set genscene properties.
            m_control.setSceneID(GenscenePropertyManager.DPP_GENSCENE_SCENEID_JAVA_VALUE);

            // Set genmedia properties.
            m_control.setBom(GenmediaPropertyManager.DPP_GENMEDIA_BOM_VALUE);
            
            // Set genppscript properties.
            m_control.setTargetDigitalPlayprint(GenppscriptPropertyManager.DPP_GENPPSCRIPT_DPP_VALUE);
            m_control.setScript(GenppscriptPropertyManager.DPP_GENPPSCRIPT_SCRIPT_VALUE);
            m_control.setTocName(GenppscriptPropertyManager.DPP_GENPPSCRIPT_TOC_JAVA_VALUE);

            // Set gendpp properties.
            m_control.setScriptPath(GendppPropertyManager.DPP_GENDPP_SCRIPT_VALUE);
            m_control.setSourceDirectory(GendppPropertyManager.DPP_GENDPP_SOURCE_DIR_VALUE);
            
		} else if (DwpProjectManager.getInstance().isCppProject(resource.getProject()))
		{
            // Set common properties.
            m_control.setDestinationDirectory(MasteringProjectManager.DPP_COMMON_DESTINATION_DIR_DEFAULT_VALUE);
            m_control.setBigEndian(true);
            m_control.setVerbose(false);
            m_control.setFixedPoint(false);
            m_control.clearTags();
            
            // Set gengroup properties.
            m_control.setActorID(GengroupPropertyManager.DPP_GENGROUP_ACTORID_CPP_VALUE);
            m_control.setGroupID(GengroupPropertyManager.DPP_GENGROUP_GROUPID_CPP_VALUE);

            // Set genscene properties.
            m_control.setSceneID(GenscenePropertyManager.DPP_GENSCENE_SCENEID_CPP_VALUE);
 
            // Set genmedia properties.
            m_control.setBom(GenmediaPropertyManager.DPP_GENMEDIA_BOM_VALUE);
            
            // Set genppscript properties.
            m_control.setTargetDigitalPlayprint(GenppscriptPropertyManager.DPP_GENPPSCRIPT_DPP_VALUE);
            m_control.setScript(GenppscriptPropertyManager.DPP_GENPPSCRIPT_SCRIPT_VALUE);
            m_control.setTocName(GenppscriptPropertyManager.DPP_GENPPSCRIPT_TOC_CPP_VALUE);

            // Set gendpp properties.
            m_control.setScriptPath(GendppPropertyManager.DPP_GENDPP_SCRIPT_VALUE);
            m_control.setSourceDirectory(GendppPropertyManager.DPP_GENDPP_SOURCE_DIR_VALUE);

		}
    }

    /**
     * Handle the specified event.
     * 
     * @param event A meta-data event.
     * 
     * @see com.IMleStudioMetaDataEventListener.vwb.platform.ocap.ui.IOcapMetaDataEventListener#handleMetaDataEvent(com.MleStudioMetaDataEvent.vwb.platform.ocap.ui.OcapMetaDataEvent)
     */
    public void handleMetaDataEvent(MleStudioMetaDataEvent event)
    {
        this.setErrorMessage(event.m_msg);
        if (event.m_state == MleStudioMetaDataEvent.OK)
            this.setValid(true);
        else if (event.m_state == MleStudioMetaDataEvent.ERROR)
        	this.setValid(false);
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
	        String projectType = metadata.getProjectType();;
	        projectMgr.init(project, projectType);
	        
        	try {
        		// Set the project data on the DWP Project Manager.
		        DwpProjectManager.getInstance().init(project);
		        
		        Object[] targetData = projectElement.getMasterTargets();
		        if (targetData.length > 0)
		        {
		        	
		        	MleTitleMetaData.MasterTargetElement targetElement = (MleTitleMetaData.MasterTargetElement) targetData[0];
		        	
		        	// Set the project data on the DWP Project Manager.
		        	if (targetElement.getCodeGeneration().equals("java"))
		        		DwpProjectManager.getInstance().setJavaProject(project);
		        	else if (targetElement.getCodeGeneration().equals("cpp"))
		        		DwpProjectManager.getInstance().setCppProject(project);

		        	// Set the project data on the DPP Project Managers.
		        	MasteringProjectManager targetMgr = MasteringProjectManager.getInstance();
		        	targetMgr.init(project);
		        	targetMgr.setTargetIdValue(project, targetElement.getId());
		        	targetMgr.setTargetTypeValue(project, targetElement.getType());
		        	targetMgr.setTargetDigitalPlayprintValue(project, targetElement.getDigitalPlayprint());
	
		        	// Set the common properties for the target.
		        	targetMgr.setDestinationDirValue(project, targetElement.getDestinationDirectroy());
		        	targetMgr.setVerboseValue(project, targetElement.isVerbose());
		        	if (targetElement.isBigEndian())
		        	    targetMgr.setBigEndian(project);
		        	else
		        		targetMgr.setLittleEndian(project);
		        	targetMgr.setJavaPackageValue(project, targetElement.getHeaderPackage());
			        
		        	ArrayList<String> tags = targetElement.getTags();
		            String[] tagValues = new String[0];
		            tagValues = tags.toArray(tagValues);
		            targetMgr.setTagsValue(project,tagValues);
		        	
		        	// Set the gengroup properties.
					targetMgr.setGengroupValue(project, targetElement.hasGengroup());
					if (targetElement.hasGengroup())
					{
						GengroupPropertyManager.getInstance().setActorIdValue(project, targetElement.getActorIdFile());
						GengroupPropertyManager.getInstance().setGroupIdValue(project, targetElement.getGroupIdFile());
						if (DwpProjectManager.getInstance().isCppProject(project))
						    GengroupPropertyManager.getInstance().setFixedPointValue(project, targetElement.isFixedPoint());
					}
					
					// Set the genscene properties.
					targetMgr.setGensceneValue(project, targetElement.hasGenscene());
					if (targetElement.hasGenscene())
					{
						GenscenePropertyManager.getInstance().setSceneIdValue(project, targetElement.getSceneIdFile());
						if (DwpProjectManager.getInstance().isCppProject(project))
						    GenscenePropertyManager.getInstance().setFixedPointValue(project, targetElement.isFixedPoint());
					}
					
					// Set the genmedia properties.
					targetMgr.setGenmediaValue(project, targetElement.hasGenmedia());
					if (targetElement.hasGenmedia())
					{
					    GenmediaPropertyManager.getInstance().setBomValue(project, targetElement.getBomFile());
					}
					
					// Set the gentables properties.
					targetMgr.setGentablesValue(project, targetElement.hasGentables());
					if (targetElement.hasGentables())
					{
					    GentablesPropertyManager.getInstance().setJavaDefaults(project);
					}
					
					// Set the genppscript properties.
					targetMgr.setGenppscriptValue(project, targetElement.hasGenppscript());
					if (targetElement.hasGenppscript())
					{
						String dpp = targetElement.getDigitalPlayprint();
						//dpp = dpp.substring(dpp.lastIndexOf("/") + 1);
						GenppscriptPropertyManager.getInstance().setDppValue(project, dpp);
						GenppscriptPropertyManager.getInstance().setScriptValue(project, targetElement.getScriptFile());
						GenppscriptPropertyManager.getInstance().setTocValue(project, targetElement.getTocName());
					}
					
					// Set the gendpp properties.
					targetMgr.setGendppValue(project, targetElement.hasGendpp());
					if (targetElement.hasGendpp())
					{
						GendppPropertyManager.getInstance().setScriptValue(project, targetElement.getScriptPath());
						GendppPropertyManager.getInstance().setSourceDirValue(project, targetElement.getSourceDirectory());
					}
		        }
			} catch (DwpException e) {
				MleLog.logError(e, "Unable to configure mastering target " + project.getName() + ": " + e.getMessage());
			} catch (DppException e) {
				MleLog.logError(e, "Unable to configure mastering target " + project.getName() + ": " + e.getMessage());
			}
        }
    }
    
    // Update the meta-data file.
    protected void updateMetaDataFile(IProject project, MleTitleMetaData metadata)
        throws FileNotFoundException, IOException, CoreException
    {
        File metaDataFile;

        if (m_foundFile != null)
        {
            // Use the existing meta-data file.
            metaDataFile = m_foundFile;
        }
        else
        {
            // Create a new meta-data file.
            IPath filepath = project.getLocation();
            filepath = filepath.append(MleStudioProjectManager.MLE_STUDIO_PROJECT_METADATA_FILENAME);
            metaDataFile = filepath.toFile();
        }

        // Create a new JAXB marshaller.
        JaxbMetaDataMarshaller marshaller = new JaxbMetaDataMarshaller();

        // Marshall the meta-data.
        OutputStream output = new BufferedOutputStream(new FileOutputStream(metaDataFile));
        marshaller.marshallMetaData(output, metadata);

        output.flush();
        output.close();
    }
}
