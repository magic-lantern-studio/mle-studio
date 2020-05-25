// COPYRIGHT_BEGIN
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.ext;

// Import standard Java classes.
import org.eclipse.swt.graphics.Point;

public interface IControlSite
{
	/**
	 * Get the extent of the embedded OLE native object.
	 * 
	 * @return a <code>Point</code> is returned containing the x and y dimensions of the
	 * embedded OLE native object.
	 */
	public Point getExtent();
}
