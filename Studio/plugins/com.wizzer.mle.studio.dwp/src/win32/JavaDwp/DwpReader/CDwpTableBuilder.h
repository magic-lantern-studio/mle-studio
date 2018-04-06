/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpTableBuilder.h
 * @ingroup MleDWPAccessJava
 *
 * This file defines the Digital Workprint domain table builder.
 *
 * @author Mark S. Millard
 * @created May 27, 2004
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

#ifndef __CDWPTABLEBUILDER_H_
#define __CDWPTABLEBUILDER_H_


// Include system header files.
#include <string.h>

// Include JNI header files.
#include "jni.h"

// Include Magic Lantern header files.
#include "mle/mlTypes.h"
#include "mle/mlMalloc.h"

// Include Digital Workprint header files.
#include "mle/DwpItem.h"


class CDwpTableBuilder
{
  public:

	// The root of the in-memory Digital Workprint
	MleDwpItem *m_root;
	// The JNI environment.
	JNIEnv *m_env;
	// The DwpTable class;
	jclass m_dwpTableClass;
	// The domain table being constructed.
	jobject m_dwpTable;
	// The current attribute node.
	jobject m_currentAttr;

  public:

	/**
	 * A constructor that initializes the root of the DWP that
	 * will be traversed.
	 *
	 * @param env A pointer to the JNI environment.
	 * @param root The root of the DWP to traverse.
	 */
	CDwpTableBuilder(JNIEnv *env, MleDwpItem *root);

	/**
	 * The destructor.
	 */
	virtual ~CDwpTableBuilder();

	/**
	 * Run, building the DWP domain table from the specified
	 * Digital Workprint.
	 *
	 * @return A Java DwpTable object will be returned.
	 */
	jobject run();

  private:

	// Hide the default constructor.
	CDwpTableBuilder() {}

	/*
	 * Initialize the builder;
	 *
	 * @returns If the builder was successfully initialized, then <b>TRUE</b>
	 * will be returned. Otherwise, <b>FALSE</b> will be returned.
	 */
	MlBoolean init();

	/*
	 * Recursively traverse the Digital Workprint, building up the
	 * DWP domain table in Java.
	 *
	 * @param builder The DWP domain table builder.
	 * @param item The DWP item to traverse.
	 * @param parentAttr The JNI object that is the parent Attribute for
	 * the specified item.
	 *
	 * @returns If the table was successfully constructed, then <b>TRUE</b>
	 * will be returned. Otherwise, <b>FALSE</b> will be returned.
	 */
	static MlBoolean execute(CDwpTableBuilder *builder,MleDwpItem *item, jobject parentAttr);


};

#endif /* __CDWPTABLEBUILDER_H_ */
