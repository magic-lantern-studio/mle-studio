/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpJNIRegistry.h
 * @ingroup MleDWPAccessJava
 *
 * This file defines the Digital Workprint JNI Registry.
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

// Include JNI header files.
#ifdef WIN32
#include "JNI.h"
#else
#include "jni.h"
#endif

// Include Digitial Workprint header files.
#include "mle/DwpDict.h"


#ifndef __CDWPJNIREGISTRY_H_
#define __CDWPJNIREGISTRY_H_


/**
 * @brief A key entry based on a Java class string.
 */
class CDwpJNIRegistryEntry : public MleDwpDictEntry
{
  public:

	/**
	 * The destructor.
	 */
    virtual ~CDwpJNIRegistryEntry();
    
	/**
	 * @brief Set the key.
	 *
	 * @param key A pointer to a key. The key must
	 * reference a character string.
	 */
    virtual void setKey(const void *key);
};


/**
 * @brief Implements a dictionary based on keys that
 * reference JNI classes.
 */
class CDwpJNIRegistry : public MleDwpDict
{
  private:

	static CDwpJNIRegistry *theRegistry;

  public:

	/** The DWP domain table Java class. */
	static char *DWP_TABLE;

	/** The Wizzer Works Framework Attribute Java class. */
	static char *FRAMEWORK_ATTRIBUTE;
	/** The Wizzer Works Framework StringAttribute Java class. */
	static char *FRAMEWORK_STRINGATTRIBUTE;
	/** The Wizzer Works Framework NumberAttribute Java class. */
	static char *FRAMEWORK_NUMBERATTRIBUTE;
	/** The Wizzer Works Framework VariableListAttribute Java class. */
	static char *FRAMEWORK_VARIABLELISTATTRIBUTE;

	/** The DWP ActorDef Attribute Java class. */
	static char *DWP_ACTORDEF_ATTRIBUTE;
	/** The DWP Forum/Set Attribute Java class. */
	static char *DWP_SET_ATTRIBUTE;

	/**
	 * The destructor.
	 */
	virtual ~CDwpJNIRegistry();

	static CDwpJNIRegistry *getInstance(JNIEnv *env);

  protected:

    /**
	 * @brief Makes a new entry of type CDwpJNIRegistryEntry.
	 *
	 * @return A pointer to a new dictionary entry is returned.
	 */
    virtual MleDwpDictEntry *makeEntry(void);

    /**
	 * @brief Implements the integer hashing function for the
     * dictionary.
	 *
	 * The hash will be performed on the key that points to
	 * a character string.
	 *
	 * @param key A pointer to the key on which to perform the hash.
	 *
	 * @return The hashed value of the string is returned.
	 */
    virtual unsigned int hash(const void *key) const;

    /**
	 * @brief Compares two keys for equality.
	 *
	 * @param key0 A pointer to the first key to compare against.
	 * @param key1 A pointer to the seconde key to compary against.
	 *
	 * @return 0 will be returned if key0 and key1 are equal. 1 will
	 * be returned if key0 is greater than key1. If key0 is less than
	 * key1, the -1 will be returned.
	 */
    virtual int compare(const void *key0,const void *key1) const;

  private:

	/*
	 * @brief The constructor.
	 *
	 * A default collection of Java class mappings is created for caching
	 * JNI calls.
	 *
	 * @param env A pointer to the JNI environment.
	 */
    CDwpJNIRegistry(JNIEnv *env);

};


#endif /* __CDWPJNIREGISTRY_H_ */
