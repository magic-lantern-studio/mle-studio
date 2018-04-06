/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpReader.h
 * @ingroup MleDWPAccessJava
 *
 * This file defines the Digital Workprint reader.
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

#ifndef __CDWPREADER_H_
#define __CDWPREADER_H_


// Include system header files.
#include <string.h>

// Include Magic Lantern header files.
#include "mle/mlTypes.h"
#include "mle/mlMalloc.h"

// Include Digital Workprint header files.
#include "mle/DwpItem.h"


class CDwpReader
{
  private:

	  // The name of the file associated with this reader.
	  char *m_filename;
	  // The buffer associated with this reader.
	  char *m_buffer;
	  // The root of the in-memory Digital Workprint
	  MleDwpItem *m_root;

  public:

	/**
	 * The default constructor.
	 */
	CDwpReader();

	/**
	 * The destructor.
	 */
	virtual ~CDwpReader();

	/**
	 * Get the name of the file that is being handled by
	 * this reader.
	 *
	 * @return The name of the file is returned as pointer to a
	 * character array.
	 */
	virtual char *getFilename()
	{ return m_filename; }

	/**
	 * Set the name of the file that is being handled by
	 * this reader.
	 *
	 * @param filename  A pointer to a character array that repesents 
	 * the name of the file to read.
	 */
	virtual void setFilename(char *filename)
	{ m_filename = strdup(filename); }

	/**
	 * Get a pointer to the buffer that is being handled by
	 * this reader.
	 *
	 * @return The buffer is returned as pointer to a
	 * character array.
	 */
	virtual char *getBuffer()
	{ return m_buffer; }

	/**
	 * Set a pointer to the buffer that is being handled by
	 * this reader.
	 *
	 * @param buffer  A pointer to a character array that repesents 
	 * the buffer to read.
	 */
	virtual void setBuffer(char *buffer)
	{ m_buffer = buffer; }

	/**
	 * Get the root of the Digital Workprint.
	 *
	 * @return The root is returned as a pointer to a MleDwpItem object.
	 */
	virtual MleDwpItem *getRoot()
	{ return m_root; }

	/**
	 * Read the Digital Workprint.
	 *
	 * @param force A flag indicating whether to force the creation
	 * of the DWP from the file or buffer.
	 * <b>TRUE</b> indicates that the DWP should always be instantiated
	 * from the input. This is the default. <b>FALSE</b> specifies that
	 * the DWP should be retrieved from the cache, if available.
	 * 
	 * @return If the DWP was successfully read, then <b>TRUE</b> will be returned.
	 * Otherwise, <b>FALSE</b> will be returned.
	 */
	virtual MlBoolean read(MlBoolean force = TRUE);

	/**
	 * Dump the Digital Workprint to standard out.
	 * 
	 * @return If the DWP was successfully dumped, then <b>TRUE</b> will be returned.
	 * Otherwise, <b>FALSE</b> will be returned.
	 */
	virtual MlBoolean dump();

	/**
	 * Get the contents of the Digital Workprint, returning it in a buffer.
	 *
	 * @param buffer A pointer to a pointer to a buffer containing the read contents.
	 * This is an output parameter.
	 * @param size A pointer to an integer containing the number of bytes read.
	 * This is an output parameter.
	 * 
	 * @return If the DWP was successfully dumped to the buffer,
	 * then <b>TRUE</b> will be returned.
	 * Otherwise, <b>FALSE</b> will be returned.
	 */
	virtual MlBoolean getContents(char **buffer, int *size);

};

#endif /* __CDWPREADER_H_ */
