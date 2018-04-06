/*
 * DwpItemAttributePropertySource.java
 * Created on May 9, 2005
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
package com.wizzer.mle.studio.dwp.attribute;

// Import standard Java classes.
import java.util.Enumeration;
import java.util.Vector;

// Import Eclipse classes.
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;


/**
 * This class implements a property source for a <code>DwpItemAttribute</code>.
 * 
 * @author Mark S. Millard
 */
public class DwpItemAttributePropertySource implements IPropertySource
{
	// The properties.
    static final private String PROPERTY_TYPE = "mle.dwp.attribute.type";
	static final private String PROPERTY_TAGS = "mle.dwp.attribute.tags";

    // The associated Attribute.
    private DwpItemAttribute m_attr = null;
	// The property descriptors.
	private IPropertyDescriptor[] m_propertyDescriptors;
	// The default values for the tags.
	private Vector m_defTags = new Vector();

	/**
	 * A constructor that specifies an associated attribute.
	 * 
	 * @param attr The associated <code>DwpItemAttribute</code>.
	 * @param defTags The default collection of tags.
	 */
    public DwpItemAttributePropertySource(DwpItemAttribute attr, Vector defTags)
    {
        super();
        m_attr = attr;
        for (int i = 0; i < defTags.size(); i++)
        {
            String tag = (String)defTags.elementAt(i);
            m_defTags.add(new String(tag));
        }
    }

	/**
	 * Returns a value for this property source that can be edited in a property sheet.
	 * <p>
	 * This value is used when this IPropertySource is appearing in the property sheet as
	 * the value of a property of some other IPropertySource.
	 * </p><p>
	 * This value is passed as the input to a cell editor opening on an IPropertySource.
	 * </p><p>
	 * This value is also used when an IPropertySource is being used as the value in a
	 * setPropertyValue message. The reciever of the message would then typically use
	 * the editable value to update the original property source or construct a new instance.
	 * </p><p>
	 * For example an email address which is a property source may have an editable value
	 * which is a string so that it can be edited in a text cell editor. The email address
	 * would also have a constructor or setter that takes the edited string so that an
	 * appropriate instance can be created or the original instance modified when the
	 * edited value is set. 
	 * </p><p>
	 * This behavior is important for another reason. When the property sheet is showing
	 * properties for more than one object (multiple selection), a property sheet entry
	 * will display and edit a single value (typically coming from the first selected object).
	 * After a property has been edited in a cell editor, the same value is set as the
	 * property value for all of the objects. This is fine for primitive types but otherwise
	 * all of the objects end up with a reference to the same value. Thus by creating an
	 * editable value and using it to update the state of the original property source object,
	 * one is able to edit several property source objects at once (multiple selection).
	 * </p>
	 * 
	 * @return A reference to this object is returned.
	 * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
     */
    public Object getEditableValue()
    {
        return this;
    }

	/**
	 * Returns the list of property descriptors for this property source.
	 * The <code>getPropertyValue</code> and <code>setPropertyValue</code> methods
	 * are used to read and write the actual property values by specifying the
	 * property ids from these property descriptors.
	 * 
	 * @return An array of available <code>IPropertyDescriptor</code> is returned.
	 * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyDescriptors()
     */
    public IPropertyDescriptor[] getPropertyDescriptors()
    {
        Vector descriptors = new Vector();
        
		// Initialize the descriptor array if necessary. The descriptors will
        // also be re-calculated if tags have been added or removed from the
        // DWP Item attribute.
		if ((m_propertyDescriptors == null) ||
		    ((m_propertyDescriptors.length - 1) != m_attr.m_tags.size()))
		{
		    // Create the Attribute type descriptor.
		    PropertyDescriptor attrTypeDescriptor = new PropertyDescriptor(
		        PROPERTY_TYPE, m_attr.getName());
		    attrTypeDescriptor.setCategory("Info");
		    
		    // Process tags.
		    int count = 0;
		    Enumeration tags = m_attr.getTags();
		    while (tags.hasMoreElements())
		    {
		        String tag = (String)tags.nextElement();
		        
		        PropertyDescriptor tagDescriptor =
		            new DwpItemTagsPropertyDescriptor(PROPERTY_TAGS + "_" + count,
		                "tag" + count++);
		        tagDescriptor.setCategory("DWP Item Tags");
		        
		        descriptors.add(tagDescriptor);
		    }
		    
		    // Allocate a descriptor array large enough to hold all the
		    // tags plus the attribute type.
		    int size = 1 + descriptors.size();
		    m_propertyDescriptors = new IPropertyDescriptor[size];
		    
		    // Populate the array of descriptors.
		    m_propertyDescriptors[0] = attrTypeDescriptor;		    
		    if (! descriptors.isEmpty())
		    {
		        for (int i = 0; i < descriptors.size(); i++)
		            m_propertyDescriptors[i+1] = (IPropertyDescriptor)descriptors.elementAt(i);
		    }
		}
		
		// Return the descriptor array.
		return m_propertyDescriptors;
    }
    
    // Extract the index of the tag from the specified string.
    private int getTagIndex(String id)
    {
        String i = id.substring(id.lastIndexOf('_')+1,id.length());
        int index = new Integer(i).intValue();
        return index;
    }

	/**
	 * Returns the value of the property with the given id if it has one.
	 * Returns null if the property's value is null value or if this source does not have
	 * the specified property.
	 * 
	 * @param id The id of the property being retrieved.
	 * 
	 * @return The property's value is returned. <b>null</b> will be returned otherwise.
	 * 
     * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
     */
    public Object getPropertyValue(Object id)
    {
        Object value = null;
        
        if (id.toString().regionMatches(0,PROPERTY_TAGS,0,PROPERTY_TAGS.length()))
        {
            int index = getTagIndex(id.toString());
            value = m_attr.m_tags.elementAt(index);
        } else if (id.equals(PROPERTY_TYPE))
        {
            value = m_attr.getStringValue();
        }
        
        return value;
    }

	/**
	 * Returns whether the value of the property with the given id has changed from
	 * its default value. Returns false if the notion of default value is not meaningful
	 * for the specified property, or if this source does not have the specified property. 
	 * 
	 * @param id The id of the property being tested.
	 * 
	 * @return <b>true</b> if the value of the specified property has changed from its
	 * original default value, and <b>false</b> otherwise.
	 * 
     * @see org.eclipse.ui.views.properties.IPropertySource#isPropertySet(java.lang.Object)
     */
    public boolean isPropertySet(Object id)
    {
        if (id.toString().regionMatches(0,PROPERTY_TAGS,0,PROPERTY_TAGS.length()))
        {
            int index = getTagIndex(id.toString());
            String defValue = (String)m_defTags.elementAt(index);
            String curValue = (String)getPropertyValue(id);
            return !curValue.equals(defValue);
        }
        
        return false;
    }

	/**
	 * Resets the property with the given id to its default value if possible.
	 * Does nothing if the notion of default value is not meaningful for the specified property,
	 * or if the property's value cannot be changed, or if this source does not have the
	 * specified property. 
	 * 
	 * @param id The id of the property to reset.
	 * 
     * @see org.eclipse.ui.views.properties.IPropertySource#resetPropertyValue(java.lang.Object)
     */
    public void resetPropertyValue(Object id)
    {
        if (id.toString().regionMatches(0,PROPERTY_TAGS,0,PROPERTY_TAGS.length()))
        {
            int index = getTagIndex(id.toString());
            m_attr.updateTag(index,(String)m_defTags.elementAt(index));
        }
    }

	/**
	 * Sets the property with the given id if possible. Does nothing if the property's value
	 * cannot be changed or if this source does not have the specified property. 
	 * 
	 * @param id The id of the property to set.
	 * @param value The value to set.
	 * 
     * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
     */
    public void setPropertyValue(Object id, Object value)
    {
        if (id.toString().regionMatches(0,PROPERTY_TAGS,0,PROPERTY_TAGS.length()))
        {
            int index = getTagIndex(id.toString());
            m_attr.updateTag(index,(String)value);
        }
    }

}
