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
package com.wizzer.mle.studio.java2d.titles;

// Import standard Java classes.
import java.util.Vector;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.runtimes.IApplicationTemplate;
import com.wizzer.mle.studio.runtimes.IApplicationTemplateEntry;

/**
 * This class implements a template for the ImageViewer Magic Lantern Title.
 * 
 * @author Mark Millard
 */
public class ImageViewerTemplate implements IApplicationTemplate
{
	// The name of the template.
	private String m_name = null;
	// The version of the template.
	private String m_version = null;
	// The template category.
	private String m_category = null;
	// A registry of template implementations.
	private Vector<IApplicationTemplateEntry> m_registry =
		new Vector<IApplicationTemplateEntry>();

	@Override
	public String getName()
	{
		return m_name;
	}

	@Override
	public String getVersion()
	{
		return m_version;
	}

	@Override
	public String getCategory()
	{
		return m_category;
	}

	@Override
	public void setName(String name)
	{
		m_name = name;
	}

	@Override
	public void setVersion(String version)
	{
		m_version = version;
	}

	@Override
	public void setCategory(String category)
	{
		m_category = category;
	}

	@Override
	public IApplicationTemplateEntry[] getTemplateEntries()
	{
		IApplicationTemplateEntry[] entries = new IApplicationTemplateEntry[m_registry.size()];
		for (int i = 0; i < m_registry.size(); i++)
			entries[i] = m_registry.elementAt(i);
		return entries;
	}


	@Override
	public void addTemplateEntry(IApplicationTemplateEntry entry)
	{
		m_registry.add(entry);
	}

	@Override
	public boolean removeTemplateEntry(IApplicationTemplateEntry entry)
	{
		return m_registry.remove(entry);
	}

}
