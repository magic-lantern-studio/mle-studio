// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.runtimes;

/**
 * This class is used carry the information required for processing an
 * Application Template.
 * 
 * @author Mark Millard
 */
public class ApplicationTemplate implements IApplicationTemplateEntry
{
	// The template type.
    private String m_type = null;
    // The source location for the template package.
    private String m_source = null;
    // The destination directory for the generated source.
    private String m_destination = null;
    // The application template identifier.
    private String m_id = null;
    
	public String getDestination()
	{
		return m_destination;
	}
	
	public void setDestination(String destination)
	{
		m_destination = destination;
	}

	public String getSource()
	{
		return m_source;
	}
	
	public void setSource(String source)
	{
		m_source = source;
	}

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
}
