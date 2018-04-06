/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpJNIRegistry.cpp
 * @ingroup MleDWPAccessJava
 *
 * This file implements the Digital Workprint JNI Registry.
 *
 * @author Mark S. Millard
 * @created June 2, 2004
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

// Include system header files.
#include <string.h>

// Include Digital Workprint header files.
#include "CDwpJNIRegistry.h"


char *CDwpJNIRegistry::DWP_TABLE                       = "com/wizzer/mle/studio/dwp/domain/DwpTable";

char *CDwpJNIRegistry::DWP_ACTORDEF_ATTRIBUTE          = "com/wizzer/mle/studio/dwp/attribute/DwpActorDefAttribute";

char *CDwpJNIRegistry::DWP_SET_ATTRIBUTE               = "com/wizzer/mle/studio/dwp/attribute/DwpSetAttribute";

char *CDwpJNIRegistry::FRAMEWORK_ATTRIBUTE             = "com/wizzer/mle/studio/framework/attribute/Attribute";

char *CDwpJNIRegistry::FRAMEWORK_STRINGATTRIBUTE       = "com/wizzer/mle/studio/framework/attribute/StringAttribute";

char *CDwpJNIRegistry::FRAMEWORK_NUMBERATTRIBUTE       = "com/wizzer/mle/studio/framework/attribute/NumberAttribute";

char *CDwpJNIRegistry::FRAMEWORK_VARIABLELISTATTRIBUTE = "com/wizzer/mle/studio/framework/attribute/VariableListAttribute";


CDwpJNIRegistry *CDwpJNIRegistry::theRegistry = NULL;


CDwpJNIRegistry *CDwpJNIRegistry::getInstance(JNIEnv *env)
{
	if (theRegistry == NULL)
		theRegistry = new CDwpJNIRegistry(env);
	return theRegistry;
}

void
CDwpJNIRegistryEntry::setKey(const void *k)
{
	delete (char *)m_key;

	m_key = k ? strcpy(new char[strlen((char *)k) + 1],(char *)k) : NULL;
}


CDwpJNIRegistryEntry::~CDwpJNIRegistryEntry()
{
	delete (char *)m_key;
}


MleDwpDictEntry *
CDwpJNIRegistry::makeEntry(void)
{
	return new CDwpJNIRegistryEntry;
}


unsigned int
CDwpJNIRegistry::hash(const void *key) const
{
	int value = 0;

	do {
		/* Rotate left. */
		value = (value << 8) | (value >> 24);

		/* Add character. */
		value += *(char *)key;
	} while ( *(char *)(key = (char *)key + 1) );

	return value;
}


int
CDwpJNIRegistry::compare(const void *key0,const void *key1) const
{
	return strcmp((char *)key0,(char *)key1);
}


CDwpJNIRegistry::CDwpJNIRegistry(JNIEnv *env)
{
	// Register the Attribute class.
	char *clazzName = FRAMEWORK_ATTRIBUTE;
	jclass clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}

	// Register the StringAttribute class.
	clazzName = FRAMEWORK_STRINGATTRIBUTE;
	clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}

	// Register the NumberAttribute class.
	clazzName = FRAMEWORK_NUMBERATTRIBUTE;
	clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}

	// Register the VaribleListAttribute class.
	clazzName = FRAMEWORK_VARIABLELISTATTRIBUTE;
	clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}

	// Register the class that manages the DWP domain table.
	clazzName = DWP_TABLE;
	clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}

	// Register the ActorDef class.
	clazzName = DWP_ACTORDEF_ATTRIBUTE;
	clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}

	// Register the Forum/Set class.
	clazzName = DWP_SET_ATTRIBUTE;
	clazz = (*env).FindClass(clazzName);
	if (clazz != NULL)
	{
		set(clazzName,clazz);
	}
}


CDwpJNIRegistry::~CDwpJNIRegistry()
{
	remove(FRAMEWORK_ATTRIBUTE);
	remove(FRAMEWORK_STRINGATTRIBUTE);
	remove(FRAMEWORK_NUMBERATTRIBUTE);
	remove(FRAMEWORK_VARIABLELISTATTRIBUTE);

	remove(DWP_TABLE);
	remove(DWP_SET_ATTRIBUTE);
	remove(DWP_ACTORDEF_ATTRIBUTE);

	theRegistry = NULL;
}
