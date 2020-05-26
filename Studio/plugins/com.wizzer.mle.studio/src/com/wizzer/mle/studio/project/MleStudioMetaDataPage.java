// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.project;

// Import Eclipse classes.
import java.util.ArrayList;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

// Import Magic Lantern classes.
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;

/**
 * This class implements a wizard page for specifying Magic Lantern Project meta-data.
 * 
 * @author Mark S. Millard
 */
public class MleStudioMetaDataPage extends WizardPage implements IMleStudioMetaDataEventListener
{
    // The page control.
    private MleStudioMetaDataControl m_control = null;
    // The associated project;
    private IProject m_project = null;

    // The project's path.
    private IPath m_locationPath;

    /**
     * @param pageName The name of the page.
     */
    public MleStudioMetaDataPage(String pageName)
    {
        super(pageName);
    }

    /**
     * @param pageName The name of the page.
     * @param title The title of the page.
     * @param titleImage An image for the page.
     */
    public MleStudioMetaDataPage(String pageName, String title, ImageDescriptor titleImage)
    {
        super(pageName, title, titleImage);
    }

    /**
     * Create the control for the meta-data.
     * 
     * @param parent The parent <code>Composite</code> widget.
     * 
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent)
    {
        // Create the control for setting the meta-data.
        m_control = new MleStudioMetaDataControl();
        MleStudioMetaDataControl.MetaDataControlConfig configuration = m_control.new MetaDataControlConfig();
        Control control = m_control.createControl(parent, SWT.NULL, configuration);
        m_control.addMetaDataListener(this);
        setControl(control);
    }

    /**
     * Update the control with the specified meta-data.
     * 
     * @param data The meta-data to update.
     */
    public void updateControl(MleTitleMetaData data)
    {    	
        Object[] projectData = data.getProjectData();
        MleTitleMetaData.ProjectDataElement projectElement = (MleTitleMetaData.ProjectDataElement) projectData[0];
        m_control.setProjectId(projectElement.getId());
        m_control.setProjectVersion(projectElement.getVersion());
        m_control.setDigitalWorkprint(projectElement.getDwpFile());

        Object[] targetData = projectElement.getMasterTargets();
        MleTitleMetaData.MasterTargetElement targetElement = (MleTitleMetaData.MasterTargetElement) targetData[0];
        m_control.setTargetType(targetElement.getType());
        m_control.setTargetId(targetElement.getId());
        m_control.setTargetDigitalPlayprint(targetElement.getDigitalPlayprint());
        
        m_control.setVerbose(targetElement.isVerbose());
        m_control.setDestinationDirectory(targetElement.getDestinationDirectroy());
        String codeGeneration = targetElement.getCodeGeneration();
        if (codeGeneration.equals("java"))
        {
        	m_control.setJavaCodeGeneration();
        	m_control.setActorID(targetElement.getActorIdFile());
        	m_control.setGroupID(targetElement.getGroupIdFile());
        	m_control.setBigEndian(targetElement.isBigEndian());
        	m_control.setHeaderPackage(targetElement.getHeaderPackage());
        } else if (codeGeneration.equals("cpp"))
        {
        	m_control.setCppCodeGeneration();
        	m_control.setActorID(targetElement.getActorIdFile());
        	m_control.setGroupID(targetElement.getGroupIdFile());
        	m_control.setFixedPoint(targetElement.isFixedPoint());
        	m_control.setBigEndian(targetElement.isBigEndian());
        	m_control.setHeaderPackage(targetElement.getHeaderPackage());
        } else
        {
        	IllegalArgumentException ex = new IllegalArgumentException();
        	MleLog.logError(ex, "Invalid code generation type.");
        }
        m_control.setSceneID(targetElement.getSceneIdFile());
        m_control.setBom(targetElement.getBomFile());
        m_control.setScript(targetElement.getScriptFile());
        m_control.setTocName(targetElement.getTocName());
        m_control.setScriptPath(targetElement.getScriptPath());
        m_control.setSourceDirectory(targetElement.getSourceDirectory());
        ArrayList<String> tags = targetElement.getTags();
        String[] tagValues = new String[0];
        tagValues = tags.toArray(tagValues);
        m_control.setTags(tagValues);
    }

    /**
     * Set the specified project.
     * 
     * @param project The <code>IProject</code> associated with the page.
     */
    public void setProject(IProject project)
    {
        m_project = project;
    }

    public void setLocationPath(IPath locationPath)
    {
        this.m_locationPath = locationPath;
        m_control.setLocationPath(locationPath);
    }

    /**
     * Retrieve the meta-data.
     * 
     * @return An instance of the meta-data is returned.
     */
    public MleTitleMetaData getMetaData()
    {
        return m_control.getMetaData();
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
            this.setPageComplete(true);
        else if (event.m_state == MleStudioMetaDataEvent.ERROR)
        	this.setPageComplete(false);
    }
}
