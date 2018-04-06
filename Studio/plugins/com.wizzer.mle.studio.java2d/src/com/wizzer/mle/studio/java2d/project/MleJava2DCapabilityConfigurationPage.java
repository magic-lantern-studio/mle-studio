// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.java2d.project;

// Import standard Java classes.
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipFile;

// Import Eclipse classes.
import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceDescription;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.corext.util.Messages;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.dialogs.StatusInfo;
import org.eclipse.jdt.internal.ui.util.CoreUtility;
import org.eclipse.jdt.internal.ui.util.ExceptionHandler;
import org.eclipse.jdt.internal.ui.wizards.ClassPathDetector;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageOne;
import org.eclipse.jdt.ui.wizards.NewJavaProjectWizardPageTwo;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyDelegatingOperation;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

// Import Magic Lantern classes.
import com.wizzer.mle.codegen.IDwpConfiguration;
import com.wizzer.mle.studio.MleException;
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
import com.wizzer.mle.studio.dwp.java.MleSimpleDwp;
import com.wizzer.mle.studio.natures.MleStudioNature;
import com.wizzer.mle.studio.project.MleProjectMessages;
import com.wizzer.mle.studio.project.MleProjectOverwriteQuery;
import com.wizzer.mle.studio.project.MleStudioProjectManager;
import com.wizzer.mle.studio.project.MleTargetConfigurationPage;
import com.wizzer.mle.studio.project.MleTemplatePage;
import com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;
import com.wizzer.mle.studio.runtimes.ApplicationTemplateManager;
import com.wizzer.mle.studio.runtimes.IApplicationTemplate;
import com.wizzer.mle.studio.runtimes.IApplicationTemplateEntry;

import com.wizzer.mle.studio.java2d.Java2DLog;
import com.wizzer.mle.studio.java2d.Java2DPlugin;


/**
 * Page two of the 'New Magic Lantern Project Wizard'.  Provides UI and project creation logic.
 * 
 * This control is designed to be sub-classed.  
 * Some common methods to allow specialization of this control are:
 * <ul>
 * <li>{@link getNatures} - Natures (other than Java Nature) to add to created project
 * </li>
 * <li>{@link configureJavaProjectSettings} - create the map of JDT settings to assign to the project.
 * </li>
 * </ul>
 * 
 * @see MleJava2DProjectCreationWizard
 */

@SuppressWarnings("restriction")
public class MleJava2DCapabilityConfigurationPage extends JavaCapabilityConfigurationPage {
	private static final String FILENAME_PROJECT= ".project"; //$NON-NLS-1$
	private static final String FILENAME_CLASSPATH= ".classpath"; //$NON-NLS-1$
    public static String MLE_ROOT_DEFAULT = "<Unknown MLE_ROOT>";

	private final NewJavaProjectWizardPageOne fFirstPage;
	private final MleTargetConfigurationPage fConfigPage;
	private final MleTemplatePage fTemplatePage;

	private URI fCurrProjectLocation; // null if location is platform location
	private IProject fCurrProject;

	private boolean fKeepContent;

	private File fDotProjectBackup;
	private File fDotClasspathBackup;
	private Boolean fIsAutobuild;
	private HashSet<IFileStore> fOrginalFolders;
	
	private IConfigurationElement fConfigElement;
	// The resource to open.
	private ArrayList m_elementsToOpen;

	/**
	 * Constructor for the {@link NewJavaProjectWizardPageTwo}.
	 * 
	 * @param mainPage the first page of the wizard
	 */
	public MleJava2DCapabilityConfigurationPage(
		NewJavaProjectWizardPageOne mainPage,
		MleTargetConfigurationPage configPage,
		MleTemplatePage templatePage,
		IConfigurationElement configElement) {
		fFirstPage= mainPage;
		fConfigPage = configPage;
		fTemplatePage = templatePage;
		
		fConfigElement = configElement;
		
		fCurrProjectLocation= null;
		fCurrProject= null;
		fKeepContent= false;

		fDotProjectBackup= null;
		fDotClasspathBackup= null;
		fIsAutobuild= null;
		
		if (Java2DPlugin.isLinux())	{
			MLE_ROOT_DEFAULT = "/opt/MagicLantern";
		} else if (Java2DPlugin.isWindows()) {
			MLE_ROOT_DEFAULT = "C:\\Program Files\\Wizzer Works\\MagicLantern";
		} else {
			MLE_ROOT_DEFAULT = "<Unknown MLE_ROOT>";
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jdt.ui.wizards.JavaCapabilityConfigurationPage#useNewSourcePage()
	 */
	protected final boolean useNewSourcePage() {
		return true;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		boolean isShownFirstTime= visible && fCurrProject == null;
		if (visible) {
			if (isShownFirstTime) { // entering from the first page
				createProvisonalProject();
			}
		} else {
			if (getContainer().getCurrentPage() == fFirstPage) { // leaving back to the first page
				removeProvisonalProject();
			}
		}
		super.setVisible(visible);
		if (isShownFirstTime) {
			setFocus();
		}
	}

	private boolean hasExistingContent(URI realLocation) throws CoreException {
		IFileStore file= EFS.getStore(realLocation);
		return file.fetchInfo().exists();
	}

	private IStatus changeToNewProject() {
		class UpdateRunnable implements IRunnableWithProgress {
			public IStatus infoStatus= Status.OK_STATUS;

			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				try {
					if (fIsAutobuild == null) {
						fIsAutobuild= Boolean.valueOf(setAutoBuilding(false));
					}
					infoStatus= updateProject(monitor);
				} catch (CoreException e) {
					throw new InvocationTargetException(e);
				} catch (OperationCanceledException e) {
					throw new InterruptedException();
				} finally {
					monitor.done();
				}
			}
		}
		UpdateRunnable op= new UpdateRunnable();
		try {
			getContainer().run(true, false, new WorkspaceModifyDelegatingOperation(op));
			return op.infoStatus;
		} catch (InvocationTargetException e) {
			final String title= NewWizardMessages.NewJavaProjectWizardPageTwo_error_title; 
			final String message= NewWizardMessages.NewJavaProjectWizardPageTwo_error_message; 
			ExceptionHandler.handle(e, getShell(), title, message);
		} catch  (InterruptedException e) {
			// cancel pressed
		}
		return null;
	}

    private boolean setAutoBuilding(boolean state) throws CoreException {
        IWorkspace workspace= ResourcesPlugin.getWorkspace();
        IWorkspaceDescription desc= workspace.getDescription();
        boolean isAutoBuilding= desc.isAutoBuilding();
        if (isAutoBuilding != state) {
            desc.setAutoBuilding(state);
            workspace.setDescription(desc);
        }
        return isAutoBuilding;
    }
	private static URI getRealLocation(String projectName, URI location) {
		if (location == null) {  // inside workspace
			try {
				URI rootLocation= ResourcesPlugin.getWorkspace().getRoot().getLocationURI();

				location= new URI(rootLocation.getScheme(), null,
						Path.fromPortableString(rootLocation.getPath()).append(projectName).toString(),
						null);
			} catch (URISyntaxException e) {
				Assert.isTrue(false, "Can't happen"); //$NON-NLS-1$
			}
		}
		return location;
	}

	private final IStatus updateProject(IProgressMonitor monitor) throws CoreException, InterruptedException {
		IStatus result= StatusInfo.OK_STATUS;
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}
		try {
			monitor.beginTask(NewWizardMessages.NewJavaProjectWizardPageTwo_operation_initialize, 7); 
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			String projectName= fFirstPage.getProjectName();

			fCurrProject= ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
			fCurrProjectLocation= fFirstPage.getProjectLocationURI();

			URI realLocation= getRealLocation(projectName, fCurrProjectLocation);
			fKeepContent= hasExistingContent(realLocation);

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			if (fKeepContent) {
				rememberExistingFiles(realLocation);
				rememberExisitingFolders(realLocation);
			}

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			try {
				createProject(fCurrProject, fCurrProjectLocation, new SubProgressMonitor(monitor, 2));
			} catch (CoreException e) {
				if (e.getStatus().getCode() == IResourceStatus.FAILED_READ_METADATA) {					
					result= new StatusInfo(IStatus.INFO, Messages.format(NewWizardMessages.NewJavaProjectWizardPageTwo_DeleteCorruptProjectFile_message, e.getLocalizedMessage()));

					deleteProjectFile(realLocation);
					if (fCurrProject.exists())
						fCurrProject.delete(true, null);

					createProject(fCurrProject, fCurrProjectLocation, null);					
				} else {
					throw e;
				}	
			}

			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			initializeBuildPath(JavaCore.create(fCurrProject), new SubProgressMonitor(monitor, 2));
			configureJavaProject(new SubProgressMonitor(monitor, 3)); // create the Java project to allow the use of the new source folder page
		} finally {
			monitor.done();
		}
		return result;
	}

	/**
	 * Evaluates the new build path and output folder according to the settings on the first page.
	 * The resulting build path is set by calling {@link #init(IJavaProject, IPath, IClasspathEntry[], boolean)}.
	 * Clients can override this method.
	 * 
	 * @param javaProject the new project which is already created when this method is called.
	 * @param monitor the progress monitor
	 * @throws CoreException thrown when initializing the build path failed
	 */
	protected void initializeBuildPath(IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}
		monitor.beginTask(NewWizardMessages.NewJavaProjectWizardPageTwo_monitor_init_build_path, 2);

		try {
			IClasspathEntry[] entries= null;
			IPath outputLocation= null;
			IProject project= javaProject.getProject();

			if (fKeepContent) {
				if (!project.getFile(FILENAME_CLASSPATH).exists()) { 
					final ClassPathDetector detector= new ClassPathDetector(fCurrProject, new SubProgressMonitor(monitor, 2));
					entries= detector.getClasspath();
					outputLocation= detector.getOutputLocation();
					if (entries.length == 0)
						entries= null;
				} else {
					monitor.worked(2);
				}
			} else {
				List<IClasspathEntry> cpEntries = new ArrayList<IClasspathEntry>();
				IWorkspaceRoot root= project.getWorkspace().getRoot();

				IClasspathEntry[] sourceClasspathEntries= fFirstPage.getSourceClasspathEntries();
				for (int i= 0; i < sourceClasspathEntries.length; i++) {
					IPath path= sourceClasspathEntries[i].getPath();
					if (path.segmentCount() > 1) {
						IFolder folder= root.getFolder(path);
						CoreUtility.createFolder(folder, true, true, new SubProgressMonitor(monitor, 1));
					}
					cpEntries.add(sourceClasspathEntries[i]);
				}
				
				// Add Magic Lantern specific entries to the classpath.
				addMleEntries(cpEntries);

				cpEntries.addAll(Arrays.asList(fFirstPage.getDefaultClasspathEntries()));

				entries= (IClasspathEntry[]) cpEntries.toArray(new IClasspathEntry[cpEntries.size()]);

				outputLocation= fFirstPage.getOutputLocation();
				if (outputLocation.segmentCount() > 1) {
					IFolder folder= root.getFolder(outputLocation);
					CoreUtility.createDerivedFolder(folder, true, true, new SubProgressMonitor(monitor, 1));
				}
			}
			if (monitor.isCanceled()) {
				throw new OperationCanceledException();
			}

			init(javaProject, outputLocation, entries, false);
		} finally {
			monitor.done();
		}
	}

	private void deleteProjectFile(URI projectLocation) throws CoreException {
		IFileStore file= EFS.getStore(projectLocation);
		if (file.fetchInfo().exists()) {
			IFileStore projectFile= file.getChild(FILENAME_PROJECT);
			if (projectFile.fetchInfo().exists()) {
				projectFile.delete(EFS.NONE, null);
			}
		}
	}

	private void rememberExisitingFolders(URI projectLocation) {
		fOrginalFolders = new HashSet<IFileStore>();

		try {
			IFileStore[] children= EFS.getStore(projectLocation).childStores(EFS.NONE, null);
			for (int i= 0; i < children.length; i++) {
				IFileStore child= children[i];
				IFileInfo info= child.fetchInfo();
				if (info.isDirectory() && info.exists() && !fOrginalFolders.contains(child.getName())) {
					fOrginalFolders.add(child);
				}
			}
		} catch (CoreException e) {
			JavaPlugin.log(e);
		}
	}

	private void restoreExistingFolders(URI projectLocation) {
		try {
			IFileStore[] children= EFS.getStore(projectLocation).childStores(EFS.NONE, null);
			for (int i= 0; i < children.length; i++) {
				IFileStore child= children[i];
				IFileInfo info= child.fetchInfo();
				if (info.isDirectory() && info.exists() && !fOrginalFolders.contains(child)) {
					child.delete(EFS.NONE, null);
					fOrginalFolders.remove(child);
				}
			}

			for (Iterator<IFileStore> iterator = fOrginalFolders.iterator(); iterator.hasNext();) {
				IFileStore deleted = iterator.next();
				deleted.mkdir(EFS.NONE, null);
			}
		} catch (CoreException e) {
			JavaPlugin.log(e);
		}
	}

	private void rememberExistingFiles(URI projectLocation) throws CoreException {
		fDotProjectBackup= null;
		fDotClasspathBackup= null;

		IFileStore file= EFS.getStore(projectLocation);
		if (file.fetchInfo().exists()) {
			IFileStore projectFile= file.getChild(FILENAME_PROJECT);
			if (projectFile.fetchInfo().exists()) {
				fDotProjectBackup= createBackup(projectFile, "project-desc"); //$NON-NLS-1$ 
			}
			IFileStore classpathFile= file.getChild(FILENAME_CLASSPATH);
			if (classpathFile.fetchInfo().exists()) {
				fDotClasspathBackup= createBackup(classpathFile, "classpath-desc"); //$NON-NLS-1$ 
			}
		}
	}

	private void restoreExistingFiles(URI projectLocation, IProgressMonitor monitor) throws CoreException {
		int ticks= ((fDotProjectBackup != null ? 1 : 0) + (fDotClasspathBackup != null ? 1 : 0)) * 2;
		monitor.beginTask("", ticks); //$NON-NLS-1$
		try {
			IFileStore projectFile= EFS.getStore(projectLocation).getChild(FILENAME_PROJECT);
			projectFile.delete(EFS.NONE, new SubProgressMonitor(monitor, 1));
			if (fDotProjectBackup != null) {
				copyFile(fDotProjectBackup, projectFile, new SubProgressMonitor(monitor, 1));
			}
		} catch (IOException e) {
			IStatus status= new Status(IStatus.ERROR, JavaUI.ID_PLUGIN, IStatus.ERROR, NewWizardMessages.NewJavaProjectWizardPageTwo_problem_restore_project, e); 
			throw new CoreException(status);
		}
		try {
			IFileStore classpathFile= EFS.getStore(projectLocation).getChild(FILENAME_CLASSPATH);
			classpathFile.delete(EFS.NONE, new SubProgressMonitor(monitor, 1));
			if (fDotClasspathBackup != null) {
				copyFile(fDotClasspathBackup, classpathFile, new SubProgressMonitor(monitor, 1));
			}
		} catch (IOException e) {
			IStatus status= new Status(IStatus.ERROR, JavaUI.ID_PLUGIN, IStatus.ERROR, NewWizardMessages.NewJavaProjectWizardPageTwo_problem_restore_classpath, e); 
			throw new CoreException(status);
		}
	}

	private File createBackup(IFileStore source, String name) throws CoreException {
		try {
			File bak= File.createTempFile("eclipse-" + name, ".bak");  //$NON-NLS-1$//$NON-NLS-2$
			copyFile(source, bak);
			return bak;
		} catch (IOException e) {
			IStatus status= new Status(IStatus.ERROR, JavaUI.ID_PLUGIN, IStatus.ERROR, Messages.format(NewWizardMessages.NewJavaProjectWizardPageTwo_problem_backup, name), e); 
			throw new CoreException(status);
		} 
	}

	private void copyFile(IFileStore source, File target) throws IOException, CoreException {
		InputStream is= source.openInputStream(EFS.NONE, null);
		FileOutputStream os= new FileOutputStream(target);
		copyFile(is, os);
	}

	private void copyFile(File source, IFileStore target, IProgressMonitor monitor) throws IOException, CoreException {
		FileInputStream is= new FileInputStream(source);
		OutputStream os= target.openOutputStream(EFS.NONE, monitor);
		copyFile(is, os);
	}

	private void copyFile(InputStream is, OutputStream os) throws IOException {		
		try {
			byte[] buffer = new byte[8192];
			while (true) {
				int bytesRead= is.read(buffer);
				if (bytesRead == -1)
					break;

				os.write(buffer, 0, bytesRead);
			}
		} finally {
			try {
				is.close();
			} finally {
				os.close();
			}
		}
	}

	/**
	 * Creates the provisional project on which the wizard is working on. The provisional project is typically
	 * created when the page is entered the first time. The early project creation is required to configure linked folders. 
	 * 
	 * @return the provisional project 
	 */
	protected IProject createProvisonalProject() {
		IStatus status= changeToNewProject();
		if (status != null && !status.isOK()) {
			ErrorDialog.openError(getShell(), NewWizardMessages.NewJavaProjectWizardPageTwo_error_title, null, status);
		}
		return fCurrProject;
	}
	
	/**
	 * Removes the provisional project. The provisional project is typically removed when the user cancels the wizard or goes
	 * back to the first page.
	 */
	protected void removeProvisonalProject() { 
		if (!fCurrProject.exists()) {
			fCurrProject= null;
			return;
		}

		IRunnableWithProgress op= new IRunnableWithProgress() {
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				doRemoveProject(monitor);
			}
		};

		try {
			getContainer().run(true, true, new WorkspaceModifyDelegatingOperation(op));
		} catch (InvocationTargetException e) {
			final String title= MleProjectMessages.getString("OcapCapabilityConfigurationPage.error_remove_title"); 
			final String message= NewWizardMessages.NewJavaProjectWizardPageTwo_error_remove_message; 
			ExceptionHandler.handle(e, getShell(), title, message);		
		} catch  (InterruptedException e) {
			// cancel pressed
		}
	}

	private final void doRemoveProject(IProgressMonitor monitor) throws InvocationTargetException {
		final boolean noProgressMonitor= (fCurrProjectLocation == null); // inside workspace
		if (monitor == null || noProgressMonitor) {
			monitor= new NullProgressMonitor();
		}
		monitor.beginTask(NewWizardMessages.NewJavaProjectWizardPageTwo_operation_remove, 3); 
		try {
			try {
				URI projLoc= fCurrProject.getLocationURI();

				boolean removeContent= !fKeepContent && fCurrProject.isSynchronized(IResource.DEPTH_INFINITE);
				if (!removeContent) {
					restoreExistingFolders(projLoc);
				}
				fCurrProject.delete(removeContent, false, new SubProgressMonitor(monitor, 2));

				restoreExistingFiles(projLoc, new SubProgressMonitor(monitor, 1));
			} finally {
				setAutoBuilding(fIsAutobuild.booleanValue()); // fIsAutobuild must be set
				fIsAutobuild= null;
			}
		} catch (CoreException e) {
			throw new InvocationTargetException(e);
		} finally {
			monitor.done();
			fCurrProject= null;
			fKeepContent= false;
		}
	}

	/**
	 * Called from the wizard on cancel.
	 */
	public void performCancel() {
		if (fCurrProject != null) {
			removeProvisonalProject();
		}
	}     

	/**
	 * Called from the wizard on finish.
	 * 
	 * @param monitor the progress monitor
	 * @throws CoreException thrown when the project creation or configuration failed
	 * @throws InterruptedException thrown when the user canceled the project creation
	 */
	@SuppressWarnings("unchecked")
	public void performFinish(IProgressMonitor monitor) throws CoreException, InterruptedException {
		try {
			monitor.beginTask(NewWizardMessages.NewJavaProjectWizardPageTwo_operation_create, 3); 
			if (fCurrProject == null) {
				updateProject(new SubProgressMonitor(monitor, 1));
			}
			configureJavaProject(new SubProgressMonitor(monitor, 2));

			if (!fKeepContent) {
				String compliance= fFirstPage.getCompilerCompliance();
				IJavaProject project= JavaCore.create(fCurrProject);
				Map options = project.getOptions(false);
				
				configureJavaProjectSettings(options, compliance);
				
				if (! options.isEmpty()) {
					project.setOptions(options);
				}

			}
		} finally {
			monitor.done();
			fCurrProject= null;
			if (fIsAutobuild != null) {
				setAutoBuilding(fIsAutobuild.booleanValue());
				fIsAutobuild= null;
			}
		}
	}

	/**
	 * When Wizard finishes, this method configures the Java project and sets any additional project natures
	 * (as provided by getNatures).
	 * 
	 * The default implementation delegates to JavaModelUtil.setCompilanceOptions() and
	 * JavaModelUtil.setDefaultClassfileOptions().
	 * 
	 * This method is designed to be over-ridden.
	 * 
	 * @param options  a Map of the Java compilation options
	 * @param compliance  the Java compliance level
	 */
	@Override
	public void configureJavaProject(IProgressMonitor monitor) throws CoreException, InterruptedException {		
		super.configureJavaProject(monitor);
		
		// Add all project natures
		configureAdditionalProjectNatures(monitor);
		
		// Configure Magic Lantern project dependencies.
		configureMagicLanternProject(fCurrProject);
		
		// Generate source from templates.
		configureProjectSource(monitor);
	}
	
	/**
	 * Get array of natureID to configure this OCAP project.  This is in addition to the Java Nature.
	 * 
	 * Note that order matters.  NatureIds that depend upon one another should appear in the array from most general to most specific because:
	 * <ul>
	 * <li>When adding a natureId, Eclipse checks that the project has been configured with any natures it depends upon.
	 * </li>
	 * <li>The natureIDs in this list are pushed on to the front of the natureId list one at a time.  Eclipse will use the first project nature it finds that has an associated image to decorate the project icon in Package Explorer.  In this array that would be the <i>last</i> nature ID.
	 * </li>
	 * </ul>
     *
	 * @return  Array of nature IDs to configure this OCAP project.
	 * 
	 */
	protected String[] getNatures() {
		return new String[] {
			MleStudioNature.NATURE_ID,
			MasteringNature.NATURE_ID};
	}

	private void configureAdditionalProjectNatures(IProgressMonitor monitor) throws CoreException, InterruptedException {
		if (monitor == null) {
			monitor= new NullProgressMonitor();
		}

		int nSteps= 6;			
		monitor.beginTask(NewWizardMessages.JavaCapabilityConfigurationPage_op_desc_java, nSteps); 
		
		try {
			String[] natures = getNatures();
			for (String natureId : natures) {
				addNatureToProject(natureId, monitor);
			}

	        // Update the project's nature.
			PlatformUI.getWorkbench().getDisplay().syncExec(
			    new Runnable() {
				    public void run() {
					    try {
					    	configureMasteringNature(fCurrProject);
					    } catch (DppException e) {
								// XXX - log an error.
					    }
					}
				});
		} catch (OperationCanceledException e) {
			throw new InterruptedException();
		} finally {
			monitor.done();
		}			

		if (monitor != null && monitor.isCanceled()) {
			throw new OperationCanceledException();
		}

	}

	private void addNatureToProject(String natureId, IProgressMonitor monitor) throws CoreException {
		IProject project= getJavaProject().getProject();

		if (!project.hasNature(natureId)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures= description.getNatureIds();
			String[] newNatures= new String[prevNatures.length + 1];
			
			// Add nature to front of list of natures so we use the first nature to get the image
			newNatures[0]= natureId;
			System.arraycopy(prevNatures, 0, newNatures, 1, prevNatures.length);

			description.setNatureIds(newNatures);
			project.setDescription(description, monitor);
		} else {
			if (monitor != null) {
				monitor.worked(1);
			}
		}
	}

	/**
	 * When Wizard finishes, this method sets the Java project options based on the 
	 * Java compliance level.  
	 * 
	 * This method is designed to be over-ridden.
	 * 
	 * @param options  a Map of the Java compilation options
	 * @param compliance  the Java compliance level
	 */
	@SuppressWarnings("unchecked")
	protected void configureJavaProjectSettings(Map options, String compliance) {
		if (compliance != null) {
			JavaCore.setComplianceOptions(compliance, options);

			options.put(JavaCore.COMPILER_CODEGEN_INLINE_JSR_BYTECODE, JavaCore.DISABLED);
			options.put(JavaCore.COMPILER_LOCAL_VARIABLE_ATTR, JavaCore.GENERATE);
			options.put(JavaCore.COMPILER_LINE_NUMBER_ATTR, JavaCore.GENERATE);
			options.put(JavaCore.COMPILER_SOURCE_FILE_ATTR, JavaCore.GENERATE);
			options.put(JavaCore.COMPILER_CODEGEN_UNUSED_LOCAL, JavaCore.PRESERVE);

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
	                if (fConfigPage.isGencastSelected())
	                {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENGROUP_BUILDER_ID);
		                cmds.add(command);
	                } else
	                    continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENSCENE_BUILDER_ID))
	            {
	                if (fConfigPage.isGensceneSelected())
		                {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENSCENE_BUILDER_ID);
		                cmds.add(command);
	                } else
	                    continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENMEDIA_BUILDER_ID))
	            {
	                if (fConfigPage.isGenmrefSelected())
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENMEDIA_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENTABLES_BUILDER_ID))
	            {
	                if (fConfigPage.isGentablesSelected())
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENTABLES_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENPPSCRIPT_BUILDER_ID))
	            {
	                if (fConfigPage.isGendppscriptSelected())
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENPPSCRIPT_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENDPP_BUILDER_ID))
	            {
	                if (fConfigPage.isGendppSelected())		    
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
	
	private void configureMagicLanternProject(IResource resource)
	{
		final IProject project = resource.getProject();
		final IResource res = resource;

		try
		{
			DwpProjectManager.getInstance().setJavaProject(project);

			Display display = Display.getDefault();
			display.syncExec(new Runnable() {
				public void run() {
					try
					{
			            MasteringProjectManager.getInstance().init(project);
			            if (DwpProjectManager.getInstance().isJavaProject(project))
			            {
					        MasteringProjectManager.getInstance().setJavaDefaults(res);
					        // XXX - Check first to determine if Java2D target.
					        configureJava2dTarget(res);
					    } else
			                throw new DppException("Unknown type of Mastering project.");
		
			            if (fConfigPage.isGencastSelected())
			            	MasteringProjectManager.getInstance().setGengroupValue(res,true);
			            else
			            	MasteringProjectManager.getInstance().setGengroupValue(res,false);
			            
			            if (fConfigPage.isGensceneSelected())
			            	MasteringProjectManager.getInstance().setGensceneValue(res,true);
			            else
			            	MasteringProjectManager.getInstance().setGensceneValue(res,false);
			            
			            if (fConfigPage.isGenmrefSelected())
			            	MasteringProjectManager.getInstance().setGenmediaValue(res,true);
			            else
			            	MasteringProjectManager.getInstance().setGenmediaValue(res,false);
			            
			            if (fConfigPage.isGentablesSelected())
			            	MasteringProjectManager.getInstance().setGentablesValue(res,true);
			            else
			            	MasteringProjectManager.getInstance().setGentablesValue(res,false);
		
				        if (fConfigPage.isGendppscriptSelected())
				        {
				        	MasteringProjectManager.getInstance().setGenppscriptValue(res,true);
		
						    String dpp = fConfigPage.getDppFilename();
						    GenppscriptPropertyManager.getInstance().setDppValue(res,dpp);
				        } else
				        {
				        	MasteringProjectManager.getInstance().setGenppscriptValue(res,false);
				        }
				        
				        if (fConfigPage.isGendppSelected())
				        	MasteringProjectManager.getInstance().setGendppValue(res,true);
				        else
				        	MasteringProjectManager.getInstance().setGendppValue(res,false);

				        // Process meta-data.
				        processMetaData(project);
				        
					} catch (DppException ex)
				    {
				        Java2DLog.logError(ex,"Unable to configure mastering process for resource "
				            + res.getName() + ".");
				    } catch (IOException ex)
				    {
				    	Java2DLog.logError(ex,"Unable to configure Magic Lantern Studio project file for resource "
					        + res.getName() + ".");
				    }
			    }
			});
	    } catch (DwpException ex)
	    {
	        Java2DLog.logError(ex,"Unable to configure mastering process for resource "
		        + resource.getName() + ".");
		}
	}
	
	// Configure the Java2d mastering target.
	private void configureJava2dTarget(IResource resource) throws DppException
	{
	    // Configure gengroup.
	    if (fConfigPage.isGencastSelected())
	        GengroupPropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genscene.
	    if (fConfigPage.isGensceneSelected())
	        GenscenePropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genmedia.
	    if (fConfigPage.isGenmrefSelected())
	        GenmediaPropertyManager.getInstance().setDefaults(resource);
	    // Configure gentables.
	    if (fConfigPage.isGentablesSelected())
	        GentablesPropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genppscript.
	    if (fConfigPage.isGendppscriptSelected())
	        GenppscriptPropertyManager.getInstance().setDefaults(resource);
	    // Congigure gendpp.
	    if (fConfigPage.isGendppSelected())
	        GendppPropertyManager.getInstance().setDefaults(resource);
	}
	
	// Configure the project source.
	private void configureProjectSource(IProgressMonitor monitor)
	{
		final SubProgressMonitor subMonitor = new SubProgressMonitor(monitor, 1);

        // Update the project's nature.
		PlatformUI.getWorkbench().getDisplay().syncExec(
		    new Runnable() {
			    public void run() {
				    try {
						// Add the resources from the specified Zip import files.
						if (fTemplatePage.useTemplate())
						{
							doApplicationTemplate(fCurrProject, new SubProgressMonitor(subMonitor, 1));
						}
				    } catch (InterruptedException e) {
						Java2DLog.logError(e, "Application template creation failed.");
				    } catch (InvocationTargetException e) {
				    	Java2DLog.logError(e, "Application template creation failed.");
				    }
				}
			});
	}

	/**
	 * Create a folder in the specified project. If the folder already exists,
	 * then it will be returned.
	 *
	 * @param project The project to create the folder in.
	 * @param folderName The name of the folder to create.
	 * 
	 * @return A folder resource will be returned.
	 */
	private IFolder createFolderInProject(IProject project,String folderName)
	{
		IFolder folder = project.getFolder(folderName);
		if (! folder.exists())
		{
			try
			{
				folder.create(true,true,null);
			} catch (CoreException ex)
			{
				Java2DLog.logError(ex,"Unable to create folder " + folderName
					+ " in project " + project.getName() + ".");
			}
		}
		
		return folder;
	}
	
	/**
	 * Create a file in the specified folder. If the file already exists,
	 * then its contents will be updated.
	 *
	 * @param folder The folder to create the file in.
	 * @param fileName The name of the file to create.
	 * @param source The data to create the contents of the file with.
	 * 
	 * @reutrn If successful, then a file resource will be returned.
	 */
	private IFile createFileInFolder(IFolder folder,String fileName,InputStream source)
	{
		IFile file = folder.getFile(fileName);
		try
		{
			if (file.exists())
			{
				// Update the contents of the file.
				file.setContents(source,true,false,null);
			} else
			{
				java.io.File systemFile = file.getLocation().toFile();
				if (systemFile.exists())
				{
					// Skip create in file system.
					// XXX - could refreshLocal on parent at this point.
					//folder.getParent().refreshLocal(depth,monitor);
				} else
				{
					file.create(source,false,null);
				}
			}
		} catch (CoreException ex)
		{
			Java2DLog.logError(ex,"Unable to create file " + fileName
				+ " in folder " + folder.getLocation().toString() + ".");
		}
		
		return file;
	}
	
    // Get the path to the Source.
    private String getSourcePath(IProject project)
    {
        IPath destPath = null;
        IJavaProject jproject = JavaCore.create(project);

        try
        {
            IClasspathEntry foundEntry = null;
            IClasspathEntry entries[] = jproject.getRawClasspath();
            for (int i = 0; i < entries.length; i++)
            {
                if (entries[i].getEntryKind() == IClasspathEntry.CPE_SOURCE)
                {
                    foundEntry = entries[i];
                    break;
                }
            }
            if (foundEntry != null)
            {
                destPath = foundEntry.getPath();
            }
            else
                throw new Exception("Source directory not specified.");
        } catch (Exception ex)
        {
        	Java2DLog.logError(ex, "Unable to get Source destination path.");
        }

        return destPath.toString();
    }
	
    // Unzip the Zip file.
    private void doApplicationTemplate(IProject project, IProgressMonitor monitor)
        throws InvocationTargetException, InterruptedException
    {
        IPath destPath;

        try
        {
            IApplicationTemplate[] templates = ApplicationTemplateManager.getInstance().getTemplates();
            for (int i = 0; i < templates.length; i++)
            {
            	IApplicationTemplate next = templates[i];
	            if ((next.getName().equals("SimpleTitle") && (fTemplatePage.getTemplateType() == MleTemplatePage.SIMPLE_RUNTIME_TEMPLATE)) ||
	                (next.getName().equals("HelloWorldTitle") && (fTemplatePage.getTemplateType() == MleTemplatePage.HELLOWORLD_RUNTIME_TEMPLATE)) ||
	                (next.getName().equals("ImageViewerTitle") && (fTemplatePage.getTemplateType() == MleTemplatePage.IMAGEVIEWER_RUNTIME_TEMPLATE)))
	            {
	            	IApplicationTemplateEntry[] entries = next.getTemplateEntries();
	            	
	                // Set up the destination directory.
	                /*String name = getSourcePath(project);
	                if ((name == null) || (name.length() == 0))
	                {
	                    // Get the default.
	                	name = entries[0].getDestination();
	                    if (name == null || name.length() == 0)
	                    {
	                        destPath = project.getFullPath();
	                    }
	                    else
	                    {
	                        IFolder folder = project.getFolder(name);
	                        if (! folder.exists())
	                        {
	                            folder.create(true, true, null);
	                        }
	                        destPath = folder.getFullPath();
	                    }
	                }
	                else
	                    destPath = new Path(name);
	                */
	            	destPath = project.getFullPath();
	
	                // Determine the source of the Zip file.
	                String importPath = entries[0].getSource();
	                if (importPath == null)
	                {
	                    importPath = "";
	                    Java2DLog.logWarning("<import/> descriptor: src missing.");
	                    return;
	                }
	
	                // Retrieve the Zip file and import the resources.
	                ZipFile zipFile = getZipFileFromPluginDir(importPath);
	                importFilesFromZip(zipFile, destPath, new SubProgressMonitor(monitor, 1));
	                
					// Add Simple Java DWP.
	                /*
					MleSimpleDwp dwp = new MleSimpleDwp(project);
					if (fConfigPage.isRehearsalPlayerSelected())
					{
						dwp.setPropertyValue(IDwpConfiguration.INCLUDE_REHEARSAL_PLAYER,new Boolean(true));
					}
					ByteArrayOutputStream dwpOut = dwp.generate();
					ByteArrayInputStream dwpIn = new ByteArrayInputStream(dwpOut.toByteArray());
					IFolder folder = createFolderInProject(project,"workprints");
					createFileInFolder(folder,fConfigPage.getDwpFilename(),dwpIn);
					*/
	            }
            }
        } catch (CoreException ex)
        {
        	throw new InvocationTargetException(ex);
        } /*catch (IOException ex)
        {
        	throw new InvocationTargetException(ex);
        } */
    }
	
	// Retrieve the Zip file relative to the specified path.
	private ZipFile getZipFileFromPluginDir(String pluginRelativePath)
	    throws CoreException
	{
		try {
			URL starterURL = new URL(Java2DPlugin.getDefault().getBundle().getEntry("/"), pluginRelativePath);
			return new ZipFile(Platform.asLocalURL(starterURL).getFile());
		} catch (IOException e) {
			String message= pluginRelativePath + ": " + e.getMessage();
			Status status= new Status(IStatus.ERROR, Java2DPlugin.getID(), IStatus.ERROR, message, e);
			throw new CoreException(status);
		}
	}
	
	// Import resources from the specified Zip file
	private void importFilesFromZip(ZipFile srcZipFile, IPath destPath, IProgressMonitor monitor)
	    throws InvocationTargetException, InterruptedException
	{		
		ZipFileStructureProvider structureProvider = new ZipFileStructureProvider(srcZipFile);
		ImportOperation op = new ImportOperation(destPath, structureProvider.getRoot(), structureProvider,
			new MleProjectOverwriteQuery(getShell()));
		op.run(monitor);
	}
	
	// Add classpath entries specific to Magic Lantern.
	private void addMleEntries(List<IClasspathEntry> entries) throws JavaModelException
	{
		// Specify a classpath variable.
		IPath toolrootPath = null;
		String toolrootEnv = System.getProperty("MLE_ROOT");
		if (toolrootEnv != null)
			toolrootPath = new Path(toolrootEnv);
		else
		    toolrootPath = new Path(MLE_ROOT_DEFAULT);
		
		JavaCore.setClasspathVariable("MLE_ROOT",toolrootPath,null);
		
		// Specify the required Magic Lantern libraries.
		String rtJarLocation = new String("MLE_ROOT");
		rtJarLocation = rtJarLocation + "/lib/java/runtime/mlert.jar";
		IPath runtimeJar = new Path(rtJarLocation);
		IClasspathEntry rtJarEntry = JavaCore.newVariableEntry(runtimeJar,null,null);
		
		String partsJarLocation = new String("MLE_ROOT");
		partsJarLocation = partsJarLocation + "/lib/java/runtime/parts.jar";
		IPath partsJar = new Path(partsJarLocation);
		IClasspathEntry partsJarEntry = JavaCore.newVariableEntry(partsJar,null,null);
		
		String actorsJarLocation = new String("MLE_ROOT");
		actorsJarLocation = actorsJarLocation + "/lib/java/runtime/actors.jar";
		IPath actorsJar = new Path(actorsJarLocation);
		IClasspathEntry actorsJarEntry = JavaCore.newVariableEntry(actorsJar,null,null);

		String rolesJarLocation = new String("MLE_ROOT");
		rolesJarLocation = rolesJarLocation + "/lib/java/runtime/roles.jar";
		IPath rolesJar = new Path(rolesJarLocation);
		IClasspathEntry rolesJarEntry = JavaCore.newVariableEntry(rolesJar,null,null);

		String propsJarLocation = new String("MLE_ROOT");
		propsJarLocation = propsJarLocation + "/lib/java/runtime/props.jar";
		IPath propsJar = new Path(propsJarLocation);
		IClasspathEntry propsJarEntry = JavaCore.newVariableEntry(propsJar,null,null);

		String mrefsJarLocation = new String("MLE_ROOT");
		mrefsJarLocation = mrefsJarLocation + "/lib/java/runtime/mrefs.jar";
		IPath mrefsJar = new Path(mrefsJarLocation);
		IClasspathEntry mrefsJarEntry = JavaCore.newVariableEntry(mrefsJar,null,null);

		String msetsJarLocation = new String("MLE_ROOT");
		msetsJarLocation = msetsJarLocation + "/lib/java/runtime/sets.jar";
		IPath setsJar = new Path(msetsJarLocation);
		IClasspathEntry setsJarEntry = JavaCore.newVariableEntry(setsJar,null,null);

		String stagesJarLocation = new String("MLE_ROOT");
		stagesJarLocation = stagesJarLocation + "/lib/java/runtime/stages.jar";
		IPath stagesJar = new Path(stagesJarLocation);
		IClasspathEntry stagesJarEntry = JavaCore.newVariableEntry(stagesJar,null,null);

		String mathJarLocation = new String("MLE_ROOT");
		mathJarLocation = mathJarLocation + "/lib/java/runtime/mlmath.jar";
		IPath mathJar = new Path(mathJarLocation);
		IClasspathEntry mathJarEntry = JavaCore.newVariableEntry(mathJar,null,null);
		
		entries.add(rtJarEntry);
		entries.add(partsJarEntry);
		entries.add(actorsJarEntry);
		entries.add(rolesJarEntry);
		entries.add(propsJarEntry);
		entries.add(mrefsJarEntry);
		entries.add(setsJarEntry);
		entries.add(stagesJarEntry);
		entries.add(mathJarEntry);
	}
	
	/**
	 * Get the elements to open.
	 * 
	 * @return An array of resources is returned.
	 */
	public IResource[] getElementsToOpen()
	{
		IResource[] resources = null;
		if (m_elementsToOpen != null)
		{
		    resources = new IResource[m_elementsToOpen.size()];
		    m_elementsToOpen.toArray(resources);
		}
		return resources;
	}

	private void processMetaData(IProject project) throws IOException
	{
		// Set the meta-data on the project resource.
		String projectType = MleJava2DProjectManager.MLE_STUDIO_JAVA2D_PROJECT_TYPE_VALUE;
		MleStudioProjectManager.getInstance().init(project, projectType);

	    // Create a meta-data file for persistence.
	    MleTitleMetaData metadata = new MleTitleMetaData();
	    metadata.setCreationDate(Calendar.getInstance());

	    MleTitleMetaData.ProjectDataElement titleData = metadata.new ProjectDataElement();
		titleData.setId(fFirstPage.getProjectName());
		titleData.setDwpFile("workprints/" + fConfigPage.getDwpFilename());
		titleData.setVersion("1.0");
		
		try
		{
			MleStudioProjectManager.getInstance().setProjectIdValue(project, fFirstPage.getProjectName());
			MleStudioProjectManager.getInstance().setDigitalWorkprintValue(project, "workprints/" + fConfigPage.getDwpFilename());
			MleStudioProjectManager.getInstance().setProjectVersionValue(project, "1.0");
            MasteringProjectManager.getInstance().setTargetIdValue(project, fFirstPage.getProjectName() + ".Java2D");
            MasteringProjectManager.getInstance().setTargetTypeValue(project, "Java2D");
            MasteringProjectManager.getInstance().setTargetDigitalPlayprintValue(project, fConfigPage.getDppFilename());
		} catch (MleException ex)
		{
			Java2DLog.logError(ex, "Unable to update meta-data on project " + project.getName() + ".");
		} catch (DppException ex)
		{
			Java2DLog.logError(ex, "Unable to update meta-data on project " + project.getName() + ".");
		}
		
		// Add the title to the meta-data.
		Object[] titles = new Object[1];
		titles[0] = titleData;
		metadata.setProjectData(titles);
		
		// Add a new mastering target.
		MleTitleMetaData.MasterTargetElement targetData = metadata.new MasterTargetElement();
		targetData.setType("Java2D");
		targetData.setId(fFirstPage.getProjectName() + ".Java2D");
		targetData.setDigitalPlayprint(fConfigPage.getDppFilename());
		
		// Initialize configuration for Java target.
		targetData.setVerbose(false);
		targetData.setBigEndian(true);
		targetData.setFixedPoint(false);
		targetData.setCodeGeneration("java");
		targetData.setHeaderPackage("gen");
		targetData.setDestinationDirectory("src/gen");
		if (fConfigPage.isGencastSelected())
		{
			targetData.setGengroup(true);
			targetData.setActorIdFile("ActorID.java");
			targetData.setGroupIdFile("GroupID.java");
		} else
			targetData.setGengroup(false);
		if (fConfigPage.isGensceneSelected())
		{
			targetData.setGenscene(true);
			targetData.setSceneIdFile("SceneID.java");
		} else
			targetData.setGenscene(false);
		if (fConfigPage.isGenmrefSelected())
		{
			targetData.setGenmedia(true);
			targetData.setBomFile("MediaBom.txt");
		} else
			targetData.setGenmedia(false);
		if (fConfigPage.isGendppscriptSelected())
		{
			targetData.setGenppscript(true);
			targetData.setScriptFile("playprint.tcl");
			targetData.setTocName("DppTOC");
		} else
			targetData.setGenppscript(false);
		if (fConfigPage.isGendppSelected())
		{
			targetData.setGendpp(true);
			targetData.setSourceDirectory("src/gen");
			targetData.setScriptPath("src/gen/playprint.tcl");
		} else
			targetData.setGendpp(false);
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("java2d");
		if (fConfigPage.isRehearsalPlayerSelected())
		    tags.add("rehearsal");
		targetData.setTags(tags);
		String[] tagsValue = new String[0];
		tagsValue = tags.toArray(tagsValue);
        try {
			MasteringProjectManager.getInstance().setTagsValue(project, tagsValue);
		} catch (DppException ex) {
			Java2DLog.logError(ex, "Unable to update meta-data on project " + project.getName() + ".");
		}
		
		MleTitleMetaData.MasterTargetElement[] targets = new MleTitleMetaData.MasterTargetElement[1];
		targets[0] = targetData;
		titleData.setMasterTargets(targets);
		
        // Create a new JAXB marshaler.
        JaxbMetaDataMarshaller marshaller = new JaxbMetaDataMarshaller();

        // Create the OCAP Project meta-data file.
        IPath filepath = project.getLocation();
        filepath = filepath.append(MleStudioProjectManager.MLE_STUDIO_PROJECT_METADATA_FILENAME);
        File file = filepath.toFile();

        // Marshal the meta-data.
        OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
        marshaller.marshallMetaData(output, metadata);

        // Close the output stream.
        output.flush();
        output.close();
	}
}
