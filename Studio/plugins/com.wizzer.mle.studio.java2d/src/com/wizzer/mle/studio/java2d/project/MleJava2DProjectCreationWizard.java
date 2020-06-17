/*
 * MleJava2DProjectCreationWizard.java
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
package com.wizzer.mle.studio.java2d.project;

// Import standard Java classes.
import java.lang.reflect.InvocationTargetException;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.operation.IRunnableWithProgress;

import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;

import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;

// Import Magic Lantern DWP classes.

// Import Magic Lantern DPP classes.

// Import Magic Lantern classes.
import com.wizzer.mle.studio.java2d.Java2DLog;
import com.wizzer.mle.studio.java2d.Java2DPlugin;
import com.wizzer.mle.studio.project.MleProjectCreationWizard;
import com.wizzer.mle.studio.project.MleProjectMessages;
import com.wizzer.mle.studio.project.MleTargetConfigurationPage;
import com.wizzer.mle.studio.project.MleTemplatePage;


/**
 * This class implements a wizard for creating a new Magic Lantern
 * Java2D Project.
 * 
 * @author Mark S. Millard
 */
public class MleJava2DProjectCreationWizard extends MleProjectCreationWizard
{
	// The main wizard creation page.
	private NewJavaProjectWizardPageOne m_mainPage;
	// The configuration page for the Java2d platform.
	private MleTargetConfigurationPage m_j2dConfigPage;
	// The Magic Lantern templates.
	private MleTemplatePage m_templatePage;
	// The Java capability configuration page.
	private MleJava2DCapabilityConfigurationPage m_javaPage;

	/**
     * The default constructor.
     */
    public MleJava2DProjectCreationWizard()
    {
        super();
        
		// Set the wizard window title.
		setWindowTitle(MleProjectMessages.getString("MleProjectCreationWizard.title"));
    }

	/**
	 * Add pages to the wizard before the wizard opens.
	 * 
	 * @see Wizard#addPages
	 */	
	public void addPages()
	{
		super.addPages();
		
		// Add the main project creation page.
		m_mainPage = new NewJavaProjectWizardPageOne();
		m_mainPage.setTitle(MleProjectMessages.getString("MleProjectCreationWizardPage.pagetitle"));
		m_mainPage.setDescription(MleProjectMessages.getString("MleProjectCreationWizardPage.pagedescription"));
		m_mainPage.setImageDescriptor(Java2DPlugin.getDefault().getImageDescriptor("icons/full/wizban/newprj_wiz.gif"));
		addPage(m_mainPage);
		
		// Create a configuration page for the Java2d target platform.
		m_j2dConfigPage = new MleTargetConfigurationPage("MleJ2dTargetConfigurationWizardPage");
		m_j2dConfigPage.setTitle(MleProjectMessages.getString("MleJ2dConfigurationWizardPage.pagetitle"));
		m_j2dConfigPage.setDescription(MleProjectMessages.getString("MleJ2dConfigurationWizardPage.pagedescription"));
		addPage(m_j2dConfigPage);
		
		m_templatePage = new MleTemplatePage("MleTemplateConfigurationWizardPage");
		m_templatePage.setTitle(MleProjectMessages.getString("MleTemplateConfigurationWizardPage.pagetitle"));
		m_templatePage.setDescription(MleProjectMessages.getString("MleTemplateConfigurationWizardPage.pagedescription"));
		addPage(m_templatePage);

		// Create a configuration page for the Java capabilities.
		m_javaPage = new MleJava2DCapabilityConfigurationPage(m_mainPage, m_j2dConfigPage, m_templatePage, m_configElement);
		addPage(m_javaPage);
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
    	/*
	    MleTargetCreationOperation runnable = new MleJavaProjectCreationOperation(
	        m_mainPage, m_j2dConfigPage, m_javaPage, new MleProjectOverwriteQuery(getShell()));
		
		IRunnableWithProgress op = new WorkspaceModifyDelegatingOperation(runnable);
		try
		{
			getContainer().run(false, true, op);
		} catch (InvocationTargetException ex)
		{
			handleException(ex.getTargetException());
			return false;
		} catch (InterruptedException ex)
		{
			return false;
		}
		*/
    	
    	// Make sure the wizard pages are complete.
    	try
    	{
    		NullProgressMonitor monitor = new NullProgressMonitor();
    	    //m_mainPage.performFinish(monitor);
    	    m_j2dConfigPage.performFinish(monitor);
    	    m_javaPage.performFinish(monitor);
    	} catch (InterruptedException ex)
    	{
    		return false;
    	} catch (CoreException ex)
    	{
    		return false;
    	}
 
		// Update the resources in the final perspective.
		BasicNewProjectResourceWizard.updatePerspective(m_configElement);
		IResource[] resources = m_javaPage.getElementsToOpen();
		if (resources != null)
		{
			for (int i = 0; i < resources.length; i++)
			    // Open the MLE project resource.
			    openResource(resources[i]);
		}
		
		return true;
    }

	// Set the default DWP and DPP file names.
	private void setDefaultFilenames(MleTargetConfigurationPage page)
	{
	    if (! page.isDwpSpecified())
	        page.setDwpFilename(m_mainPage.getProjectName() + ".dwp");
        if (! page.isDppSpecified())
            page.setDppFilename(m_mainPage.getProjectName() + ".dpp");
	}
	
	public boolean performCancel()
	{
		boolean status = true;
		m_javaPage.performCancel();
		return status;
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
		if (page instanceof NewJavaProjectWizardPageOne)
		{
            setDefaultFilenames(m_j2dConfigPage);
        	return m_j2dConfigPage;
		}
		
		return super.getNextPage(page);
	}

}
