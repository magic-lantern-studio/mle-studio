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
