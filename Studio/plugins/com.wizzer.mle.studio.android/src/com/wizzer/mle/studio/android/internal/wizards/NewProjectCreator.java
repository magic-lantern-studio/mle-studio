/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Eclipse Public License, Version 1.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.eclipse.org/org/documents/epl-v10.php
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.wizzer.mle.studio.android.internal.wizards;

import com.android.AndroidConstants;
import com.android.ide.common.layout.LayoutConstants;
import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.AdtUtils;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatPreferences;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlFormatStyle;
import com.android.ide.eclipse.adt.internal.editors.formatting.XmlPrettyPrinter;
import com.android.ide.eclipse.adt.internal.preferences.AdtPrefs;
import com.android.ide.eclipse.adt.internal.project.AndroidNature;
import com.android.ide.eclipse.adt.internal.project.ProjectHelper;
import com.android.ide.eclipse.adt.internal.refactorings.extractstring.ExtractStringRefactoring;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.wizzer.mle.codegen.IDwpConfiguration;
import com.wizzer.mle.studio.MleException;
import com.wizzer.mle.studio.MleLog;
import com.wizzer.mle.studio.android.Activator;
import com.wizzer.mle.studio.android.AndroidLog;
import com.wizzer.mle.studio.android.dpp.ResourceNature;
import com.wizzer.mle.studio.android.dwp.MleSimpleDwp;
import com.wizzer.mle.studio.android.internal.wizards.NewProjectWizardState.Mode;
import com.wizzer.mle.studio.android.project.MleAndroidProjectManager;
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
import com.wizzer.mle.studio.dwp.attribute.DwpGroupAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIncludeAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpPackageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSceneAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSetAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpStageAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector2PropertyAttribute;
import com.wizzer.mle.studio.dwp.domain.DwpTable;
import com.wizzer.mle.studio.dwp.domain.DwpTableHandler;
import com.wizzer.mle.studio.dwp.domain.DwpTableHandlerFactory;
import com.wizzer.mle.studio.dwp.domain.IDwpTableHandler;
import com.wizzer.mle.studio.dwp.editor.DwpResource;
import com.wizzer.mle.studio.framework.Find;
import com.wizzer.mle.studio.framework.FindEvent;
import com.wizzer.mle.studio.framework.FindFilter;
import com.wizzer.mle.studio.framework.IFindListener;
import com.wizzer.mle.studio.framework.PathUtil;
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.StringAttribute;
import com.wizzer.mle.studio.project.MleStudioProjectManager;
import com.wizzer.mle.studio.project.MleTemplatePage;
import com.wizzer.mle.studio.project.metadata.JaxbMetaDataMarshaller;
import com.wizzer.mle.studio.project.metadata.MetaDataException;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;
import com.android.io.StreamException;
import com.android.resources.Density;
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.filesystem.IFileSystem;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.dialogs.IOverwriteQuery;
import org.eclipse.ui.wizards.datatransfer.ImportOperation;
import org.eclipse.ui.wizards.datatransfer.ZipFileStructureProvider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.zip.ZipFile;
import java.util.Set;

/**
 * The actual project creator invoked from the New Project Wizard
 * <p/>
 * Note: this class is public so that it can be accessed from unit tests.
 * It is however an internal class. Its API may change without notice.
 * It should semantically be considered as a private final class.
 */
public class NewProjectCreator  {

	/** The Magic Lantern Mastering Nature ID. */
    public static final String MASTERING_NATURE_ID = "com.wizzer.mle.studio.dpp.MasteringNature";
    /** The Magic Lantern Android Mastering Nature ID. */
    public static final String RESOURCE_NATURE_ID = "com.wizzer.mle.studio.android.ResourceNature";

    private static final String PARAM_SDK_TOOLS_DIR = "ANDROID_SDK_TOOLS";          //$NON-NLS-1$
    private static final String PARAM_ACTIVITY = "ACTIVITY_NAME";                   //$NON-NLS-1$
    private static final String PARAM_APPLICATION = "APPLICATION_NAME";             //$NON-NLS-1$
    private static final String PARAM_PACKAGE = "PACKAGE";                          //$NON-NLS-1$
    private static final String PARAM_IMPORT_RESOURCE_CLASS = "IMPORT_RESOURCE_CLASS"; //$NON-NLS-1$
    private static final String PARAM_PROJECT = "PROJECT_NAME";                     //$NON-NLS-1$
    private static final String PARAM_STRING_NAME = "STRING_NAME";                  //$NON-NLS-1$
    private static final String PARAM_STRING_CONTENT = "STRING_CONTENT";            //$NON-NLS-1$
    private static final String PARAM_IS_NEW_PROJECT = "IS_NEW_PROJECT";            //$NON-NLS-1$
    private static final String PARAM_SAMPLE_LOCATION = "SAMPLE_LOCATION";          //$NON-NLS-1$
    private static final String PARAM_SRC_FOLDER = "SRC_FOLDER";                    //$NON-NLS-1$
    private static final String PARAM_SDK_TARGET = "SDK_TARGET";                    //$NON-NLS-1$
    private static final String PARAM_MIN_SDK_VERSION = "MIN_SDK_VERSION";          //$NON-NLS-1$
    // Warning: The expanded string PARAM_TEST_TARGET_PACKAGE must not contain the
    // string "PACKAGE" since it collides with the replacement of PARAM_PACKAGE.
    private static final String PARAM_TEST_TARGET_PACKAGE = "TEST_TARGET_PCKG";     //$NON-NLS-1$
    private static final String PARAM_TARGET_SELF = "TARGET_SELF";                  //$NON-NLS-1$
    private static final String PARAM_TARGET_MAIN = "TARGET_MAIN";                  //$NON-NLS-1$
    private static final String PARAM_TARGET_EXISTING = "TARGET_EXISTING";          //$NON-NLS-1$
    private static final String PARAM_REFERENCE_PROJECT = "REFERENCE_PROJECT";      //$NON-NLS-1$

    private static final String PH_ACTIVITIES = "ACTIVITIES";                       //$NON-NLS-1$
    private static final String PH_USES_SDK = "USES-SDK";                           //$NON-NLS-1$
    private static final String PH_INTENT_FILTERS = "INTENT_FILTERS";               //$NON-NLS-1$
    private static final String PH_STRINGS = "STRINGS";                             //$NON-NLS-1$
    private static final String PH_TEST_USES_LIBRARY = "TEST-USES-LIBRARY";         //$NON-NLS-1$
    private static final String PH_TEST_INSTRUMENTATION = "TEST-INSTRUMENTATION";   //$NON-NLS-1$

    private static final String RAW_DIRECTORY =
        "raw" + AdtConstants.WS_SEP;
    private static final String BIN_DIRECTORY =
        SdkConstants.FD_OUTPUT + AdtConstants.WS_SEP;
    private static final String BIN_CLASSES_DIRECTORY =
        SdkConstants.FD_OUTPUT + AdtConstants.WS_SEP +
        SdkConstants.FD_CLASSES_OUTPUT + AdtConstants.WS_SEP;
    private static final String RES_DIRECTORY =
        SdkConstants.FD_RESOURCES + AdtConstants.WS_SEP;
    private static final String ASSETS_DIRECTORY =
        SdkConstants.FD_ASSETS + AdtConstants.WS_SEP;
    private static final String DRAWABLE_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + AdtConstants.WS_SEP;
    private static final String DRAWABLE_HDPI_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + "-" + Density.HIGH.getResourceValue() +   //$NON-NLS-1$
        AdtConstants.WS_SEP;
    private static final String DRAWABLE_MDPI_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + "-" + Density.MEDIUM.getResourceValue() + //$NON-NLS-1$
        AdtConstants.WS_SEP;
    private static final String DRAWABLE_LDPI_DIRECTORY =
        AndroidConstants.FD_RES_DRAWABLE + "-" + Density.LOW.getResourceValue() +    //$NON-NLS-1$
        AdtConstants.WS_SEP;
    private static final String LAYOUT_DIRECTORY =
        AndroidConstants.FD_RES_LAYOUT + AdtConstants.WS_SEP;
    private static final String VALUES_DIRECTORY =
        AndroidConstants.FD_RES_VALUES + AdtConstants.WS_SEP;
    private static final String GEN_SRC_DIRECTORY =
        SdkConstants.FD_GEN_SOURCES + AdtConstants.WS_SEP;

    private static final String TEMPLATES_DIRECTORY = "templates/"; //$NON-NLS-1$
    private static final String TEMPLATE_MANIFEST = TEMPLATES_DIRECTORY
            + "AndroidManifest.template"; //$NON-NLS-1$
    private static final String TEMPLATE_ACTIVITIES = TEMPLATES_DIRECTORY
            + "activity.template"; //$NON-NLS-1$
    private static final String TEMPLATE_USES_SDK = TEMPLATES_DIRECTORY
            + "uses-sdk.template"; //$NON-NLS-1$
    private static final String TEMPLATE_INTENT_LAUNCHER = TEMPLATES_DIRECTORY
            + "launcher_intent_filter.template"; //$NON-NLS-1$
    private static final String TEMPLATE_TEST_USES_LIBRARY = TEMPLATES_DIRECTORY
            + "test_uses-library.template"; //$NON-NLS-1$
    private static final String TEMPLATE_TEST_INSTRUMENTATION = TEMPLATES_DIRECTORY
            + "test_instrumentation.template"; //$NON-NLS-1$



    private static final String TEMPLATE_STRINGS = TEMPLATES_DIRECTORY
            + "strings.template"; //$NON-NLS-1$
    private static final String TEMPLATE_STRING = TEMPLATES_DIRECTORY
            + "string.template"; //$NON-NLS-1$
    private static final String PROJECT_ICON = "ic_launcher.png"; //$NON-NLS-1$
    private static final String ICON_HDPI = "ic_launcher_hdpi.png"; //$NON-NLS-1$
    private static final String ICON_MDPI = "ic_launcher_mdpi.png"; //$NON-NLS-1$
    private static final String ICON_LDPI = "ic_launcher_ldpi.png"; //$NON-NLS-1$

    private static final String STRINGS_FILE = "strings.xml";       //$NON-NLS-1$

    private static final String STRING_RSRC_PREFIX = LayoutConstants.STRING_PREFIX;
    private static final String STRING_APP_NAME = "app_name";       //$NON-NLS-1$
    private static final String STRING_HELLO_WORLD = "hello";       //$NON-NLS-1$
    private static final String STRING_DPP_NAME = "dpp_filename";

    private static final String[] DEFAULT_DIRECTORIES = new String[] {
            BIN_DIRECTORY, BIN_CLASSES_DIRECTORY, RES_DIRECTORY, ASSETS_DIRECTORY };
    private static final String[] RES_DIRECTORIES = new String[] {
            DRAWABLE_DIRECTORY, LAYOUT_DIRECTORY, RAW_DIRECTORY, VALUES_DIRECTORY };
    private static final String[] RES_DENSITY_ENABLED_DIRECTORIES = new String[] {
            DRAWABLE_HDPI_DIRECTORY, DRAWABLE_MDPI_DIRECTORY, DRAWABLE_LDPI_DIRECTORY,
            LAYOUT_DIRECTORY, RAW_DIRECTORY, VALUES_DIRECTORY };

    private static final String JAVA_ACTIVITY_TEMPLATE = "java_file.template";  //$NON-NLS-1$
    private static final String LAYOUT_TEMPLATE = "layout.template";            //$NON-NLS-1$
    private static final String MAIN_LAYOUT_XML = "main.xml";                   //$NON-NLS-1$

	/** The default MLE_ROOT directory. */
    public static final String MLE_ROOT_DEFAULT = "C:\\Program Files\\Wizzer Works\\MagicLantern";

    private static final String MLE_TEMPLATES_DIRECTORY = "archive/templates/";
    private static final String MLE_ACTIVITY_TEMPLATE = "Simple/SimpleActivity.template";
    private static final String MLE_TITLEDATA_TEMPLATE = "Simple/TitleData.template";
    private static final String MLE_TITLEREGISTRY_TEMPLATE = "Simple/TitleRegistry.template";
    		
    private final NewProjectWizardState mValues;
    private final IRunnableContext mRunnableContext;
    private Object mPackageName;
    
    private IOverwriteQuery m_overwriteQuery;

    public NewProjectCreator(NewProjectWizardState values, IRunnableContext runnableContext, IOverwriteQuery overwriteQuery) {
        mValues = values;
        mRunnableContext = runnableContext;
        m_overwriteQuery = overwriteQuery;
    }

    /**
     * Before actually creating the project for a new project (as opposed to using an
     * existing project), we check if the target location is a directory that either does
     * not exist or is empty.
     *
     * If it's not empty, ask the user for confirmation.
     *
     * @param destination The destination folder where the new project is to be created.
     * @return True if the destination doesn't exist yet or is an empty directory or is
     *         accepted by the user.
     */
    private boolean validateNewProjectLocationIsEmpty(IPath destination) {
        File f = new File(destination.toOSString());
        if (f.isDirectory() && f.list().length > 0) {
            return AdtPlugin.displayPrompt("New Android Project",
                    "You are going to create a new Android Project in an existing, non-empty, directory. Are you sure you want to proceed?");
        }
        return true;
    }

    /**
     * Structure that describes all the information needed to create a project.
     * This is collected from the pages by {@link NewProjectCreator#createAndroidProjects()}
     * and then used by
     * {@link NewProjectCreator#createProjectAsync(IProgressMonitor, ProjectInfo, ProjectInfo)}.
     */
    private static class ProjectInfo {
        private final IProject mProject;
        private final IProjectDescription mDescription;
        private final Map<String, Object> mParameters;
        private final HashMap<String, String> mDictionary;

        public ProjectInfo(IProject project,
                IProjectDescription description,
                Map<String, Object> parameters,
                HashMap<String, String> dictionary) {
                    mProject = project;
                    mDescription = description;
                    mParameters = parameters;
                    mDictionary = dictionary;
        }

        public IProject getProject() {
            return mProject;
        }

        public IProjectDescription getDescription() {
            return mDescription;
        }

        public Map<String, Object> getParameters() {
            return mParameters;
        }

        public HashMap<String, String> getDictionary() {
            return mDictionary;
        }
    }

    /**
     * Creates the android project.
     * @return True if the project could be created.
     */
    public boolean createAndroidProjects() {
        final ProjectInfo mainData = collectMainPageInfo();
        final ProjectInfo testData = collectTestPageInfo();

        // Create a monitored operation to create the actual project
        WorkspaceModifyOperation op = new WorkspaceModifyOperation() {
            @Override
            protected void execute(IProgressMonitor monitor) throws InvocationTargetException {
                createProjectAsync(monitor, mainData, testData);
            }
        };

        // Run the operation in a different thread
        runAsyncOperation(op);
        return true;
    }

    /**
     * Collects all the parameters needed to create the main project.
     * @return A new {@link ProjectInfo} on success. Returns null if the project cannot be
     *    created because parameters are incorrect or should not be created because there
     *    is no main page.
     */
    private ProjectInfo collectMainPageInfo() {
        if (mValues.mode == Mode.TEST) {
            return null;
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        final IProject project = workspace.getRoot().getProject(mValues.projectName);
        final IProjectDescription description = workspace.newProjectDescription(project.getName());

        // keep some variables to make them available once the wizard closes
        mPackageName = mValues.packageName;

        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put(PARAM_PROJECT, mValues.projectName);
        parameters.put(PARAM_PACKAGE, mPackageName);
        parameters.put(PARAM_APPLICATION, STRING_RSRC_PREFIX + STRING_APP_NAME);
        parameters.put(PARAM_SDK_TOOLS_DIR, AdtPlugin.getOsSdkToolsFolder());
        parameters.put(PARAM_IS_NEW_PROJECT, mValues.mode == Mode.ANY && !mValues.useExisting);
        parameters.put(PARAM_SAMPLE_LOCATION, mValues.chosenSample);
        parameters.put(PARAM_SRC_FOLDER, mValues.sourceFolder);
        parameters.put(PARAM_SDK_TARGET, mValues.target);
        parameters.put(PARAM_MIN_SDK_VERSION, mValues.minSdk);

        if (mValues.createActivity) {
            parameters.put(PARAM_ACTIVITY, mValues.activityName);
        }

        // create a dictionary of string that will contain name+content.
        // we'll put all the strings into values/strings.xml
        final HashMap<String, String> dictionary = new HashMap<String, String>();
        dictionary.put(STRING_DPP_NAME, "playprint.dpp");
        dictionary.put(STRING_APP_NAME, mValues.applicationName);

        IPath path = new Path(mValues.projectLocation.getPath());
        IPath defaultLocation = Platform.getLocation();
        if ((!mValues.useDefaultLocation || mValues.useExisting)
                && !defaultLocation.isPrefixOf(path)) {
            description.setLocation(path);
        }

        if (mValues.mode == Mode.ANY && !mValues.useExisting && !mValues.useDefaultLocation &&
                !validateNewProjectLocationIsEmpty(path)) {
            return null;
        }

        return new ProjectInfo(project, description, parameters, dictionary);
    }

    /**
     * Collects all the parameters needed to create the test project.
     *
     * @return A new {@link ProjectInfo} on success. Returns null if the project cannot be
     *    created because parameters are incorrect or should not be created because there
     *    is no test page.
     */
    private ProjectInfo collectTestPageInfo() {
        if (mValues.mode != Mode.TEST && !mValues.createPairProject) {
            return null;
        }

        IWorkspace workspace = ResourcesPlugin.getWorkspace();
        String projectName =
                mValues.mode == Mode.TEST ? mValues.projectName : mValues.testProjectName;
        final IProject project = workspace.getRoot().getProject(projectName);
        final IProjectDescription description = workspace.newProjectDescription(project.getName());

        final Map<String, Object> parameters = new HashMap<String, Object>();

        String pkg =
                mValues.mode == Mode.TEST ? mValues.packageName : mValues.testPackageName;

        parameters.put(PARAM_PROJECT, projectName);
        parameters.put(PARAM_PACKAGE, pkg);
        parameters.put(PARAM_APPLICATION, STRING_RSRC_PREFIX + STRING_APP_NAME);
        parameters.put(PARAM_SDK_TOOLS_DIR, AdtPlugin.getOsSdkToolsFolder());
        parameters.put(PARAM_IS_NEW_PROJECT, true);
        parameters.put(PARAM_SRC_FOLDER, mValues.sourceFolder);
        parameters.put(PARAM_SDK_TARGET, mValues.target);
        parameters.put(PARAM_MIN_SDK_VERSION, mValues.minSdk);

        // Test-specific parameters
        String testedPkg = mValues.createPairProject
                ? mValues.packageName : mValues.testTargetPackageName;
        if (testedPkg == null) {
            assert mValues.testingSelf;
            testedPkg = pkg;
        }

        parameters.put(PARAM_TEST_TARGET_PACKAGE, testedPkg);

        if (mValues.testingSelf) {
            parameters.put(PARAM_TARGET_SELF, true);
        } else {
            parameters.put(PARAM_TARGET_EXISTING, true);
            parameters.put(PARAM_REFERENCE_PROJECT, mValues.testedProject);
        }

        if (mValues.createPairProject) {
            parameters.put(PARAM_TARGET_MAIN, true);
        }

        // create a dictionary of string that will contain name+content.
        // we'll put all the strings into values/strings.xml
        final HashMap<String, String> dictionary = new HashMap<String, String>();
        dictionary.put(STRING_APP_NAME, mValues.testApplicationName);

        IPath path = new Path(mValues.projectLocation.getPath());
        IPath defaultLocation = Platform.getLocation();
        if ((!mValues.useDefaultLocation || mValues.useExisting)
                && !path.equals(defaultLocation)) {
            description.setLocation(path);
        }

        if (!mValues.useDefaultLocation && !validateNewProjectLocationIsEmpty(path)) {
            return null;
        }

        return new ProjectInfo(project, description, parameters, dictionary);
    }

    /**
     * Runs the operation in a different thread and display generated
     * exceptions.
     *
     * @param op The asynchronous operation to run.
     */
    private void runAsyncOperation(WorkspaceModifyOperation op) {
        try {
            mRunnableContext.run(true /* fork */, true /* cancelable */, op);
        } catch (InvocationTargetException e) {

            AdtPlugin.log(e, "New Project Wizard failed");

            // The runnable threw an exception
            Throwable t = e.getTargetException();
            if (t instanceof CoreException) {
                CoreException core = (CoreException) t;
                if (core.getStatus().getCode() == IResourceStatus.CASE_VARIANT_EXISTS) {
                    // The error indicates the file system is not case sensitive
                    // and there's a resource with a similar name.
                    MessageDialog.openError(AdtPlugin.getDisplay().getActiveShell(),
                            "Error", "Error: Case Variant Exists");
                } else {
                    ErrorDialog.openError(AdtPlugin.getDisplay().getActiveShell(),
                            "Error", core.getMessage(), core.getStatus());
                }
            } else {
                // Some other kind of exception
                String msg = t.getMessage();
                Throwable t1 = t;
                while (msg == null && t1.getCause() != null) {
                    msg = t1.getMessage();
                    t1 = t1.getCause();
                }
                if (msg == null) {
                    msg = t.toString();
                }
                MessageDialog.openError(AdtPlugin.getDisplay().getActiveShell(), "Error", msg);
            }
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the actual project(s). This is run asynchronously in a different thread.
     *
     * @param monitor An existing monitor.
     * @param mainData Data for main project. Can be null.
     * @throws InvocationTargetException to wrap any unmanaged exception and
     *         return it to the calling thread. The method can fail if it fails
     *         to create or modify the project or if it is canceled by the user.
     */
    private void createProjectAsync(IProgressMonitor monitor,
            ProjectInfo mainData,
            ProjectInfo testData)
                throws InvocationTargetException {
        monitor.beginTask("Create Android Project", 100);
        try {
            IProject mainProject = null;

            if (mainData != null) {
                mainProject = createEclipseProject(
                        new SubProgressMonitor(monitor, 50),
                        mainData.getProject(),
                        mainData.getDescription(),
                        mainData.getParameters(),
                        mainData.getDictionary());

                if (mainProject != null) {
                    final IJavaProject javaProject = JavaCore.create(mainProject);
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            IWorkingSet[] workingSets = mValues.workingSets;
                            if (workingSets.length > 0 && javaProject != null
                                    && javaProject.exists()) {
                                PlatformUI.getWorkbench().getWorkingSetManager()
                                        .addToWorkingSets(javaProject, workingSets);
                            }
                        }
                    });
                }
            }

            if (testData != null) {

                Map<String, Object> parameters = testData.getParameters();
                if (parameters.containsKey(PARAM_TARGET_MAIN) && mainProject != null) {
                    parameters.put(PARAM_REFERENCE_PROJECT, mainProject);
                }

                IProject testProject = createEclipseProject(
                        new SubProgressMonitor(monitor, 50),
                        testData.getProject(),
                        testData.getDescription(),
                        parameters,
                        testData.getDictionary());
                if (testProject != null) {
                    final IJavaProject javaProject = JavaCore.create(testProject);
                    Display.getDefault().syncExec(new Runnable() {

                        public void run() {
                            IWorkingSet[] workingSets = mValues.workingSets;
                            if (workingSets.length > 0 && javaProject != null
                                    && javaProject.exists()) {
                                PlatformUI.getWorkbench().getWorkingSetManager()
                                        .addToWorkingSets(javaProject, workingSets);
                            }
                        }
                    });
                }
            }
        } catch (CoreException e) {
            throw new InvocationTargetException(e);
        } catch (IOException e) {
            throw new InvocationTargetException(e);
        } catch (StreamException e) {
            throw new InvocationTargetException(e);
        } finally {
            monitor.done();
        }
    }
    
    // Build the default Magic Lantern Digital Workprint.
	private IFile buildDefaultWorkprint(IProject project)
 	{
 	    IFile dwp;
 	    
 		// Place workprint files under a common folder.
 		IFolder folder = project.getFolder("workprints");
 		createFolder(folder);
 		
        // Create a domain table for the DWP.
        DwpTable dwpDomainTable = new DwpTable();
        // Create child attributes.
        StringAttribute top = (StringAttribute) dwpDomainTable.getTopAttribute();
        Attribute dwpItems = top.getChildByName("DWP Items");
        DwpIncludeAttribute includeAttr = new DwpIncludeAttribute("$MLE_WORKPRINTS/Parts/sets/android/workprints/MleAndroid2dSet.wpf", false);
        dwpItems.addChild(includeAttr, dwpDomainTable);
        // Build the Stage attributes.
        DwpStageAttribute stageAttr = new DwpStageAttribute("MyStage", "Mle2dStage", false);
        stageAttr.addTag("android");
        DwpSetAttribute setAttr = new DwpSetAttribute("2dSet", "Mle2dSet", false);
        stageAttr.addChild(setAttr, dwpDomainTable);
        Float[] position = new Float[2];
        position[0] = new Float(0.0); position[1] = new Float(0.0);
        DwpVector2PropertyAttribute positionAttr = new DwpVector2PropertyAttribute("position", "MlVector2", position, false );
        setAttr.addChild(positionAttr, dwpDomainTable);
        Float[] size = new Float[2];
        size[0] = new Float(320); size[1] = new Float(480);
        DwpVector2PropertyAttribute sizeAttr = new DwpVector2PropertyAttribute("size", "MlVector2", size, false);
        setAttr.addChild(sizeAttr, dwpDomainTable);
        dwpItems.addChild(stageAttr, dwpDomainTable);
        // Build the Scene attributes.
        DwpSceneAttribute sceneAttr = new DwpSceneAttribute("scene1", "MleScene", false);
        DwpPackageAttribute scenePkgAttr = new DwpPackageAttribute("com.wizzer.mle.runtime.core", false);
        sceneAttr.addChild(scenePkgAttr, dwpDomainTable);
        DwpGroupAttribute groupAttr = new DwpGroupAttribute("group1", "MleGroup", false);
        DwpPackageAttribute groupPkgAttr = new DwpPackageAttribute("com.wizzer.mle.runtime.core", false);
        groupAttr.addChild(groupPkgAttr, dwpDomainTable);
        sceneAttr.addChild(groupAttr, dwpDomainTable);
        dwpItems.addChild(sceneAttr, dwpDomainTable);

        // Get a handler for the DWP table.
        IDwpTableHandler dwpHandler = DwpTableHandlerFactory.getInstance().createTableHandler(dwpDomainTable);
        // Create a workspace resource for the DWP.
        String dwpFilename = mValues.mleTarget.dwpFilename;
		DwpResource dwpResource = new DwpResource(dwpFilename,(DwpTableHandler)dwpHandler);
		// Write the resource to the file system.
		dwp = dwpResource.createFileInFolder(folder);
 		
		return dwp;
	}
	
	// Recursively check the project hierarchy to determine whether the project
    // file system has a .mleproject file.
    private File m_foundFile = null;

    // Determine if the project has a .mleproject file.
    private File hasProjectMetaData(IProject project) throws CoreException
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
        IPath location = project.getLocation();
        finder.doName(location.toOSString());

        return m_foundFile;
    }
	
    // Retrieve the Magic Lantern Digital Workprint from the project.
	private IFile retrieveWorkprint(IProject project)
 	{
 	    IFile dwp = null;
 	    
 	    try {
			File mleProject = hasProjectMetaData(project);
			if (mleProject != null)
			{
				MleTitleMetaData metadata = null;
				
				// Use the meta-data associated with the project.
                InputStream input = new BufferedInputStream(new FileInputStream(mleProject));

                // Create a new JAXB marshaller.
                JaxbMetaDataMarshaller marshaller = new JaxbMetaDataMarshaller();

                // Retrieve the meta-data;
                metadata = (MleTitleMetaData) marshaller.unmarshallMetaData(input);

                input.close();
                
                // Get the project data from the meta-data.
                Object[] projectData = metadata.getProjectData();
                MleTitleMetaData.ProjectDataElement projectElement = (MleTitleMetaData.ProjectDataElement) projectData[0];
                
                // Extract the name of the Digital Workprint.
                String dwpName = projectElement.getDwpFile();
                
                dwp = project.getFile(dwpName);
                
                StringBuffer dwpFilename = new StringBuffer(dwpName);
                int start = dwpFilename.lastIndexOf("/") + 1;
                mValues.mleTarget.dwpFilename = dwpFilename.substring(start);
                int end = dwpFilename.lastIndexOf(".");
                mValues.mleTarget.dppFilename = dwpFilename.substring(start, end) + ".dpp";
                
                Object[] masterTargets = projectElement.getMasterTargets();
                MleTitleMetaData.MasterTargetElement targetElement = (MleTitleMetaData.MasterTargetElement) masterTargets[0];
               
                mValues.mleTarget.gentablesSelected = targetElement.hasGentables();
                mValues.mleTarget.gensceneSelected = targetElement.hasGenscene();
                mValues.mleTarget.genmrefSelected = targetElement.hasGenmedia();
                mValues.mleTarget.gencastSelected = targetElement.hasGengroup();
                mValues.mleTarget.gendppscriptSelected = targetElement.hasGenppscript();
                mValues.mleTarget.gendppSelected = targetElement.hasGendpp();
			} else
			{
				String msg = "Magic Lantern project file not found.";
				AndroidLog.logWarning(msg);
			}
		} catch (CoreException ex) {
			String msg = "Sample is not a Magic Lantern project.";
			AndroidLog.logError(ex, msg);
		} catch (FileNotFoundException ex) {
			String msg = "Magic Lantern project file not found.";
			AndroidLog.logError(ex, msg);
		} catch (MetaDataException ex) {
			AndroidLog.logError(ex, ex.getMessage());
		} catch (IOException ex) {
			AndroidLog.logError(ex, ex.getMessage());
		}
 	    
 	    return dwp;
 	}
	
	// Configure the project state.
	private void configureMagicLanternProject(IResource resource)
	{
	    IProject project = resource.getProject();
	    
	    try
	    {
	        DwpProjectManager.getInstance().setJavaProject(project);
	        
	        // Set the mastering state.
	        //if (mValues.mleTarget.masteringSelected)
	        //{
	            MasteringProjectManager.getInstance().init(project);
	            if (DwpProjectManager.getInstance().isJavaProject(project))
	            {
			        MasteringProjectManager.getInstance().setJavaDefaults(resource);
			        // XXX - Check first to determine if Java2D target.
			        configureJava2dTarget(resource);
			    } else
	                throw new DppException("Unknown type of Mastering project.");

	            if (mValues.mleTarget.gencastSelected)
	            	MasteringProjectManager.getInstance().setGengroupValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGengroupValue(resource,false);
	            
	            if (mValues.mleTarget.gensceneSelected)
	            	MasteringProjectManager.getInstance().setGensceneValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGensceneValue(resource,false);
	            
	            if (mValues.mleTarget.genmrefSelected)
	            	MasteringProjectManager.getInstance().setGenmediaValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGenmediaValue(resource,false);
	            
	            if (mValues.mleTarget.gentablesSelected)
	            	MasteringProjectManager.getInstance().setGentablesValue(resource,true);
	            else
	            	MasteringProjectManager.getInstance().setGentablesValue(resource,false);

		        if (mValues.mleTarget.gendppscriptSelected)
		        {
		        	MasteringProjectManager.getInstance().setGenppscriptValue(resource,true);

				    String dpp = mValues.mleTarget.dppFilename;
				    GenppscriptPropertyManager.getInstance().setDppValue(resource,dpp);
		        } else
		        {
		        	MasteringProjectManager.getInstance().setGenppscriptValue(resource,false);
		        }
		        
		        if (mValues.mleTarget.gendppSelected)
		        	MasteringProjectManager.getInstance().setGendppValue(resource,true);
		        else
		        	MasteringProjectManager.getInstance().setGendppValue(resource,false);
		        
		        // Process meta-data.
		        processMetaData(project);
	        //}
	    } catch (DppException ex)
	    {
	        MleLog.logError(ex,"Unable to configure mastering process for resource "
	            + resource.getName() + ".");
	    } catch (DwpException ex)
	    {
	        MleLog.logError(ex,"Unable to configure mastering process for resource "
		        + resource.getName() + ".");
	    } catch (IOException ex)
	    {
	        MleLog.logError(ex,"Unable to configure mastering process for resource "
		        + resource.getName() + ".");
	    }
	}
	
	// Configure the Java2d mastering target.
	private void configureJava2dTarget(IResource resource) throws DppException
	{
	    // Configure gengroup.
	    if (mValues.mleTarget.gencastSelected)
	        GengroupPropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genscene.
	    if (mValues.mleTarget.gensceneSelected)
	        GenscenePropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genmedia.
	    if (mValues.mleTarget.genmrefSelected)
	        GenmediaPropertyManager.getInstance().setDefaults(resource);
	    // Configure gentables.
	    if (mValues.mleTarget.gentablesSelected)
	        GentablesPropertyManager.getInstance().setJavaDefaults(resource);
	    // Configure genppscript.
	    if (mValues.mleTarget.gendppscriptSelected)
	        GenppscriptPropertyManager.getInstance().setDefaults(resource);
	    // Configure gendpp.
	    if (mValues.mleTarget.gendppSelected)
	        GendppPropertyManager.getInstance().setDefaults(resource);
	}
    
    // Initialize the specified project's nature.
 	private void initializeMagicLanternNatures(IProject project) throws DppException
 	{
 	    try
 	    {
 	        IProjectDescription description = project.getDescription();
 	        String[] natures = description.getNatureIds();
 	        String[] newNatures = new String[natures.length + 2];
 	        System.arraycopy(natures, 0, newNatures, 0, natures.length);
 	        newNatures[natures.length] = MASTERING_NATURE_ID;
 	        newNatures[natures.length + 1] = RESOURCE_NATURE_ID;
 	        description.setNatureIds(newNatures);
 	        project.setDescription(description, null);
 	    } catch (CoreException ex)
 	    {
 	        throw new DppException("Unable to initialize project nature.",ex);
 	    }

         // Update the project's nature.
         configureMasteringNature(project);
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
	                if (mValues.mleTarget.gencastSelected)
	                {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENGROUP_BUILDER_ID);
		                cmds.add(command);
	                } else
	                    continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENSCENE_BUILDER_ID))
	            {
	                if (mValues.mleTarget.gensceneSelected)
		                {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENSCENE_BUILDER_ID);
		                cmds.add(command);
	                } else
	                    continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENMEDIA_BUILDER_ID))
	            {
	                if (mValues.mleTarget.genmrefSelected)
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENMEDIA_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;
	                
	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENTABLES_BUILDER_ID))
	            {
	                if (mValues.mleTarget.gentablesSelected)
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENTABLES_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENPPSCRIPT_BUILDER_ID))
	            {
	                if (mValues.mleTarget.gendppscriptSelected)
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENPPSCRIPT_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(MasteringNature.GENDPP_BUILDER_ID))
	            {
	                if (mValues.mleTarget.gendppSelected)		    
		            {
		                ICommand command = desc.newCommand();
		                command.setBuilderName(MasteringNature.GENDPP_BUILDER_ID);
		                cmds.add(command);
		            } else
		                continue;

	            } else if (commands[i].getBuilderName().equals(ResourceNature.RESOURCE_BUILDER_ID))
	            {
	            	ICommand command = desc.newCommand();
	                command.setBuilderName(ResourceNature.RESOURCE_BUILDER_ID);
	                cmds.add(command);

	            } else
	            {
	                ICommand command = desc.newCommand();
	                command.setBuilderName(commands[i].getBuilderName());
	                command.setArguments(commands[i].getArguments());
	                cmds.add(command);
	            }
    	    }
    	    
            // Create a new commands array and set it on the project.
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

    /**
     * Creates the actual project, sets its nature and adds the required folders
     * and files to it. This is run asynchronously in a different thread.
     *
     * @param monitor An existing monitor.
     * @param project The project to create.
     * @param description A description of the project.
     * @param parameters Template parameters.
     * @param dictionary String definition.
     * @return The project newly created
     * @throws StreamException
     */
    private IProject createEclipseProject(IProgressMonitor monitor,
            IProject project,
            IProjectDescription description,
            Map<String, Object> parameters,
            Map<String, String> dictionary)
                throws CoreException, IOException, StreamException {

        // get the project target
        IAndroidTarget target = (IAndroidTarget) parameters.get(PARAM_SDK_TARGET);
        boolean legacy = target.getVersion().getApiLevel() < 4;

        // Create project and open it
        project.create(description, new SubProgressMonitor(monitor, 10));
        if (monitor.isCanceled()) throw new OperationCanceledException();

        project.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 10));

		// Build the default resources. The new Digital Workprint is returned.
		//IFile dwp = buildDefaultWorkprint(project);
		//configureMagicLanternProject(dwp);

        // Add the Java and android nature to the project
        AndroidNature.setupProjectNatures(project, monitor);

        // Initialize the Magic Lantern natures.
     	try {
     	   initializeMagicLanternNatures(project);
        } catch (DppException ex) {
     		MleLog.logError(ex,"Unable to initialize Mastering nature.");
     	}

        // Create folders in the project if they don't already exist
        addDefaultDirectories(project, AdtConstants.WS_ROOT, DEFAULT_DIRECTORIES, monitor);
        String[] sourceFolders = new String[] {
                    (String) parameters.get(PARAM_SRC_FOLDER),
                    GEN_SRC_DIRECTORY
                };
        addDefaultDirectories(project, AdtConstants.WS_ROOT, sourceFolders, monitor);

        // Create the resource folders in the project if they don't already exist.
        if (legacy) {
            addDefaultDirectories(project, RES_DIRECTORY, RES_DIRECTORIES, monitor);
        } else {
            addDefaultDirectories(project, RES_DIRECTORY, RES_DENSITY_ENABLED_DIRECTORIES, monitor);
        }

        // Setup class path: mark folders as source folders
        IJavaProject javaProject = JavaCore.create(project);
        setupSourceFolders(javaProject, sourceFolders, monitor);

        if (((Boolean) parameters.get(PARAM_IS_NEW_PROJECT)).booleanValue()) {
            // Create files in the project if they don't already exist
            addManifest(project, parameters, dictionary, monitor);

            // add the default app icon
            addIcon(project, legacy, monitor);

            // Create the default package components
            addSampleCode(project, sourceFolders[0], parameters, dictionary, monitor);

            // add the string definition file if needed
            if (dictionary.size() > 0) {
                addStringDictionaryFile(project, dictionary, monitor);
            }

            // add the default proguard config
            File libFolder = new File((String) parameters.get(PARAM_SDK_TOOLS_DIR),
                    SdkConstants.FD_LIB);
            // Note: FN_PROGUARD_CFG use to be 'proguard.cfg'.
            addLocalFile(project,
                    new File(libFolder, SdkConstants.FN_PROJECT_PROGUARD_FILE),
                    monitor);

            // Set output location
            javaProject.setOutputLocation(project.getFolder(BIN_CLASSES_DIRECTORY).getFullPath(),
                    monitor);
            
            // Add the resources from the specified Zip import files.
    		if (mValues.mleTarget.useTemplate)
    		{
    			IConfigurationElement imports[] = mValues.mleTarget.applicationImports;
    			int nImports;
    			if (imports == null) nImports = 0;
    			else nImports = imports.length;
    			for (int i = 0; i < nImports; i++)
    			{
    				try {
						doImports(project, imports[i], new SubProgressMonitor(monitor, 1));
					} catch (InvocationTargetException ex) {
						AndroidLog.logError(ex, "Unable to import application: " + ex.getMessage());
					} catch (InterruptedException ex) {
						AndroidLog.logError(ex, "Unable to import application: " + ex.getMessage());
					}
    			}
    		}
        }

        File sampleDir = (File) parameters.get(PARAM_SAMPLE_LOCATION);
        if (sampleDir != null) {
            // Copy project
            copySampleCode(project, sampleDir, parameters, dictionary, monitor);
            // Retrieve the Digital Workprint from the project.
            IFile dwp = retrieveWorkprint(project);
            configureMagicLanternProject(dwp);
        } else {
        	if (mValues.mode != Mode.TEST)
        	{
	    		// Build the default resources. The new Digital Workprint is returned.
	    		IFile dwp = buildDefaultWorkprint(project);
	    		configureMagicLanternProject(dwp);
        	}
        }

        // Create the reference to the target project
        if (parameters.containsKey(PARAM_REFERENCE_PROJECT)) {
            IProject refProject = (IProject) parameters.get(PARAM_REFERENCE_PROJECT);
            if (refProject != null) {
                IProjectDescription desc = project.getDescription();

                // Add out reference to the existing project reference.
                // We just created a project with no references so we don't need to expand
                // the currently-empty current list.
                desc.setReferencedProjects(new IProject[] { refProject });

                project.setDescription(desc, IResource.KEEP_HISTORY,
                        new SubProgressMonitor(monitor, 10));

                IClasspathEntry entry = JavaCore.newProjectEntry(
                        refProject.getFullPath(), //path
                        new IAccessRule[0], //accessRules
                        false, //combineAccessRules
                        new IClasspathAttribute[0], //extraAttributes
                        false //isExported

                );
                ProjectHelper.addEntryToClasspath(javaProject, entry);
            }
        }
        
        // Create the reference to the Magic Lantern libraries.
        addMagicLanternClasspath(javaProject, target);

        Sdk.getCurrent().initProject(project, target);

        // Fix the project to make sure all properties are as expected.
        // Necessary for existing projects and good for new ones to.
        ProjectHelper.fixProject(project);

        return project;
    }

    /**
     * Adds default directories to the project.
     *
     * @param project The Java Project to update.
     * @param parentFolder The path of the parent folder. Must end with a
     *        separator.
     * @param folders Folders to be added.
     * @param monitor An existing monitor.
     * @throws CoreException if the method fails to create the directories in
     *         the project.
     */
    private void addDefaultDirectories(IProject project, String parentFolder,
            String[] folders, IProgressMonitor monitor) throws CoreException {
        for (String name : folders) {
            if (name.length() > 0) {
                IFolder folder = project.getFolder(parentFolder + name);
                if (!folder.exists()) {
                    folder.create(true /* force */, true /* local */,
                            new SubProgressMonitor(monitor, 10));
                }
            }
        }
    }

    /**
     * Adds the manifest to the project.
     *
     * @param project The Java Project to update.
     * @param parameters Template Parameters.
     * @param dictionary String List to be added to a string definition
     *        file. This map will be filled by this method.
     * @param monitor An existing monitor.
     * @throws CoreException if the method fails to update the project.
     * @throws IOException if the method fails to create the files in the
     *         project.
     */
    private void addManifest(IProject project, Map<String, Object> parameters,
            Map<String, String> dictionary, IProgressMonitor monitor)
            throws CoreException, IOException {

        // get IFile to the manifest and check if it's not already there.
        IFile file = project.getFile(SdkConstants.FN_ANDROID_MANIFEST_XML);
        if (!file.exists()) {

            // Read manifest template
            String manifestTemplate = AdtPlugin.readEmbeddedTextFile(TEMPLATE_MANIFEST);

            // Replace all keyword parameters
            manifestTemplate = replaceParameters(manifestTemplate, parameters);

            if (manifestTemplate == null) {
                // Inform the user there will be not manifest.
                AdtPlugin.logAndPrintError(null, "Create Project" /*TAG*/,
                        "Failed to generate the Android manifest. Missing template %s",
                        TEMPLATE_MANIFEST);
                // Abort now, there's no need to continue
                return;
            }

            if (parameters.containsKey(PARAM_ACTIVITY)) {
                // now get the activity template
                String activityTemplate = AdtPlugin.readEmbeddedTextFile(TEMPLATE_ACTIVITIES);

                // If the activity name doesn't contain any dot, it's in the form
                // "ClassName" and we need to expand it to ".ClassName" in the XML.
                String name = (String) parameters.get(PARAM_ACTIVITY);
                if (name.indexOf('.') == -1) {
                    // Duplicate the parameters map to avoid changing the caller
                    parameters = new HashMap<String, Object>(parameters);
                    parameters.put(PARAM_ACTIVITY, "." + name); //$NON-NLS-1$
                }

                // Replace all keyword parameters to make main activity.
                String activities = replaceParameters(activityTemplate, parameters);

                // set the intent.
                String intent = AdtPlugin.readEmbeddedTextFile(TEMPLATE_INTENT_LAUNCHER);

                if (activities != null) {
                    if (intent != null) {
                        // set the intent to the main activity
                        activities = activities.replaceAll(PH_INTENT_FILTERS, intent);
                    }

                    // set the activity(ies) in the manifest
                    manifestTemplate = manifestTemplate.replaceAll(PH_ACTIVITIES, activities);
                }
            } else {
                // remove the activity(ies) from the manifest
                manifestTemplate = manifestTemplate.replaceAll(PH_ACTIVITIES, "");  //$NON-NLS-1$
            }

            // Handle the case of the test projects
            if (parameters.containsKey(PARAM_TEST_TARGET_PACKAGE)) {
                // Set the uses-library needed by the test project
                String usesLibrary = AdtPlugin.readEmbeddedTextFile(TEMPLATE_TEST_USES_LIBRARY);
                if (usesLibrary != null) {
                    manifestTemplate = manifestTemplate.replaceAll(
                            PH_TEST_USES_LIBRARY, usesLibrary);
                }

                // Set the instrumentation element needed by the test project
                String instru = AdtPlugin.readEmbeddedTextFile(TEMPLATE_TEST_INSTRUMENTATION);
                if (instru != null) {
                    manifestTemplate = manifestTemplate.replaceAll(
                            PH_TEST_INSTRUMENTATION, instru);
                }

                // Replace PARAM_TEST_TARGET_PACKAGE itself now
                manifestTemplate = replaceParameters(manifestTemplate, parameters);

            } else {
                // remove the unused entries
                manifestTemplate = manifestTemplate.replaceAll(PH_TEST_USES_LIBRARY, "");     //$NON-NLS-1$
                manifestTemplate = manifestTemplate.replaceAll(PH_TEST_INSTRUMENTATION, "");  //$NON-NLS-1$
            }

            String minSdkVersion = (String) parameters.get(PARAM_MIN_SDK_VERSION);
            if (minSdkVersion != null && minSdkVersion.length() > 0) {
                String usesSdkTemplate = AdtPlugin.readEmbeddedTextFile(TEMPLATE_USES_SDK);
                if (usesSdkTemplate != null) {
                    String usesSdk = replaceParameters(usesSdkTemplate, parameters);
                    manifestTemplate = manifestTemplate.replaceAll(PH_USES_SDK, usesSdk);
                }
            } else {
                manifestTemplate = manifestTemplate.replaceAll(PH_USES_SDK, "");
            }

            // Reformat the file according to the user's formatting settings
            manifestTemplate = reformat(XmlFormatStyle.MANIFEST, manifestTemplate);

            // Save in the project as UTF-8
            InputStream stream = new ByteArrayInputStream(
                    manifestTemplate.getBytes("UTF-8")); //$NON-NLS-1$
            file.create(stream, false /* force */, new SubProgressMonitor(monitor, 10));
        }
    }

    /**
     * Adds the string resource file.
     *
     * @param project The Java Project to update.
     * @param strings The list of strings to be added to the string file.
     * @param monitor An existing monitor.
     * @throws CoreException if the method fails to update the project.
     * @throws IOException if the method fails to create the files in the
     *         project.
     */
    private void addStringDictionaryFile(IProject project,
            Map<String, String> strings, IProgressMonitor monitor)
            throws CoreException, IOException {

        // create the IFile object and check if the file doesn't already exist.
        IFile file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                                     + VALUES_DIRECTORY + AdtConstants.WS_SEP + STRINGS_FILE);
        if (!file.exists()) {
            // get the Strings.xml template
            String stringDefinitionTemplate = AdtPlugin.readEmbeddedTextFile(TEMPLATE_STRINGS);

            // get the template for one string
            String stringTemplate = AdtPlugin.readEmbeddedTextFile(TEMPLATE_STRING);

            // get all the string names
            Set<String> stringNames = strings.keySet();

            // loop on it and create the string definitions
            StringBuilder stringNodes = new StringBuilder();
            for (String key : stringNames) {
                // get the value from the key
                String value = strings.get(key);

                // Escape values if necessary
                value = ExtractStringRefactoring.escapeString(value);

                // place them in the template
                String stringDef = stringTemplate.replace(PARAM_STRING_NAME, key);
                stringDef = stringDef.replace(PARAM_STRING_CONTENT, value);

                // append to the other string
                if (stringNodes.length() > 0) {
                    stringNodes.append('\n');
                }
                stringNodes.append(stringDef);
            }

            // put the string nodes in the Strings.xml template
            stringDefinitionTemplate = stringDefinitionTemplate.replace(PH_STRINGS,
                                                                        stringNodes.toString());

            // reformat the file according to the user's formatting settings
            stringDefinitionTemplate = reformat(XmlFormatStyle.RESOURCE, stringDefinitionTemplate);

            // write the file as UTF-8
            InputStream stream = new ByteArrayInputStream(
                    stringDefinitionTemplate.getBytes("UTF-8")); //$NON-NLS-1$
            file.create(stream, false /* force */, new SubProgressMonitor(monitor, 10));
        }
    }

    /** Reformats the given contents with the current formatting settings */
    private String reformat(XmlFormatStyle style, String contents) {
        if (AdtPrefs.getPrefs().getUseCustomXmlFormatter()) {
            XmlFormatPreferences formatPrefs = XmlFormatPreferences.create();
            return XmlPrettyPrinter.prettyPrint(contents, formatPrefs, style,
                    null /*lineSeparator*/);
        } else {
            return contents;
        }
    }

    /**
     * Adds default application icon to the project.
     *
     * @param project The Java Project to update.
     * @param legacy whether we're running in legacy mode (no density support)
     * @param monitor An existing monitor.
     * @throws CoreException if the method fails to update the project.
     */
    private void addIcon(IProject project, boolean legacy, IProgressMonitor monitor)
            throws CoreException {
        if (legacy) { // density support
            // do medium density icon only, in the default drawable folder.
            IFile file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
            if (!file.exists()) {
                addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_MDPI), monitor);
            }
        } else {
            // do all 3 icons.
            IFile file;

            // high density
            file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_HDPI_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
            if (!file.exists()) {
                addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_HDPI), monitor);
            }

            // medium density
            file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_MDPI_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
            if (!file.exists()) {
                addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_MDPI), monitor);
            }

            // low density
            file = project.getFile(RES_DIRECTORY + AdtConstants.WS_SEP
                    + DRAWABLE_LDPI_DIRECTORY + AdtConstants.WS_SEP + PROJECT_ICON);
            if (!file.exists()) {
                addFile(file, AdtPlugin.readEmbeddedFile(TEMPLATES_DIRECTORY + ICON_LDPI), monitor);
            }
        }
    }

    /**
     * Creates a file from a data source.
     * @param dest the file to write
     * @param source the content of the file.
     * @param monitor the progress monitor
     * @throws CoreException
     */
    private void addFile(IFile dest, byte[] source, IProgressMonitor monitor) throws CoreException {
        if (source != null) {
            // Save in the project
            InputStream stream = new ByteArrayInputStream(source);
            dest.create(stream, false /* force */, new SubProgressMonitor(monitor, 10));
        }
    }

    /**
     * Creates the package folder and copies the sample code in the project.
     *
     * @param project The Java Project to update.
     * @param parameters Template Parameters.
     * @param dictionary String List to be added to a string definition
     *        file. This map will be filled by this method.
     * @param monitor An existing monitor.
     * @throws CoreException if the method fails to update the project.
     * @throws IOException if the method fails to create the files in the
     *         project.
     */
    private void addSampleCode(IProject project, String sourceFolder,
            Map<String, Object> parameters, Map<String, String> dictionary,
            IProgressMonitor monitor) throws CoreException, IOException {
        // create the java package directories.
        IFolder pkgFolder = project.getFolder(sourceFolder);
        String packageName = (String) parameters.get(PARAM_PACKAGE);

        // The PARAM_ACTIVITY key will be absent if no activity should be created,
        // in which case activityName will be null.
        String activityName = (String) parameters.get(PARAM_ACTIVITY);

        Map<String, Object> java_activity_parameters = new HashMap<String, Object>(parameters);
        java_activity_parameters.put(PARAM_IMPORT_RESOURCE_CLASS, "");  //$NON-NLS-1$

        if (activityName != null) {

            String resourcePackageClass = null;

            // An activity name can be of the form ".package.Class", ".Class" or FQDN.
            // The initial dot is ignored, as it is always added later in the templates.
            int lastDotIndex = activityName.lastIndexOf('.');

            if (lastDotIndex != -1) {

                // Resource class
                if (lastDotIndex > 0) {
                    resourcePackageClass = packageName + "." + AdtConstants.FN_RESOURCE_BASE; //$NON-NLS-1$
                }

                // Package name
                if (activityName.startsWith(".")) {  //$NON-NLS-1$
                    packageName += activityName.substring(0, lastDotIndex);
                } else {
                    packageName = activityName.substring(0, lastDotIndex);
                }

                // Activity Class name
                activityName = activityName.substring(lastDotIndex + 1);
            }

            java_activity_parameters.put(PARAM_ACTIVITY, activityName);
            java_activity_parameters.put(PARAM_PACKAGE, packageName);
            if (resourcePackageClass != null) {
                String importResourceClass = "\nimport " + resourcePackageClass + ";";  //$NON-NLS-1$ // $NON-NLS-2$
                java_activity_parameters.put(PARAM_IMPORT_RESOURCE_CLASS, importResourceClass);
            }
        }

        String[] components = packageName.split(AdtConstants.RE_DOT);
        for (String component : components) {
            pkgFolder = pkgFolder.getFolder(component);
            if (!pkgFolder.exists()) {
                pkgFolder.create(true /* force */, true /* local */,
                        new SubProgressMonitor(monitor, 10));
            }
        }

        if (activityName != null) {
            // create the main activity Java file
            String activityJava = activityName + AdtConstants.DOT_JAVA;
            IFile file = pkgFolder.getFile(activityJava);
            if (!file.exists()) {
                copyMagicLanternFile(MLE_ACTIVITY_TEMPLATE, file, java_activity_parameters, monitor, false);
            }
            String titleDataJava = "TitleData" + AdtConstants.DOT_JAVA;
            file = pkgFolder.getFile(titleDataJava);
            if (!file.exists()) {
                copyMagicLanternFile(MLE_TITLEDATA_TEMPLATE, file, java_activity_parameters, monitor, false);
            }
            String titleRegistryJava = "TitleRegistry" + AdtConstants.DOT_JAVA;
            file = pkgFolder.getFile(titleRegistryJava);
            if (!file.exists()) {
                copyMagicLanternFile(MLE_TITLEREGISTRY_TEMPLATE, file, java_activity_parameters, monitor, false);
            }
        }

        // create the layout file
        /*
        IFolder layoutfolder = project.getFolder(RES_DIRECTORY).getFolder(LAYOUT_DIRECTORY);
        IFile file = layoutfolder.getFile(MAIN_LAYOUT_XML);
        if (!file.exists()) {
            copyFile(LAYOUT_TEMPLATE, file, parameters, monitor, true);
            if (activityName != null) {
                dictionary.put(STRING_HELLO_WORLD, String.format("Hello World, %1$s!",
                        activityName));
            } else {
                dictionary.put(STRING_HELLO_WORLD, "Hello World!");
            }
        }
        */
    }

    private void copySampleCode(IProject project, File sampleDir,
            Map<String, Object> parameters, Map<String, String> dictionary,
            IProgressMonitor monitor) throws CoreException {
        // Copy the sampleDir into the project directory recursively
        IFileSystem fileSystem = EFS.getLocalFileSystem();
        IFileStore sourceDir = fileSystem.getStore(sampleDir.toURI());
        IFileStore destDir = fileSystem.getStore(AdtUtils.getAbsolutePath(project));
        sourceDir.copy(destDir, EFS.OVERWRITE, null);
    }

    /**
     * Adds a file to the root of the project
     * @param project the project to add the file to.
     * @param source the file to add. It'll keep the same filename once copied into the project.
     * @throws FileNotFoundException
     * @throws CoreException
     */
    private void addLocalFile(IProject project, File source, IProgressMonitor monitor)
            throws FileNotFoundException, CoreException {
        IFile dest = project.getFile(source.getName());
        if (dest.exists() == false) {
            FileInputStream stream = new FileInputStream(source);
            dest.create(stream, false /* force */, new SubProgressMonitor(monitor, 10));
        }
    }

    /**
     * Adds the given folder to the project's class path.
     *
     * @param javaProject The Java Project to update.
     * @param sourceFolders Template Parameters.
     * @param monitor An existing monitor.
     * @throws JavaModelException if the classpath could not be set.
     */
    private void setupSourceFolders(IJavaProject javaProject, String[] sourceFolders,
            IProgressMonitor monitor) throws JavaModelException {
        IProject project = javaProject.getProject();

        // get the list of entries.
        IClasspathEntry[] entries = javaProject.getRawClasspath();

        // remove the project as a source folder (This is the default)
        entries = removeSourceClasspath(entries, project);

        // add the source folders.
        for (String sourceFolder : sourceFolders) {
            IFolder srcFolder = project.getFolder(sourceFolder);

            // remove it first in case.
            entries = removeSourceClasspath(entries, srcFolder);
            entries = ProjectHelper.addEntryToClasspath(entries,
                    JavaCore.newSourceEntry(srcFolder.getFullPath()));
        }

        javaProject.setRawClasspath(entries, new SubProgressMonitor(monitor, 10));
    }


    /**
     * Removes the corresponding source folder from the class path entries if
     * found.
     *
     * @param entries The class path entries to read. A copy will be returned.
     * @param folder The parent source folder to remove.
     * @return A new class path entries array.
     */
    private IClasspathEntry[] removeSourceClasspath(IClasspathEntry[] entries, IContainer folder) {
        if (folder == null) {
            return entries;
        }
        IClasspathEntry source = JavaCore.newSourceEntry(folder.getFullPath());
        int n = entries.length;
        for (int i = n - 1; i >= 0; i--) {
            if (entries[i].equals(source)) {
                IClasspathEntry[] newEntries = new IClasspathEntry[n - 1];
                if (i > 0) System.arraycopy(entries, 0, newEntries, 0, i);
                if (i < n - 1) System.arraycopy(entries, i + 1, newEntries, i, n - i - 1);
                n--;
                entries = newEntries;
            }
        }
        return entries;
    }


    /**
     * Copies the given file from our resource folder to the new project.
     * Expects the file to the US-ASCII or UTF-8 encoded.
     *
     * @throws CoreException from IFile if failing to create the new file.
     * @throws MalformedURLException from URL if failing to interpret the URL.
     * @throws FileNotFoundException from RandomAccessFile.
     * @throws IOException from RandomAccessFile.length() if can't determine the
     *         length.
     */
    private void copyFile(String resourceFilename, IFile destFile,
            Map<String, Object> parameters, IProgressMonitor monitor, boolean reformat)
            throws CoreException, IOException {

        // Read existing file.
        String template = AdtPlugin.readEmbeddedTextFile(
                TEMPLATES_DIRECTORY + resourceFilename);

        // Replace all keyword parameters
        template = replaceParameters(template, parameters);

        if (reformat) {
            // Guess the formatting style based on the file location
            XmlFormatStyle style = XmlFormatStyle.getForFile(destFile.getProjectRelativePath());
            if (style != null) {
                template = reformat(style, template);
            }
        }

        // Save in the project as UTF-8
        InputStream stream = new ByteArrayInputStream(template.getBytes("UTF-8")); //$NON-NLS-1$
        destFile.create(stream, false /* force */, new SubProgressMonitor(monitor, 10));
    }

    /**
     * Replaces placeholders found in a string with values.
     *
     * @param str the string to search for placeholders.
     * @param parameters a map of <placeholder, Value> to search for in the string
     * @return A new String object with the placeholder replaced by the values.
     */
    private String replaceParameters(String str, Map<String, Object> parameters) {

        if (parameters == null) {
            AdtPlugin.log(IStatus.ERROR,
                    "NPW replace parameters: null parameter map. String: '%s'", str);  //$NON-NLS-1$
            return str;
        } else if (str == null) {
            AdtPlugin.log(IStatus.ERROR,
                    "NPW replace parameters: null template string");  //$NON-NLS-1$
            return str;
        }

        for (Entry<String, Object> entry : parameters.entrySet()) {
            if (entry != null && entry.getValue() instanceof String) {
                Object value = entry.getValue();
                if (value == null) {
                    AdtPlugin.log(IStatus.ERROR,
                    "NPW replace parameters: null value for key '%s' in template '%s'",  //$NON-NLS-1$
                    entry.getKey(),
                    str);
                } else {
                    str = str.replaceAll(entry.getKey(), (String) value);
                }
            }
        }

        return str;
    }
    
    private void addMagicLanternClasspath(IJavaProject javaProject, IAndroidTarget target)
    		throws JavaModelException
    {
		// Specify Magic Lantern classpath variables.
		IPath toolrootPath = null;
		String toolrootEnv = System.getProperty("MLE_ROOT");
		if (toolrootEnv != null)
			toolrootPath = new Path(toolrootEnv);
		else
		    toolrootPath = new Path(MLE_ROOT_DEFAULT);

		try
		{
			JavaCore.setClasspathVariable("MLE_ROOT",toolrootPath,null);
		} catch (JavaModelException ex)
		{
			MleLog.logError(ex,"Unable to set Classpath variables.");
		}
		
		String targetDir = MleTargetState.getAndroidApi(target);
		
		// Create local "libs" directory.
		IFolder libsFolder = createFolderInProject(javaProject.getProject(), "libs");

		// Specify the required Magic Lantern libraries.
		String mleRootPath = PathUtil.filenameExpand("$MLE_ROOT");
		
		//String rtJarLocation = new String("MLE_ROOT");
		//rtJarLocation = rtJarLocation + "/lib/" + targetDir + "/mlert.jar";
		String rtJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/mlert.jar");
		IPath runtimeJar = new Path(rtJarLocation);
		String rtJavadocLocation = new String("$MLE_ROOT");
		rtJavadocLocation = PathUtil.filenameExpand(rtJavadocLocation);
		rtJavadocLocation = rtJavadocLocation + "\\lib\\" + targetDir + "\\mlertdoc.jar";
		File rtJavadocFile = new File(rtJavadocLocation);
		IClasspathAttribute rtAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + rtJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry rtJarEntry = JavaCore.newVariableEntry(runtimeJar,null,null,null,rtAtts,false);
		IClasspathEntry rtJarEntry = JavaCore.newLibraryEntry(runtimeJar, null, null, null, rtAtts, false);
		
		// Copy mlert.jar library to "libs" directory.
		String jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\mlert.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "mlert.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create mlert.jar in " + jarLocation);
		}
		
		//String partsJarLocation = new String("MLE_ROOT");
		//partsJarLocation = partsJarLocation + "/lib/" + targetDir + "/parts.jar";
		String partsJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/parts.jar");
		IPath partsJar = new Path(partsJarLocation);
		String partsJavadocLocation = new String("$MLE_ROOT");
		partsJavadocLocation = PathUtil.filenameExpand(partsJavadocLocation);
		partsJavadocLocation = partsJavadocLocation + "\\lib\\" + targetDir + "\\partsdoc.jar";
		File partsJavadocFile = new File(partsJavadocLocation);
		IClasspathAttribute partsAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + partsJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry partsJarEntry = JavaCore.newVariableEntry(partsJar,null,null,null,partsAtts,false);
		IClasspathEntry partsJarEntry = JavaCore.newLibraryEntry(partsJar, null, null, null, partsAtts, false);
		
		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\parts.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "parts.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create parts.jar in " + jarLocation);
		}
				
		//String actorsJarLocation = new String("MLE_ROOT");
		//actorsJarLocation = actorsJarLocation + "/lib/" + targetDir + "/actors.jar";
		String actorsJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/actors.jar");
		IPath actorsJar = new Path(actorsJarLocation);
		String actorsJavadocLocation = new String("$MLE_ROOT");
		actorsJavadocLocation = PathUtil.filenameExpand(actorsJavadocLocation);
		actorsJavadocLocation = actorsJavadocLocation + "\\lib\\" + targetDir + "\\actorsdoc.jar";
		File actorsJavadocFile = new File(actorsJavadocLocation);
		IClasspathAttribute actorsAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + actorsJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry actorsJarEntry = JavaCore.newVariableEntry(actorsJar,null,null,null,actorsAtts,false);
		IClasspathEntry actorsJarEntry = JavaCore.newLibraryEntry(actorsJar, null, null, null, actorsAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\actors.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "actors.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create actors.jar in " + jarLocation);
		}

		//String rolesJarLocation = new String("MLE_ROOT");
		//rolesJarLocation = rolesJarLocation + "/lib/" + targetDir + "/roles.jar";
		String rolesJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/roles.jar");
		IPath rolesJar = new Path(rolesJarLocation);
		String rolesJavadocLocation = new String("$MLE_ROOT");
		rolesJavadocLocation = PathUtil.filenameExpand(rolesJavadocLocation);
		rolesJavadocLocation = rolesJavadocLocation + "\\lib\\" + targetDir + "\\rolesdoc.jar";
		File rolesJavadocFile = new File(rolesJavadocLocation);
		IClasspathAttribute rolesAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + rolesJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry rolesJarEntry = JavaCore.newVariableEntry(rolesJar,null,null,null,rolesAtts,false);
		IClasspathEntry rolesJarEntry = JavaCore.newLibraryEntry(rolesJar, null, null, null, rolesAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\roles.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "roles.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create roles.jar in " + jarLocation);
		}

		//String propsJarLocation = new String("MLE_ROOT");
		//propsJarLocation = propsJarLocation + "/lib/" + targetDir + "/props.jar";
		String propsJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/props.jar");
		IPath propsJar = new Path(propsJarLocation);
		String propsJavadocLocation = new String("$MLE_ROOT");
		propsJavadocLocation = PathUtil.filenameExpand(propsJavadocLocation);
		propsJavadocLocation = propsJavadocLocation + "\\lib\\" + targetDir + "\\propsdoc.jar";
		File propsJavadocFile = new File(propsJavadocLocation);
		IClasspathAttribute propsAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + propsJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry propsJarEntry = JavaCore.newVariableEntry(propsJar,null,null,null,propsAtts,false);
		IClasspathEntry propsJarEntry = JavaCore.newLibraryEntry(propsJar, null, null, null, propsAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\props.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "props.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create props.jar in " + jarLocation);
		}

		//String mrefsJarLocation = new String("MLE_ROOT");
		//mrefsJarLocation = mrefsJarLocation + "/lib/" + targetDir + "/mrefs.jar";
		String mrefsJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/mrefs.jar");
		IPath mrefsJar = new Path(mrefsJarLocation);
		String mrefsJavadocLocation = new String("$MLE_ROOT");
		mrefsJavadocLocation = PathUtil.filenameExpand(mrefsJavadocLocation);
		mrefsJavadocLocation = mrefsJavadocLocation + "\\lib\\" + targetDir + "\\mrefsdoc.jar";
		File mrefsJavadocFile = new File(mrefsJavadocLocation);
		IClasspathAttribute mrefsAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + mrefsJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry mrefsJarEntry = JavaCore.newVariableEntry(mrefsJar,null,null,null,mrefsAtts,false);
		IClasspathEntry mrefsJarEntry = JavaCore.newLibraryEntry(mrefsJar, null, null, null, mrefsAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\mrefs.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "mrefs.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create mrefs.jar in " + jarLocation);
		}

		//String msetsJarLocation = new String("MLE_ROOT");
		//msetsJarLocation = msetsJarLocation + "/lib/" + targetDir + "/sets.jar";
		String msetsJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/sets.jar");
		IPath setsJar = new Path(msetsJarLocation);
		String setsJavadocLocation = new String("$MLE_ROOT");
		setsJavadocLocation = PathUtil.filenameExpand(setsJavadocLocation);
		setsJavadocLocation = setsJavadocLocation + "\\lib\\" + targetDir + "\\setsdoc.jar";
		File setsJavadocFile = new File(setsJavadocLocation);
		IClasspathAttribute setsAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + setsJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry setsJarEntry = JavaCore.newVariableEntry(setsJar,null,null,null,setsAtts,false);
		IClasspathEntry setsJarEntry = JavaCore.newLibraryEntry(setsJar, null, null, null, setsAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\sets.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "sets.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create sets.jar in " + jarLocation);
		}

		//String stagesJarLocation = new String("MLE_ROOT");
		//stagesJarLocation = stagesJarLocation + "/lib/" + targetDir + "/stages.jar";
		String stagesJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/stages.jar");
		IPath stagesJar = new Path(stagesJarLocation);
		String stagesJavadocLocation = new String("$MLE_ROOT");
		stagesJavadocLocation = PathUtil.filenameExpand(stagesJavadocLocation);
		stagesJavadocLocation = stagesJavadocLocation + "\\lib\\" + targetDir + "\\stagesdoc.jar";
		File stagesJavadocFile = new File(stagesJavadocLocation);
		IClasspathAttribute stagesAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + stagesJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry stagesJarEntry = JavaCore.newVariableEntry(stagesJar,null,null,null,stagesAtts,false);
		IClasspathEntry stagesJarEntry = JavaCore.newLibraryEntry(stagesJar, null, null, null, stagesAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\stages.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "stages.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create stages.jar in " + jarLocation);
		}

		//String mathJarLocation = new String("MLE_ROOT");
		//mathJarLocation = mathJarLocation + "/lib/" + targetDir + "/mlmath.jar";
		String mathJarLocation = new String(javaProject.getProject().getFullPath() + "/libs/mlmath.jar");
		IPath mathJar = new Path(mathJarLocation);
		String mlmathJavadocLocation = new String("$MLE_ROOT");
		mlmathJavadocLocation = PathUtil.filenameExpand(mlmathJavadocLocation);
		mlmathJavadocLocation = mlmathJavadocLocation + "\\lib\\" + targetDir + "\\mlmathdoc.jar";
		File mlmathJavadocFile = new File(mlmathJavadocLocation);
		IClasspathAttribute mlmathAtts[] = new IClasspathAttribute[] {
			JavaCore.newClasspathAttribute("javadoc_location", "jar:" + mlmathJavadocFile.toURI().toString() + "!/doc"),
		};
		//IClasspathEntry mathJarEntry = JavaCore.newVariableEntry(mathJar,null,null,null,mlmathAtts,false);
		IClasspathEntry mathJarEntry = JavaCore.newLibraryEntry(mathJar, null, null, null, mlmathAtts, false);

		// Copy library to "libs" directory.
		jarLocation = new String(mleRootPath) + "\\lib\\" + targetDir + "\\mlmath.jar";
		try {
			FileInputStream libIn = new FileInputStream(jarLocation);
			createFileInFolder(libsFolder, "mlmath.jar", libIn);
		} catch (FileNotFoundException ex) {
			MleLog.logError(ex, "Unable to create mlmath.jar in " + jarLocation);
		}

		ProjectHelper.addEntryToClasspath(javaProject, rtJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, partsJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, actorsJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, rolesJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, propsJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, mrefsJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, setsJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, stagesJarEntry);
		ProjectHelper.addEntryToClasspath(javaProject, mathJarEntry);
    }
    
    /**
	 * Creates a folder if it does not exist, returns true if a folder
	 * was created.
	 */
	protected boolean createFolder(IFolder newFolder)
	{
		if (! newFolder.exists())
		{
			try {
				newFolder.create(true, true, null);
			} catch (CoreException ex) {
				MleLog.logWarning("Unable to create resource folder " + newFolder.getName());
				return false;
			};
		} else {
			return false;
	    };
	    
	    return true;
	}
	
	/**
	 * Create a folder in the specified project. If the folder already exists,
	 * then it will be returned.
	 *
	 * @param project The project to create the folder in.
	 * @param folderName The name of the folder to create.
	 * 
	 * @return A folder resource will be returned. If the folder could not be created,
	 * then <b>null</b> will be returned.
	 */
	protected IFolder createFolderInProject(IProject project,String folderName)
	{
		IFolder folder = project.getFolder(folderName);
		if (! folder.exists())
		{
			try
			{
				folder.create(true,true,null);
			} catch (CoreException ex)
			{
				MleLog.logError(ex,"Unable to create folder " + folderName
					+ " in project " + project.getName() + ".");
				folder = null;
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
	 * @reutrn If successful, then a file resource will be returned. If the file can
	 * not be created, then <b>null</b> will be returned.
	 */
	protected IFile createFileInFolder(IFolder folder,String fileName,InputStream source)
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
			MleLog.logError(ex,"Unable to create file " + fileName
				+ " in folder " + folder.getLocation().toString() + ".");
			file = null;
		}
		
		return file;
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

			if ((type.equals("SimpleAndroidTemplate") &&
				(mValues.mleTarget.templateType == MleTemplatePage.SIMPLE_RUNTIME_TEMPLATE)))
			{
				// Set up the destination directory.
				String name = curr.getAttribute("dest");
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
				String importPath = curr.getAttribute("src");
				if (importPath == null) {
					importPath = "";
					MleLog.logWarning("<import/> descriptor: import missing.");
					return;
				}
			    
			    // Retrieve the Zip file and import the resources.
				ZipFile zipFile = getZipFileFromPluginDir(importPath);
				importFilesFromZip(zipFile, destPath, new SubProgressMonitor(monitor, 1));
				
				// Add Simple Java DWP.
				MleSimpleDwp dwp = new MleSimpleDwp(project);
				if (mValues.mleTarget.rehearsalPlayerSelected)
				{
					dwp.setPropertyValue(IDwpConfiguration.INCLUDE_REHEARSAL_PLAYER,new Boolean(true));
				}
				ByteArrayOutputStream dwpOut = dwp.generate();
				ByteArrayInputStream dwpIn = new ByteArrayInputStream(dwpOut.toByteArray());
				IFolder folder = createFolderInProject(project,"workprints");
				createFileInFolder(folder,mValues.mleTarget.dwpFilename,dwpIn);

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
		try {
			URL starterURL = new URL(Activator.getDefault().getBundle().getEntry("/"), pluginRelativePath);
			return new ZipFile(Platform.asLocalURL(starterURL).getFile());
		} catch (IOException e) {
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
	
	private void processMetaData(IProject project) throws IOException
	{
		// Set the meta-data on the project resource.
		String projectType = MleAndroidProjectManager.MLE_STUDIO_ANDROID_PROJECT_TYPE_VALUE;
		MleStudioProjectManager.getInstance().init(project, projectType);

	    // Create a meta-data file for persistence.
	    MleTitleMetaData metadata = new MleTitleMetaData();
	    metadata.setCreationDate(Calendar.getInstance());

	    MleTitleMetaData.ProjectDataElement titleData = metadata.new ProjectDataElement();
		titleData.setId(mValues.projectName);
		titleData.setDwpFile("workprints/" + mValues.mleTarget.dwpFilename);
		titleData.setVersion("1.0");
		
		try
		{
			MleStudioProjectManager.getInstance().setProjectIdValue(project, mValues.projectName);
			MleStudioProjectManager.getInstance().setDigitalWorkprintValue(project, "workprints/" + mValues.mleTarget.dwpFilename);
			MleStudioProjectManager.getInstance().setProjectVersionValue(project, "1.0");
            MasteringProjectManager.getInstance().setTargetIdValue(project, mValues.projectName + ".Android");
            MasteringProjectManager.getInstance().setTargetTypeValue(project, "Android");
            MasteringProjectManager.getInstance().setTargetDigitalPlayprintValue(project, mValues.mleTarget.dppFilename);
		} catch (MleException ex)
		{
			AndroidLog.logError(ex, "Unable to update meta-data on project " + project.getName() + ".");
		} catch (DppException ex)
		{
			AndroidLog.logError(ex, "Unable to update meta-data on project " + project.getName() + ".");
		}
		
		// Add the title to the meta-data.
		Object[] titles = new Object[1];
		titles[0] = titleData;
		metadata.setProjectData(titles);
		
		// Add a new mastering target.
		MleTitleMetaData.MasterTargetElement targetData = metadata.new MasterTargetElement();
		targetData.setType("Android");
		targetData.setId(mValues.projectName + ".Android");
		targetData.setDigitalPlayprint(mValues.mleTarget.dppFilename);
		
		// Initialize configuration for Java target.
		targetData.setVerbose(false);
		targetData.setBigEndian(true);
		targetData.setFixedPoint(false);
		targetData.setCodeGeneration("java");
		targetData.setHeaderPackage("gen");
		targetData.setDestinationDirectory("src/gen");
		if (mValues.mleTarget.gencastSelected)
		{
			targetData.setGengroup(true);
			targetData.setActorIdFile("ActorID.java");
			targetData.setGroupIdFile("GroupID.java");
		} else
			targetData.setGengroup(false);
		if (mValues.mleTarget.gensceneSelected)
		{
			targetData.setGenscene(true);
			targetData.setSceneIdFile("SceneID.java");
		} else
			targetData.setGenscene(false);
		if (mValues.mleTarget.genmrefSelected)
		{
			targetData.setGenmedia(true);
			targetData.setBomFile("MediaBom.txt");
		} else
			targetData.setGenmedia(false);
		if (mValues.mleTarget.gendppscriptSelected)
		{
			targetData.setGenppscript(true);
			targetData.setScriptFile("playprint.py");
			targetData.setTocName("DppTOC");
		} else
			targetData.setGenppscript(false);
		if (mValues.mleTarget.gendppSelected)
		{
			targetData.setGendpp(true);
			targetData.setSourceDirectory("src/gen");
			targetData.setScriptPath("src/gen/playprint.py");
		} else
			targetData.setGendpp(false);
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("android");
		if (mValues.mleTarget.rehearsalPlayerSelected)
		    tags.add("rehearsal");
		targetData.setTags(tags);
		String[] tagsValue = new String[0];
		tagsValue = tags.toArray(tagsValue);
        try {
			MasteringProjectManager.getInstance().setTagsValue(project, tagsValue);
		} catch (DppException ex) {
			AndroidLog.logError(ex, "Unable to update meta-data on project " + project.getName() + ".");
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
	
	/**
     * Copies the given file from our resource folder to the new project.
     * Expects the file to the US-ASCII or UTF-8 encoded.
     *
     * @throws CoreException from IFile if failing to create the new file.
     * @throws MalformedURLException from URL if failing to interpret the URL.
     * @throws FileNotFoundException from RandomAccessFile.
     * @throws IOException from RandomAccessFile.length() if can't determine the
     *         length.
     */
    private void copyMagicLanternFile(String resourceFilename, IFile destFile,
            Map<String, Object> parameters, IProgressMonitor monitor, boolean reformat)
            throws CoreException, IOException
    {
        // Read existing file.
        String template = Activator.readEmbeddedTextFile(
                MLE_TEMPLATES_DIRECTORY + resourceFilename);

        // Replace all keyword parameters
        template = replaceParameters(template, parameters);

        if (reformat) {
            // Guess the formatting style based on the file location
            XmlFormatStyle style = XmlFormatStyle.getForFile(destFile.getProjectRelativePath());
            if (style != null) {
                template = reformat(style, template);
            }
        }

        // Save in the project as UTF-8
        InputStream stream = new ByteArrayInputStream(template.getBytes("UTF-8")); //$NON-NLS-1$
        destFile.create(stream, false /* force */, new SubProgressMonitor(monitor, 10));
    }
}
