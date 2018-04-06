/*
 * AbstractPropertyContextMenuHandler.java
 * Created on Apr 14, 2005
 */

// COPYRIGHT_BEGIN
//
//  Copyright (C) 2000-2007  Wizzer Works
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
package com.wizzer.mle.studio.dwp.ui;

// Import Eclipse classes.
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.ui.AttributeTreeViewer;
//import com.wizzer.mle.studio.dwp.attribute.DwpActorAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpFloatArrayPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpFloatPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIntArrayPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIntPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRotationPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpScalarPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpStringPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpTransformPropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector2PropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector3PropertyAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpVector4PropertyAttribute;

/**
 * This class is used to provide a handler for a context menu in an
 * <code>AttributetreeViewer</code> for attributes requiring property
 * support ( i.e. <code>DwpActorAttribute</code>).
 *
 * @author Mark S. Millard
 */
public abstract class AbstractPropertyContextMenuHandler
    extends AbstractItemContextMenuHandler
{
    // Integer property type.
    protected final String PROPERTY_TYPE_INT = "int";
    // Float property type.
    protected final String PROPERTY_TYPE_FLOAT = "float";
    // String property type.
    protected final String PROPERTY_TYPE_STRING = "string";
    // Scalar property type.
    protected final String PROPERTY_TYPE_SCALAR = "MlScalar";
    // Vector2 property type.
    protected final String PROPERTY_TYPE_VECTOR2 = "MlVector2";
    // Vector3 property type.
    protected final String PROPERTY_TYPE_VECTOR3 = "MlVector3";
    // Vector4 property type.
    protected final String PROPERTY_TYPE_VECTOR4 = "MlVector4";
    // Rotation property type.
    protected final String PROPERTY_TYPE_ROTATION = "MlRotation";
    // Transform property type.
    protected final String PROPERTY_TYPE_TRANSFORM = "MlTransform";
    // Integer property type.
    protected final String PROPERTY_TYPE_INTARRAY = "IntArray";
    // Float property type.
    protected final String PROPERTY_TYPE_FLOATARRAY = "FloatArray";
    
	// A menu item for adding an Integer Property item.
    protected MenuItem m_popupAddIntPropertyItem = null;
	// A menu item for adding a Float Property item.
    protected MenuItem m_popupAddFloatPropertyItem = null;
	// A menu item for adding a String Property item.
    protected MenuItem m_popupAddStringPropertyItem = null;
	// A menu item for adding a Scalar Property item.
    protected MenuItem m_popupAddScalarPropertyItem = null;
	// A menu item for adding a Vector2 Property item.
    protected MenuItem m_popupAddVector2PropertyItem = null;
	// A menu item for adding a Vector3 Property item.
    protected MenuItem m_popupAddVector3PropertyItem = null;
	// A menu item for adding a Vector4 Property item.
    protected MenuItem m_popupAddVector4PropertyItem = null;
	// A menu item for adding a Rotation Property item.
    protected MenuItem m_popupAddRotationPropertyItem = null;
	// A menu item for adding a Transform Property item.
    protected MenuItem m_popupAddTransformPropertyItem = null;
	// A menu item for adding an Integer Array Property item.
    protected MenuItem m_popupAddIntArrayPropertyItem = null;
	// A menu item for adding a Float Array Property item.
    protected MenuItem m_popupAddFloatArrayPropertyItem = null;
	// The current count of properties.
	private int m_propertyCount = 0;

	/**
     * The default constructor.
     */
	protected AbstractPropertyContextMenuHandler()
    {
        super();
    }

    /**
     *  Create the context menu.
     */
	protected void createContextMenu()
	{
		// Add menu item for adding Integer Property DWP item.
		m_popupAddIntPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddIntPropertyItem.setText("Add DWP Integer Property Item");
		m_popupAddIntPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_INT);
				}
			});

		// Add menu item for adding Float Property DWP item.
		m_popupAddFloatPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddFloatPropertyItem.setText("Add DWP Float Property Item");
		m_popupAddFloatPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_FLOAT);
				}
			});

		// Add menu item for adding String Property DWP item.
		m_popupAddStringPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddStringPropertyItem.setText("Add DWP String Property Item");
		m_popupAddStringPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_STRING);
				}
			});

		// Add menu item for adding Scalar Property DWP item.
		m_popupAddScalarPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddScalarPropertyItem.setText("Add DWP MlScalar Property Item");
		m_popupAddScalarPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_SCALAR);
				}
			});

		// Add menu item for adding Vector2 Property DWP item.
		m_popupAddVector2PropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddVector2PropertyItem.setText("Add DWP MlVector2 Property Item");
		m_popupAddVector2PropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_VECTOR2);
				}
			});

		// Add menu item for adding Vector3 Property DWP item.
		m_popupAddVector3PropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddVector3PropertyItem.setText("Add DWP MlVector3 Property Item");
		m_popupAddVector3PropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_VECTOR3);
				}
			});

		// Add menu item for adding Vector4 Property DWP item.
		m_popupAddVector4PropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddVector4PropertyItem.setText("Add DWP MlVector4 Property Item");
		m_popupAddVector4PropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_VECTOR4);
				}
			});

		// Add menu item for adding Rotation Property DWP item.
		m_popupAddRotationPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddRotationPropertyItem.setText("Add DWP MlRotation Property Item");
		m_popupAddRotationPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_ROTATION);
				}
			});

		// Add menu item for adding Transform Property DWP item.
		m_popupAddTransformPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddTransformPropertyItem.setText("Add DWP MlTransform Property Item");
		m_popupAddTransformPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_TRANSFORM);
				}
			});

		// Add menu item for adding Integer Array Property DWP item.
		m_popupAddIntArrayPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddIntArrayPropertyItem.setText("Add DWP Integer Array Property Item");
		m_popupAddIntArrayPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_INTARRAY);
				}
			});

		// Add menu item for adding Float Array Property DWP item.
		m_popupAddFloatArrayPropertyItem = new MenuItem(m_popupMenu,SWT.PUSH);
		m_popupAddFloatArrayPropertyItem.setText("Add DWP Float Array Property Item");
		m_popupAddFloatArrayPropertyItem.addListener(SWT.Selection,
			new Listener()
			{
				public void handleEvent(Event e)
				{
					popupAddListElementActionPerformed(e,
					    DwpItemAttribute.TYPE_DWP_PROPERTY, PROPERTY_TYPE_FLOATARRAY);
				}
			});

	    super.createContextMenu();
	}

	/**
	 * Process the "Add List Element" action.
	 * 
	 * @param event The selection event that caused this handler to be invoked.
	 * @param attrType The type of <code>Attribute</code> to add.
	 * @param propType The type of <code>Property</code> to add.
	 */
	public void popupAddListElementActionPerformed(Event event, String attrType,
	    String propType)
	{
		IAttribute attr = m_viewer.getSelectedAttribute();
		
		if (attrType == DwpItemAttribute.TYPE_DWP_PROPERTY)
		{
		    if (propType == PROPERTY_TYPE_INT)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "int";
			    Integer value = new Integer(0);
			    DwpIntPropertyAttribute property = new DwpIntPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_FLOAT)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "float";
			    Float value = new Float(0);
			    DwpFloatPropertyAttribute property = new DwpFloatPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_STRING)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "string";
			    String value = "unknown";
			    DwpStringPropertyAttribute property = new DwpStringPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_VECTOR2)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "MlVector2";
			    Float[] value = new Float[2];
			    value[0] = new Float(0);
			    value[1] = new Float(0);
			    DwpVector2PropertyAttribute property = new DwpVector2PropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
		    } else if (propType == PROPERTY_TYPE_VECTOR3)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "MlVector3";
			    Float[] value = new Float[3];
			    value[0] = new Float(0);
			    value[1] = new Float(0);
			    value[2] = new Float(0);
			    DwpVector3PropertyAttribute property = new DwpVector3PropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
		    } else if (propType == PROPERTY_TYPE_VECTOR4)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "MlVector4";
			    Float[] value = new Float[4];
			    value[0] = new Float(0);
			    value[1] = new Float(0);
			    value[2] = new Float(0);
			    value[3] = new Float(0);
			    DwpVector4PropertyAttribute property = new DwpVector4PropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
		    } else if (propType == PROPERTY_TYPE_ROTATION)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "MlRotation";
			    Float[] value = new Float[4];
			    value[0] = new Float(0);
			    value[1] = new Float(0);
			    value[2] = new Float(0);
			    value[3] = new Float(0);
			    DwpRotationPropertyAttribute property = new DwpRotationPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_TRANSFORM)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "MlTransform";
			    Float[][] value = new Float[4][3];
				for (int i = 0; i < 4; i ++)
					for (int j = 0; j < 3; j++)
					    value[i][j] = new Float(0);
			    DwpTransformPropertyAttribute property = new DwpTransformPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_INTARRAY)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "IntArray";
			    Integer[] value = new Integer[2];
			    value[0] = new Integer(0);
			    value[1] = new Integer(0);
			    DwpIntArrayPropertyAttribute property = new DwpIntArrayPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_FLOATARRAY)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "FloatArray";
			    Float[] value = new Float[2];
			    value[0] = new Float(0);
			    value[1] = new Float(0);
			    DwpFloatArrayPropertyAttribute property = new DwpFloatArrayPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			} else if (propType == PROPERTY_TYPE_SCALAR)
		    {
			    String name = "prop" + m_propertyCount++;
			    String type = "MlScalar";
			    Float value = new Float(0);
			    DwpScalarPropertyAttribute property = new DwpScalarPropertyAttribute(
			        name, type, value, false);
			    attr.addChild(property,m_viewer.getTable());
			}
		}
	}

	/**
	 * Get a context <code>Menu</code> for the specified <code>IAttribute</code>.
	 * 
	 * @param attribute The <code>IAttribute</code> to create a context menu for.
	 * 
	 * @return A reference to a <code>Menu</code> is returned.
	 */
    public abstract Menu getContextMenu(IAttribute attribute, AttributeTreeViewer viewer);

}
