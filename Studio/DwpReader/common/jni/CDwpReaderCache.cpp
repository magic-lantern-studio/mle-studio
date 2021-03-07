/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpReaderCache.cpp
 * @ingroup MleDWPAccessJava
 *
 * This file implements the Digital Workprint reader cache.
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

// Include header files.
#include "CDwpReaderCache.h"


// The singleton instance of the cache.
CDwpReaderCache *CDwpReaderCache::g_theCache = NULL;


CDwpReaderCache *CDwpReaderCache::getInstance()
{
	if (g_theCache == NULL)
		g_theCache = new CDwpReaderCache();
	return g_theCache;
}


CDwpReaderCache::~CDwpReaderCache()
{
	if (g_theCache != NULL)
		delete g_theCache;
}


void CDwpReaderCache::add(const char *key, CDwpReader *reader)
{
	MleDwpDict::set(key, reader);
}


CDwpReader *CDwpReaderCache::remove(const char *key)
{
	// Retrieve the reader matching the key.
	CDwpReader *reader = this->find(key);

	// Remove the entry from the cache.
	if (reader != NULL)
		MleDwpDict::remove(key);

	return reader;
}


CDwpReader *CDwpReaderCache::find(const char *key)
{
	return (CDwpReader *) MleDwpDict::find(key);
}


void CDwpReaderCache::flush()
{
	MleDwpDictIter *iter = new MleDwpDictIter((MleDwpDict) *g_theCache);
	while (iter->next())
	{
		const char *key = (const char *) iter->getKey();
		this->remove(key);
	}
}