// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.java2d.titles;

// Import standard Java classes.
import java.util.Vector;

// Import Magic Lantern Studio classes.
import com.wizzer.mle.studio.runtimes.IApplicationTemplate;
import com.wizzer.mle.studio.runtimes.IApplicationTemplateEntry;

/**
 * This class implements a template for the HelloWorld Magic Lantern Title.
 * 
 * @author Mark Millard
 */
public class HelloWorldTemplate implements IApplicationTemplate
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
