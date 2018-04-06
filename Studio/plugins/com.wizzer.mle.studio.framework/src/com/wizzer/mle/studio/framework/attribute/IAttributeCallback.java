// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2011  Wizzer Works
//
//  Wizzer Works makes available all content in this file ("Content").
//  Unless otherwise indicated below, the Content is provided to you
//  under the terms and conditions of the Common Public License Version 1.0
//  ("CPL"). A copy of the CPL is available at
//
//      http://opensource.org/licenses/cpl1.0.php
//
//  For purposes of the CPL, "Program" will mean the Content.
//
//  For information concerning this Makefile, contact Mark S. Millard,
//  of Wizzer Works at msm@wizzerworks.com.
//
//  More information concerning Wizzer Works may be found at
//
//      http://www.wizzerworks.com
//
// COPYRIGHT_END

// Declare package.
package com.wizzer.mle.studio.framework.attribute;


/**
 * This interface specifies a callback that can be used to apply a method
 * to each node in an Attribute tree.
 * 
 * @author Mark S. Millard
 */
public interface IAttributeCallback
{
    /**
     * Invoke the callback method.
     * 
     * @param node An <code>IAttribute</code> in the Attribute tree which the callback
     * is being associated with.
     * @param data Anciliary data that can be used by the callback method.
     * 
     * @return If the callback is successful, <b>true</b> will be returned.
     * Otherwise, <b>false</b> will be returned.
     */
    public boolean callback(IAttribute node, Object data);
    
    /**
     * Set the anciliary data.
     * 
     * @param data Additional data that is associated with the callback.
     * This may be set to <b>null</b>.
     */
    public void setData(Object data);
    
	/**
	 * Get the anciliary data.
	 * 
	 * @return Additional data that is associated with the callback
	 * is returned. This may be <b>null</b>.
	 */
    public Object getData();
  
	/**
	 * Invoke the setup method.
	 * <p>
	 * This method is called prior to actually invoking the callback.
	 * It provides a hook for iterating over an <code>Attribute</code>
	 * tree recursively and setting up a state that the callback is expecting.
	 * </p>
	 * 
	 * @param node An <code>IAttribute</code> in the Attribute tree which the callback
	 * is being associated with.
	 * @param data Anciliary data that can be used by the callback method.
	 * 
	 * @return If the setup is successful, <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
    public boolean prefix(IAttribute node, Object data);

	/**
	 * Invoke the teardown method.
	 * <p>
	 * This method is called after invoking the callback.
	 * It provides a hook for iterating over an <code>Attribute</code>
	 * tree recursively and tearing down the state that was established by the
	 * <code>prefix</code> method.
	 * </p>
	 * 
	 * @param node An <code>IAttribute</code> in the Attribute tree which the callback
	 * is being associated with.
	 * @param data Anciliary data that can be used by the callback method.
	 * 
	 * @return If the teardown is successful, <b>true</b> will be returned.
	 * Otherwise, <b>false</b> will be returned.
	 */
    public boolean postfix(IAttribute node, Object data);
}
