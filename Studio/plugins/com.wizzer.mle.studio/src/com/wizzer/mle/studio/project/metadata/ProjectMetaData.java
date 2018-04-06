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
import java.io.Serializable;
import java.util.Calendar;
import java.util.List;

/**
 * This class is a container for Magic Lantern project meta-data.
 * 
 * @author Mark S. Millard
 */
public abstract class ProjectMetaData implements Serializable, Cloneable
{
    // Serialization identifier.
	private static final long serialVersionUID = -2314678386404065310L;
	
	/** The major version value for the format of the project meta-data. */
    public static final int MAJOR_VERSION = 1;
    /** The minor version value for the format of the project meta-data. */
    public static final int MINOR_VERSION = 0;

    /** The major version value. */
    protected int m_majorVersion = MAJOR_VERSION;
    /** The minor version value. */
    protected int m_minorVersion = MINOR_VERSION;
    /** An optional creation date. */
    protected Calendar m_creationDate;

    /** The type of Magic Lantern project (i.e. MLE_JAVA2D_STUDIO, MLE_BRENDER_STUDIO). */
    protected String m_projectType;
    /** The version of the type of Magic Lantern project (i.e. 1.0). */
    protected String m_projectTypeVersion;    

    /**
     * Get the major version value for the MLE project.
     * 
     * @return An integer is returned.
     */
    public int getMajorVersion()
    {
        return m_majorVersion;
    }

    /**
     * Get the minor version value for the VWB project.
     * 
     * @return An integer is returned.
     */
    public int getMinorVersion()
    {
        return m_minorVersion;
    }

    /**
     * Get the creation date for the MLE project.
     * 
     * @return A <code>Calendar</code> is returned. <b>null</b> will be returned
     * if the creation date has not been initialized.
     */
    public Calendar getCreationDate()
    {
        return m_creationDate;
    }

    /**
     * Set the creation date for the MLE project.
     * 
     * @param date A <code>Calendar</code> is expected.
     */
    public void setCreationDate(Calendar date)
    {
        m_creationDate = date;
    }

    /**
     * Get the project type as an arbitrary <code>String</code>.
     * 
     * @return The project type is returned.
     */
    public abstract String getProjectType();

    /**
     * Get the version of the project type as an arbitrary <code>String</code>.
     * 
     * @return The version is returned.
     */
    public abstract String getProjectTypeVersion();

    /**
     * Get the project meta-data.
     * 
     * @return An array of <code>Object</code> is returned containing the project's
     * meta-data.
     */
    public abstract Object[] getProjectData();

    /**
     * Set the project meta-data;
     * 
     * @param data The project meta-data to set.
     */
    public abstract void setProjectData(Object[] data);

}
