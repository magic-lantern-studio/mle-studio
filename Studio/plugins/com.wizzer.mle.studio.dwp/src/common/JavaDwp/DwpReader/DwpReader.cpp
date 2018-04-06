/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file DwpReader.c
 * @ingroup MleDWPAccessJava
 *
 * This file implements the JNI interface for the Digital Workprint reader.
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

// Include system header files.
#ifdef WIN32
#include <windows.h>
#endif /* WIN32 */

// Include JNI header files.
#include <jni.h>
#include "com_wizzer_mle_studio_dwp_DwpReader.h"

// Include Magic Lantern header files.
#include "mle/mlMalloc.h"

// Inlclude Digital Workprint header files.
#include "mle/Dwp.h"
#include "mle/DwpItem.h"
#include "mle/DwpInput.h"

// Include Reader header files.
#include "CDwpReaderCache.h"
#include "CDwpTableBuilder.h"


// A flag indicating whether the Digital Workprint API has been initialized or not.
static MlBoolean g_dwpInitialized = FALSE;

// Initialize the Digital Workprint API.
static void initDwp()
{
	if (g_dwpInitialized == FALSE)
	{
		// Initialize the API.
		mleDwpInit();
		// Set the flag indicating that we have already done this.
		g_dwpInitialized = TRUE;
	}
}


/*
 * Class:     com_wizzer_mle_studio_dwp_DwpReader
 * Method:    buildTableFromNativeDwp
 * Signature: (Ljava/lang/String;)Lcom/wizzer/mle/studio/dwp/domain/DwpTable;
 */
JNIEXPORT jobject JNICALL
Java_com_wizzer_mle_studio_dwp_DwpReader_buildTableFromNativeDwp(JNIEnv *env, jobject obj, jstring filename)
{
	jobject dwpTable = NULL;

	// Allocate space for the DWP filename.
	int len = (*env).GetStringUTFLength(filename);
	char *dwpFilename = (char *) mlMalloc(len + 1);

	// Retrieve the filename.
	if (dwpFilename != NULL)
	{
	    (*env).GetStringUTFRegion(filename, 0, len, dwpFilename);
		dwpFilename[len] = '\0';
	} else
	{
		// OutOfMemoryError already thrown, just return NULL.
		return NULL;
	}

	// Check to see if the file reader has been cached.
	CDwpReaderCache *theCache = CDwpReaderCache::getInstance();
	CDwpReader *reader = theCache->find(dwpFilename);
	if (reader == NULL)
	{
    	// Create a new reader if necessary.
		reader = new CDwpReader();
		reader->setFilename(dwpFilename);

	    // Add the reader to the cache.
		theCache->add(dwpFilename,reader);
	}

	// Initialize the Digital Workprint API.
	initDwp();

	// Read the Digital Workprint. Always attempt to get a cached
	// DWP.
	MlBoolean status = reader->read(FALSE);
	if (status)
	{
		// Generate a DwpTable to return to the calling Java class.
		CDwpTableBuilder *builder = new CDwpTableBuilder(env,reader->getRoot());
        dwpTable = builder->run();
	} else
	{
		// Unable to read the specified name, clean up.
		theCache->remove(dwpFilename);
		delete reader;
	}

	// Clean up.
	fflush(stdout);
	fflush(stderr);
	mlFree(dwpFilename);

	return dwpTable;
}

/*
 * Class:     com_wizzer_mle_studio_dwp_DwpReader
 * Method:    buildArrayFromNativeDwp
 * Signature: (Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL
Java_com_wizzer_mle_studio_dwp_DwpReader_buildArrayFromNativeDwp(JNIEnv *env, jobject obj, jstring filename)
{
	jbyteArray retValue = NULL;
	char *buffer = NULL;
	int size = 0;

	// Allocate space for the DWP filename.
	int len = (*env).GetStringUTFLength(filename);
	char *dwpFilename = (char *) mlMalloc(len + 1);

	// Retrieve the filename.
	if (dwpFilename != NULL)
	{
	    (*env).GetStringUTFRegion(filename, 0, len, dwpFilename);
		dwpFilename[len] = '\0';
	} else
	{
		// OutOfMemoryError already thrown, just return NULL.
		return retValue;
	}

	// Check to see if the file reader has been cached.
	CDwpReaderCache *theCache = CDwpReaderCache::getInstance();
	CDwpReader *reader = theCache->find(dwpFilename);
	if (reader == NULL)
	{
    	// Create a new reader if necessary.
		reader = new CDwpReader();
		reader->setFilename(dwpFilename);

	    // Add the reader to the cache.
		theCache->add(dwpFilename,reader);
	}

	// Initialize the Digital Workprint API.
	initDwp();

	// Read the Digital Workprint. Always attempt to get a cached
	// DWP.
	MlBoolean status = reader->read(FALSE);
	if (status)
	{
		// Retrieve the contents of the Digital Workprint into a buffer.
		if (reader->getContents(&buffer, &size))
		{
			retValue = (*env).NewByteArray(size);
			if (retValue == NULL)
			{
				retValue = NULL; /* exception thrown */
			} else
			{
				(*env).SetByteArrayRegion(retValue,0,size,(jbyte *)buffer);
			}

		} else
			retValue = NULL;
	} else
	{
		// Unable to read the specified name, clean up.
		mlFree(buffer);
		theCache->remove(dwpFilename);
		delete reader;
	}

	// Clean up.
	fflush(stdout);
	fflush(stderr);
	mlFree(buffer);
	mlFree(dwpFilename);

	return retValue;
}

/*
 * Class:     com_wizzer_mle_studio_dwp_DwpReader
 * Method:    buildTableFromNativeData
 * Signature: ([B)Lcom/wizzer/mle/studio/dwp/domain/DwpTable;
 */
JNIEXPORT jobject JNICALL
Java_com_wizzer_mle_studio_dwp_DwpReader_buildTableFromNativeData(JNIEnv *env, jobject obj, jbyteArray data)
{
    jobject dwpTable = NULL;

	// Get the number of bytes in the array.
	jsize numBytes = (*env).GetArrayLength(data);

	// Get a pointer to the array.
	jbyte *bytes = (*env).GetByteArrayElements(data,NULL);
	if (bytes != NULL)
	{
		bytes[numBytes] = 0;

		// Create a new reader if necessary.
		CDwpReader *reader = new CDwpReader();
		reader->setBuffer((char *)bytes);

		// Initialize the Digital Workprint API.
		initDwp();

		// Read the Digital Workprint. Always attempt to get a cached
		// DWP.
		MlBoolean status = reader->read(FALSE);
		if (status)
		{
			// Generate a DwpTable to return to the calling Java class.
			CDwpTableBuilder *builder = new CDwpTableBuilder(env,reader->getRoot());
			dwpTable = builder->run();
		} else
		{
			// Unable to read the specified name, clean up.
			delete reader;
		}

	} else
	{
		// OutOfMemoryError already thrown, just return NULL.
		return NULL;
	}

	// Clean up.
	fflush(stdout);
	fflush(stderr);

	return dwpTable;
}
