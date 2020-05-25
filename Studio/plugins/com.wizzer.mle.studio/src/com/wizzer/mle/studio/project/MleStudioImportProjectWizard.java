// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.project;

// Import standard Java classes.
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

// Import Eclipse classes.
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.MlePlugin;
import com.wizzer.mle.studio.framework.Find;
import com.wizzer.mle.studio.framework.FindEvent;
import com.wizzer.mle.studio.framework.FindFilter;
import com.wizzer.mle.studio.framework.IFindListener;
import com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;

/**
 * This abstract class is a <code>Wizard</code> for importing existing
 * Magic Lantern Projects.
 * 
 * @author Mark S. Millard
 */
public abstract class MleStudioImportProjectWizard extends Wizard implements IImportWizard
{
    // The import project page.
    protected MleStudioImportProjectPage m_projectPage;
    // The meta-data page.
    protected MleStudioMetaDataPage m_metaDataPage;

    /**
     * The default constructor.
     */
    public MleStudioImportProjectWizard()
    {
    	super();
    }
    
    /**
     * Add pages to the wizard before the wizard opens.
     * 
     * @see Wizard#addPages
     */
    public void addPages()
    {
        ImageDescriptor idesc = MlePlugin.getDefault().getImageDescriptor("icons/importprj_wiz.gif");

        // Add a page that allows the user to specify an OCAP Project.
        m_projectPage = new MleStudioImportProjectPage();
        m_projectPage.setTitle(MleImportProjectMessages.getString("MleStudioImportProjectPage.pagetitle"));
        m_projectPage.setDescription(MleImportProjectMessages.getString("MleStudioImportProjectPage.pagedescription"));
        m_projectPage.setImageDescriptor(idesc);
        addPage(m_projectPage);

        // Add a page that allows the user to specify the OCAP Project
        // meta-data.
        String pageTitle = MleImportProjectMessages.getString("MleStudioMetaDataPage.pagetitle");
        m_metaDataPage = new MleStudioMetaDataPage(pageTitle);
        m_metaDataPage.setTitle(pageTitle);
        m_metaDataPage.setDescription(MleImportProjectMessages.getString("MleStudioMetaDataPage.pagedescription"));
        m_metaDataPage.setImageDescriptor(idesc);
        addPage(m_metaDataPage);
    }

	/**
     * Initializes this creation wizard using the passed workbench and object
     * selection.
     * 
     * @param workbench The workbench.
     * @param selection The selected objects.
     * 
     * @see org.eclipse.ui.IWorkbenchWizard#init(org.eclipse.ui.IWorkbench,
     *      org.eclipse.jface.viewers.IStructuredSelection)
     */
	public void init(IWorkbench workbench, IStructuredSelection selection)
	{
		// Do nothing for now.
	}

	// Recursively check the project hierarchy to determine whether the project
    // file system has a .mleproject file.
    private File m_foundFile = null;

    // Determine if the project has a .mleproject file.
    protected File hasProjectMetaData() throws CoreException
    {
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
        IPath location = m_projectPage.getLocationPath();
        finder.doName(location.toOSString());

        return m_foundFile;
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
    
    // Get the workspace from the plugin.
    protected IWorkspace getWorkspace()
    {
        IWorkspace workspace = MlePlugin.getWorkspace();
        return workspace;
    }
}
