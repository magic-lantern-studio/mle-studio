/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpReader.cpp
 * @ingroup MleDWPAccessJava
 *
 * This file implements the Digital Workprint reader.
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
#include <stdio.h>

// Include Digital Workprint header files.
#include "mle/DwpInput.h"
#include "mle/DwpOutput.h"

#include "CDwpReader.h"


CDwpReader::CDwpReader()
  : m_filename(NULL), m_buffer(NULL), m_root(NULL)
{
	// Do nothing extra.
}


CDwpReader::~CDwpReader()
{
	if (m_filename != NULL)
		mlFree(m_filename);
	if (m_root != NULL)
		delete m_root;
}


MlBoolean CDwpReader::read(MlBoolean force)
{
	MlBoolean retValue = FALSE;

	// Make sure the filename or buffer has been set.
	if ((m_filename == NULL) && (m_buffer == NULL))
		return retValue;
	if ((m_filename != NULL) && (m_buffer != NULL))
		return retValue;

	// Check if there has been a previously read DWP.
	if (m_root != NULL)
	{
		// Check if we're sking for a cached read.
		if (force != TRUE)
			return TRUE;
		else
			delete m_root;
	}

	// Attempt to open the DWP.
	MleDwpInput in;
	if (m_filename != NULL)
	{
		if ( in.openFile(m_filename) )
		{
			printf("CDwpReader: can't open file '%s'\n", m_filename);
			return retValue;
		}
	} else
	{
		if ( in.setBuffer(m_buffer) )
		{
			printf("CDwpReader: can't set input buffer.");
			return retValue;
		}
	}

	// Read the DWP.
	m_root = new MleDwpItem;
	while ( MleDwpItem::read(&in,m_root) )
		;

	// Close the DWP file.
	if (m_filename != NULL)
	    in.closeFile();

	// Set the return value for success.
	retValue = TRUE;

	return retValue;
}


MlBoolean CDwpReader::dump()
{
	MlBoolean status;

	// Make sure that the DWP has been previously read in.
	if (m_root == NULL)
		return FALSE;

	// Create a new DWP Output item.
	MleDwpOutput *out = new MleDwpOutput;
    if (out != NULL)
	{
        out->setFilePointer(stdout);
		if (m_root->write(out) == 0)
			status = TRUE;
		else
			status = FALSE;
	}

	// Clean up.
	delete out;

	return status;
}


MlBoolean CDwpReader::getContents(char **buffer, int *size)
{
	MlBoolean status;
	int bufferSize;

	// Make sure that the DWP has been previously read in.
	if (m_root == NULL)
		return FALSE;

	// Allocate some space for the output buffer.
	bufferSize = 1024;
	*buffer = (char *) mlMalloc(bufferSize);

	// Create a new DWP Output item.
	MleDwpOutput *out = new MleDwpOutput;
    if (out != NULL)
	{
        out->setBuffer(*buffer,bufferSize);
		if (m_root->write(out) == 0)
			status = TRUE;
		else
			status = FALSE;
	}

	// Get the actual buffer size. The size may be smaller than the inital size we
	// allocated for, or it may be larger if the buffer had to be extended during writing.
	out->getBuffer(buffer,&bufferSize);

	// Create a buffer that is the actual size of the data that was written to it.
	// Do not include the terminating '\0' character.
	char *retBuffer = (char *) mlMalloc(bufferSize-1);
	memcpy(retBuffer,*buffer,bufferSize-1);
	mlFree(*buffer);
	*buffer = retBuffer;
	*size = bufferSize-1;

	// Clean up.
	delete out;

	return status;
}
