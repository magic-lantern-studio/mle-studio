/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpReaderCache.h
 * @ingroup MleDWPAccessJava
 *
 * This file defines the Digital Workprint reader cache for previously read
 * workprints.
 *
 * @author Mark S. Millard
 * @created May 4, 2004
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

#ifndef __CDWPREADERCACHE_H_
#define __CDWPREADERCACHE_H_


// Include Digital Workprint header files.
#include "mle/DwpDict.h"

// Include the Java Digital Workprint support header files.
#include "CDwpReader.h"


/**
 * This class is used to cache Digital Workprints that have been loaded
 * from the file system.
 */
class CDwpReaderCache : public MleDwpDict
{
  private:

	// The singleton instance of the cache.
	static CDwpReaderCache *g_theCache;

  public:

	/**
	 * Get the singleton instance of the cache.
	 *
	 * @return A pointer to the cache is returned.
	 */
	static CDwpReaderCache *getInstance();

	/**
	 * The destructor.
	 */
	virtual ~CDwpReaderCache();

	/**
	 * Add a Digital Workprint Reader to the cache.
	 *
	 * @param key The key to use to access the cache.
	 * @param reader The Digital Workprint Reader to add to the cache.
	 */
	virtual void add(const char *key, CDwpReader *reader);

	/**
	 * Remove a Digital Worprint Reader from the cache.
	 *
	 * @param key The key associated with Digital Workprint Reader to
	 * remove from the cache.
	 *
	 * @return A reference to the removed CDwpReader is returned.
	 */
	virtual CDwpReader *remove(const char *key);

	/**
	 * Find the Digital Workprint Reader in the cache.
	 *
	 * @param key The key associated with the Digital Workprint
	 * Reader.
	 *
	 * @return A pointer to the Digital Workprint is returned.
	 * <b>NULL</b> will be returned if the key does not match a cached
	 * workprint.
	 */
	virtual CDwpReader *find(const char *key);

	/**
	 * Flush all readers from the cache.
	 */
	virtual void flush();

  private:

	/*
	 * Hide the default constructor.
	 */
	CDwpReaderCache() {};

};

#endif /* __CDWPREADERCACHE_H_ */
