/*
 * Copyright (C) 2007 Google Inc.
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

/*
 * References:
 * org.eclipse.jdt.internal.ui.wizards.JavaProjectWizard
 * org.eclipse.jdt.internal.ui.wizards.JavaProjectWizardFirstPage
 */

package com.wizzer.mle.studio.android.project;

//import com.android.ide.eclipse.adt.AndroidConstants;

import org.eclipse.core.filesystem.URIUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.util.TextProcessor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import java.io.File;
import java.io.FileFilter;
import java.net.URI;
import java.util.regex.Pattern;

/**
 * NewAndroidProjectCreationPage is a project creation page that provides the
 * following fields:
 * <ul>
 * <li> Package name
 * <li> Activity name
 * <li> Location of the SDK
 * </ul>
 */
public class NewAndroidProjectCreationPage extends WizardPage {

    // constants
    /** Initial value for all name fields (project, activity, application, package). Used
     * whenever a value is requested before controls are created. */
    static final String INITIAL_NAME = "";  //$NON-NLS-1$
    /** Initial value for the Create New Project radio; False means Create From Existing would be
     * the default.*/
    static final boolean INITIAL_CREATE_NEW_PROJECT = true;
    /** Initial value for the Use Default Location check box. */
    static final boolean INITIAL_USE_DEFAULT_LOCATION = true;

    /** Pattern for characters accepted in a project name. Since this will be used as a
     * directory name, we're being a bit conservative on purpose. It cannot start with a space. */
    static final Pattern sProjectNamePattern = Pattern.compile("^[\\w][\\w. -]*$");  //$NON-NLS-1$

    private String mCustomLocationOsPath = "";  //$NON-NLS-1$
    private String mCustomPackageName = "";  //$NON-NLS-1$
    private String mCustomActivityName = "";  //$NON-NLS-1$
    private String mSourceFolder = "";  //$NON-NLS-1$

    // widgets
    private Text mProjectNameField;
    private Text mPackageNameField;
    private Text mActivityNameField;
    private Text mApplicationNameField;
    private Button mCreateNewProjectRadio;
    private Button mUseDefaultLocation;
    private Label mLocationLabel;
    private Text mLocationPathField;
    private Button mBrowseButton;
    private boolean mInternalLocationPathUpdate;

    /**
     * Creates a new project creation wizard page.
     *
     * @param pageName the name of this page
     */
    public NewAndroidProjectCreationPage(String pageName) {
        super(pageName);
        setPageComplete(false);
    }

    // --- Getters used by NewProjectWizard ---

    /**
     * Returns the current project location path as entered by the user, or its
     * anticipated initial value. Note that if the default has been returned the
     * path in a project description used to create a project should not be set.
     *
     * @return the project location path or its anticipated initial value.
     */
    public IPath getLocationPath() {
        return new Path(getProjectLocation());
    }

    /** Returns the value of the project name field with leading and trailing spaces removed. */
    public String getProjectName() {
        return mProjectNameField == null ? INITIAL_NAME : mProjectNameField.getText().trim();
    }

    /** Returns the value of the package name field with spaces trimmed. */
    public String getPackageName() {
        return mPackageNameField == null ? INITIAL_NAME : mPackageNameField.getText().trim();
    }
    
    public void setPackageName(String name) {
    	mPackageNameField.setText(name);
    }

    /** Returns the value of the activity name field with spaces trimmed. */
    public String getActivityName() {
        return mActivityNameField == null ? INITIAL_NAME : mActivityNameField.getText().trim();
    }
    
    public void setActivityName(String name) {
    	mActivityNameField.setText(name);
    }

    /** Returns the value of the application name field with spaces trimmed. */
    public String getApplicationName() {
        // Return the name of the activity as default application name.
        return mApplicationNameField == null ? getActivityName()
                                             : mApplicationNameField.getText().trim();
    }

    /** Returns the value of the "Create New Project" radio. */
    public boolean isNewProject() {
        return mCreateNewProjectRadio == null ? INITIAL_CREATE_NEW_PROJECT
                                              : mCreateNewProjectRadio.getSelection();
    }

    /** Returns the value of the Use Default Location field. */
    public boolean useDefaultLocation() {
        return mUseDefaultLocation == null ? INITIAL_USE_DEFAULT_LOCATION
                                           : mUseDefaultLocation.getSelection();
    }

    /** Returns the internal source folder (for the "existing project" mode) or the default
     * "src" constant. */
    public String getSourceFolder() {
        if (isNewProject()) {
            return AndroidConstants.FD_SOURCES;
        } else {
            return mSourceFolder;
        }
    }

    /**
     * Overrides @DialogPage.setVisible(boolean) to put the focus in the project name when
     * the dialog is made visible.
     */
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            mProjectNameField.setFocus();
        }
    }

    // --- UI creation ---

    /**
     * Creates the top level control for this dialog page under the given parent
     * composite.
     *
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite parent) {
        Composite composite = new Composite(parent, SWT.NULL);
        composite.setFont(parent.getFont());

        initializeDialogUnits(parent);

        composite.setLayout(new GridLayout());
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));

        createProjectNameGroup(composite);
        createLocationGroup(composite);
        createPropertiesGroup(composite);

        // Update state the first time
        enableLocationWidgets();
        setPageComplete(validatePage());
        // Show description the first time
        setErrorMessage(null);
        setMessage(null);
        setControl(composite);
    }

    /**
     * Creates the group for the project name:
     * [label: "Project Name"] [text field]
     *
     * @param parent the parent composite
     */
    private final void createProjectNameGroup(Composite parent) {
        Composite group = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

        // new project label
        Label label = new Label(group, SWT.NONE);
        label.setText("Project name:");
        label.setFont(parent.getFont());

        // new project name entry field
        mProjectNameField = new Text(group, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        mProjectNameField.setLayoutData(data);
        mProjectNameField.setFont(parent.getFont());
        mProjectNameField.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event event) {
                updateLocationPathField(null);
            }
        });
    }


    /**
     * Creates the group for the Project options:
     * [radio] Create new project
     * [radio] Create project from existing sources
     * [check] Use default location
     * Location [text field] [browse button]
     *
     * @param parent the parent composite
     */
    private final void createLocationGroup(Composite parent) {
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        // Layout has 4 columns of non-equal size
        group.setLayout(new GridLayout());
        group.setLayoutData(new GridData(GridData.FILL_BOTH));
        group.setFont(parent.getFont());
        group.setText("Contents");

        mCreateNewProjectRadio = new Button(group, SWT.RADIO);
        mCreateNewProjectRadio.setText("Create new project in workspace");
        mCreateNewProjectRadio.setSelection(INITIAL_CREATE_NEW_PROJECT);
        Button existing_project_radio = new Button(group, SWT.RADIO);
        existing_project_radio.setText("Create project from existing source");
        existing_project_radio.setSelection(!INITIAL_CREATE_NEW_PROJECT);

        mUseDefaultLocation = new Button(group, SWT.CHECK);
        mUseDefaultLocation.setText("Use default location");
        mUseDefaultLocation.setSelection(INITIAL_USE_DEFAULT_LOCATION);

        SelectionListener location_listener = new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                enableLocationWidgets();
                setPageComplete(validatePage());
            }
        };

        mCreateNewProjectRadio.addSelectionListener(location_listener);
        existing_project_radio.addSelectionListener(location_listener);
        mUseDefaultLocation.addSelectionListener(location_listener);

        Composite location_group = new Composite(group, SWT.NONE);
        location_group.setLayout(new GridLayout(4, /* num columns */
                false /* columns of not equal size */));
        location_group.setLayoutData(new GridData(GridData.FILL_BOTH));
        location_group.setFont(parent.getFont());

        mLocationLabel = new Label(location_group, SWT.NONE);
        mLocationLabel.setText("Location:");

        mLocationPathField = new Text(location_group, SWT.BORDER);
        GridData data = new GridData(GridData.FILL, /* horizontal alignment */
                GridData.BEGINNING, /* vertical alignment */
                true,  /* grabExcessHorizontalSpace */
                false, /* grabExcessVerticalSpace */
                2,     /* horizontalSpan */
                1);    /* verticalSpan */
        mLocationPathField.setLayoutData(data);
        mLocationPathField.setFont(parent.getFont());
        mLocationPathField.addListener(SWT.Modify, new Listener() {
           public void handleEvent(Event event) {
               onLocationPathFieldModified();
            }
        });

        mBrowseButton = new Button(location_group, SWT.PUSH);
        mBrowseButton.setText("Browse...");
        setButtonLayoutData(mBrowseButton);
        mBrowseButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                openDirectoryBrowser();
            }
        });
    }

    /**
     * Display a directory browser and update the location path field with the selected path
     */
    private void openDirectoryBrowser() {

        String existing_dir = getLocationPathFieldValue();

        // Disable the path if it doesn't exist
        if (existing_dir.length() == 0) {
            existing_dir = null;
        } else {
            File f = new File(existing_dir);
            if (!f.exists()) {
                existing_dir = null;
            }
        }

        DirectoryDialog dd = new DirectoryDialog(mLocationPathField.getShell());
        dd.setMessage("Browse for folder");
        dd.setFilterPath(existing_dir);
        String abs_dir = dd.open();

        if (abs_dir != null) {
            updateLocationPathField(abs_dir);
        }
    }

    /**
     * Creates the group for the project properties:
     * - Package name [text field]
     * - Activity name [text field]
     * - Application name [text field]
     *
     * @param parent the parent composite
     */
    private final void createPropertiesGroup(Composite parent) {
        // project specification group
        Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        group.setLayout(layout);
        group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        group.setFont(parent.getFont());
        group.setText("Properties");

        // package specification group
        // new package label
        Label label = new Label(group, SWT.NONE);
        label.setText("Package name:");
        label.setFont(parent.getFont());

        // new package name entry field
        mPackageNameField = new Text(group, SWT.BORDER);
        GridData data = new GridData(GridData.FILL_HORIZONTAL);
        mPackageNameField.setLayoutData(data);
        mPackageNameField.setFont(parent.getFont());
        mPackageNameField.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event event) {
                onPackageNameFieldModified();
            }
        });

        // activity specification group
        // new activity label
        label = new Label(group, SWT.NONE);
        label.setText("Activity name:");
        label.setFont(parent.getFont());

        // new activity name entry field
        mActivityNameField = new Text(group, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        mActivityNameField.setLayoutData(data);
        mActivityNameField.setFont(parent.getFont());
        mActivityNameField.addListener(SWT.Modify, new Listener() {
            public void handleEvent(Event event) {
                onActivityNameFieldModified();
            }
        });

        // new activity label
        label = new Label(group, SWT.NONE);
        label.setText("Application name:");
        label.setFont(parent.getFont());

        // new activity name entry field
        mApplicationNameField = new Text(group, SWT.BORDER);
        data = new GridData(GridData.FILL_HORIZONTAL);
        mApplicationNameField.setLayoutData(data);
        mApplicationNameField.setFont(parent.getFont());
    }


    //--- Internal getters & setters ------------------

    /** Returns the location path field value with spaces trimmed. */
    private String getLocationPathFieldValue() {
        return mLocationPathField == null ? "" : mLocationPathField.getText().trim();
    }

    /** Returns the current project location, depending on the Use Default Location check box. */
    public String getProjectLocation() {
        if (isNewProject() && useDefaultLocation()) {
            return Platform.getLocation().toString();
        } else {
            return getLocationPathFieldValue();
        }
    }

    /**
     * Creates a project resource handle for the current project name field
     * value.
     * <p>
     * This method does not create the project resource; this is the
     * responsibility of <code>IProject::create</code> invoked by the new
     * project resource wizard.
     * </p>
     *
     * @return the new project resource handle
     */
    public IProject getProjectHandle() {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(getProjectName());
    }

    // --- UI Callbacks ----

    /**
     * Enables or disable the location widgets depending on the user selection:
     * the location path is enabled when using the "existing source" mode (i.e. not new project)
     * or in new project mode with the "use default location" turned off.
     */
    private void enableLocationWidgets() {
        boolean is_new_project = isNewProject();
        boolean use_default = useDefaultLocation();
        boolean location_enabled = !is_new_project || !use_default;

        mUseDefaultLocation.setEnabled(is_new_project);

        mLocationLabel.setEnabled(location_enabled);
        mLocationPathField.setEnabled(location_enabled);
        mBrowseButton.setEnabled(location_enabled);

        mPackageNameField.setEnabled(is_new_project);
        mActivityNameField.setEnabled(is_new_project);

        updateLocationPathField(null);
        updatePackageAndActivityFields();
    }

    /**
     * Updates the location directory path field.
     * <br/>
     * When custom user selection is enabled, use the abs_dir argument if not null and also
     * save it internally. If abs_dir is null, restore the last saved abs_dir. This allows the
     * user selection to be remembered when the user switches from default to custom.
     * <br/>
     * When custom user selection is disabled, use the workspace default location with the
     * current project name. This does not change the internally cached abs_dir.
     *
     * @param abs_dir A new absolute directory path or null to use the default.
     */
    private void updateLocationPathField(String abs_dir) {
        boolean is_new_project = isNewProject();
        boolean use_default = useDefaultLocation();
        boolean custom_location = !is_new_project || !use_default;

        if (!mInternalLocationPathUpdate) {
            mInternalLocationPathUpdate = true;
            if (custom_location) {
                if (abs_dir != null) {
                    mCustomLocationOsPath = TextProcessor.process(abs_dir);
                }
                if (!mLocationPathField.getText().equals(mCustomLocationOsPath)) {
                    mLocationPathField.setText(mCustomLocationOsPath);
                }
                extractNamesFromAndroidManifest();
            } else {
                String value = Platform.getLocation().append(getProjectName()).toString();
                value = TextProcessor.process(value);
                if (!mLocationPathField.getText().equals(value)) {
                    mLocationPathField.setText(value);
                }
            }
            setPageComplete(validatePage());
            mInternalLocationPathUpdate = false;
        }
    }

    /**
     * The location path field is either modified internally (from updateLocationPathField)
     * or manually by the user when the custom_location mode is not set.
     *
     * Ignore the internal modification. When modified by the user, memorize the choice and
     * validate the page.
     */
    private void onLocationPathFieldModified() {
        if (!mInternalLocationPathUpdate) {
            // When the updates doesn't come from updateLocationPathField, it must be the user
            // editing the field manually, in which case we want to save the value internally.
            mCustomLocationOsPath = getLocationPathFieldValue();
            extractNamesFromAndroidManifest();
            setPageComplete(validatePage());
        }
    }

    /**
     * The package name field is either modified internally (from extractNamesFromAndroidManifest)
     * or manually by the user when the custom_location mode is not set.
     *
     * Ignore the internal modification. When modified by the user, memorize the choice and
     * validate the page.
     */
    private void onPackageNameFieldModified() {
        if (isNewProject()) {
            mCustomPackageName = getPackageName();
            setPageComplete(validatePage());
        }
    }

    /**
     * The activity name field is either modified internally (from extractNamesFromAndroidManifest)
     * or manually by the user when the custom_location mode is not set.
     *
     * Ignore the internal modification. When modified by the user, memorize the choice and
     * validate the page.
     */
    private void onActivityNameFieldModified() {
        if (isNewProject()) {
            mCustomActivityName = getActivityName();
            setPageComplete(validatePage());
        }
    }

    /**
     * Called when the radio buttons are changed between the "create new project" and the
     * "use existing source" mode. This reverts the fields to whatever the user manually
     * entered before.
     */
    private void updatePackageAndActivityFields() {
        if (isNewProject()) {
            if (mCustomPackageName.length() > 0 &&
                    !mPackageNameField.getText().equals(mCustomPackageName)) {
                mPackageNameField.setText(mCustomPackageName);
            }

            if (mCustomActivityName.length() > 0 &&
                    !mActivityNameField.getText().equals(mCustomActivityName)) {
                mActivityNameField.setText(mCustomActivityName);
            }
        }
    }

    /**
     * Extract names from an android manifest.
     * This is done only if the user selected the "use existing source" and a manifest xml file
     * can actually be found in the custom user directory.
     */
    private void extractNamesFromAndroidManifest() {
        if (!isNewProject()) {
            File f = new File(getProjectLocation());
            if (f.isDirectory()) {
                Path path = new Path(f.getPath());
                String osPath = path.append(AndroidConstants.FN_ANDROID_MANIFEST).toOSString();
                AndroidManifestFromPath manifest = new AndroidManifestFromPath(osPath);
                if (manifest.exists()) {
                    String packageName = null;
                    String activityName = null;
                    try {
                        packageName = manifest.getPackageName();
                        activityName = manifest.getActivityName(1);
                    } catch (Exception e) {
                    }


                    if (packageName != null && packageName.length() > 0) {
                        mPackageNameField.setText(packageName);
                    }

                    if (activityName != null || activityName.length() > 0) {
                        mActivityNameField.setText(activityName);

                        // If project name and application names are empty, use the activity
                        // name as a default. If the activity name has dots, it's a part of a
                        // package specification and only the last identifier must be used.
                        if (activityName.indexOf('.') != -1) {
                            String[] ids = activityName.split(AndroidConstants.RE_DOT);
                            activityName = ids[ids.length - 1];
                        }
                        if (mProjectNameField.getText().length() == 0) {
                            mProjectNameField.setText(activityName);
                        }
                        if (mApplicationNameField.getText().length() == 0) {
                            mApplicationNameField.setText(activityName);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns whether this page's controls currently all contain valid values.
     *
     * @return <code>true</code> if all controls are valid, and
     *         <code>false</code> if at least one is invalid
     */
    protected boolean validatePage() {
        IWorkspace workspace = ResourcesPlugin.getWorkspace();

        if (validateProjectField(workspace)
            && validateLocationPath(workspace)
            && validatePackageField()
            && validateActivityField()
            && validateSourceFolder()) {
            setStatus(null, WizardPage.NONE);
            return true;
        } else {
            // Return false if there's an error (either the message type is set to
            // error or there's an actual error message) so that the finish button be disabled.
            return getMessageType() != WizardPage.ERROR && getErrorMessage() == null;
        }
    }

    /**
     * Validates the project name field.
     *
     * @return True if field is ok, otherwise false and sets up the reported error.
     */
    private boolean validateProjectField(IWorkspace workspace) {
        // Validate project field
        String projectFieldContents = getProjectName();
        if (projectFieldContents.length() == 0) {
            return setStatus("Project name must be specified", WizardPage.ERROR);
        }

        // Limit the project name to shell-agnostic characters since it will be used to
        // generate the final package
        if (!sProjectNamePattern.matcher(projectFieldContents).matches()) {
            return setStatus("The project name must start with an alphanumeric characters, followed by one or more alphanumerics, digits, dots, dashes, underscores or spaces.",
                    WizardPage.ERROR);
        }

        IStatus nameStatus = workspace.validateName(projectFieldContents, IResource.PROJECT);
        if (!nameStatus.isOK()) {
            return setStatus(nameStatus.getMessage(), WizardPage.ERROR);
        }

        if (getProjectHandle().exists()) {
            return setStatus("A project with that name already exists in the workspace",
                             WizardPage.ERROR);
        }

        return true;
    }

    /**
     * Validates the location path field.
     *
     * @return True if field is ok, otherwise false and sets up the reported error.
     */
    private boolean validateLocationPath(IWorkspace workspace) {
        Path path = new Path(getProjectLocation());
        if (isNewProject()) {
            if (!useDefaultLocation()) {
                // If not using the default value validate the location.
                URI uri = URIUtil.toURI(path.toOSString());
                IStatus locationStatus = workspace.validateProjectLocationURI(getProjectHandle(),
                        uri);
                if (!locationStatus.isOK()) {
                    return setStatus(locationStatus.getMessage(), WizardPage.ERROR);
                } else {
                    // The location is valid as far as Eclipse is concerned (i.e. mostly not
                    // an existing workspace project.) Check it either doesn't exist or is
                    // a directory that is empty.
                    File f = new File(path.toOSString());
                    if (f.exists() && !f.isDirectory()) {
                        return setStatus("A directory name must be specified.", WizardPage.ERROR);
                    } else if (f.isDirectory()) {
                        // However if the directory exists, we should put a warning if it is not
                        // empty. We don't put an error (we'll ask the user again for confirmation
                        // before using the directory.)
                        String[] l = f.list();
                        if (l.length != 0) {
                            return setStatus("The selected output directory is not empty.",
                                    WizardPage.WARNING);
                        }
                    }
                }
            } else {
                // Otherwise validate the path string is not empty
                if (getProjectLocation().length() == 0) {
                    return setStatus("A directory name must be specified.", WizardPage.ERROR);
                }
            }
        } else {
            // Must be an existing directory
            File f = new File(path.toOSString());
            if (!f.isDirectory()) {
                return setStatus("An existing directory name must be specified.", WizardPage.ERROR);
            }
            // Check there's an android manifest in the directory
            String osPath = path.append(AndroidConstants.FN_ANDROID_MANIFEST).toOSString();
            AndroidManifestFromPath manifest = new AndroidManifestFromPath(osPath);
            if (!manifest.exists()) {
                return setStatus(
                        String.format("File %1$s not found in %2$s.",
                                AndroidConstants.FN_ANDROID_MANIFEST, f.getName()),
                        WizardPage.ERROR);
            }

            // Parse it. As a side effect
            String packageName = manifest.getPackageName();
            if (packageName == null || packageName.length() == 0) {
                return setStatus(
                        String.format("No package name defined in %1$s.", osPath),
                        WizardPage.ERROR);
            }

            String activityName = manifest.getActivityName(1);
            if (activityName == null || activityName.length() == 0) {
                return setStatus(
                        String.format("No activity name defined in %1$s.", osPath),
                        WizardPage.ERROR);
            }
        }

        return true;
    }

    /**
     * Validates the activity name field.
     * This is skipped if an existing manifest is being used.
     *
     * @return True if field is ok, otherwise false and sets up the reported error.
     */
    private boolean validateActivityField() {
        if (isNewProject()) {
            // Validate activity field
            String activityFieldContents = getActivityName();
            if (activityFieldContents.length() == 0) {
                return setStatus("Activity name must be specified.", WizardPage.ERROR);
            }

            // Check it's a valid activity string
            IStatus status;
            status = JavaConventions.validateTypeVariableName(activityFieldContents);
            if (!status.isOK()) {
                return setStatus(status.getMessage(),
                        status.getSeverity() == IStatus.ERROR ? WizardPage.ERROR
                                                              : WizardPage.WARNING);
            }
        }

        return true;
    }

    /**
     * Validates the package name field.
     * This is skipped if an existing manifest is being used.
     *
     * @return True if field is ok, otherwise false and sets up the reported error.
     */
    private boolean validatePackageField() {
        if (isNewProject()) {
            // Validate package field
            String packageFieldContents = getPackageName();
            if (packageFieldContents.length() == 0) {
                return setStatus("Package name must be specified.", WizardPage.ERROR);
            }

            // Check it's a valid package string
            IStatus status = JavaConventions.validatePackageName(packageFieldContents);
            if (!status.isOK()) {
                return setStatus(status.getMessage(),
                        status.getSeverity() == IStatus.ERROR ? WizardPage.ERROR
                                                              : WizardPage.WARNING);
            }

            // The Android Activity Manager does not accept packages names with only one
            // identifier. Check the package name has at least one dot in them (the previous rule
            // validated that if such a dot exist, it's not the first nor last characters of the
            // string.)
            if (packageFieldContents.indexOf('.') == -1) {
                return setStatus("Package name must have at least two identifiers.",
                        WizardPage.ERROR);
            }
        }

        return true;
    }

    /**
     * Validates that an existing project actually has a source folder.
     *
     * For project in "use existing source" mode, this tries to find the source folder.
     * A source folder should be just under the project directory and it should have all
     * the directories composing the package+activity name.
     *
     * As a side effect, it memorizes the source folder in mSourceFolder.
     *
     * TODO: support multiple source folders for multiple activities.
     */
    private boolean validateSourceFolder() {
        // This check does nothing when creating a new project
        if (isNewProject()) return true;

        String osTarget = getActivityName();
        if (osTarget.indexOf('.') == -1) {
            osTarget = getPackageName() + File.separator + osTarget;
        } else if (osTarget.indexOf('.') == 0) {
            osTarget = getPackageName() + osTarget;
        }
        osTarget = osTarget.replace('.', File.separatorChar) + AndroidConstants.DOT_JAVA;

        String projectPath = getProjectLocation();
        File projectDir = new File(projectPath);
        File[] all_dirs = projectDir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        for (File f : all_dirs) {
            Path path = new Path(f.getAbsolutePath());
            File java_activity = new File(path.append(osTarget).toOSString());
            if (java_activity.isFile()) {
                mSourceFolder = f.getName();
                return true;
            }
        }

        if (all_dirs.length > 0) {
            return setStatus(
                    String.format("%1$s can not be found under %2$s.", osTarget, projectPath),
                    WizardPage.ERROR);
        } else {
            return setStatus(
                    String.format("No source folders can be found in %1$s.", projectPath),
                    WizardPage.ERROR);
        }
    }
    /**
     * Sets the error message for the wizard with the given message icon.
     *
     * @return As a convenience, always returns false so that the caller can
     *         abort immediately.
     */
    private boolean setStatus(String message, int messageType) {
        if (message == null) {
            setErrorMessage(null);
            setMessage(null);
        } else if (!message.equals(getMessage())) {
            setMessage(message, messageType);
        }
        return false;
    }

}
