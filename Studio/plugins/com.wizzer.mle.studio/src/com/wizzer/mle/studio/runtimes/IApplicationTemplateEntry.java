// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.runtimes;

/**
 * This interface is used to specify a specific application template implementation.
 * 
 * @author Mark Millard
 */
public interface IApplicationTemplateEntry
{
	/**
	 * Get an optional identifier for the application template.
	 * 
	 * @return The identifier is returned as a <code>String</code>.
	 */
	public String getId();

	/**
	 * Get the type of the template package.
	 * <p>
	 * Package types that are currently supported include <b>zip</b> files.
	 * </p>
	 * 
	 * @return The type of the application template is returned.
	 */
	public String getType();
	
	/**
	 * Get the source package for the application template.
	 * <p>
	 * This can be a relative path or a URL. It will be up to the actual template
	 * to determine how to resolve the location.
	 * </p>
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getSource();
	
	/**
	 * Get the destination directory for the generated application source.
	 * <p>
	 * The directory should be a relative path from the top of the OCAP Project
	 * that is using the template (i.e. src).
	 * </p>
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getDestination();
}
