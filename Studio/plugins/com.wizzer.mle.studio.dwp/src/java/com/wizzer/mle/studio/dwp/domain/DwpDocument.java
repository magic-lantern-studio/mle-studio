/*
 * DwpDocument.java
 * Created on Apr 13, 2005
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
package com.wizzer.mle.studio.dwp.domain;

// Import Magic Lantern Tool Framework classes.
import com.wizzer.mle.studio.framework.attribute.Attribute;
import com.wizzer.mle.studio.framework.attribute.IAttribute;
import com.wizzer.mle.studio.framework.attribute.VariableListAttribute;

// Import Digital Workprint classes.
import com.wizzer.mle.studio.dwp.attribute.DwpActorDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpBootAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpDocumentAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpGroupAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpIncludeAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpItemAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpMediaRefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpRoleDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSceneAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpSetDefAttribute;
import com.wizzer.mle.studio.dwp.attribute.DwpStageAttribute;

/**
 * This class provides utility for creating and managing a Digital Workprint
 * document.
 * 
 * @author Mark S. Millard
 */
public class DwpDocument implements IDwpItemAttributeFactory
{
    // Current count of Stages.
    private int m_stageCount = 0;
    // Current count of Scenes.
    private int m_sceneCount = 0;
    // Current count of Groups.
    private int m_groupCount = 0;
    // Current count of Media References.
    private int m_mrefCount = 0;
    // Current count of Actor Definitions.
    private int m_actorDefCount = 0;
    // Current count of Set Definitions.
    private int m_setDefCount = 0;
    // Current count of Role Definitions.
    private int m_roleDefCount = 0;

    
    /**
     * The default constructor.
     */
    public DwpDocument()
    {
        super();
    }

    /**
     * Create an instance of the DWP Item Attribute as specified
     * by the argument <i>type</i>.
	 * 
	 * @param vla The Variable List Attribute managing the list of items.
	 * @param data User data associated with the callback. Ignored.
	 * 
	 * @return A reference to an Attribute Tree is returned containing the required fields.
     * 
     * @see com.wizzer.mle.studio.dwp.domain.IDwpItemAttributeFactory#createDwpItemAttribute(com.wizzer.mle.studio.framework.attribute.VariableListAttribute, java.lang.Object)
     */
    public IAttribute createDwpItemAttribute(VariableListAttribute vla,
            Object type)
    {
        if (vla instanceof DwpDocumentAttribute)
            return createDwpItemAttribute((DwpDocumentAttribute)vla, type);
        
        return null;
    }

	/**
	 * Create a default instance of a Digital Workprint Item.
	 * <p>
	 * A <code>StringAttribute</code> is created as a proxy for DWP item.
	 * </p>
	 * 
	 * @param vla The Variable List Attribute managing the list of items.
	 * @param data User data associated with the callback. Ignored.
	 * 
	 * @return A reference to an Attribute Tree is returned containing the required fields.
	 */
	public IAttribute createDwpItemAttribute(DwpDocumentAttribute vla, Object data)
	{
	    Attribute result = null;

	    long count = vla.getCount();
		
		String type = (String)data;
		if (type.equals(DwpItemAttribute.TYPE_DWP_INCLUDE))
		{
		    result = new DwpIncludeAttribute("unknown", false);
		}  else if (type.equals(DwpItemAttribute.TYPE_DWP_STAGE))
		{
		    String name = "stage" + m_stageCount++;
		    result = new DwpStageAttribute(name, "MyStage", false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_SCENE))
		{
		    String name = "scene" + m_sceneCount++;
		    result = new DwpSceneAttribute(name, "MyScene", false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_GROUP))
		{
		    String name = "group" + m_groupCount++;
		    result = new DwpGroupAttribute(name, "MyGroup", false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_MEDIAREF))
		{
		    String name = "mediaRef" + m_mrefCount++;
		    result = new DwpMediaRefAttribute(name, "MyMediaRef", false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_ACTORDEF))
		{
		    String name = "actorDef" + m_actorDefCount++;
		    result = new DwpActorDefAttribute(name, false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_ROLEDEF))
		{
		    String name = "roleDef" + m_roleDefCount++;
		    result = new DwpRoleDefAttribute(name, false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_SETDEF))
		{
		    String name = "setDef" + m_setDefCount++;
		    result = new DwpSetDefAttribute(name, false);
		} else if (type.equals(DwpItemAttribute.TYPE_DWP_BOOT))
		{
		    String name = "scene0";
		    result = new DwpBootAttribute(name, false);
		}

		// Create a proxy for a Digital Workprint item by default.
		//Attribute result = new StringAttribute("DWP_Item_Proxy_" + count,"", 0, true);

		return result;
	}

}
