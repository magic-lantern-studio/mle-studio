/** @defgroup MleDWPAccessJava Magic Lantern Digital Workprint Library API for Java */

/**
 * @file CDwpTableBuilder.cpp
 * @ingroup MleDWPAccessJava
 *
 * This file implements the Digital Workprint domain table builder.
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

// Include system header files.
#include <stdio.h>

#include "math/rotation.h"
#include "math/transfrm.h"

// Include Digital Workprint header files.
#include "mle/DwpItem.h"
#include "mle/DwpDatatype.h"
#include "mle/DwpProperty.h"
#include "mle/DwpInclude.h"
#include "mle/DwpDSOFile.h"
#include "mle/DwpHeaderFile.h"
#include "mle/DwpSourceFile.h"
#include "mle/DwpPackage.h"
#include "mle/DwpSet.h"
#include "mle/DwpSetDef.h"
#include "mle/DwpActorDef.h"
#include "mle/DwpRoleDef.h"
#include "mle/DwpPropertyDef.h"
#include "mle/DwpGroup.h"
#include "mle/DwpRoleBinding.h"
#include "mle/DwpRoleAttachment.h"
#include "mle/DwpRoleSetMapping.h"
#include "mle/DwpActor.h"
#include "mle/DwpInt.h"
#include "mle/DwpFloat.h"
#include "mle/DwpString.h"
#include "mle/DwpScalar.h"
#include "mle/DwpVector2.h"
#include "mle/DwpVector3.h"
#include "mle/DwpVector4.h"
#include "mle/DwpIntArray.h"
#include "mle/DwpFloatArray.h"
#include "mle/DwpRotation.h"
#include "mle/DwpTransform.h"
#include "mle/DwpMediaRef.h"
#include "mle/DwpMedia.h"
#include "mle/DwpMediaRefClass.h"
#include "mle/DwpMediaRefSource.h"
#include "mle/DwpMediaRefTarget.h"
#include "mle/DwpStage.h"
#include "mle/DwpStageDef.h"
#include "mle/DwpScene.h"
#include "mle/DwpBoot.h"
#include "mle/DwpGroupRef.h"

#include "CDwpTableBuilder.h"


static MlBoolean addAttribute(CDwpTableBuilder *builder, jobject attr, jobject parentAttr)
{
	MlBoolean rval = FALSE;

	JNIEnv *env = builder->m_env;
	jclass dwpTableClass = builder->m_dwpTableClass;
	jobject table = builder->m_dwpTable;

	if (parentAttr == NULL)
	{
		// Attach the attribute to the root of the table.

		// Get the method ID for the getDwpElements() method.
		jmethodID methodID = (*env).GetMethodID(dwpTableClass, "getDwpElements",
			"()Lcom/wizzer/mle/studio/framework/attribute/VariableListAttribute;");
		if (methodID != NULL)
		{
			// Call the getDwpElements() method, obtaining the VariableListAttribute.
			jobject vla = (*env).CallObjectMethod(table,methodID);
			if (vla != NULL)
			{
				// Find the VariableListAttribute class.
				jclass vlaClass = (*env).GetObjectClass(vla);
				if (vlaClass != NULL)
				{
					// Get the method ID for addChild(Attribute child, Observer obs) method.
					methodID = (*env).GetMethodID(vlaClass, "addChild",
						"(Lcom/wizzer/mle/studio/framework/attribute/Attribute;Ljava/util/Observer;)Lcom/wizzer/mle/studio/framework/attribute/Attribute;");
					if (methodID != NULL)
					{
						// Call the addChild() method.
						jobject newChild = (*env).CallObjectMethod(vla, methodID, attr, table);
						if (newChild != NULL)
						{
							rval = TRUE;
							// Free local references.
							(*env).DeleteLocalRef(newChild);
						}
					}

					// Free local references.
					(*env).DeleteLocalRef(vlaClass);
				}

				// Free local references.
				(*env).DeleteLocalRef(vla);
			}
		}
	} else
	{
		// Attach the attribute to the supplied parent.

		// Find the Attribute class.
		jclass attrClass = (*env).GetObjectClass(parentAttr);
		if (attrClass != NULL)
		{
			// Get the method ID for addChild(Attribute child, Observer obs) method.
			jmethodID methodID = (*env).GetMethodID(attrClass, "addChild",
				"(Lcom/wizzer/mle/studio/framework/attribute/Attribute;Ljava/util/Observer;)Lcom/wizzer/mle/studio/framework/attribute/Attribute;");
			if (methodID != NULL)
			{
				// Call the addChild() method.
				jobject newChild = (*env).CallObjectMethod(parentAttr, methodID, attr, table);
				if (newChild != NULL)
				{
					rval = TRUE;
					// Free local references.
					(*env).DeleteLocalRef(newChild);
				}
			}

			// Free local references.
			(*env).DeleteLocalRef(attrClass);
		}
	}

	return rval;
}


static MlBoolean addAttributeTags(CDwpTableBuilder *builder, MleDwpItem *item, jobject attr)
{
	MlBoolean rval = FALSE;

	// Find the DwpItemAttribute class.
	jclass dwpItemAttributeClass = (*(builder->m_env)).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpItemAttribute");
	if (dwpItemAttributeClass != NULL)
	{
		// Get the method ID for the addTag(String tag) method.
		jmethodID mid = (*(builder->m_env)).GetMethodID(dwpItemAttributeClass, "addTag", "(Ljava/lang/String;)Z");
		if (mid != NULL)
		{
			if (item->hasAnyTag() == TRUE)
			{
				int count = 0;
				const char *nextTag = item->getNthTag(count++);
				while (nextTag != NULL)
				{
					// Add tag to Attribute.
					jstring tagStr = (*(builder->m_env)).NewStringUTF(nextTag);
					if (tagStr != NULL)
					{
						jboolean result = (*(builder->m_env)).CallBooleanMethod(
							attr, mid, tagStr);
					} else
					{
						rval = FALSE;
						goto endTagProcessing;
					}

					// Get next tag.
					nextTag = item->getNthTag(count++);
				}
				rval = TRUE;
			} else
				rval = TRUE;
		}
	}

endTagProcessing:
	return rval;
}


static jobject createNameAttribute(CDwpTableBuilder *builder, MleDwpItem *item, jclass clazz)
{
	jobject attr = NULL;
	JNIEnv *env = builder->m_env;

	// Get the method ID for the DwpNameAttribute(String name,boolean isReadOnly) constructor.
	jmethodID cid = (*env).GetMethodID(clazz, "<init>", "(Ljava/lang/String;Z)V");
	if (cid != NULL)
	{
		// Create the arguments for the constructor.
		jstring nameStr = NULL;
		if (item->isa(MleDwpMediaRefSource::typeId))
		{
			int flags = ((MleDwpMediaRefSource *)item)->getFlags();
			char buffer[20];
			itoa(flags, buffer, 10);

			nameStr = (*env).NewStringUTF(buffer);
		} else if (item->isa(MleDwpMediaRefTarget::typeId))
		{
			int flags = ((MleDwpMediaRefTarget *)item)->getFlags();
			char buffer[20];
			itoa(flags, buffer, 10);

			nameStr = (*env).NewStringUTF(buffer);
		} else
			nameStr = (*env).NewStringUTF(item->getName());
		jboolean isReadOnly = JNI_FALSE;
		if (nameStr != NULL)
		{
			// Construct the factory class.
			jobject dwpAttr = (*env).NewObject(clazz, cid, nameStr, isReadOnly);
			if (dwpAttr != NULL)
			{
				// Set return value.
				attr = dwpAttr;
			}

		}

		// Free local references.
		if (nameStr != NULL) (*env).DeleteLocalRef(nameStr);

	}

	return attr;
}


static jobject createNameTypeAttribute(CDwpTableBuilder *builder, MleDwpItem *item, jclass clazz)
{
	jobject attr = NULL;
	JNIEnv *env = builder->m_env;

	// Get the method ID for the DwpNameTypeAttribute(String name,String type,boolean isReadOnly) constructor.
	jmethodID cid = (*env).GetMethodID(clazz, "<init>", "(Ljava/lang/String;Ljava/lang/String;Z)V");
	if (cid != NULL)
	{
		// Create the arguments for the constructor.
		jstring nameStr = (*env).NewStringUTF(item->getName());

		jstring typeStr;
		if (item->isa(MleDwpSet::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpSet *)item)->getType());
		} else if (item->isa(MleDwpActor::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpActor *)item)->getActorClass());
		} else if (item->isa(MleDwpRoleBinding::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpRoleBinding *)item)->getSet());
		} else if (item->isa(MleDwpRoleAttachment::typeId))
		{
			if (nameStr != NULL) (*env).DeleteLocalRef(nameStr);
		    nameStr = (*env).NewStringUTF(((MleDwpRoleAttachment *)item)->getParent());
			typeStr = (*env).NewStringUTF(((MleDwpRoleAttachment *)item)->getChild());
		} else if (item->isa(MleDwpRoleSetMapping::typeId))
		{
			typeStr = (*env).NewStringUTF(((MleDwpRoleSetMapping *)item)->getSet());
		} else if (item->isa(MleDwpGroup::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpGroup *)item)->getGroupClass());
		} else if (item->isa(MleDwpPropertyDef::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpPropertyDef *)item)->getType());
		} else if (item->isa(MleDwpMediaRef::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpMediaRef *)item)->getMediaRefClass());
		} else if (item->isa(MleDwpMediaRefClass::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpMediaRefClass *)item)->getClassName());
		} else if (item->isa(MleDwpStage::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpStage *)item)->getStageClass());
		} else if (item->isa(MleDwpScene::typeId))
		{
		    typeStr = (*env).NewStringUTF(((MleDwpScene *)item)->getSceneClass());
		}


		jboolean isReadOnly = JNI_FALSE;
		if ((nameStr != NULL) && (typeStr != NULL))
		{
			// Construct the factory class.
			jobject dwpAttr = (*env).NewObject(clazz, cid, nameStr, typeStr, isReadOnly);
			if (dwpAttr != NULL)
			{
				// Set return value.
				attr = dwpAttr;
			}
		}

		// Free local references.
		if (nameStr != NULL) (*env).DeleteLocalRef(nameStr);
		if (typeStr != NULL) (*env).DeleteLocalRef(typeStr);

	}

	return attr;
}


static jobject getPropertyValue(JNIEnv *env, MleDwpProperty *item)
{
	jobject retValue;
	jclass propertyValueClass = NULL;
	const MleDwpDatatype *dataType = item->getDatatype();

	if (dataType->isa(MleDwpInt::typeId))
	{
		int value;
		dataType->get(&(item->m_data),&value);

		// Create an Integer object.

		// Find the Integer class.
		propertyValueClass = (*env).FindClass("java/lang/Integer");
		if (propertyValueClass != NULL)
		{
			// Get the method ID for the Integer(int value) constructor.
			jmethodID cid = (*env).GetMethodID(propertyValueClass, "<init>", "(I)V");
			if (cid != NULL)
			{
				retValue = (*env).NewObject(propertyValueClass, cid, value);
			}

			// Free local references.
			(*env).DeleteLocalRef(propertyValueClass);

		}

	} else if (dataType->isa(MleDwpFloat::typeId))
	{
		float value;
		dataType->get(&(item->m_data),&value);

		// Create a Float object.

		// Find the Float class.
		propertyValueClass = (*env).FindClass("java/lang/Float");
		if (propertyValueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(propertyValueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				retValue = (*env).NewObject(propertyValueClass, cid, value);
			}

			// Free local references.
			(*env).DeleteLocalRef(propertyValueClass);

		}

	} else if (dataType->isa(MleDwpString::typeId))
	{
		char *value;
		dataType->get(&(item->m_data),&value);

		// Create a String object.
		jstring strValue = (*env).NewStringUTF(value);

		// Find the String class.
		propertyValueClass = (*env).FindClass("java/lang/String");
		if (propertyValueClass != NULL)
		{
			// Get the method ID for the String(String value) constructor.
			jmethodID cid = (*env).GetMethodID(propertyValueClass, "<init>", "(Ljava/lang/String;)V");
			if (cid != NULL)
			{
				retValue = (*env).NewObject(propertyValueClass, cid, strValue);
			}

			// Free local references.
			(*env).DeleteLocalRef(propertyValueClass);

		}

		// Free local references.
		(*env).DeleteLocalRef(strValue);

	} else if (dataType->isa(MleDwpScalar::typeId))
	{
		MlScalar value;
		dataType->get(&(item->m_data),&value);

		// Create a Float object.
		float floatValue;
		SET_FLOAT_FROM_SCALAR(floatValue,&value);

		// Find the Float class.
		propertyValueClass = (*env).FindClass("java/lang/Float");
		if (propertyValueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(propertyValueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				retValue = (*env).NewObject(propertyValueClass, cid, floatValue);
			}

			// Free local references.
			(*env).DeleteLocalRef(propertyValueClass);

		}

	} else if (dataType->isa(MleDwpVector2::typeId))
	{
		MlVector2 value;
		dataType->get(&(item->m_data),&value);

		// Create an array of Float.
		float floatValue[2];
		SET_FLOAT_FROM_SCALAR(floatValue[0],&value[0]);
		SET_FLOAT_FROM_SCALAR(floatValue[1],&value[1]);

#if 0
		jfloatArray floatArray = (*env).NewFloatArray(2);
        (*env).SetFloatArrayRegion(floatArray,0,2,floatValue);

		retValue = floatArray;
#else
		jobjectArray floatArray = NULL;

		// Find the Float class.
		jclass valueClass = (*env).FindClass("java/lang/Float");
		if (valueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				// Create instances for the array.
				jobject v0 = (*env).NewObject(valueClass, cid, floatValue[0]);
				jobject v1 = (*env).NewObject(valueClass, cid, floatValue[1]);

				// Create the array and initialize it.
				floatArray = (*env).NewObjectArray(2,valueClass,NULL);
				(*env).SetObjectArrayElement(floatArray,0,v0);
				(*env).SetObjectArrayElement(floatArray,1,v1);
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);
		}

		retValue = floatArray;
#endif /* 0 */

	} else if (dataType->isa(MleDwpVector3::typeId))
	{
		MlVector3 value;
		dataType->get(&(item->m_data),&value);

		// Create an array of Float.
		float floatValue[3];
		SET_FLOAT_FROM_SCALAR(floatValue[0],&value[0]);
		SET_FLOAT_FROM_SCALAR(floatValue[1],&value[1]);
		SET_FLOAT_FROM_SCALAR(floatValue[2],&value[2]);

#if 0
		jfloatArray floatArray = (*env).NewFloatArray(3);
        (*env).SetFloatArrayRegion(floatArray,0,3,floatValue);

		retValue = floatArray;
#else
		jobjectArray floatArray = NULL;

		// Find the Float class.
		jclass valueClass = (*env).FindClass("java/lang/Float");
		if (valueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				// Create instances for the array.
				jobject v0 = (*env).NewObject(valueClass, cid, floatValue[0]);
				jobject v1 = (*env).NewObject(valueClass, cid, floatValue[1]);
				jobject v2 = (*env).NewObject(valueClass, cid, floatValue[2]);

				// Create the array and initialize it.
				floatArray = (*env).NewObjectArray(3,valueClass,NULL);
				(*env).SetObjectArrayElement(floatArray,0,v0);
				(*env).SetObjectArrayElement(floatArray,1,v1);
				(*env).SetObjectArrayElement(floatArray,2,v2);
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);
		}

		retValue = floatArray;
#endif /* 0 */

	} else if (dataType->isa(MleDwpVector4::typeId))
	{
		MlVector4 value;
		dataType->get(&(item->m_data),&value);

		// Create an array of Float.
		float floatValue[4];
		SET_FLOAT_FROM_SCALAR(floatValue[0],&value[0]);
		SET_FLOAT_FROM_SCALAR(floatValue[1],&value[1]);
		SET_FLOAT_FROM_SCALAR(floatValue[2],&value[2]);
		SET_FLOAT_FROM_SCALAR(floatValue[3],&value[3]);

#if 0
		jfloatArray floatArray = (*env).NewFloatArray(4);
        (*env).SetFloatArrayRegion(floatArray,0,4,floatValue);

		retValue = floatArray;
#else
		jobjectArray floatArray = NULL;

		// Find the Float class.
		jclass valueClass = (*env).FindClass("java/lang/Float");
		if (valueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				// Create instances for the array.
				jobject v0 = (*env).NewObject(valueClass, cid, floatValue[0]);
				jobject v1 = (*env).NewObject(valueClass, cid, floatValue[1]);
				jobject v2 = (*env).NewObject(valueClass, cid, floatValue[2]);
				jobject v3 = (*env).NewObject(valueClass, cid, floatValue[3]);

				// Create the array and initialize it.
				floatArray = (*env).NewObjectArray(4,valueClass,NULL);
				(*env).SetObjectArrayElement(floatArray,0,v0);
				(*env).SetObjectArrayElement(floatArray,1,v1);
				(*env).SetObjectArrayElement(floatArray,2,v2);
				(*env).SetObjectArrayElement(floatArray,3,v3);
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);
		}

		retValue = floatArray;
#endif /* 0 */

	} else if (dataType->isa(MleDwpIntArray::typeId))
	{
		MleArray<int> value;
		dataType->get(&(item->m_data),&value);

#if 0
		// Create an array of Integer.
		int size = value.size();
		jint *valueArray = new jint[size];
		for (int i = 0; i < size; i++)
			valueArray[i] = value[i];

		jintArray intArray = (*env).NewIntArray(size);
        (*env).SetIntArrayRegion(intArray,0,size,valueArray);

		// Clean up.
		delete [] valueArray;

	    retValue = intArray;
#else
		int size = value.size();
		jobjectArray intArray = NULL;

		// Find the Integer class.
		jclass valueClass = (*env).FindClass("java/lang/Integer");
		if (valueClass != NULL)
		{
			// Get the method ID for the Integer(int value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(I)V");
			if (cid != NULL)
			{
				// Create the array and initialize it.
				intArray = (*env).NewObjectArray(size,valueClass,NULL);
				for (int i = 0; i < size; i++)
				{
					jobject obj = (*env).NewObject(valueClass, cid, value[i]);
					(*env).SetObjectArrayElement(intArray,i,obj);
				}
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);
		}

		retValue = intArray;
#endif /* 0 */

	} else if (dataType->isa(MleDwpFloatArray::typeId))
	{
		MleArray<float> value ;
		dataType->get(&(item->m_data),&value);

#if 0
		// Create an array of Float.
		int size = value.size();
		jfloat *valueArray = new jfloat[size];
		for (int i = 0; i < size; i++)
			valueArray[i] = value[i];

		jfloatArray floatArray = (*env).NewFloatArray(size);
        (*env).SetFloatArrayRegion(floatArray,0,size,valueArray);

		// Clean up.
		delete [] valueArray;

	    retValue = floatArray;
#else
		int size = value.size();
		jobjectArray floatArray = NULL;

		// Find the Integer class.
		jclass valueClass = (*env).FindClass("java/lang/Float");
		if (valueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				// Create the array and initialize it.
				floatArray = (*env).NewObjectArray(size,valueClass,NULL);
				for (int i = 0; i < size; i++)
				{
					jobject obj = (*env).NewObject(valueClass, cid, value[i]);
					(*env).SetObjectArrayElement(floatArray,i,obj);
				}
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);
		}

		retValue = floatArray;
#endif /* 0 */

	} else if (dataType->isa(MleDwpRotation::typeId))
	{
		MlRotation value;
		dataType->get(&(item->m_data),&value);

		// Create an array of Float.
		float floatValue[4];
		SET_FLOAT_FROM_SCALAR(floatValue[0],&value[0]);
		SET_FLOAT_FROM_SCALAR(floatValue[1],&value[1]);
		SET_FLOAT_FROM_SCALAR(floatValue[2],&value[2]);
		SET_FLOAT_FROM_SCALAR(floatValue[3],&value[3]);

#if 0
		jfloatArray floatArray = (*env).NewFloatArray(4);
        (*env).SetFloatArrayRegion(floatArray,0,4,floatValue);
		
		retValue = floatArray;
#else
		jobjectArray rotation = NULL;

		// Find the Float class.
		jclass valueClass = (*env).FindClass("java/lang/Float");
		if (valueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				// Create instances for the array.
				jobject v0 = (*env).NewObject(valueClass, cid, floatValue[0]);
				jobject v1 = (*env).NewObject(valueClass, cid, floatValue[1]);
				jobject v2 = (*env).NewObject(valueClass, cid, floatValue[2]);
				jobject v3 = (*env).NewObject(valueClass, cid, floatValue[3]);

				// Create the array and initialize it.
				rotation = (*env).NewObjectArray(4,valueClass,NULL);
				(*env).SetObjectArrayElement(rotation,0,v0);
				(*env).SetObjectArrayElement(rotation,1,v1);
				(*env).SetObjectArrayElement(rotation,2,v2);
				(*env).SetObjectArrayElement(rotation,3,v3);
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);

		}

		retValue = rotation;
#endif /* 0 */

	} else if (dataType->isa(MleDwpTransform::typeId))
	{
		MlTransform value;
		dataType->get(&(item->m_data),&value);

		// Create an array of Float.
		float floatValue[12];
		for (int i = 0; i < 4; i++)
		{
			for (int j = 0; j < 3; j++)
			{
				SET_FLOAT_FROM_SCALAR(floatValue[j + (i * 3)],&value[i][j]);
			}
		}

#if 0
		jfloatArray floatArray = (*env).NewFloatArray(12);
        (*env).SetFloatArrayRegion(floatArray,0,12,floatValue);

		retValue = floatArray;
#else
		jobjectArray transform = NULL;

		// Find the Float class.
		jclass valueClass = (*env).FindClass("java/lang/Float");
		if (valueClass != NULL)
		{
			// Get the method ID for the Float(float value) constructor.
			jmethodID cid = (*env).GetMethodID(valueClass, "<init>", "(F)V");
			if (cid != NULL)
			{
				// Create instances for the array values.
				jobject v[12];
				v[0] = (*env).NewObject(valueClass, cid, floatValue[0]);
				v[1] = (*env).NewObject(valueClass, cid, floatValue[1]);
				v[2] = (*env).NewObject(valueClass, cid, floatValue[2]);
				v[3] = (*env).NewObject(valueClass, cid, floatValue[3]);
				v[4] = (*env).NewObject(valueClass, cid, floatValue[4]);
				v[5] = (*env).NewObject(valueClass, cid, floatValue[5]);
				v[6] = (*env).NewObject(valueClass, cid, floatValue[6]);
				v[7] = (*env).NewObject(valueClass, cid, floatValue[7]);
				v[8] = (*env).NewObject(valueClass, cid, floatValue[8]);
				v[9] = (*env).NewObject(valueClass, cid, floatValue[9]);
				v[10] = (*env).NewObject(valueClass, cid, floatValue[10]);
				v[11] = (*env).NewObject(valueClass, cid, floatValue[11]);


				// Create the 2-dimensional array and initialize it.
				jobjectArray row = (*env).NewObjectArray(3,valueClass,NULL);
				transform = (*env).NewObjectArray(4,(*env).GetObjectClass(row),NULL);
				int element = 0;
				for (int i = 0; i < 4; i++)
				{
					row = (*env).NewObjectArray(3,valueClass,NULL);
					(*env).SetObjectArrayElement(row,0,v[element++]);
					(*env).SetObjectArrayElement(row,1,v[element++]);
					(*env).SetObjectArrayElement(row,2,v[element++]);
					(*env).SetObjectArrayElement(transform,i,row);
				}
			}

			// Free local references.
			(*env).DeleteLocalRef(valueClass);

		}

		retValue = transform;
#endif /* 0 */

	} else
		retValue = NULL;

	return retValue;
}


static jobject createNameTypeValueAttribute(CDwpTableBuilder *builder, MleDwpProperty *item, jclass clazz)
{
	jobject attr = NULL;
	JNIEnv *env = builder->m_env;
	const MleDwpDatatype *dataType = item->getDatatype();

	jmethodID cid;
	if (dataType->isa(MleDwpInt::typeId))
	{
		// Get the method ID for the DwpIntPropertyAttribute(String name,String type,Integer value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Integer;Z)V");
	} else if (dataType->isa(MleDwpFloat::typeId))
	{
		// Get the method ID for the DwpFloatPropertyAttribute(String name,String type,Float value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpString::typeId))
	{
		// Get the method ID for the DwpStringPropertyAttribute(String name,String type,String value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V");
	} else if (dataType->isa(MleDwpScalar::typeId))
	{
		// Get the method ID for the DwpScalarPropertyAttribute(String name,String type,Float value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpVector2::typeId))
	{
		// Get the method ID for the DwpVector2PropertyAttribute(String name,String type,Float[] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpVector3::typeId))
	{
		// Get the method ID for the DwpVector3PropertyAttribute(String name,String type,Float[] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpVector4::typeId))
	{
		// Get the method ID for the DwpVector4PropertyAttribute(String name,String type,Float[] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpIntArray::typeId))
	{
		// Get the method ID for the DwpIntArrayPropertyAttribute(String name,String type,Integer[] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Integer;Z)V");
	} else if (dataType->isa(MleDwpFloatArray::typeId))
	{
		// Get the method ID for the DwpFloatArrayPropertyAttribute(String name,String type,Float[] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpRotation::typeId))
	{
		// Get the method ID for the DwpRotationPropertyAttribute(String name,String type,Float[] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[Ljava/lang/Float;Z)V");
	} else if (dataType->isa(MleDwpTransform::typeId))
	{
		// Get the method ID for the DwpTransformPropertyAttribute(String name,String type,Float[][] value,boolean isReadOnly) constructor.
		cid = (*env).GetMethodID(clazz, "<init>",
			"(Ljava/lang/String;Ljava/lang/String;[[Ljava/lang/Float;Z)V");
	}

	if (cid != NULL)
	{
		// Create the arguments for the constructor.
		jstring nameStr = (*env).NewStringUTF(item->getName());
		const MleDwpDatatype *dataType = item->getDatatype();
		jstring typeStr = (*env).NewStringUTF(dataType->getName());
		jobject value = getPropertyValue(env,item);
		jboolean isReadOnly = JNI_FALSE;
		if ((nameStr != NULL) && (typeStr != NULL) && (value != NULL))
		{
			// Construct the factory class.
			jobject dwpAttr = (*env).NewObject(clazz, cid, nameStr, typeStr, value, isReadOnly);
			if (dwpAttr != NULL)
			{
				// Set return value.
				attr = dwpAttr;
			}
		}

		// Free local references.
		if (nameStr != NULL) (*env).DeleteLocalRef(nameStr);
		if (typeStr != NULL) (*env).DeleteLocalRef(typeStr);
		if (value != NULL) (*env).DeleteLocalRef(value);

	}

	return attr;
}


static jobject createInclude(CDwpTableBuilder *builder, MleDwpInclude *item)
{
	jobject includeAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Include class.
	jclass includeClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpIncludeAttribute");
	if (includeClass != NULL)
	{
		// Create the Include Attribute.
		includeAttr = createNameAttribute(builder, item, includeClass);

		// Free local references.
		(*env).DeleteLocalRef(includeClass);

	}

	return includeAttr;
}


static jobject createHeaderFile(CDwpTableBuilder *builder, MleDwpHeaderFile *item)
{
	jobject headerFileAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the HeaderFile class.
	jclass headerFileClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpHeaderFileAttribute");
	if (headerFileClass != NULL)
	{
		// Create the HeaderFile Attribute.
		headerFileAttr = createNameAttribute(builder, item, headerFileClass);

		// Free local references.
		(*env).DeleteLocalRef(headerFileClass);

	}

	return headerFileAttr;
}


static jobject createSourceFile(CDwpTableBuilder *builder, MleDwpSourceFile *item)
{
	jobject sourceFileAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the SourceFile class.
	jclass sourceFileClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpSourceFileAttribute");
	if (sourceFileClass != NULL)
	{
		// Create the SourceFile Attribute.
		sourceFileAttr = createNameAttribute(builder, item, sourceFileClass);

		// Free local references.
		(*env).DeleteLocalRef(sourceFileClass);

	}

	return sourceFileAttr;
}


static jobject createPackage(CDwpTableBuilder *builder, MleDwpPackage *item)
{
	jobject packageAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Package class.
	jclass packageClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpPackageAttribute");
	if (packageClass != NULL)
	{
		// Create the Package Attribute.
		packageAttr = createNameAttribute(builder, item, packageClass);

		// Free local references.
		(*env).DeleteLocalRef(packageClass);

	}

	return packageAttr;
}


static jobject createDSOFile(CDwpTableBuilder *builder, MleDwpDSOFile *item)
{
	jobject dsoFileAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Include class.
	jclass dsoFileClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpDSOFileAttribute");
	if (dsoFileClass != NULL)
	{
		// Create the DSOFile Attribute.
		dsoFileAttr = createNameAttribute(builder, item, dsoFileClass);

		// The name of item will be ""; however, the item's value is really stored in the m_dsofile field.
		const char *dsoFile = item->getDSOFile();
		jstring nameStr = (*env).NewStringUTF(dsoFile);

		// Get the method ID for setValue(Object value) method.
		jmethodID methodID = (*env).GetMethodID(dsoFileClass, "setValue",
		    "(Ljava/lang/Object;)V");
		if (methodID != NULL)
		{
			(*env).CallVoidMethod(dsoFileAttr, methodID, nameStr);
		}

		// Free local references.
		(*env).DeleteLocalRef(nameStr);
		(*env).DeleteLocalRef(dsoFileClass);

	}

	return dsoFileAttr;
}


static jobject createStageDef(CDwpTableBuilder *builder, MleDwpStageDef *item)
{
	jobject stageDefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the StageDef class.
	jclass stageDefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpStageDefAttribute");
	if (stageDefClass != NULL)
	{
		// Create the StageDef Attribute.
		stageDefAttr = createNameAttribute(builder, item, stageDefClass);

		// Free local references.
		(*env).DeleteLocalRef(stageDefClass);

	}

	return stageDefAttr;
}


static jobject createActorDef(CDwpTableBuilder *builder, MleDwpActorDef *item)
{
	jobject actorDefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the ActorDef class.
	jclass actorDefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpActorDefAttribute");
	if (actorDefClass != NULL)
	{
		// Create the ActorDef Attribute.
		actorDefAttr = createNameAttribute(builder, item, actorDefClass);

		// Free local references.
		(*env).DeleteLocalRef(actorDefClass);

	}

	return actorDefAttr;
}


static jobject createRoleDef(CDwpTableBuilder *builder, MleDwpRoleDef *item)
{
	jobject roleDefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the RoleDef class.
	jclass roleDefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpRoleDefAttribute");
	if (roleDefClass != NULL)
	{
		// Create the RoleDef Attribute.
		roleDefAttr = createNameAttribute(builder, item, roleDefClass);

		// Free local references.
		(*env).DeleteLocalRef(roleDefClass);

	}

	return roleDefAttr;
}

static jobject createSetDef(CDwpTableBuilder *builder, MleDwpSetDef *item)
{
	jobject setDefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the SetDef class.
	jclass setDefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpSetDefAttribute");
	if (setDefClass != NULL)
	{
		// Create the SetDef Attribute.
		setDefAttr = createNameAttribute(builder, item, setDefClass);

		// Free local references.
		(*env).DeleteLocalRef(setDefClass);

	}

	return setDefAttr;
}


static jobject createSet(CDwpTableBuilder *builder, MleDwpSet *item)
{
	jobject setAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Forum/Set class.
	jclass setClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpSetAttribute");
	if (setClass != NULL)
	{
		setAttr = createNameTypeAttribute(builder, item, setClass);

		// Free local references.
		(*env).DeleteLocalRef(setClass);

	}

	return setAttr;
}


static jobject createGroup(CDwpTableBuilder *builder, MleDwpGroup *item)
{
	jobject groupAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Group class.
	jclass groupClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpGroupAttribute");
	if (groupClass != NULL)
	{
		// Create the Group Attribute.
		groupAttr = createNameTypeAttribute(builder, item, groupClass);

		// Free local references.
		(*env).DeleteLocalRef(groupClass);

	}

	return groupAttr;
}


static jobject createGroupRef(CDwpTableBuilder *builder, MleDwpGroupRef *item)
{
	jobject grouprefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the GroupRef class.
	jclass grouprefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpGroupRefAttribute");
	if (grouprefClass != NULL)
	{
		// Create the Include Attribute.
		grouprefAttr = createNameAttribute(builder, item, grouprefClass);

		// Free local references.
		(*env).DeleteLocalRef(grouprefClass);

	}

	return grouprefAttr;
}


static jobject createActor(CDwpTableBuilder *builder, MleDwpActor *item)
{
	jobject actorAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Actor class.
	jclass actorClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpActorAttribute");
	if (actorClass != NULL)
	{
		// Create the Actor Attribute.
		actorAttr = createNameTypeAttribute(builder, item, actorClass);

		// Free local references.
		(*env).DeleteLocalRef(actorClass);

	}

	return actorAttr;
}


static jobject createRoleBinding(CDwpTableBuilder *builder, MleDwpRoleBinding *item)
{
	jobject roleBindingAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the RoleBinding class.
	jclass roleBindingClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpRoleBindingAttribute");
	if (roleBindingClass != NULL)
	{
		// Create the RoleBinding Attribute.
		roleBindingAttr = createNameTypeAttribute(builder, item, roleBindingClass);

		// Free local references.
		(*env).DeleteLocalRef(roleBindingClass);

	}

	return roleBindingAttr;
}


static jobject createRoleAttachment(CDwpTableBuilder *builder, MleDwpRoleAttachment *item)
{
	jobject roleAttachmentAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the RoleAttachment class.
	jclass roleAttachmentClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpRoleAttachmentAttribute");
	if (roleAttachmentClass != NULL)
	{
		// Create the RoleAttachment Attribute.
		roleAttachmentAttr = createNameTypeAttribute(builder, item, roleAttachmentClass);

		// Free local references.
		(*env).DeleteLocalRef(roleAttachmentClass);

	}

	return roleAttachmentAttr;
}


static jobject createRoleSetMapping(CDwpTableBuilder *builder, MleDwpRoleSetMapping *item)
{
	jobject roleSetMappingAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the RoleSetMapping class.
	jclass roleSetMappingClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpRoleSetMappingAttribute");
	if (roleSetMappingClass != NULL)
	{
		// Create the RoleSetMapping Attribute.
		roleSetMappingAttr = createNameTypeAttribute(builder, item, roleSetMappingClass);

		// Free local references.
		(*env).DeleteLocalRef(roleSetMappingClass);

	}

	return roleSetMappingAttr;
}


static jobject createPropertyDef(CDwpTableBuilder *builder, MleDwpPropertyDef *item)
{
	jobject propertyDefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the PropertyDef class.
	jclass propertyDefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpPropertyDefAttribute");
	if (propertyDefClass != NULL)
	{
		// Create the PropertyDef Attribute.
		propertyDefAttr = createNameTypeAttribute(builder, item, propertyDefClass);

		// Free local references.
		(*env).DeleteLocalRef(propertyDefClass);

	}

	return propertyDefAttr;
}


static jobject createProperty(CDwpTableBuilder *builder, MleDwpProperty *item)
{
	jobject propertyAttr = NULL;
	JNIEnv *env = builder->m_env;
	const MleDwpDatatype *dataType = item->getDatatype();

	jclass propertyClass = NULL;
	if (dataType != NULL)
	{
		if (dataType->isa(MleDwpInt::typeId))
		{
			// Find the integer property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpIntPropertyAttribute");
		} else if (dataType->isa(MleDwpFloat::typeId))
		{
			// Find the float property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpFloatPropertyAttribute");
		} else if (dataType->isa(MleDwpString::typeId))
		{
			// Find the string property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpStringPropertyAttribute");
		} else if (dataType->isa(MleDwpScalar::typeId))
		{
			// Find the MlScalar property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpScalarPropertyAttribute");
		} else if (dataType->isa(MleDwpVector2::typeId))
		{
			// Find the MlVector2 property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpVector2PropertyAttribute");
		} else if (dataType->isa(MleDwpVector3::typeId))
		{
			// Find the MlVector3 property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpVector3PropertyAttribute");
		} else if (dataType->isa(MleDwpVector4::typeId))
		{
			// Find the MlVector4 property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpVector4PropertyAttribute");
		} else if (dataType->isa(MleDwpIntArray::typeId))
		{
			// Find the IntArray property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpIntArrayPropertyAttribute");
		} else if (dataType->isa(MleDwpFloatArray::typeId))
		{
			// Find the FloatArray property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpFloatArrayPropertyAttribute");
		} else if (dataType->isa(MleDwpRotation::typeId))
		{
			// Find the MlRotation property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpRotationPropertyAttribute");
		} else if (dataType->isa(MleDwpTransform::typeId))
		{
			// Find the MlTransform property class.
			propertyClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpTransformPropertyAttribute");
		}
	}

	if (propertyClass != NULL)
	{
		// Create the Property Attribute.
		propertyAttr = createNameTypeValueAttribute(builder, item, propertyClass);

		// Free local references.
		(*env).DeleteLocalRef(propertyClass);

	}

	return propertyAttr;
}


static jobject createMediaRef(CDwpTableBuilder *builder, MleDwpMediaRef *item)
{
	jobject mediarefAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the MediaRef class.
	jclass mediarefClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpMediaRefAttribute");
	if (mediarefClass != NULL)
	{
		// Create the MediaRef Attribute.
		mediarefAttr = createNameTypeAttribute(builder, item, mediarefClass);

		// Free local references.
		(*env).DeleteLocalRef(mediarefClass);

	}

	return mediarefAttr;
}


static jobject createMediaRefSource(CDwpTableBuilder *builder, MleDwpMediaRefSource *item)
{
	jobject mediarefSourceAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the MediaRefSource class.
	jclass mediarefSourceClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpMediaRefSourceAttribute");
	if (mediarefSourceClass != NULL)
	{
		// Create the MediaRefSource Attribute.
		mediarefSourceAttr = createNameAttribute(builder, item, mediarefSourceClass);

		// Free local references.
		(*env).DeleteLocalRef(mediarefSourceClass);

	}

	return mediarefSourceAttr;
}


static jobject createMediaRefTarget(CDwpTableBuilder *builder, MleDwpMediaRefTarget *item)
{
	jobject mediarefTargetAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the MediaRefTarget class.
	jclass mediarefTargetClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpMediaRefTargetAttribute");
	if (mediarefTargetClass != NULL)
	{
		// Create the MediaRefTarget Attribute.
		mediarefTargetAttr = createNameAttribute(builder, item, mediarefTargetClass);

		// Free local references.
		(*env).DeleteLocalRef(mediarefTargetClass);

	}

	return mediarefTargetAttr;
}


static jobject createMedia(CDwpTableBuilder *builder, MleDwpMedia *item)
{
	jobject mediaAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Media class.
	jclass mediaClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpMediaAttribute");
	if (mediaClass != NULL)
	{
		// Get the method ID for the DwpMediaAttribute(Integer flags,String label,String filename,boolean isReadOnly) constructor.
		jmethodID cid = (*env).GetMethodID(mediaClass, "<init>",
			"(Ljava/lang/Integer;Ljava/lang/String;Ljava/lang/String;Z)V");

		if (cid != NULL)
		{
			// Create the arguments for the constructor.

			jobject flags = NULL;
			jclass intClass = (*env).FindClass("java/lang/Integer");
			if (intClass != NULL)
			{
				// Get the method ID for the Integer(int value) constructor.
				jmethodID cid = (*env).GetMethodID(intClass, "<init>", "(I)V");
				if (cid != NULL)
				{
					flags = (*env).NewObject(intClass, cid, item->getFlags());
				}

				// Free local references.
				(*env).DeleteLocalRef(intClass);

			}

			jstring labelStr = (*env).NewStringUTF(item->getLabel());
			jstring urlStr = (*env).NewStringUTF(item->getFilename());
			jboolean isReadOnly = JNI_FALSE;

			if ((flags != NULL) && (labelStr != NULL) && (urlStr != NULL))
			{
				// Construct the factory class.
				jobject dwpAttr = (*env).NewObject(mediaClass, cid, flags, labelStr, urlStr, isReadOnly);
				if (dwpAttr != NULL)
				{
					// Set return value.
					mediaAttr = dwpAttr;
				}
			}

			// Free local references.
			if (labelStr != NULL) (*env).DeleteLocalRef(labelStr);
			if (urlStr != NULL) (*env).DeleteLocalRef(urlStr);
			if (flags != NULL) (*env).DeleteLocalRef(flags);

		}

		// Free local references.
		(*env).DeleteLocalRef(mediaClass);

	}

	return mediaAttr;
}


static jobject createMediaRefClass(CDwpTableBuilder *builder, MleDwpMediaRefClass *item)
{
	jobject mediarefClassAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the MediaRefClass class.
	jclass mediarefClassClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpMediaRefClassAttribute");
	if (mediarefClassClass != NULL)
	{
		mediarefClassAttr = createNameTypeAttribute(builder, item, mediarefClassClass);

		// Free local references.
		(*env).DeleteLocalRef(mediarefClassClass);

	}

	return mediarefClassAttr;
}


static jobject createStage(CDwpTableBuilder *builder, MleDwpStage *item)
{
	jobject stageAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Stage class.
	jclass stageClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpStageAttribute");
	if (stageClass != NULL)
	{
		stageAttr = createNameTypeAttribute(builder, item, stageClass);

		// Free local references.
		(*env).DeleteLocalRef(stageClass);

	}

	return stageAttr;
}


static jobject createScene(CDwpTableBuilder *builder, MleDwpScene *item)
{
	jobject sceneAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Scene class.
	jclass sceneClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpSceneAttribute");
	if (sceneClass != NULL)
	{
		sceneAttr = createNameTypeAttribute(builder, item, sceneClass);

		// Free local references.
		(*env).DeleteLocalRef(sceneClass);

	}

	return sceneAttr;
}


static jobject createBoot(CDwpTableBuilder *builder, MleDwpBoot *item)
{
	jobject bootAttr = NULL;
	JNIEnv *env = builder->m_env;

	// Find the Boot class.
	jclass bootClass = (*env).FindClass("com/wizzer/mle/studio/dwp/attribute/DwpBootAttribute");
	if (bootClass != NULL)
	{
		// Create the Include Attribute.
		bootAttr = createNameAttribute(builder, item, bootClass);

		// Free local references.
		(*env).DeleteLocalRef(bootClass);

	}

	return bootAttr;
}


CDwpTableBuilder::CDwpTableBuilder(JNIEnv *env,MleDwpItem *root)
  :m_root(root), m_env(env)
{
	m_dwpTableClass = NULL;
	m_dwpTable = NULL;
	m_currentAttr = NULL;
}


CDwpTableBuilder::~CDwpTableBuilder()
{
	if (m_dwpTableClass != NULL)
		// Free local reference.
		(*m_env).DeleteLocalRef(m_dwpTableClass);
	if (m_dwpTable != NULL)
		// Free local reference.
		(*m_env).DeleteLocalRef(m_dwpTable);

}


MlBoolean CDwpTableBuilder::execute(CDwpTableBuilder *builder, MleDwpItem *item, jobject parentAttr)
{
	MLE_ASSERT(builder);
	MLE_ASSERT(item);

	MlBoolean rval = FALSE;
	jobject attr = NULL;

	// Process the DWP item.
	rval = TRUE;
	if (item->isa(MleDwpSet::typeId))
	{
		// Create the Set attribute.
		attr = createSet(builder, (MleDwpSet *)item);
	} else if (item->isa(MleDwpActorDef::typeId))
	{
		// Create the ActorDef attribute.
		attr = createActorDef(builder, (MleDwpActorDef *)item);
	} else if (item->isa(MleDwpRoleDef::typeId))
	{
		// Create the RoleDef attribute.
		attr = createRoleDef(builder, (MleDwpRoleDef *)item);
	} else if (item->isa(MleDwpSetDef::typeId))
	{
		// Create the SetDef attribute.
		attr = createSetDef(builder, (MleDwpSetDef *)item);
	} else if (item->isa(MleDwpPropertyDef::typeId))
	{
		// Create the PropertyDef attribute.
		attr = createPropertyDef(builder, (MleDwpPropertyDef *)item);
	} else if (item->isa(MleDwpInclude::typeId))
	{
		// Create the Include attribute.
		attr = createInclude(builder, (MleDwpInclude *)item);
	} else if (item->isa(MleDwpDSOFile::typeId))
	{
		// Create the DSOFile attribute.
		attr = createDSOFile(builder, (MleDwpDSOFile *)item);
	} else if (item->isa(MleDwpHeaderFile::typeId))
	{
		// Create the HeaderFile attribute.
		attr = createHeaderFile(builder, (MleDwpHeaderFile *)item);
	} else if (item->isa(MleDwpSourceFile::typeId))
	{
		// Create the SourceFile attribute.
		attr = createSourceFile(builder, (MleDwpSourceFile *)item);
	} else if (item->isa(MleDwpPackage::typeId))
	{
		// Create the Package attribute.
		attr = createPackage(builder, (MleDwpPackage *)item);
	} else if (item->isa(MleDwpRoleBinding::typeId))
	{
		// Create the RoleBinding attribute.
		attr = createRoleBinding(builder, (MleDwpRoleBinding *)item);
	} else if (item->isa(MleDwpRoleAttachment::typeId))
	{
		// Create the RoleAttachment attribute.
		attr = createRoleAttachment(builder, (MleDwpRoleAttachment *)item);
	} else if (item->isa(MleDwpRoleSetMapping::typeId))
	{
		// Create the RoleSetMapping attribute.
		attr = createRoleSetMapping(builder, (MleDwpRoleSetMapping *)item);
	} else if (item->isa(MleDwpActor::typeId))
	{
		// Create the Actor attribute.
		attr = createActor(builder, (MleDwpActor *)item);
	} else if (item->isa(MleDwpGroup::typeId))
	{
		// Create the Group attribute.
		attr = createGroup(builder, (MleDwpGroup *)item);
	} else if (item->isa(MleDwpProperty::typeId))
	{
		// Create the Property attribute.
		attr = createProperty(builder, (MleDwpProperty *)item);
	} else if (item->isa(MleDwpMediaRef::typeId))
	{
		// Create the MediaRef attribute.
		attr = createMediaRef(builder, (MleDwpMediaRef *)item);
	} else if (item->isa(MleDwpMediaRefSource::typeId))
	{
		// Create the MediaRefSource attribute.
		attr = createMediaRefSource(builder, (MleDwpMediaRefSource *)item);
	} else if (item->isa(MleDwpMediaRefTarget::typeId))
	{
		// Create the MediaRefTarget attribute.
		attr = createMediaRefTarget(builder, (MleDwpMediaRefTarget *)item);
	} else if (item->isa(MleDwpMedia::typeId))
	{
		// Create the Media attribute.
		attr = createMedia(builder, (MleDwpMedia *)item);
	} else if (item->isa(MleDwpMediaRefClass::typeId))
	{
		// Create the MediaRefClass attribute.
		attr = createMediaRefClass(builder, (MleDwpMediaRefClass *)item);
	} else if (item->isa(MleDwpStageDef::typeId))
	{
		// Create the StageDef attribute.
		attr = createStageDef(builder, (MleDwpStageDef *)item);
	} else if (item->isa(MleDwpStage::typeId))
	{
		// Create the Stage attribute.
		attr = createStage(builder, (MleDwpStage *)item);
	} else if (item->isa(MleDwpScene::typeId))
	{
		// Create the Scene attribute.
		attr = createScene(builder, (MleDwpScene *)item);
	} else if (item->isa(MleDwpBoot::typeId))
	{
		// Create the Boot attribute.
		attr = createBoot(builder, (MleDwpBoot *)item);
	} else if (item->isa(MleDwpGroupRef::typeId))
	{
		// Create the GroupRef attribute.
		attr = createGroupRef(builder, (MleDwpGroupRef *)item);
	}

	// Add the Attribute to the DWP domain table.
	if (attr != NULL)
	{
		addAttribute(builder, attr, parentAttr);

		// Add the tags to the attribute.
		addAttributeTags(builder, item, attr);
	}

	/* Recurse over children. */
	MleDwpItem *child = item->getFirstChild();
	while ( child )
	{
		/* Build up child attributes */
		if ( CDwpTableBuilder::execute(builder,child,attr) )
		{
			rval = TRUE;
		}

		child = child->getNext();
	}

	return rval;
}


MlBoolean CDwpTableBuilder::init()
{
	// Find the DwpTable class.
	m_dwpTableClass = (*m_env).FindClass("com/wizzer/mle/studio/dwp/domain/DwpTable");
	if (m_dwpTableClass == NULL)
	{
		m_dwpTable = NULL; /* exception thrown */
	} else
	{
		// Get the method ID for the DwpTable() constructor.
		jmethodID cid = (*m_env).GetMethodID(m_dwpTableClass,
			"<init>", "()V");
		if (cid == NULL)
		{
			m_dwpTable = NULL; /* exception thrown */
		} else
		{
			// Finally, create the new DwpTable.
			jobject dwpTable = (*m_env).NewObject(m_dwpTableClass, cid);
			if (dwpTable != NULL)
			{
				// Set return value.
				m_dwpTable = dwpTable;
			} else
				m_dwpTable = NULL;
		}
	}

	if (m_dwpTable != NULL)
		return TRUE;
	else
		return FALSE;
}


jobject CDwpTableBuilder::run()
{
	// Initialize the builder.
	if (! init())
		return m_dwpTable;

	// Build the domain table.
	if (! execute(this,m_root,NULL))
	{
		if (m_dwpTable != NULL)
		{
		    (*m_env).DeleteLocalRef(m_dwpTable);
			m_dwpTable = NULL;
		}

		if (m_currentAttr != NULL)
		{
			(*m_env).DeleteLocalRef(m_currentAttr);
			m_currentAttr = NULL;
		}

		return m_dwpTable;
	}

	// Return the domain table.
	return m_dwpTable;
}
