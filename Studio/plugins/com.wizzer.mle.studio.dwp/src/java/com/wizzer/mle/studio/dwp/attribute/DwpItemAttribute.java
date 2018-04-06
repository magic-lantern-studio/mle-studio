/*
 * DwpItemAttribute.java
 * Created on May 28, 2004
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
import java.util.Vector;
import java.util.Enumeration;

// Import Eclipse classes.
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.ui.views.properties.IPropertySource;

// Import Wizzer Works Framework packages.
import com.wizzer.mle.studio.framework.attribute.Attribute;

/**
 * This class is a partial implementation of an <code>Attribute</code> that represents
 * a Magic Lantern Digital Workprint item.
 * 
 * @author Mark S. Millard
 */
public abstract class DwpItemAttribute extends Attribute implements IAdaptable
{
	/** DWP Actor Definition attribute type. */
	public static final String TYPE_DWP_ACTORDEF        = "com.wizzer.mle.studio.dwp.attribute.ACTORDEF";
	/** DWP Role Definition attribute type. */
	public static final String TYPE_DWP_ROLEDEF         = "com.wizzer.mle.studio.dwp.attribute.ROLEDEF";
	/** DWP Property Definition attribute type. */
	public static final String TYPE_DWP_PROPERTYDEF     = "com.wizzer.mle.studio.dwp.attribute.PROPERTYDEF";
	/** DWP Set/Forum Definition attribute type. */
	public static final String TYPE_DWP_SETDEF          = "com.wizzer.mle.studio.dwp.attribute.SETDEF";
	/** DWP Stage Definition attribute type. */
	public static final String TYPE_DWP_STAGEDEF        = "com.wizzer.mle.studio.dwp.attribute.STAGEDEF";
	/** DWP Group Reference attribute type. */
	public static final String TYPE_DWP_GROUPREF        = "com.wizzer.mle.studio.dwp.attribute.GROUPREF";
	/** DWP Actor attribute type. */
	public static final String TYPE_DWP_ACTOR           = "com.wizzer.mle.studio.dwp.attribute.ACTOR";
	/** DWP Property attribute type. */
	public static final String TYPE_DWP_PROPERTY        = "com.wizzer.mle.studio.dwp.attribute.PROPERTY";
	/** DWP Set attribute type. */
	public static final String TYPE_DWP_SET             = "com.wizzer.mle.studio.dwp.attribute.SET";
	/** DWP Stage attribute type. */
	public static final String TYPE_DWP_STAGE           = "com.wizzer.mle.studio.dwp.attribute.STAGE";
	/** DWP Group attribute type. */
	public static final String TYPE_DWP_GROUP           = "com.wizzer.mle.studio.dwp.attribute.GROUP";
	/** DWP Include attribute type. */
	public static final String TYPE_DWP_INCLUDE         = "com.wizzer.mle.studio.dwp.attribute.INCLUDE";
	/** DWP DSO File attribute type. */
	public static final String TYPE_DWP_DSOFILE         = "com.wizzer.mle.studio.dwp.attribute.DSOFILE";
	/** DWP Header File attribute type. */
	public static final String TYPE_DWP_HEADERFILE      = "com.wizzer.mle.studio.dwp.attribute.HEADERFILE";
	/** DWP Source File attribute type. */
	public static final String TYPE_DWP_SOURCEFILE      = "com.wizzer.mle.studio.dwp.attribute.SOURCEFILE";
	/** DWP Delegate/Role Binding attribute type. */
	public static final String TYPE_DWP_ROLEBINDING     = "com.wizzer.mle.studio.dwp.attribute.ROLEBINDING";
	/** DWP Delagate/Role Attachment attribute type. */
	public static final String TYPE_DWP_ROLEATTACHMENT  = "com.wizzer.mle.studio.dwp.attribute.ROLEATTACHMENT";
	/** DWP Delagate/Role Set Mapping attribute type. */
	public static final String TYPE_DWP_ROLESETMAPPING  = "com.wizzer.mle.studio.dwp.attribute.ROLESETMAPPING";
	/** DWP Media Reference attribute type. */
	public static final String TYPE_DWP_MEDIAREF        = "com.wizzer.mle.studio.dwp.attribute.MEDIAREF";
	/** DWP Media Reference Source attribute type. */
	public static final String TYPE_DWP_MEDIAREFSOURCE  = "com.wizzer.mle.studio.dwp.attribute.MEDIAREFSOURCE";
	/** DWP Media Reference Target attribute type. */
	public static final String TYPE_DWP_MEDIAREFTARGET  = "com.wizzer.mle.studio.dwp.attribute.MEDIAREFTARGET";
	/** DWP Media Reference Class attribute type. */
	public static final String TYPE_DWP_MEDIAREFCLASS   = "com.wizzer.mle.studio.dwp.attribute.MEDIAREFCLASS";
	/** DWP Media attribute type. */
	public static final String TYPE_DWP_MEDIA           = "com.wizzer.mle.studio.dwp.attribute.MEDIA";
	/** DWP Scene attribute type. */
	public static final String TYPE_DWP_SCENE           = "com.wizzer.mle.studio.dwp.attribute.SCENE";
	/** DWP Boot attribute type. */
	public static final String TYPE_DWP_BOOT            = "com.wizzer.mle.studio.dwp.attribute.BOOT";
	/** DWP Package attribute type. */
	public static final String TYPE_DWP_PACKAGE         = "com.wizzer.mle.studio.dwp.attribute.PACKAGE";

	// Collection of DWP tags.
	protected Vector m_tags = new Vector();
	
	// The property source.
	private DwpItemAttributePropertySource m_attrPropertySource = null;

	
	/**
	 * A constructor that initializes the <code>DwpItemAttribute</code>.
	 * <p>
	 * Note that the name of the <code>Attribute</code> is mapped to the DWP item type
	 * (i.e. "ActorDef", "Actor", "Set" ...).
	 * </p>
	 * 
	 * @param type The type of the DWP item.
	 * @param value The contents of the DWP item. This parameter may be <b>null</b>.
	 * @param isReadOnly A flag indicating whether the attribute should be read-only or not.
	 */
	public DwpItemAttribute(String type, Object value, boolean isReadOnly)
	{
		this.setName(type);
		this.setValue(value);
		this.setReadOnly(isReadOnly);
	}
	
	/**
	 * Get the type for the Digital Workprint item.
	 * 
	 * @return A <code>String</code> is returned.
	 */
	public String getDwpItemType()
	{
		return this.getName();
	}
	
	/**
	 * Set the type for the Digital Workprint item.
	 * 
	 * @param type A <code>String</code> representing the DWP item type.
	 */
	public void setDwpItemType(String type)
	{
		this.setName(type);
	}

   /**
    * Get the Digital Workprint item as an array of tokens.
    * <p>
    * For example a DWP PropertyDef, <i>( PropertyDef z int )</i>, would result
    * in an array containing three elements:
    * <ol>
    * <li>String[0] = "PropertyDef"</li>
    * <li>String[1] = "z"</li>
    * <li>String[2] = "int"</li>
    * </ol>
    * </p>
    * 
    * @return An array of <>String<> is returned where each element in the array
    * is a token in the Digital Workprint item.
    */
   protected String[] getTokens()
    {
    	return toString().split(" ");
    }
   
  /**
   * Add a tag to this Digital Workprint item.
   * 
   * @param tag The tag to add.
   * 
   * @return If the tag is successfully added, then <b>true</b> will be returned.
   * Otherwise, <b>false</b> will be returned.
   */
   public boolean addTag(String tag)
   {
       boolean retValue = true;
       
       if (! m_tags.contains(tag))
       {
           retValue = m_tags.add(tag);
           
           // Notify observers that this Attribute has changed.
           this.setChanged();
           this.notifyObservers(this);
       }
       
       return retValue;
   }
   
   /**
    * Remove the specified tag from the Digital Workprint item.
    * 
    * @param tag The tag to remove.
    * 
    * @return If the tag is successfully removed, then <b>true</b> will be returned.
    * Otherwise, <b>false</b> will be returned.
    */
   public boolean removeTag(String tag)
   {
       boolean retValue = m_tags.remove(tag);
       
       if (retValue == true)
       {
           // Notify observers that this Attribute has changed.
           this.setChanged();
           this.notifyObservers(this);
       }

       return retValue;
   }
   
   /**
    * Remove all tags from the Digital Workprint item.
    */
   public void clearTags()
   {
       m_tags.removeAllElements();
       
       // Notify observers that this Attribute has changed.
       this.setChanged();
       this.notifyObservers(this);
   }
   
   /**
    * Get the tags associated with the Digital Workprint item.
    * 
    * @return Returns an enumeration of the tags of this DWP.
    * The returned Enumeration object will generate all items in this vector of tags.
    * The first item generated is the item at index 0, then the item at index 1, and so on. 
    */
   public Enumeration getTags()
   {
       return m_tags.elements();
   }
   
   /**
    * Update the value of the tag located at the specifed <b>index</b>.
    * 
    * @param index The location of the tag.
    * @param value The <code>String</code> value to update.
    */
   public void updateTag(int index, String value)
   {
       m_tags.setElementAt(value,index);
       
       // Notify observers that this Attribute has changed.
       this.setChanged();
       this.notifyObservers(this);
   }
   
	/**
	 * Copy the tags to the specified <code>DwpItemAttribute</code>.
	 * <p>
	 * This method does not remove existing tags from the specified attribute.
	 * It just adds the tags from <b>this</b> <code>DwpItemAttribute</code>.
	 * </p>
	 */
	protected void copyTags(DwpItemAttribute attr)
	{
	    for (int i = 0; i < m_tags.size(); i++)
	    {
	        String tag = (String)m_tags.elementAt(i);
	        attr.addTag(new String(tag));
	    }
	}
	
	/**
	 * Get the tags in a format compatible with the DWP file format.
	 * 
	 * @return A <code>String</code> is returned in the form of "+tag0 +tag1 +tag2".
	 * The number of tokens in the string depends on the number of tags reqistered
	 * with the <code>DwpItemAttribute</code>.
	 */
	protected String getTagsString()
	{
	    StringBuffer buffer = new StringBuffer();
	    
	    for (int i = 0; i < m_tags.size(); i++)
	    {
	        String tag = "+" + (String)m_tags.elementAt(i) + " ";
	        buffer.append(tag);
	    }
	    
	    return buffer.toString().trim();
	}
   
	/**
	 * Returns an object which is an instance of the given class associated with this object.
	 * Returns null if no such object can be found.
	 * 
	 * @param adapter The adapter class to look up.
	 * 
	 * @return A object castable to the given class, or <b>null</b> if this object does
	 * not have an adapter for the given class.
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	public Object getAdapter(Class adapter)
	{
	   if (adapter == IPropertySource.class)
	   {
		  if (m_attrPropertySource == null)
		  {
			  // Cache the property source.
			  m_attrPropertySource = new DwpItemAttributePropertySource(this,m_tags);
		  }
		  
		  return m_attrPropertySource;
	   }
	   
	   return null;
	}
}
