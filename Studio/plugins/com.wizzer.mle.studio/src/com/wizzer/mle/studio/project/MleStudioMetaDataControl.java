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
package com.wizzer.mle.studio.project;

// Import standard Java classes.
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;

// Import Eclipse classes.
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.dpp.project.MasteringMessages;
import com.wizzer.mle.studio.dpp.properties.GendppPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GengroupPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenmediaPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenppscriptPropertyManager;
import com.wizzer.mle.studio.dpp.properties.GenscenePropertyManager;
import com.wizzer.mle.studio.project.metadata.MleTitleMetaData;

public class MleStudioMetaDataControl
{
    // The associated project;
    private IPath m_locationPath = null;
    // The UI control.
    private Composite m_theControl = null;

    protected Composite m_topControl;

    /** The verbose button widget. */
    protected Button m_verboseButton;
    /** The Java code generation button widget. */
    protected Button m_javaCodegenButton;
    /** The C++ code generation button widget. */
    protected Button m_cppCodegenButton;
    /** The big endian button widget. */
    protected Button m_bigEndianButton;
    /** The little endian button widget. */
    protected Button m_littleEndianButton;
    /** The destination directory text widget. */
    protected Text m_destinationNameText;
    /** The destination directory browse button widget. */
    protected Button m_destinationBrowseButton;
    /** The gengroup ActorId text widget. */
    protected Text m_gengroupActorIdText;
    /** The gengroup GroupId text widget. */
    protected Text m_gengroupGroupIdText;
    /** The gengroup fixed-point button. */
    protected Button m_gengroupFixedPointButton;
    /** The genscene SceneId text widget. */
    protected Text m_gensceneSceneIdText;
    /** The genscene fixed-point button. */
    protected Button m_gensceneFixedPointButton;
    /** The genmedia BOM text widget. */
    protected Text m_genmediaBomText;
    /** The genppscript DPP text widget. */
    protected Text m_genppscriptDppText;
    /** The genppscript script text widget. */
    protected Text m_genppscriptScriptText;
    /** The genppscript TOC text widget. */
    protected Text m_genppscriptTocText;
    /** The gendpp script widget. */
    protected Text m_gendppScriptText;
    /** The gendpp TCL script browse button. */
    protected Button m_gendppScriptBrowseButton;
    /** The gendpp source directory text widget. */
    protected Text m_gendppSourceDirText;
    /** The gendpp source directroy browse button. */
    protected Button m_gendppSourceBrowseButton;

    /** The Java package name button widget. */
    protected Text m_javaPackageText;
    /** The tag discriminator table widget. */
    protected Table m_tagTable;

    /** Registry of event listeners. */
    protected Vector m_eventListeners = new Vector();

    /** Magic Lantern Project meta data. */
    protected MleTitleMetaData m_metaData = null;
    
    /** Current error message. */
    protected String m_errorMsg = null;
    
    public class MetaDataControlConfig
    {
    	public boolean m_isVerbose = false;

    	public boolean m_isDppProject = true;
    	public boolean m_hasGengroup = true;
    	public boolean m_hasGenscene = true;
    	public boolean m_hasGenmedia = true;
    	public boolean m_hasGentables = true;
    	public boolean m_hasGenppscript = true;
    	public boolean m_hasGendpp = true;

    	public boolean m_isJavaProject = true;
    	public boolean m_isCppProject = false;
    	
    	public String m_actorId = GengroupPropertyManager.DPP_GENGROUP_ACTORID_JAVA_VALUE;
    	public String m_groupId = GengroupPropertyManager.DPP_GENGROUP_GROUPID_JAVA_VALUE;
    	public String m_sceneId = GenscenePropertyManager.DPP_GENSCENE_SCENEID_JAVA_VALUE;
    	
    	public boolean m_isBigEndian = true;
    	public boolean m_isFixedPoint = false;
    	
    	public String m_bom = "MediaBom.txt";
    	
    	public String m_dpp = "Simple.dpp";
    	
    	public String m_script = "playprint.tcl";
    	
    	public String m_scriptPath = "src/gen/playprint.tcl";
    	
    	public String m_toc = "DppTOC";
    	
    	public String m_srcDirectory = "src/gen";
    	
    	public String m_dstDirectory = "src/gen";
    	
    	public String m_javaPackage = "gen";
    	
    	public Vector m_tags = new Vector();
    	
    	public boolean isDppProject()
    	{
    		return m_isDppProject;
    	}
    	
    	public boolean getGengroupValue()
    	{
    		return m_hasGengroup;
    	}
    	
    	public boolean getGensceneValue()
    	{
    		return m_hasGenscene;
    	}
    	
    	public boolean getGenmediaValue()
    	{
    		return m_hasGenmedia;
    	}
    	
    	public boolean getGentablesValue()
    	{
    		return m_hasGentables;
    	}
    	
    	public boolean getGenppscriptValue()
    	{
    		return m_hasGenppscript;
    	}
    	
    	public boolean getGendppValue()
    	{
    		return m_hasGendpp;
    	}
    	
    	public boolean getVerboseValue()
    	{
    		return m_isVerbose;
    	}
    	
    	public boolean isJavaProject()
    	{
    		return m_isJavaProject;
    	}
    	
    	public boolean isCppProject()
    	{
    		return m_isCppProject;
    	}
    	
    	public String getActorIdValue()
    	{
    		return m_actorId;
    	}
    	
    	public String getGroupIdValue()
    	{
    		return m_groupId;
    	}
    	
    	public String getSceneIdValue()
    	{
    		return m_sceneId;
    	}
    	
    	public boolean getFixedPointValue()
    	{
    		return m_isFixedPoint;
    	}
    	
    	public boolean isBigEndian()
    	{
    		return m_isBigEndian;
    	}
    	
    	public boolean isLittleEndian()
    	{
    		return (! m_isBigEndian);
    	}
    	
    	public String getBomValue()
    	{
    		return m_bom;
    	}
    	
    	public String getDppValue()
    	{
    		return m_dpp;
    	}
    	
    	public String[] getTagsValue()
    	{
    		String[] tags = null;
    		if (! m_tags.isEmpty())
    		{
    			tags = new String[m_tags.size()];
    			for (int i = 0; i < m_tags.size(); i++)
    			{
    				tags[i] = new String((String)m_tags.get(i));
    			}
    		}
    		return tags;
    	}
    	
    	public String getScriptValue()
    	{
    		return m_script;
    	}
    	
    	public String getScriptPathValue()
    	{
    		return m_scriptPath;
    	}
    	
    	public String getTocValue()
    	{
    		return m_toc;
    	}
    	
    	public String getSourceDirValue()
    	{
    		return m_srcDirectory;
    	}
    	
    	public String getDestinationDirValue()
    	{
    		return m_dstDirectory;
    	}
    	
    	public String getJavaPackageValue()
    	{
    		return m_javaPackage;
    	}
    }

    /**
     * The default constructor.
     * <p>
     * A single title entry is created with placeholder data.
     * </p>
     */
    public MleStudioMetaDataControl()
    {
    	m_metaData = new MleTitleMetaData();
    	Calendar date = Calendar.getInstance();
    	m_metaData.setCreationDate(date);

		// Create a new title entry.
		MleTitleMetaData.ProjectDataElement titleElement = m_metaData.new ProjectDataElement();
		titleElement.setId("www.wizzerworks.com.unknown.project");
		titleElement.setDwpFile("workprints/unknown.dwp");
		titleElement.setVersion("1.0");
		
		// Add a new mastering target.
		MleTitleMetaData.MasterTargetElement targetElement = m_metaData.new MasterTargetElement();
		targetElement.setId("www.wizzerworks.com.unknown.target");
		targetElement.setDigitalPlayprint("gen/unknown.dpp");

		targetElement.setVerbose(false);
		targetElement.setBigEndian(true);
		targetElement.setFixedPoint(false);
		targetElement.setCodeGeneration("java");
		targetElement.setHeaderPackage("gen");
		targetElement.setDestinationDirectory("src/gen");
		targetElement.setGengroup(true);
		targetElement.setActorIdFile("ActorID.java");
		targetElement.setGroupIdFile("GroupID.java");
		targetElement.setGenscene(true);
		targetElement.setSceneIdFile("SceneID.java");
		targetElement.setGenmedia(true);
		targetElement.setBomFile("MediaBom.txt");
		targetElement.setGenppscript(true);
		targetElement.setScriptFile("playprint.tcl");
		targetElement.setTocName("DppTOC");
		targetElement.setGendpp(true);
		targetElement.setSourceDirectory("src/gen");
		targetElement.setScriptPath("src/gen/playprint.tcl");
		
		MleTitleMetaData.MasterTargetElement[] targets = new MleTitleMetaData.MasterTargetElement[1];
		targets[0] = targetElement;
		titleElement.setMasterTargets(targets);
		
		// Add the title to the meta-data.
		Object[] titles = new Object[1];
		titles[0] = titleElement;
		m_metaData.setProjectData(titles);
    }
    
    /**
     * Create the control for the meta-data using the specified
     * parent and style.
     * <p>
     * The top level control is created, but not populated with the
     * widgets comprising the meta-data UI. The additional widgets
     * are created using <code>initControl</code>.
     * </p>
     * 
     * @param parent The parent <code>Composite</code> widget.
     * @param style The <code>SWT</code> style.
     * @param configuration The initial configuration data for the control.
     * 
     * @return A reference to the new <code>Control</code> is returned.
     */
    public Control createControl(Composite parent, int style, MetaDataControlConfig configuration)
    {
        m_topControl = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        m_topControl.setLayout(layout);
        GridData gd = new GridData(GridData.FILL_BOTH);
        m_topControl.setLayoutData(gd);

        if (! configuration.isDppProject())
        {
            Label infoLabel = new Label(m_topControl, SWT.NONE);
            infoLabel.setText(MasteringMessages.getString("MasteringPropertyPage.notDppProject"));
            return m_topControl;
        }

        boolean hasGengroup = configuration.getGengroupValue();
        boolean hasGenscene = configuration.getGensceneValue();
        boolean hasGenmedia = configuration.getGenmediaValue();
        boolean hasGentables = configuration.getGentablesValue();
        boolean hasGenppscript = configuration.getGenppscriptValue();
        boolean hasGendpp = configuration.getGendppValue();

        // Create a tab folder.
        TabFolder tabFolder = new TabFolder(m_topControl, SWT.NONE);
        gd = new GridData(GridData.FILL_BOTH);
        tabFolder.setLayoutData(gd);
        
        // Create a tab item for common mastering properties.
        TabItem commonTab = new TabItem(tabFolder, SWT.NONE);

        // Create a composite container for the common mastering properties.
        Composite commonTabComposite = new Composite(tabFolder, SWT.NONE);
        layout = new GridLayout();
        commonTabComposite.setLayout(layout);
        commonTab.setControl(commonTabComposite);
        commonTab.setText(MasteringMessages.getString("MasteringPropertyPage.commonTabName"));
 
        // Create common controls.
        createCommonControls(commonTabComposite, configuration);

        if (hasGengroup)
        {
	        // Create a tab item for gengroup mastering properties.
	        TabItem gengroupTab = new TabItem(tabFolder, SWT.NONE);
	
	        // Create a composite container for the gengroup mastering properties.
	        Composite gengroupTabComposite = new Composite(tabFolder, SWT.NONE);
	        layout = new GridLayout();
	        gengroupTabComposite.setLayout(layout);
	        gengroupTab.setControl(gengroupTabComposite);
	        gengroupTab.setText(MasteringMessages.getString("MasteringPropertyPage.gengroupTabName"));
	
	        // Create the contents for the gengroup command.
	        createGengroupControls(gengroupTabComposite, configuration);
        }

        if (hasGenscene)
        {
	        // Create a tab item for genscene mastering properties.
	        TabItem gensceneTab = new TabItem(tabFolder, SWT.NONE);
	
	        // Create a composite container for the genscene mastering properties.
	        Composite gensceneTabComposite = new Composite(tabFolder, SWT.NONE);
	        layout = new GridLayout();
	        gensceneTabComposite.setLayout(layout);
	        gensceneTab.setControl(gensceneTabComposite);
	        gensceneTab.setText(MasteringMessages.getString("MasteringPropertyPage.gensceneTabName"));
	
	        // Create the contents for the genscene command.
	        createGensceneControls(gensceneTabComposite, configuration);
        }

        if (hasGenmedia)
        {
	        // Create a tab item for genmedia mastering properties.
	        TabItem genmediaTab = new TabItem(tabFolder, SWT.NONE);
	
	        // Create a composite container for the genmedia mastering properties.
	        Composite genmediaTabComposite = new Composite(tabFolder, SWT.NONE);
	        layout = new GridLayout();
	        genmediaTabComposite.setLayout(layout);
	        genmediaTab.setControl(genmediaTabComposite);
	        genmediaTab.setText(MasteringMessages.getString("MasteringPropertyPage.genmediaTabName"));
	
	        // Create the contents for the genmedia command.
	        createGenmediaControls(genmediaTabComposite, configuration);
        }

        if (hasGenppscript)
        {
	        // Create a tab item for genppscript mastering properties.
	        TabItem genppscriptTab = new TabItem(tabFolder, SWT.NONE);
	
	        // Create a composite container for the genppscript mastering properties.
	        Composite genppscriptTabComposite = new Composite(tabFolder, SWT.NONE);
	        layout = new GridLayout();
	        genppscriptTabComposite.setLayout(layout);
	        genppscriptTab.setControl(genppscriptTabComposite);
	        genppscriptTab.setText(MasteringMessages.getString("MasteringPropertyPage.genppscriptTabName"));
	
	        // Create the contents for the genppscript command.
	        createGenppscriptControls(genppscriptTabComposite, configuration);
        }

        if (hasGendpp)
        {
	        // Create a tab item for gendpp mastering properties.
	        TabItem gendppTab = new TabItem(tabFolder, SWT.NONE);
	
	        // Create a composite container for the gendpp mastering properties.
	        Composite gendppTabComposite = new Composite(tabFolder, SWT.NONE);
	        layout = new GridLayout();
	        gendppTabComposite.setLayout(layout);
	        gendppTab.setControl(gendppTabComposite);
	        gendppTab.setText(MasteringMessages.getString("MasteringPropertyPage.gendppTabName"));
	
	        // Create the contents for the gendpp command.
	        createGendppControls(gendppTabComposite, configuration);
        }

        return m_topControl;
    }
    
    /**
     * Create the widgets for specifying common mastering parameters.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the common control.
     */
    protected void createCommonControls(Composite parent, MetaDataControlConfig configuration)
    {
		createDestinationControls(parent, configuration);
		createCodeGenerationControls(parent, configuration);
		createByteOrderControls(parent, configuration);
		createTagControls(parent, configuration);
		
		m_verboseButton = new Button(parent, SWT.CHECK);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		m_verboseButton.setLayoutData(gd);
		m_verboseButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleVerboseButtonPressed();
			}
		});
		m_verboseButton.setText(MasteringMessages.getString("MasteringPropertyPage.verbose"));
		
		if (configuration.getVerboseValue())
		    m_verboseButton.setSelection(true);
		else
		    m_verboseButton.setSelection(false);
    }

    /**
     * Create the widgets for the <b>gengroup</b> mastering command.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the gengroup control.
     */
    protected void createGengroupControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Composite gengroupGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        gengroupGroup.setLayout(layout);
        GridData gengroupGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gengroupGroupData.horizontalSpan = 2;
        gengroupGroup.setLayoutData(gengroupGroupData);

		// Controls for specifying Actor ID information.		
		Label actorIdLabel = new Label(gengroupGroup, SWT.NONE);
		actorIdLabel.setText(MasteringMessages.getString("MasteringPropertyPage.actorIdFileLabel"));
		
		m_gengroupActorIdText = new Text(gengroupGroup, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_gengroupActorIdText.setLayoutData(gd);
		m_gengroupActorIdText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGengroupActorIdTextModified();
		    }
        });

		// Controls for specifying the Group ID information.
		Label groupIdLabel = new Label(gengroupGroup, SWT.NONE);
		groupIdLabel.setText(MasteringMessages.getString("MasteringPropertyPage.groupIdFileLabel"));
		
		m_gengroupGroupIdText = new Text(gengroupGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_gengroupGroupIdText.setLayoutData(gd);
		m_gengroupGroupIdText.addModifyListener(new ModifyListener()
        {
		    public void modifyText(ModifyEvent event)
		    {
		        handleGengroupGroupIdTextModified();
		    }
        });
		
		Label dummy = new Label(gengroupGroup, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		dummy.setLayoutData(gd);
		
		m_gengroupFixedPointButton = new Button(gengroupGroup, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_gengroupFixedPointButton.setLayoutData(gd);
		m_gengroupFixedPointButton.setText(MasteringMessages.getString("MasteringPropertyPage.useFixedPointLabel"));
		m_gengroupFixedPointButton.setEnabled(false);
		
		if (configuration.isJavaProject())
		{
		    String actorId = configuration.getActorIdValue();
		    if ((actorId == null) || (actorId.equals("")))
		    	m_gengroupActorIdText.setText(GengroupPropertyManager.DPP_GENGROUP_ACTORID_JAVA_VALUE);
		    else
		        m_gengroupActorIdText.setText(actorId);

		    String groupId = configuration.getGroupIdValue();
		    if ((groupId == null) || (groupId.equals("")))
		    	m_gengroupGroupIdText.setText(GengroupPropertyManager.DPP_GENGROUP_GROUPID_JAVA_VALUE);
		    else
		        m_gengroupGroupIdText.setText(groupId);

			// Java targets don't support fixed-point format.
			m_gengroupFixedPointButton.setEnabled(false);
		} else if (configuration.isCppProject())
		{
		    String actorId = configuration.getActorIdValue();
		    if ((actorId == null) || (actorId.equals("")))
		    	m_gengroupActorIdText.setText(GengroupPropertyManager.DPP_GENGROUP_ACTORID_CPP_VALUE);
		    else
		        m_gengroupActorIdText.setText(actorId);

		    String groupId = configuration.getGroupIdValue();
		    if ((groupId == null) || (groupId.equals("")))
		    	m_gengroupGroupIdText.setText(GengroupPropertyManager.DPP_GENGROUP_GROUPID_CPP_VALUE);
		    else
		        m_gengroupGroupIdText.setText(groupId);
		    
		    if (configuration.getFixedPointValue())
		        m_gengroupFixedPointButton.setEnabled(true);
		    else
		        m_gengroupFixedPointButton.setEnabled(false);
		    
		}
    }
    /**
     * Create the widgets for the <b>genscene</b> mastering command.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the genscene control.
     */
    protected void createGensceneControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Composite gensceneGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        gensceneGroup.setLayout(layout);
        GridData gensceneGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gensceneGroupData.horizontalSpan = 2;
        gensceneGroup.setLayoutData(gensceneGroupData);

		// Controls for spcefiying Scene ID information.		
		Label sceneIdLabel = new Label(gensceneGroup, SWT.NONE);
		sceneIdLabel.setText(MasteringMessages.getString("MasteringPropertyPage.sceneIdFileLabel"));
		
		m_gensceneSceneIdText = new Text(gensceneGroup, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_gensceneSceneIdText.setLayoutData(gd);
		m_gensceneSceneIdText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGensceneSceneIdTextModified();
		    }
        });
		
		Label dummy = new Label(gensceneGroup, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		dummy.setLayoutData(gd);
		
		m_gensceneFixedPointButton = new Button(gensceneGroup, SWT.CHECK);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_gensceneFixedPointButton.setLayoutData(gd);
		m_gensceneFixedPointButton.setText(MasteringMessages.getString("MasteringPropertyPage.useFixedPointLabel"));
		m_gensceneFixedPointButton.setEnabled(false);
		
		if (configuration.isJavaProject())
		{
		    String sceneId = configuration.getSceneIdValue();
		    if ((sceneId == null) || (sceneId.equals("")))
		    	m_gensceneSceneIdText.setText(GenscenePropertyManager.DPP_GENSCENE_SCENEID_JAVA_VALUE);
		    else
		        m_gensceneSceneIdText.setText(sceneId);

			// Java targets don't support fixed-point format.
			m_gensceneFixedPointButton.setEnabled(false);
		} else if (configuration.isCppProject())
		{
		    String sceneId = configuration.getSceneIdValue();
		    if ((sceneId == null) || (sceneId.equals("")))
		    	m_gensceneSceneIdText.setText(GenscenePropertyManager.DPP_GENSCENE_SCENEID_CPP_VALUE);
		    else
		        m_gensceneSceneIdText.setText(sceneId);
		    
		    if (configuration.getFixedPointValue())
		        m_gensceneFixedPointButton.setEnabled(true);
		    else
		        m_gensceneFixedPointButton.setEnabled(false);
		    
		}
    }
    /**
     * Create the widgets for the <b>genmedia</b> mastering command.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the genmedia control.
     */
    protected void createGenmediaControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Composite genmediaGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        genmediaGroup.setLayout(layout);
        GridData genmediaGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        genmediaGroupData.horizontalSpan = 2;
        genmediaGroup.setLayoutData(genmediaGroupData);

		// Controls for specifying Scene ID information.		
		Label bomLabel = new Label(genmediaGroup, SWT.NONE);
		bomLabel.setText(MasteringMessages.getString("MasteringPropertyPage.bomFileLabel"));
		
		m_genmediaBomText = new Text(genmediaGroup, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_genmediaBomText.setLayoutData(gd);
		m_genmediaBomText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		    	handleGenmediaBomTextModified();
		    }
        });
		
	    String bom = configuration.getBomValue();
	    if ((bom == null) || (bom.equals("")))
	    	m_genmediaBomText.setText(GenmediaPropertyManager.DPP_GENMEDIA_BOM_VALUE);
	    else
	        m_genmediaBomText.setText(bom);
    }
    
    /**
     * Create the widgets for the <b>genppscript</b> mastering command.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the genppscript control.
     */
    protected void createGenppscriptControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Composite genppscriptGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        genppscriptGroup.setLayout(layout);
        GridData genppscriptGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        genppscriptGroupData.horizontalSpan = 2;
        genppscriptGroup.setLayoutData(genppscriptGroupData);

		// Controls for specifying Digital Playprint information.		
		Label dppLabel = new Label(genppscriptGroup, SWT.NONE);
		dppLabel.setText(MasteringMessages.getString("MasteringPropertyPage.dppFileLabel"));
		
		m_genppscriptDppText = new Text(genppscriptGroup, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_genppscriptDppText.setLayoutData(gd);
		m_genppscriptDppText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGenppscriptDppTextModified();
		    }
        });

		// Controls for specifying TCL script information.		
		Label scriptLabel = new Label(genppscriptGroup, SWT.NONE);
		scriptLabel.setText(MasteringMessages.getString("MasteringPropertyPage.scriptFileLabel"));
		
		m_genppscriptScriptText = new Text(genppscriptGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_genppscriptScriptText.setLayoutData(gd);
		m_genppscriptScriptText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGenppscriptScriptTextModified();
		    }
        });

		// Controls for specifying TOC information.		
		Label tocLabel = new Label(genppscriptGroup, SWT.NONE);
		tocLabel.setText(MasteringMessages.getString("MasteringPropertyPage.tocNameLabel"));
		
		m_genppscriptTocText = new Text(genppscriptGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan  = 2;
		m_genppscriptTocText.setLayoutData(gd);
		m_genppscriptTocText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGenppscriptTocTextModified();
		    }
        });

	    String dpp = configuration.getDppValue();
	    if ((dpp == null) || (dpp.equals("")))
	    	m_genppscriptDppText.setText(GenppscriptPropertyManager.DPP_GENPPSCRIPT_DPP_VALUE);
	    else
	        m_genppscriptDppText.setText(dpp);
	    
	    String script = configuration.getScriptValue();
	    if ((script == null) || (script.equals("")))
	    	m_genppscriptScriptText.setText(GenppscriptPropertyManager.DPP_GENPPSCRIPT_SCRIPT_VALUE);
	    else
	        m_genppscriptScriptText.setText(script);

		if (configuration.isJavaProject())
		{
		    String toc = configuration.getTocValue();
		    if ((toc == null) || (toc.equals("")))
		    	m_genppscriptTocText.setText(GenppscriptPropertyManager.DPP_GENPPSCRIPT_TOC_JAVA_VALUE);
		    else
		        m_genppscriptTocText.setText(toc);
		} else if (configuration.isCppProject())
		{
		    String toc = configuration.getTocValue();
		    if ((toc == null) || (toc.equals("")))
		    	m_genppscriptTocText.setText(GenppscriptPropertyManager.DPP_GENPPSCRIPT_TOC_CPP_VALUE);
		    else
		        m_genppscriptTocText.setText(toc);
		}
    }

    /**
     * Create the widgets for the <b>gendpp</b> mastering command.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the gendpp control.
     */
    protected void createGendppControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Composite gendppGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        gendppGroup.setLayout(layout);
        GridData gendppGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        gendppGroupData.horizontalSpan = 2;
        gendppGroup.setLayoutData(gendppGroupData);

		// Controls for specifying source information.
		Composite sourceGroup = new Composite(gendppGroup, SWT.NONE);
		layout = new GridLayout(3,false);
		sourceGroup.setLayout(layout);
		GridData sourceGroupData  = new GridData(
	            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		sourceGroupData.horizontalSpan = 3;
		sourceGroup.setLayoutData(sourceGroupData);
		
		Label srcLabel = new Label(sourceGroup, SWT.NONE);
		srcLabel.setText(MasteringMessages.getString("MasteringPropertyPage.sourceDirLabel"));
		
		m_gendppSourceDirText = new Text(sourceGroup, SWT.BORDER);
		GridData gd = new GridData(
			GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		m_gendppSourceDirText.setLayoutData(gd);
		m_gendppSourceDirText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGendppSourceDirTextModified();
		    }
        });

        m_gendppSourceBrowseButton = new Button(sourceGroup, SWT.TRAIL);
        m_gendppSourceBrowseButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleSourceBrowseButtonPressed();
			}
		});
        m_gendppSourceBrowseButton.setText(MasteringMessages.getString("MasteringPropertyPage.browseLabel"));

		// Controls for specifying TCL script information.
		Composite scriptGroup = new Composite(gendppGroup, SWT.NONE);
		layout = new GridLayout(3,false);
		scriptGroup.setLayout(layout);
		GridData scriptGroupData  = new GridData(
	            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		scriptGroupData.horizontalSpan = 3;
		scriptGroup.setLayoutData(scriptGroupData);

		Label scriptLabel = new Label(scriptGroup, SWT.NONE);
		scriptLabel.setText(MasteringMessages.getString("MasteringPropertyPage.scriptFileLabel"));
		
		m_gendppScriptText = new Text(scriptGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		m_gendppScriptText.setLayoutData(gd);
		m_gendppScriptText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleGendppScriptTextModified();
		    }
        });

        m_gendppScriptBrowseButton = new Button(scriptGroup, SWT.TRAIL);
        m_gendppScriptBrowseButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleScriptBrowseButtonPressed();
			}
		});
        m_gendppScriptBrowseButton.setText(MasteringMessages.getString("MasteringPropertyPage.browseLabel"));

	    String scriptPath = configuration.getScriptPathValue();
	    if ((scriptPath == null) || (scriptPath.equals("")))
	    	m_gendppScriptText.setText(GendppPropertyManager.DPP_GENDPP_SCRIPT_VALUE);
	    else
	        m_gendppScriptText.setText(scriptPath);

	    String source = configuration.getSourceDirValue();
	    if ((source == null) || (source.equals("")))
	    	m_gendppSourceDirText.setText(GendppPropertyManager.DPP_GENDPP_SOURCE_DIR_VALUE);
	    else
	        m_gendppSourceDirText.setText(source);
    }

    /**
     * Create the widgets for the common mastering properties dealing with
     * destination parameters.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the destination control.
     */
    protected void createDestinationControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Composite destGroup = new Composite(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        destGroup.setLayout(layout);
        GridData destGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        destGroupData.horizontalSpan = 2;
        destGroup.setLayoutData(destGroupData);
        
        Label destLabel = new Label(destGroup, SWT.NONE);
        destLabel.setText(MasteringMessages.getString("MasteringPropertyPage.destinationDirLabel"));

		m_destinationNameText = new Text(destGroup, SWT.BORDER);
		GridData data = new GridData(
				GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
		m_destinationNameText.addModifyListener(new ModifyListener()
        {
		    public void modifyText(ModifyEvent event)
		    {
		        handleDestinationNameTextModified();
		    }
        });
		m_destinationNameText.setLayoutData(data);
		m_destinationNameText.setFont(font);

        m_destinationBrowseButton = new Button(destGroup, SWT.TRAIL);
        m_destinationBrowseButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleDestinationBrowseButtonPressed();
			}
		});
        m_destinationBrowseButton.setText(MasteringMessages.getString("MasteringPropertyPage.destinationBrowseLabel"));

        String destination = configuration.getDestinationDirValue();
        if ((destination == null) || (destination.equals("")))
            destination = "gen";
		m_destinationNameText.setText(destination);
    }
    
    /**
     * Create the widgets for the common mastering properties dealing with
     * the byte order of generated code and playprint chunks.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the byte order control.
     */
    protected void createByteOrderControls(Composite parent, MetaDataControlConfig configuration)
    {
		Group endianGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		endianGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		endianGroup.setLayoutData(gd);
		endianGroup.setText(MasteringMessages.getString("MasteringPropertyPage.byteOrderLabel"));
		
		m_bigEndianButton = new Button(endianGroup, SWT.RADIO | SWT.LEFT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		m_bigEndianButton.setLayoutData(gd);
		m_bigEndianButton.setText(MasteringMessages.getString("MasteringPropertyPage.bigEndianLabel"));
		
		m_littleEndianButton = new Button(endianGroup, SWT.RADIO | SWT.LEFT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		m_littleEndianButton.setLayoutData(gd);
		m_littleEndianButton.setText(MasteringMessages.getString("MasteringPropertyPage.littleEndianLabel"));

		if (configuration.isJavaProject())
		{
			m_bigEndianButton.setSelection(true);
			
			// Java targets always use big endian format.
			m_littleEndianButton.setEnabled(false);
		} else if (configuration.isCppProject())
		{
		    if (configuration.isBigEndian())
		    {
		        m_bigEndianButton.setSelection(true);
		    } else if (configuration.isLittleEndian())
		    {
		        m_littleEndianButton.setSelection(true);
		    }
		}
    }
    
    /**
     * Create the widgets for the code generation group.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the code generation control.
     */
    protected void createCodeGenerationControls(Composite parent, MetaDataControlConfig configuration)
    {
		Group codegenGroup = new Group(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 3;
		codegenGroup.setLayout(layout);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		codegenGroup.setLayoutData(gd);
		codegenGroup.setText(MasteringMessages.getString("MasteringPropertyPage.codeGenerationLabel"));
		
		m_javaCodegenButton = new Button(codegenGroup, SWT.RADIO | SWT.LEFT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		m_javaCodegenButton.setLayoutData(gd);
		m_javaCodegenButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleJavaCodeGenerationButtonPressed();
			}
		});
		m_javaCodegenButton.setText(MasteringMessages.getString("MasteringPropertyPage.javaCodeGeneration"));
		
		Label dummy = new Label(codegenGroup, SWT.NONE);
		dummy.setText("xx");
		dummy.setVisible(false);
		
		Label javaPackageLabel = new Label(codegenGroup, SWT.NONE);
		javaPackageLabel.setText(MasteringMessages.getString("MasteringPropertyPage.javaPackageLabel"));
		
		m_javaPackageText = new Text(codegenGroup, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		m_javaPackageText.setLayoutData(gd);
		m_javaPackageText.addModifyListener(new ModifyListener()
		{
		    public void modifyText(ModifyEvent event)
		    {
		        handleJavaPackageTextModified();
		    }
        });
		
		m_cppCodegenButton = new Button(codegenGroup, SWT.RADIO | SWT.LEFT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		m_cppCodegenButton.setLayoutData(gd);
		m_cppCodegenButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleCppCodeGenerationButtonPressed();
			}
		});
		m_cppCodegenButton.setText(MasteringMessages.getString("MasteringPropertyPage.cppCodeGeneration"));

		// Determine visual state.
		if (configuration.isJavaProject())
		{
		    m_javaCodegenButton.setSelection(true);
			m_javaPackageText.setText(configuration.getJavaPackageValue());
		    
		    // Disable irrelevant widgets.
		    m_cppCodegenButton.setEnabled(false);
		} else if (configuration.isCppProject())
		{
		    m_cppCodegenButton.setSelection(true);
		    
		    // Disable irrelevant widgets.
		    m_javaCodegenButton.setEnabled(false);
		    m_javaPackageText.setEnabled(false);
		}
    }
    
    /**
     * Create the widgets for the common mastering properties dealing with
     * tag discriminators.
     * 
     * @param parent The parent <code>Composite</code>.
     * @param configuration The initial configuration data for the tags control.
     */
    protected void createTagControls(Composite parent, MetaDataControlConfig configuration)
    {
        Font font = parent.getFont();

        Group tagGroup = new Group(parent, SWT.NONE);
        GridLayout layout = new GridLayout(3,false);
        tagGroup.setLayout(layout);
        GridData tagGroupData = new GridData(
            GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL);
        tagGroupData.horizontalSpan = 2;
        tagGroup.setLayoutData(tagGroupData);
        tagGroup.setText(MasteringMessages.getString("MasteringPropertyPage.tagEditorLabel"));

		m_tagTable = new Table(tagGroup, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		m_tagTable.setLayoutData(gd);
		m_tagTable.setLinesVisible(true);
		m_tagTable.setHeaderVisible(true);
		
		TableColumn tagColumn = new TableColumn(m_tagTable,SWT.LEFT);
		tagColumn.setText(MasteringMessages.getString("MasteringPropertyPage.tagColumn"));
		tagColumn.setWidth(300);
		
		// Load tags from properties.
	    String[] tags = configuration.getTagsValue();
	    if ((tags != null) && (tags.length > 0))
	    {
		    for (int i = 0; i < tags.length; i++)
		    {
		        TableItem item = new TableItem(m_tagTable,SWT.NONE);
		        item.setText(tags[i]);
		    }
	        updateTagMetaData();
	    }
		
		Button addTagButton = new Button(tagGroup,SWT.PUSH);
		gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		gd.grabExcessHorizontalSpace = true;
		addTagButton.setLayoutData(gd);
		addTagButton.setFont(font);
		addTagButton.addSelectionListener(new SelectionAdapter()
				{
			public void widgetSelected(SelectionEvent event)
			{
				handleAddTagButtonPressed();
			}
		});
		addTagButton.setText(MasteringMessages.getString("MasteringPropertyPage.addTag"));
				
		Button deleteTagButton = new Button(tagGroup,SWT.PUSH);
		//gd = new GridData(GridData.HORIZONTAL_ALIGN_END);
		//gd.grabExcessHorizontalSpace = true;
		//deleteTagButton.setLayoutData(gd);
		deleteTagButton.setFont(font);
		deleteTagButton.addSelectionListener(new SelectionAdapter()
		{
			public void widgetSelected(SelectionEvent event)
			{
				handleDeleteTagButtonPressed();
			}
		});
		deleteTagButton.setText(MasteringMessages.getString("MasteringPropertyPage.deleteTag"));

    }
    
    /**
     * Handle text field modification for the common destination directory.
     */
    protected void handleDestinationNameTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setDestinationDirectory(m_destinationNameText.getText());
		
		validatePage();
    }
    
    protected void handleGengroupActorIdTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setActorIdFile(m_gengroupActorIdText.getText());
		
		validatePage();
    }
    
    protected void handleGengroupGroupIdTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setGroupIdFile(m_gengroupGroupIdText.getText());
		
		validatePage();
    }
    
    protected void handleGensceneSceneIdTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setSceneIdFile(m_gensceneSceneIdText.getText());
		
		validatePage();
    }
    
    protected void handleGenmediaBomTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setBomFile(m_genmediaBomText.getText());
		
		validatePage();
    }
    
    protected void handleGenppscriptDppTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setDigitalPlayprint(m_genppscriptDppText.getText());
		
		validatePage();
    }
    
    protected void handleGenppscriptScriptTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

        target.setScriptFile(m_genppscriptScriptText.getText());
		
		validatePage();
    }
    
    protected void handleGenppscriptTocTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

        target.setTocName(m_genppscriptTocText.getText());
		
		validatePage();
    }
    
    protected void handleGendppSourceDirTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setSourceDirectory(m_gendppSourceDirText.getText());
		
		validatePage();
    }
    
    protected void handleGendppScriptTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setScriptPath(m_gendppScriptText.getText());
		
		validatePage();
    }
    
    protected void handleJavaPackageTextModified()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];
		
		target.setHeaderPackage(m_javaPackageText.getText());
		
		validatePage();
    }
    
    /**
     * Handle verbose button pressed.
     */
    protected void handleVerboseButtonPressed()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];
		
		target.setVerbose(m_verboseButton.getSelection());
		
    	validatePage();
    }

    /**
     * Handle the destination browse button being pressed.
     */
    protected void handleDestinationBrowseButtonPressed()
    {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setMessage(MasteringMessages.getString("MasteringPropertyPage.selectDestination"));
        
        String directory = dialog.open();
        if (directory != null)
        {
            m_destinationNameText.setText(directory);
        }
    }

	/**
     * Handle the source browse button being pressed.
     */
    protected void handleSourceBrowseButtonPressed()
    {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setMessage(MasteringMessages.getString("MasteringPropertyPage.selectSource"));
        
        String directory = dialog.open();
        if (directory != null)
        {
            m_gendppSourceDirText.setText(directory);
        }
    }

    /**
     * Handle the script browse button being pressed.
     */
    protected void handleScriptBrowseButtonPressed()
    {
        FileDialog dialog = new FileDialog(getShell());
        dialog.setText(MasteringMessages.getString("MasteringPropertyPage.selectScript"));
        
        String file = dialog.open();
        if (file != null)
        {
            m_gendppScriptText.setText(file);
        }
    }

    /**
     * Handle the Java code generation button being pressed.
     */
    protected void handleJavaCodeGenerationButtonPressed()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

        m_javaPackageText.setEditable(true);
        
        m_gengroupFixedPointButton.setEnabled(false);
        //m_gengroupActorIdText.setText(GengroupPropertyManager.DPP_GENGROUP_ACTORID_JAVA_VALUE);
        m_gengroupActorIdText.setText(target.getActorIdFile());
        //m_gengroupGroupIdText.setText(GengroupPropertyManager.DPP_GENGROUP_GROUPID_JAVA_VALUE);
        m_gengroupGroupIdText.setText(target.getGroupIdFile());

        m_gensceneFixedPointButton.setEnabled(false);
        //m_gensceneSceneIdText.setText(GenscenePropertyManager.DPP_GENSCENE_SCENEID_JAVA_VALUE);
        m_gensceneSceneIdText.setText(target.getSceneIdFile());
        
        validatePage();
    }

    /**
     * Handle the C++ code generation button being pressed.
     */
    protected void handleCppCodeGenerationButtonPressed()
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

        m_javaPackageText.setEditable(false);
        
        m_gengroupFixedPointButton.setEnabled(true);
        //m_gengroupActorIdText.setText(GengroupPropertyManager.DPP_GENGROUP_ACTORID_CPP_VALUE);
        m_gengroupActorIdText.setText(target.getActorIdFile());
        //m_gengroupGroupIdText.setText(GengroupPropertyManager.DPP_GENGROUP_GROUPID_CPP_VALUE);
        m_gengroupGroupIdText.setText(target.getGroupIdFile());

        m_gensceneFixedPointButton.setEnabled(true);
        //m_gensceneSceneIdText.setText(GenscenePropertyManager.DPP_GENSCENE_SCENEID_CPP_VALUE);
        m_gensceneSceneIdText.setText(target.getSceneIdFile());
       
        validatePage();
    }
    
    /**
     * This class is used to present a dialog to the user for
     * adding or editing a tag discriminator.
     * 
     * @author Mark S. Millard
     */
    public class TagDialog extends Dialog
    {
        // The resultant tag discriminator.
    	private Object m_result = null;
    	// The widget for editting the tag discriminator.
    	private Text m_tagText = null;
    	
    	public TagDialog (Shell parent)
    	{
    		super(parent);
    	}
    	
    	public void setTag(String tag)
    	{
    	    if (m_tagText != null)
    	        m_tagText.setText(tag);
    	    else
    	        m_result = tag;
    	}
    	
    	public String getTag()
    	{
    	    return (String)m_result;
    	}
    	
    	protected Control createDialogArea(Composite parent)
    	{
    		Composite dialogArea = (Composite) super.createDialogArea(parent);
    		
    		Composite composite = new Composite(dialogArea,SWT.NONE);
    		GridLayout layout = new GridLayout();
    		layout.numColumns = 2;
    		composite.setLayout(layout);
    		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
    		composite.setLayoutData(gd);
    		
    		// Create a label.
    		Label tagLabel = new Label(composite,SWT.NONE);
    		tagLabel.setText(MasteringMessages.getString("MasteringPropertyPage.tagLabel"));
    		
    		// Create the text widget.
    		m_tagText = new Text(composite,SWT.BORDER);
    		gd = new GridData(GridData.FILL_HORIZONTAL);
    		m_tagText.setLayoutData(gd);
    		m_tagText.addModifyListener(new ModifyListener()
    		{
    			public void modifyText(ModifyEvent evt)
    			{
    				m_result = m_tagText.getText();
    				validateTag();
    			}
    		});
    		
    		return dialogArea;
    	}
    	
    	protected void validateTag()
    	{
    	    Button okButton = getButton(IDialogConstants.OK_ID);
			if ((m_result == null) || (m_result.equals("")))
			{
			    if (okButton != null)
			        okButton.setEnabled(false);
			} else
			{
			    if (okButton != null)
			        okButton.setEnabled(true);
			}
    	}
    }
    
    // Search the tag table for an existing entry.
    private boolean tagExists(String tag)
    {
        boolean found = false;
        
        TableItem[] items = m_tagTable.getItems();
        for (int i = 0; i < items.length; i++)
        {
            if (items[i].getText().equals(tag))
            {
                found = true;
                break;
            }
        }
        
        return found;
    }

    /**
     * Handle the add tag button being pressed.
     */
    protected void handleAddTagButtonPressed()
    {
        TagDialog dialog = new TagDialog(getShell());
        
        dialog.create();
        dialog.getShell().setText(MasteringMessages.getString("MasteringPropertyPage.addTagEditorTitle"));
        dialog.setTag("");
        
        int status = dialog.open();
        if (status == Window.OK)
        {
	        String tag = dialog.getTag();
	        
	        if ((tag != null) && (! tagExists(tag)))
	        {
	    		TableItem item = new TableItem(m_tagTable,SWT.NONE);
	    		item.setText(tag);
	    		updateTagMetaData();
	        }
        }
    }

    /**
     * Handle the delete tag button being pressed.
     */
    protected void handleDeleteTagButtonPressed()
    {
        int foundItem = m_tagTable.getSelectionIndex();
        if (foundItem != -1)
        {
            m_tagTable.remove(foundItem);
            updateTagMetaData();
        } else
        {
            String message = MasteringMessages.getString("MasteringPropertyPage.selectTagDiscriminator");
            String title = MasteringMessages.getString("MasteringPropertyPage.deleteTagDiscriminatorTitle");
            String[] buttons = new String[1];
            buttons[0] = new String(MasteringMessages.getString("MasteringPropertyPage.Ok"));
            MessageDialog dialog = new MessageDialog(getShell(),
               title, null, message, MessageDialog.INFORMATION,
               buttons, 0);
            dialog.open();
        }
    }
    
    /**
     * Set the specified project.
     * 
     * @param project The <code>IProject</code> associated with the page.
     */
    public void setLocationPath(IPath locationPath)
    {
        m_locationPath = locationPath;
    }
    
    public void setProjectId(String id)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];
        project.setId(id);
    }
    
    public void setProjectVersion(String version)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];
        project.setVersion(version);
    }
    
    public void setDigitalWorkprint(String dwp)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];
        project.setDwpFile(dwp);
    }
    
    public void setTargetType(String type)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setType(type);
    }
    
    public void setTargetId(String id)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setId(id);
    }
    
    public void setTargetDigitalPlayprint(String dpp)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setDigitalPlayprint(dpp);
		
		m_genppscriptDppText.setText(dpp);
    }
    
    public void setVerbose(boolean verbose)
    {
    	m_verboseButton.setSelection(verbose);
    }
    
    public void setJavaCodeGeneration()
    {
        handleJavaCodeGenerationButtonPressed();
    }
    
    public void setCppCodeGeneration()
    {
    	handleCppCodeGenerationButtonPressed();
    }
    
    public void setBigEndian(boolean endian)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setBigEndian(endian);
		if (endian == true)
		{
			// Big Endian format.
			m_bigEndianButton.setSelection(true);
			m_littleEndianButton.setSelection(false);
		} else
		{
			// Little Endian format.
			m_bigEndianButton.setSelection(false);
			m_littleEndianButton.setSelection(true);
		}
    }
    
    public void setFixedPoint(boolean fixed)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setFixedPoint(fixed);
		m_gengroupFixedPointButton.setSelection(fixed);
		m_gensceneFixedPointButton.setSelection(fixed);
    }
    
    public void setActorID(String actorId)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setActorIdFile(actorId);
		m_gengroupActorIdText.setText(actorId);
    }
    
    public void setGroupID(String groupId)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setGroupIdFile(groupId);
		m_gengroupGroupIdText.setText(groupId);
    }
    
    public void setSceneID(String sceneId)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setSceneIdFile(sceneId);
		m_gensceneSceneIdText.setText(sceneId);
    }
    
    public void setBom(String bom)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setBomFile(bom);
		m_genmediaBomText.setText(bom);
    }
    
    public void setScript(String script)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setScriptFile(script);
		m_genppscriptScriptText.setText(script);
    }
    
    public void setTocName(String name)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];
		
		target.setTocName(name);
		m_genppscriptTocText.setText(name);
    }
    
    public void setScriptPath(String path)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setScriptPath(path);
		m_gendppScriptText.setText(path);
    }
    
    public void setDestinationDirectory(String dir)
    {
    	/*
       	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		target.setDestinationDirectory(dir);
		*/
		m_destinationNameText.setText(dir);
    }
    
    public void setSourceDirectory(String dir)
    {
    	/*
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

        target.setSourceDirectory(dir);
        */
        m_gendppSourceDirText.setText(dir);
    }
    
    public void setHeaderPackage(String value)
    {
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];
		
		target.setHeaderPackage(value);
		m_javaPackageText.setText(value);
    }
    
    public void setTags(String[] tags)
    {
    	m_tagTable.removeAll();
    	if ((tags != null) && (tags.length > 0))
	    {
		    for (int i = 0; i < tags.length; i++)
		    {
		        TableItem item = new TableItem(m_tagTable,SWT.NONE);
		        item.setText(tags[i]);
		    }
	    }
        updateTagMetaData();
    }
    
    public String[] getTags()
    {
    	TableItem[] tags = m_tagTable.getItems();
        String[] tagValues = new String[tags.length];
        for (int i = 0; i < tags.length; i++)
        {
            tagValues[i] = new String(tags[i].getText());
        }
        
        return tagValues;
    }
    
    public void clearTags()
    {
    	m_tagTable.clearAll();
    }
    
    private void updateTagMetaData()
    {
		// Reset meta-data.
    	Object[] projectData = m_metaData.getProjectData();
        MleTitleMetaData.ProjectDataElement project = (MleTitleMetaData.ProjectDataElement) projectData[0];

		Object[] targetData = project.getMasterTargets();
		MleTitleMetaData.MasterTargetElement target = (MleTitleMetaData.MasterTargetElement) targetData[0];

		ArrayList<String> tags = new ArrayList<String>();
		TableItem[] items = m_tagTable.getItems();
		for (int i = 0; i < items.length; i++)
			tags.add(new String(items[i].getText()));
		target.setTags(tags);
    }
    
    /**
     * Retrieve the title meta-data.
     * 
     * @return An instance of <code>MleTitleMetaData</code> is returned.
     */
    public MleTitleMetaData getMetaData()
    {
        return m_metaData;
    }

    /**
     * Send the specified event to all registered listeners.
     * 
     * @param event The meta-data event to send.
     */
    public void sendEvent(MleStudioMetaDataEvent event)
    {
        for (int i = 0; i < m_eventListeners.size(); i++)
        {
            IMleStudioMetaDataEventListener listener = (IMleStudioMetaDataEventListener) m_eventListeners.get(i);
            listener.handleMetaDataEvent(event);
        }
    }
    
    /**
     * Add a listener for meta-data events.
     * 
     * @param listener The event listener to add.
     */
    public void addMetaDataListener(IMleStudioMetaDataEventListener listener)
    {
        m_eventListeners.add(listener);
    }

    /**
     * Remove the specified meta-data listener.
     * 
     * @param listener The event listener to remove.
     */
    public void removeMetaDataListener(IMleStudioMetaDataEventListener listener)
    {
        m_eventListeners.remove(listener);
    }

    /**
     * Determine if the page contains valid entries.
     * 
     * @return <b>true</b> is returned if the page is valid. Otherwise, <b>false</b>
     * will be returned.
     */
    protected boolean validatePage()
    {
        if (! validateCommon())
        {
            setValid(false);
            return false;
        }
        
        if (! validateGengroup())
        {
            setValid(false);
            return false;
        }
        
        if (! validateGenscene())
        {
            setValid(false);
            return false;
        }
        
        if (! validateGenmedia())
        {
            setValid(false);
            return false;
        }
        
        if (! validateGenppscript())
        {
            setValid(false);
            return false;
        }
        
        if (! validateGendpp())
        {
            setValid(false);
            return false;
        }

        setValid(true);
        return true;
    }

    /**
     * Determine whether this page's common specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the destination widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateCommon()
    {
        boolean retValue = true;
        
        if (m_destinationNameText != null)
        {
	        if ((m_destinationNameText.getText() != null) &&
	            (! m_destinationNameText.getText().equals("")))
	        {
	            setErrorMessage(null);
	        } else
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.destinationEmpty"));
	            retValue = false;
	        }
        }
        
        if ((m_javaCodegenButton != null) && (m_javaPackageText != null))
        {
	        if (m_javaCodegenButton.getSelection())
	        {
	            if ((m_javaPackageText.getText() != null) &&
	                (! m_javaPackageText.getText().equals("")))
	            {
	                setErrorMessage(null);
	            } else
	            {
	                setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.packageEmpty"));
	                retValue = false;
	            }
	        }
        }
        
        return retValue;
    }

    /**
     * Determine whether this page's gengroup specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the gengroup widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateGengroup()
    {
        boolean retValue = true;
        
        if (m_gengroupActorIdText != null)
        {
	        if ((m_gengroupActorIdText.getText() == null) ||  m_gengroupActorIdText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.actoridEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }
        
        if (m_gengroupGroupIdText != null)
        {
	        if ((m_gengroupGroupIdText.getText() == null) || m_gengroupGroupIdText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.groupidEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }

        return retValue;
    }

    /**
     * Determine whether this page's genscene specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the genscene widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateGenscene()
    {
        boolean retValue = true;
        
        if (m_gensceneSceneIdText != null)
        {
	        if ((m_gensceneSceneIdText.getText() == null) ||  m_gensceneSceneIdText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.sceneidEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }

        return retValue;
    }

    /**
     * Determine whether this page's genmedia specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the genmedia widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateGenmedia()
    {
        boolean retValue = true;
        
        if (m_genmediaBomText != null)
        {
	        if ((m_genmediaBomText.getText() == null) ||  m_genmediaBomText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.bomEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }

        return retValue;
    }

    /**
     * Determine whether this page's genppscript specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the genppscript widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateGenppscript()
    {
        boolean retValue = true;
        
        if (m_genppscriptDppText != null)
        {
	        if ((m_genppscriptDppText.getText() == null) ||  m_genppscriptDppText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.dppEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }
        
        if (m_genppscriptScriptText != null)
        {
	        if ((m_genppscriptScriptText.getText() == null) ||  m_genppscriptScriptText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.scriptEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }
        
        if (m_genppscriptTocText != null)
        {
	        if ((m_genppscriptTocText.getText() == null) ||  m_genppscriptTocText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.tocEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }

        return retValue;
    }

    /**
     * Determine whether this page's gendpp specification
     * controls currently all contain valid values.
     * 
     * @return <b>true</b> is returned if the gendpp widgets
     * contain valid input. Otherwise <b>false</b> will be returned.
     */
    protected boolean validateGendpp()
    {
        boolean retValue = true;
        
        if (m_gendppSourceDirText != null)
        {
	        if ((m_gendppSourceDirText.getText() == null) ||  m_gendppSourceDirText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.sourceEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }
        
        if (m_gendppScriptText != null)
        {
	        if ((m_gendppScriptText.getText() == null) ||  m_gendppScriptText.getText().equals(""))
	        {
	            setErrorMessage(MasteringMessages.getString("MasteringPropertyPage.scriptEmpty"));
	            setValid(false);
	            return false;
	        } else
	        {
	            setErrorMessage(null);
	        }
        }

        return retValue;
    }

    private Shell getShell()
    {
		Shell shell = null;
		shell = PlatformUI.getWorkbench().getDisplay().getActiveShell();
		return shell;
	}
    
    private void setErrorMessage(String msg)
    {
    	m_errorMsg = msg;
    }

    private void setValid(boolean value)
    {
    	MleStudioMetaDataEvent event = new MleStudioMetaDataEvent(this);
    	event.m_msg = m_errorMsg;
    	if (value)
    	    event.m_state = MleStudioMetaDataEvent.OK;
    	else
    		event.m_state = MleStudioMetaDataEvent.ERROR;
    	sendEvent(event);
    }
}
