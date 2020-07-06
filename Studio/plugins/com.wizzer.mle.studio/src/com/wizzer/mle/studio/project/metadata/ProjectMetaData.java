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
package com.wizzer.mle.studio.project.metadata;

// Import standard Java classes.
import java.io.Serializable;
import java.util.Calendar;

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
