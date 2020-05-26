// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.ext;

// Import Eclipse classes.
import org.eclipse.swt.widgets.Composite;

/**
 * This class is an adapter for creating an <code>OleControlSiteEx</code>.
 * 
 * @author Mark Millard
 */
public class OleControlSiteAdapter implements IControlSiteAdapter
{
	// The control site identifier.
	private String m_id = null;
	// The name of the control site.
	private String m_name = null;
	// The version of the control site.
	private String m_version = null;
	
	@Override
	public IControlSite createControlSite(Composite parent, int style, String progId)
	{
		OleControlSiteEx controlSite = new OleControlSiteEx(parent, style, progId);
		return controlSite;
	}

	@Override
	public void setId(String id)
	{
		m_id = id;
	}

	@Override
	public String getId()
	{
		return m_id;
	}

	@Override
	public void setName(String name)
	{
		m_name = name;
	}

	@Override
	public String getName()
	{
		return m_name;
	}

	@Override
	public void setVersion(String version)
	{
		m_version = version;
	}

	@Override
	public String getVersion()
	{
		return m_version;
	}

}
