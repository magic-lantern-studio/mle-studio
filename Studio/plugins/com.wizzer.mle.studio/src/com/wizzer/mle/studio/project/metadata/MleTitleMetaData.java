// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2010  Wizzer Works
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
package com.wizzer.mle.studio.project.metadata;

// Import standard Java classes.
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

/**
 * This class implements the meta-data for a title in
 * Magic Lantern.
 * 
 * @author Mark S. Millard
 */
public class MleTitleMetaData extends ProjectMetaData
{
    /** A string identifying the type of MLE project meta-data. */
    public static final String PROJECT_TYPE = "MLE_STUDIO";
    /** A string identifying the version of the MLE project meta-data. */
    public static final String PROJECT_TYPE_VERSION = "1.0";

	// The serialization unique identifier.
	private static final long serialVersionUID = 1711605290729204517L;

    // The collection of meta-data elements.
    private Vector m_data = new Vector();

    /**
     * Utility class for <code>Title.MasterTarget</code> elements.
     * 
     * @author Mark Millard
     */
    public class MasterTargetElement
    {
    	// The target type  (i.e. MLE_JAVA2D).
    	private String m_type;
    	// The target identifier.
    	private String m_id;
    	// The target Digital Playprint.
    	private String m_dpp;
    	private boolean m_verbose = false;
    	private boolean m_bigEndian = true;
    	private boolean m_fixedPoint = false;
    	private boolean m_hasGengroup = true;
    	private boolean m_hasGenmedia = true;
    	private boolean m_hasGentables = true;
    	private boolean m_hasGenscene = true;
    	private boolean m_hasGenppscript = true;
    	private boolean m_hasGendpp = true;
    	private String m_destinationDirectory = null;
    	private String m_codeGeneration = null;
    	private String m_actorIdFile = null;
    	private String m_groupIdFile = null;
    	private String m_sceneIdFile = null;
    	private String m_bomFile = null;
    	private String m_scriptFile = null;
    	private String m_tocName = null;
    	private String m_sourceDirectory = null;
    	private String m_scriptPath = null;
    	private String m_headerPackage = null;
    	private ArrayList<String> m_tags = new ArrayList<String>();
    	
    	public String getType()
    	{
    		return m_type;
    	}
    	
    	public void setType(String type)
    	{
    		m_type = type;
    	}
    	
    	public String getId()
    	{
    		return m_id;
    	}
    	
    	public void setId(String id)
    	{
    		m_id = id;
    	}
    	
    	public String getDigitalPlayprint()
    	{
    		return m_dpp;
    	}
    	
    	public void setDigitalPlayprint(String dpp)
    	{
    		m_dpp = dpp;
    	}
    	
    	public void setVerbose(boolean verbose)
    	{
    		m_verbose = verbose;
    	}
    	
    	public boolean isVerbose()
    	{
    		return m_verbose;
    	}
    	
    	public void setBigEndian(boolean endian)
    	{
    		m_bigEndian = endian;
    	}
    	
    	public boolean isBigEndian()
    	{
    		return m_bigEndian;
    	}
    	
    	public void setFixedPoint(boolean fixedPoint)
    	{
    		m_fixedPoint = fixedPoint;
    	}
    	
    	public boolean isFixedPoint()
    	{
    		return m_fixedPoint;
    	}
    	
    	public void setDestinationDirectory(String dir)
    	{
    		m_destinationDirectory = dir;
    	}

    	public String getDestinationDirectroy()
    	{
    		return m_destinationDirectory;
    	}
    	
    	public void setCodeGeneration(String value)
    	{
    		m_codeGeneration = value;
    	}
    	
    	public String getCodeGeneration()
    	{
    		return m_codeGeneration;
    	}
    	
    	public void setActorIdFile(String actorIdFile)
    	{
    		m_actorIdFile = actorIdFile;
    	}
    	
    	public String getActorIdFile()
    	{
    		return m_actorIdFile;
    	}
    	
    	public void setGroupIdFile(String groupIdFile)
    	{
    		m_groupIdFile = groupIdFile;
    	}
    	
    	public String getGroupIdFile()
    	{
    		return m_groupIdFile;
    	}
    	
    	public void setSceneIdFile(String sceneIdFile)
    	{
    		m_sceneIdFile = sceneIdFile;
    	}
    	
    	public String getSceneIdFile()
    	{
    		return m_sceneIdFile;
    	}
    	
    	public void setBomFile(String bomFile)
    	{
    		m_bomFile = bomFile;
    	}
    	
    	public String getBomFile()
    	{
    		return m_bomFile;
    	}
    	
    	public void setScriptFile(String script)
    	{
    		m_scriptFile = script;
    	}
    	
    	public String getScriptFile()
    	{
    		return m_scriptFile;
    	}
    	
    	public void setTocName(String name)
    	{
    		m_tocName = name;
    	}
    	
    	public String getTocName()
    	{
    		return m_tocName;
    	}
    	
    	public void setSourceDirectory(String dir)
    	{
    		m_sourceDirectory = dir;
    	}
    	
    	public String getSourceDirectory()
    	{
    		return m_sourceDirectory;
    	}
    	
    	public void setScriptPath(String path)
    	{
    		m_scriptPath = path;
    	}
    	
    	public String getScriptPath()
    	{
    		return m_scriptPath;
    	}
    	
    	public void setHeaderPackage(String javaPackage)
    	{
    		m_headerPackage = javaPackage;
    	}
    	
    	public String getHeaderPackage()
    	{
    		return m_headerPackage;
    	}
    	
    	public void setGengroup(boolean value)
    	{
    		m_hasGengroup = value;
    	}
    	
    	public boolean hasGengroup()
    	{
    		return m_hasGengroup;
    	}
    	
       	public void setGenmedia(boolean value)
    	{
    		m_hasGenmedia = value;
    	}
    	
    	public boolean hasGenmedia()
    	{
    		return m_hasGenmedia;
    	}

       	public void setGenscene(boolean value)
    	{
    		m_hasGenscene = value;
    	}
    	
    	public boolean hasGenscene()
    	{
    		return m_hasGenscene;
    	}

       	public void setGentables(boolean value)
    	{
    		m_hasGentables = value;
    	}
    	
    	public boolean hasGentables()
    	{
    		return m_hasGentables;
    	}

       	public void setGenppscript(boolean value)
    	{
    		m_hasGenppscript = value;
    	}
    	
    	public boolean hasGenppscript()
    	{
    		return m_hasGenppscript;
    	}

       	public void setGendpp(boolean value)
    	{
    		m_hasGendpp = value;
    	}
    	
    	public boolean hasGendpp()
    	{
    		return m_hasGendpp;
    	}
    	
    	public void setTags(List<String> tags)
    	{
    		m_tags.clear();
    		for (int i = 0; i < tags.size(); i++)
    			m_tags.add(new String(tags.get(i)));
    	}
    	
    	public ArrayList<String> getTags()
    	{
    		return m_tags;
    	}

    	/**
         * @see java.lang.Object#equals(java.lang.Object)
         */
    	public boolean equals(Object obj)
        {
            boolean equal = false;
            
            if (obj instanceof MasterTargetElement)
            {
            	MasterTargetElement element = (MasterTargetElement) obj;
            	
            	if (m_type.equals(element.m_type) &&
            		m_id.equals(element.m_id) &&
            	    m_dpp.equals(element.m_dpp) &&
            	    (m_verbose == element.m_verbose) &&
            	    (m_bigEndian == element.m_bigEndian) &&
            	    (m_fixedPoint == element.m_fixedPoint) &&
            	    (m_hasGengroup == element.m_hasGengroup) &&
            	    (m_hasGenmedia == element.m_hasGenmedia) &&
            	    (m_hasGenscene == element.m_hasGenscene) &&
            	    (m_hasGenppscript == element.m_hasGenscene) &&
            	    (m_hasGendpp == element.m_hasGendpp) &&
            	    m_codeGeneration.equals(element.m_codeGeneration) &&
            	    m_destinationDirectory.equals(element.m_destinationDirectory) &&
            	    m_headerPackage.equals(element.m_headerPackage) &&
            	    m_actorIdFile.equals(element.m_actorIdFile) &&
            	    m_groupIdFile.equals(element.m_actorIdFile) &&
            	    m_sceneIdFile.equals(element.m_sceneIdFile) &&
            	    m_bomFile.equals(element.m_bomFile) &&
            	    m_scriptFile.equals(element.m_scriptFile) &&
            	    m_tocName.equals(element.m_tocName) &&
            	    m_sourceDirectory.equals(element.m_sourceDirectory) &&
            	    m_scriptPath.equals(m_scriptPath) &&
            	    m_tags.equals(element.m_tags))
            	   equal = true;
            }
            
            return equal;
        }
    }

    /**
     * Utility class for <code>Title</code> elements.
     * 
     * @author Mark Millard
     */
    public class ProjectDataElement
    {
        public static final String VERSION = "1.0";

        // The title identifier.
        private String m_id = null;
        // The DWP file this title is associated with.
        private String m_dwpFile = null;
        // The version of this title data.
        private String m_version = VERSION;
        
        // The collection of mastering targets.
        Vector m_targets = new Vector();

        /**
         * The default constructor.
         */
        public ProjectDataElement()
        {}

        public void setId(String value)
        {
            m_id = value;
        }

        public String getId()
        {
            return m_id;
        }

        public void setDwpFile(String value)
        {
            m_dwpFile = value;
        }

        public String getDwpFile()
        {
            return m_dwpFile;
        }

        public void setVersion(String value)
        {
            m_version = value;
        }

        public String getVersion()
        {
            return m_version;
        }
        
        public Object[] getMasterTargets()
        {
        	return m_targets.toArray();
        }
        
        public void setMasterTargets(MasterTargetElement[] targets)
        {
        	for (int i = 0; i < targets.length; i++)
        	{
        		m_targets.add(targets[i]);
        	}
        }

        /**
         * @see java.lang.Object#equals(java.lang.Object)
         */
        public boolean equals(Object obj)
        {
            boolean equal = false;

            if (obj instanceof ProjectDataElement)
            {
                ProjectDataElement element = (ProjectDataElement) obj;

                if ((m_id.equals(element.m_id)) &&
                	(m_dwpFile.equals(element.m_dwpFile)) &&
                    (m_version.equals(element.m_version)))
                	equal = true;
                
                if (! m_targets.isEmpty())
                {
                	for (int i = 0; i < m_targets.size(); i++)
                	{
                		MasterTargetElement thisTarget = (MasterTargetElement) m_targets.get(i);
                		MasterTargetElement thatTarget = (MasterTargetElement) element.m_targets.get(i);
                		if (! thisTarget.equals(thatTarget))
                		{
                			equal = false;
                			break;
                		}
                	}
                }
            }

            return equal;
        }
    }

	@Override
	public Object[] getProjectData()
	{
		return m_data.toArray();
	}

	@Override
	public String getProjectType()
	{
		return PROJECT_TYPE;
	}

	@Override
	public String getProjectTypeVersion()
	{
		return PROJECT_TYPE_VERSION;
	}

	@Override
	public void setProjectData(Object[] data)
	{
        for (int i = 0; i < data.length; i++)
        {
            m_data.add(data[i]);
        }
	}

    // Compare the specified calendars using a restricted format.
    private boolean compareCalendarFormat(Calendar cal1, Calendar cal2)
    {
        boolean result = false;

        DateFormat df = DateFormat.getDateTimeInstance();
        String cal1Format = df.format(cal1.getTime());
        String cal2Format = df.format(cal2.getTime());

        //System.out.println("This date: " + cal1Format);
        //System.out.println("Other date: " + cal2Format);

        result = cal1Format.equals(cal2Format);

        return result;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        boolean equal = false;

        if (obj instanceof MleTitleMetaData)
        {
        	MleTitleMetaData metadata = (MleTitleMetaData) obj;

            if ((this.m_majorVersion == metadata.m_majorVersion) &&
                (this.m_minorVersion == metadata.m_minorVersion) &&
                (this.m_creationDate == null ? metadata.m_creationDate == null : compareCalendarFormat(
                     this.m_creationDate, metadata.m_creationDate)))
            {
                if ((this.m_projectType.equals(metadata.m_projectType)) &&
                    (this.m_projectTypeVersion == null ? metadata.m_projectTypeVersion == null
                         : this.m_projectTypeVersion.equals(metadata.m_projectTypeVersion)))
                {
                    Object[] thisTitleData = getProjectData();
                    Object[] thatTitleData = metadata.getProjectData();
                    if (thisTitleData.length == thatTitleData.length)
                    {
                        for (int i = 0; i < thisTitleData.length; i++)
                        {
                            if (! thisTitleData[i].equals(thatTitleData[i]))
                            {
                                equal = false;
                                break;
                            }
                            equal = true;
                        }
                    }
                }
            }
        }

        return equal;
    }
}
