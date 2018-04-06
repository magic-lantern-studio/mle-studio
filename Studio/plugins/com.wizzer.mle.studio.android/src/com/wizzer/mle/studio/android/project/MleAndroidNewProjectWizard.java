// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.android.project;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.wizzer.mle.studio.android.internal.wizards.MleTargetState;
import com.wizzer.mle.studio.android.internal.wizards.NewProjectWizard;
import com.wizzer.mle.studio.android.internal.wizards.ProjectNamePage;
import com.wizzer.mle.studio.project.MleTargetConfigurationPage;

public class MleAndroidNewProjectWizard extends NewProjectWizard implements IExecutableExtension
{
	// The configuration page for the Magic Lantern platform.
	private MleTargetConfigurationPage m_configPage;
	// The state of the target to be created.
	private MleTargetState m_targetState;
	// The configuration element.
	protected IConfigurationElement m_configElement;

	/**
     * The default constructor.
     */
    public MleAndroidNewProjectWizard()
    {
        super();
        
		// Set the wizard window title.
		//setWindowTitle(MleProjectMessages.getString("MleProjectCreationWizard.title"));
		
		m_targetState = new MleTargetState();
    }
    
    /**
	 * Add pages to the wizard before the wizard opens.
	 * 
	 * @see Wizard#addPages
	 */	
	public void addPages()
	{
		super.addPages();
		
		// Create a configuration page for the Android target platform.
		m_configPage = new MleTargetConfigurationPage("MleAndroidTargetConfigurationWizardPage");
		m_configPage.setTitle(MleProjectMessages.getString("MleAndroidConfigurationWizardPage.pagetitle"));
		m_configPage.setDescription(MleProjectMessages.getString("MleAndroidConfigurationWizardPage.pagedescription"));
		addPage(m_configPage);
		
		mValues.mleTarget = m_targetState;
	}
	
	/**
	 * Stores the configuration element for the wizard.  The configuration element will be
	 * used in <code>performFinish</code> to set the result perspective.
	 * 
	 * @param cfig The configuration element used to trigger this execution.
	 * It can be queried by the executable extension for specific configuration properties.
	 * @param propertyName The name of an attribute of the configuration element used on
	 * the createExecutableExtension(String) call. This argument can be used in the
	 * cases where a single configuration element is used to define multiple executable
	 * extensions.
	 * @param data Data Adapter data in the form of a <code>String</code>,
	 * a <code>Hashtable</code>, or <b>null</b>.
	 * 
	 * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
	 */
	public void setInitializationData(IConfigurationElement cfig, String propertyName, Object data)
	{
		// Remember the configuration element.
		m_configElement = cfig;
	}
	
	/**
     * Wrap-up project creation.
     * 
     * @return <b>true</b> is returned to indicate the finish request was accepted,
     * and <b>false</b> is returned to indicate that the finish request was refused.
     * 
     * @see org.eclipse.jface.wizard.IWizard#performFinish()
     */
    public boolean performFinish()
    {
    	m_targetState.dwpFilename = m_configPage.getDwpFilename();
    	m_targetState.dppFilename = m_configPage.getDppFilename();
    	
    	m_targetState.gencastSelected = m_configPage.isGencastSelected();
    	m_targetState.gensceneSelected = m_configPage.isGensceneSelected();
    	m_targetState.genmrefSelected = m_configPage.isGenmrefSelected();
    	m_targetState.gentablesSelected = m_configPage.isGentablesSelected();
    	m_targetState.gendppscriptSelected = m_configPage.isGendppscriptSelected();
    	m_targetState.gendppSelected = m_configPage.isGendppSelected();
    	
    	m_targetState.rehearsalPlayerSelected = m_configPage.isRehearsalPlayerSelected();
    	
    	//m_targetState.useTemplate = m_configPage.useTemplate();
    	//m_targetState.templateType = m_configPage.getTemplateType();
    	m_targetState.useTemplate = false;
    	m_targetState.templateType = 0;
    			
    	// Get the "import" elements. Import elements are Zip files containing
    	// resources that will become part of the new MLE Project.
    	IConfigurationElement[] imports = m_configElement.getChildren("import");
    	m_targetState.applicationImports = imports;
    	
        boolean status = super.performFinish();
        
        return status;
    }
    
	// Set the default DWP and DPP file names.
	private void setDefaultFilenames(MleTargetConfigurationPage page)
	{
	    if (! page.isDwpSpecified())
	        page.setDwpFilename(getProjectName() + ".dwp");
        if (! page.isDppSpecified())
            page.setDppFilename(getProjectName() + ".dpp");
	}

	/**
	 * Get the successor of the given page.
	 * 
	 * @param page The reference page to use.
	 * 
	 * @return The next page is returned, or <b>null</b> if there is none.
	 */
	public IWizardPage getNextPage(IWizardPage page)
	{
		if (page instanceof ProjectNamePage)
		{
            setDefaultFilenames(m_configPage);
		}
		
		return super.getNextPage(page);
	}

}
