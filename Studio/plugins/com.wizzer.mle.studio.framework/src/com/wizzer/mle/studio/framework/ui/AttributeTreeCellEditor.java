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
package com.wizzer.mle.studio.framework.ui;

// Import Eclipse packages.
import org.eclipse.swt.widgets.*;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellEditorListener;
import org.eclipse.jface.util.IPropertyChangeListener;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.FrameworkPlugin;
import com.wizzer.mle.studio.framework.attribute.*;


/**
 * This class is a Meta-Cell Editor for a <code>TableTreeViewer</code>.
 * <p>
 * Currently, this <code>CellEditor</code> provides a wrapper around the Framework
 * <code>StringCellEditor</code>, <code>NumberCellEditor</code>, <code>ChoiceCellEditor</code>,
 * and <code>FileCellEditor</code> editors.
 * </p>
 * 
 * @author Mark S. Millard
 * 
 * @see com.wizzer.mle.studio.framework.ui.StringCellEditor
 * @see com.wizzer.mle.studio.framework.ui.NumberCellEditor
 * @see com.wizzer.mle.studio.framework.ui.ChoiceCellEditor
 * @see com.wizzer.mle.studio.framework.ui.FileCellEditor
 */
public class AttributeTreeCellEditor extends CellEditor
{
	/**
	 * The current <code>Attribute</code> type. This must be set prior to
	 * constructing a new instance of <code>AttributeTreeCellEditor</code> so
	 * that the constructor knows which type of <code>CellEditor</code> to wrap.
	 */
	static protected String g_currentAttributeType = Attribute.TYPE_UNKNOWN;

    // The underlying Cell Editor.
    private AttributeCellEditor m_attrEditor;

    /**
     * Get the current <code>Attribute</code> type that is being managed by this
     * editor.
     * 
     * @return The current <code>Attribute</code> type is returned.
     */
    static public String getAttributeType()
    {
    	return g_currentAttributeType;    
    }
    
	/**
	 * Set the current <code>Attribute</code> type that will be managed by this
	 * editor.
	 * 
	 * @param attr The current <code>Attribute</code> from which the type is to
	 * be extracted.
	 */
    static public void setAttributeType(Attribute attr)
    {
    	g_currentAttributeType = attr.getType();
    }
    
	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent.
	 * <>p
	 * The <code>Attribute</code> type must be set prior to invoking this constructor. By default,
	 * a <code>StringCellEditor</code> will be created if the type has not been set.
	 * </p>
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeTreeCellEditor#setAttributeType(Attribute)
	 */
	public AttributeTreeCellEditor(Composite parent)
	{
		super(parent);
		
		// Note: this.createControl(parent) is called from the super's constructor.
		// So, we don't need to do it here.
	}

	/**
	 * A constructor that creates its controls under the specified <code>Composite</code> parent
	 * with the specified style.
	 * <p>
	 * The <code>Attribute</code> type must be set prior to invoking this constructor. By default,
	 * a <code>StringCellEditor</code> will be created if the type has not been set.
	 * </p>
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * @param style The style to use for the new editor component.
	 * 
	 * @see com.wizzer.mle.studio.framework.ui.AttributeTreeCellEditor#setAttributeType(Attribute)
	 */
	public AttributeTreeCellEditor(Composite parent, int style)
	{
		super(parent,style);
		
		// Note: this.createControl(parent) is called from the super's constructor.
		// So, we don't need to do it here.
	}
	
	/**
	 * Create the control for the editor.
	 * <p>
	 * This method uses the current <code>Attribute</code> type to determine which
	 * <code>CellEditor</code> to provide. If the type is not set, then a default
	 * <code>StringCellEditor</code> will be created.
	 * </p>
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * 
     * @see com.wizzer.mle.studio.framework.ui.StringCellEditor
     * @see com.wizzer.mle.studio.framework.ui.NumberCellEditor
     * @see com.wizzer.mle.studio.framework.ui.ChoiceCellEditor
     * @see com.wizzer.mle.studio.framework.ui.FileCellEditor
	 * @see com.wizzer.mle.studio.framework.ui.AttributeTreeCellEditor#setAttributeType(Attribute)
	 */
    protected Control createControl(Composite parent)
    {
    	if (FrameworkPlugin.getDefault() == null)
    	{
    		// Not using the Eclipse plugin, attempt to use core Attributes.
	    	if ((g_currentAttributeType == Attribute.TYPE_STRING) ||
	    	    (g_currentAttributeType == Attribute.TYPE_HEX_STRING) ||
	    	    (g_currentAttributeType == Attribute.TYPE_VARIABLE_LIST))
	    	{
				m_attrEditor = new StringCellEditor(parent);
	    	} else if (g_currentAttributeType == Attribute.TYPE_INTEGER)
	    	{
				m_attrEditor = new NumberCellEditor(parent);
	    	} else if (g_currentAttributeType == Attribute.TYPE_CHOICE)
	    	{
				m_attrEditor = new ChoiceCellEditor(parent);
	    	} else if (g_currentAttributeType == Attribute.TYPE_FILE)
	    	{
				m_attrEditor = new FileCellEditor(parent);
	    	} else
	    	{
				m_attrEditor = new StringCellEditor(parent);
				g_currentAttributeType = Attribute.TYPE_STRING;
	    	}
    	} else
    	{
    		// Using the Eclipse plugin, retrieve from registry.
			AttributeCellEditorFactory registry = AttributeCellEditorFactory.getInstance();
	    	if (registry.hasFactory(g_currentAttributeType))
	    	{
	    		m_attrEditor = registry.createInstance(g_currentAttributeType,parent);
	    	} else
	    	{
				m_attrEditor = new StringCellEditor(parent);
				g_currentAttributeType = Attribute.TYPE_STRING;
	    	}
    	}

    	return m_attrEditor.getControl();
    }
 
    /**
	 * Get the value from the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @return A reference to the <code>Attribute</code>'s value is returned.
	 */
    public Object doGetValue()
    {
   	    Object attribute = null;
   	   
        attribute = m_attrEditor.doGetValue();

	    // Return the attribute.
	    return attribute;
    }

    /**
	 * Set the value in the editor.
	 * <p>
	 * This method implements the Eclipse SWT <code>CellEditor</code> method.
	 * </p>
	 * 
	 * @param attr A reference to the <code>Attribute</code>'s value.
	 */
    public void doSetValue(Object attr)
    {
        m_attrEditor.doSetValue(attr);
    }
	
    /**
	 * Set the focus on the control.
	 */
    protected void doSetFocus()
    {
	    m_attrEditor.doSetFocus();
    }
    
    /**
     * Active the editor.
     */
    public void activate()
    {
    	if (g_currentAttributeType != Attribute.TYPE_UNKNOWN)
    	{
    		m_attrEditor.activate();
      	}
    }

    /**
     * Deactivate the editor.
     */
	public void deactivate()
	{
		if (g_currentAttributeType != Attribute.TYPE_UNKNOWN)
		{
			m_attrEditor.deactivate();
		}
	}
	
	/**
	 * Get the underlying <code>Control</code>.
	 * 
	 * @return The <code>Control</code> for the managed <code>CellEditor</code> is returned.
	 */
	public Control getControl()
	{
		Control control = null;
		
		if (g_currentAttributeType != Attribute.TYPE_UNKNOWN)
		{
			control = m_attrEditor.getControl();
		}
		
		return control;
	}
	
	/**
	 * Get the <code>Attribute</code> that is being managed by this <code>CellEditor</code>.
	 * 
	 * @return The managed <code>Attribute</code> is returned.
	 *
	 */
	public Attribute getAttribute()
	{
		Attribute result = null;
		
		result = m_attrEditor.getAttribute();
		
		return result;
	}
	
	/**
	 * Sets the <code>Attribute</code> for the editor.
	 * <p>
	 * As a side effect, the underlying Cell Editor is updated to reflect the new
	 * <code>Attribute</code> value.
	 * </p>
	 * 
	 * @param parent The parent <code>Composite</code> in the display hierarchy.
	 * @param attr A reference to the <code>Attribute</code> to use with the
	 * editor.
	 * 
	 * @return A reference to the underlying Cell Editor <code>Control</code> is returned.
	 */
	public void setAttribute(Attribute attr)
	{
		if (attr.getType() != g_currentAttributeType)
		    return;

		m_attrEditor.setAttribute(attr);
	}
	
	/**
	 * Hide the label.
	 * <p>
	 * This routine will set the visibility of the label to be hidden. It will also dispose all the resources
	 * associated with the label. Thus, once the label is disposed, it can not be brought back to life.
	 * </p>
	 */
	public void hideLabel()
	{
		m_attrEditor.hideLabel();
	}
	
	/**
	 * Add a Cell Editor listener.
	 * 
	 * @param listener The <code>ICellEditorListener</code> to add.
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#addListener(org.eclipse.jface.viewers.ICellEditorListener)
	 */
	public void addListener(ICellEditorListener listener)
	{
		m_attrEditor.addListener(listener);
	}
	
	/**
	 * Add a Property Change listener.
	 * 
	 * @param listener The <code>IPropertyChangeListener</code> to add.
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#addPropertyChangeListener(org.eclipse.jface.util.IPropertyChangeListener)
	 */
	public void addPropertyChangeListener(IPropertyChangeListener listener)
	{
		m_attrEditor.addPropertyChangeListener(listener);;
	}

}
