// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
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
package com.wizzer.mle.studio.framework;

// Import standard Java classes.
import java.io.File;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Class to encapsulate the filtration for 'find'-like behavior.
 * 
 * @author Mark S. Millard
 */
public class FindFilter implements FilenameFilter
{
    private boolean m_sizeSet;
    private int m_size;
    private String m_name;
    private Pattern m_nameRE;

    /**
     * The default constructor.
     */
    public FindFilter()
    {
        super();
    }
    
    /**
     * Set the filter for size.
     * 
     * @param sizeFilter A regular expression.
     */
    public void setSizeFilter(String sizeFilter)
    {
        m_size = Integer.parseInt(sizeFilter);
        m_sizeSet = true;
    }
    
    /**
     * Convert the given shell wildcard pattern into internal form
     * (a regex).
     * 
     * @param nameFilter A regular expression.
     */
    public void setNameFilter(String nameFilter)
    {
        m_name = nameFilter;
        StringBuffer sb = new StringBuffer('^');
        for (int i = 0; i < nameFilter.length(); i++)
        {
            char c = nameFilter.charAt(i);
            switch(c)
            {
                case '.': sb.append("\\."); break;
                case '*': sb.append(".*"); break;
                case '?': sb.append('.'); break;
                default:  sb.append(c); break;
            }
        }
        sb.append('$');
        try
        {
            m_nameRE = Pattern.compile(sb.toString());
        } catch (PatternSyntaxException ex)
        {
            FrameworkLog.logError(ex,"Regular Expression: " + sb.toString() +
                " didn't compile.");
        }
    }

    /**
     * Do the filtering. For now, only filter on name.
     * 
     * @see java.io.FileFilter#accept(java.io.File)
     */
    public boolean accept(File dir, String fileName)
    {
        File f = new File(dir,fileName);
        
        if (f.isDirectory())
        {
            // Allow recursion;
            return true;
        }
        
        if (m_nameRE != null)
            return m_nameRE.matcher(fileName).matches();
        
        // XXX - implement size handling.

        // Catch-all.
        return false;
    }

}
