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
package com.wizzer.mle.studio.framework;

import java.io.File;
import java.util.Vector;

/**
 * This class may used to find files by name, size, or other criteria.
 * 
 * @author Mark S. Millard
 */
public class Find
{
    /** The find filter. */
    protected FindFilter m_filter = null;
    /** The registry of callbacks. */
    protected Vector m_registry = new Vector();

    // Hide the default constructor.
    private Find() {}
    
    /**
     * Constructs an instance of <code>Find</code> with the specified filter.
     * 
     * @param filter The filter to use for finding files.
     */
    public Find(FindFilter filter)
    {
        super();
        m_filter = filter;
    }
    
    /**
     * Handle one file system object by name.
     * 
     * @param s The name of the file system object.
     */
    public void doName(String s)
    {
        File f = new File(s);
        if (! f.exists())
        {
            //System.out.println(s + " does not exist.");
            return;
        }
        
        if (f.isFile())
        {
            doFile(f);
        } else if (f.isDirectory())
        {
            String objects[] = f.list(m_filter);
            for (int i = 0; i < objects.length; i++)
                doName(s + File.separator + objects[i]);
        } else
        {
            //System.err.println("Unknown type: " + s);
        }
            
    }
    
    /*
     * Process one regular file.
     * 
     * @param f The file to process.
     */
    private void doFile(File f)
    {
        //System.out.println("f " + f.getPath());
        FindEvent event = new FindEvent(this);
        event.m_file = f;
        sendEvent(event);
    }
    
    /**
     * Add a listener to the registry of objects that will respond
     * to found files.
     * 
     * @param listener The find listener to add.
     */
    public void addListener(IFindListener listener)
    {
        m_registry.add(listener);
    }

    /**
     * Remove the specified listener from the registry.
     * 
     * @param listener The find listener to remove.
     */
    public void removeListener(IFindListener listener)
    {
        m_registry.remove(listener);
    }

    /**
     * Send the specified event to the list of registered listeners.
     * 
     * @param event The event to process.
     */
    public void sendEvent(FindEvent event)
    {
        for (int i = 0; i < m_registry.size(); i++)
        {
            IFindListener listener = (IFindListener)m_registry.get(i);
            listener.handleEvent(event);
        }
    }

}
