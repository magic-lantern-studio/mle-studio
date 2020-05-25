// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.project;

// Import standard Java packages.
import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

// Import OCAP Plug-in packages.
import com.wizzer.mle.studio.MleLog;

/**
 * This class is used to manage messages as a resource bundle for the import
 * Magic Lantern Studio Project wizard.
 * 
 * @author Mark S. Millard
 */
public class MleImportProjectMessages
{
	// The name of the resource bundle.
	private static final String RESOURCE_BUNDLE = MleImportProjectMessages.class.getName();
	// The resource bundle.
	private static ResourceBundle g_resourceBundle = ResourceBundle.getBundle(RESOURCE_BUNDLE);

	// Hide the default constructor.
	private MleImportProjectMessages()
	{
		// Do nothing for now.
	}

	/**
	 * Get the value of the specified key from the message resource bundle.
	 * 
	 * @param key The key identifying which resource to retrieve.
	 * 
	 * @return The value of the resource is returned as a <code>String</code>.
	 * If the resource is missing, a bogus string is returned identifying
	 * which key is the offender.
	 */
	public static String getString(String key)
	{
		try {
			return g_resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			MleLog.logWarning("Missing Resource: " + key);
			return '!' + key + '!';
		}
	}
	
	/**
	 * Gets a string from the resource bundle and formats it with the argument.
	 * 
	 * @param key The string used to get the bundle value, must not be <b>null</b>.
	 */
	public static String getFormattedString(String key, Object arg)
	{
		return MessageFormat.format(getString(key), new Object[] { arg });
	}


	/**
	 * Gets a string from the resource bundle and formats it with arguments.
	 */	
	public static String getFormattedString(String key, Object[] args)
	{
		return MessageFormat.format(getString(key), args);
	}

}
