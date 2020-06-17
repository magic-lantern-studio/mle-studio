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
package com.wizzer.mle.studio.runtimes;

/**
 * This interface is used to manage a Magic Lantern Studio Project application template.
 * 
 * @author Mark S. Millard
 */
public interface IApplicationTemplate
{
	/**
	 * Get the name of the application template.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getName();
	
	/**
	 * Set the name of the application template.
	 * 
	 * @param name The name of the application template.
	 */
	public void setName(String name);
	
	/**
	 * Get the version of the application template.
	 * 
	 * @return The version is returned as a <code>String</code>.
	 */
	public String getVersion();
	
	/**
	 * Set the version of the application template.
	 * 
	 * @param version The version of the application template.
	 */
	public void setVersion(String version);
	
	/**
	 * Get the category for the application template.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getCategory();
	
	/**
	 * Set the application template category.
	 * 
	 * @param category The application template category.
	 */
	public void setCategory(String category);
	
	/**
	 * Retrieve the collection of entries that are associated with this template.
	 * <p>
	 * A template can have one or more entries which implement the template.
	 * Each entry may be different in the way the generated source is constructed.
	 * </p>
	 * 
	 * @return An array of <code>IApplicationTemplateEntry</code> items is
	 * returned.
	 */
    public IApplicationTemplateEntry[] getTemplateEntries();
    
    /**
     * Add an application template entry.
     * 
     * @param entry The application template entry to add.
     */
    public void addTemplateEntry(IApplicationTemplateEntry entry);
    
    /**
     * Remove an application template entry.
     * 
     * @param entry The application template entry to remove.
     */
    public boolean removeTemplateEntry(IApplicationTemplateEntry entry);
}
