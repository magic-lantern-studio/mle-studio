// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.ext;

// Import Eclipse classes.
import org.eclipse.swt.widgets.Composite;

/**
 * This interface is used to create a control site (i.e. OleControlSiteEx)
 * via the <code>ControlSiteFactory</code>. It is necessary to take this round-about
 * way of creating an OleControlSiteEx class because the implementation of the
 * class is different for 32-bit and 64-bit Windows.
 * 
 * @author Mark S. Millard
 */
public interface IControlSiteAdapter
{
	/** Identifier for a native Windows OLE Control Site. */
	public static final String OLE_CONTROL_SITE = "OleControlSiteEx";

	/**
	 * Create the control site associated with this adapter.
	 * 
	 * @param parent A composite widget; must be an OleFrame.
	 * @param style The bitwise OR'ing of widget styles.
	 * @param progId The unique program identifier of an OLE Document application;
	 * the value of the ProgID key or the value of the VersionIndependentProgID key
	 * specified in the registry for the desired OLE Document (for example,
	 * the VersionIndependentProgID for Word is Word.Document) program identifier.
	 * 
	 * @return A reference to the newly created control site is returned.
	 */
	public IControlSite createControlSite(Composite parent, int style, String progId);
	
	/**
	 * Set the control site identifier.
	 * 
	 * @param id The identifier to set.
	 */
	public void setId(String id);
	
	/**
	 * Get the control site identifier.
	 * 
	 * @return The control site identifier is returned.
	 */
	public String getId();
	
	/**
	 * Set the name of the control site.
	 * 
	 * @param name The name to set.
	 */
	public void setName(String name);
	
	/**
	 * Get the name of the control site.
	 * 
	 * @return The name of the control site is returned. The return value may be
	 * <b>null</b> since this field is optional.
	 */
	public String getName();
	
	/**
	 * Set the version of the control site.
	 * 
	 * @param version The version of the control site.
	 */
	public void setVersion(String version);
	
	/**
	 * Get the version of the control site.
	 * 
	 * @return The version of the control site is returned. The return value may be
	 * <b>null</b> since this field is optional.
	 */
	public String getVersion();
}
