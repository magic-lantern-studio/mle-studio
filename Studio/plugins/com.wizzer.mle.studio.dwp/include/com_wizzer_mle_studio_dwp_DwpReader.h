/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class com_wizzer_mle_studio_dwp_DwpReader */

#ifndef _Included_com_wizzer_mle_studio_dwp_DwpReader
#define _Included_com_wizzer_mle_studio_dwp_DwpReader
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     com_wizzer_mle_studio_dwp_DwpReader
 * Method:    buildTableFromNativeDwp
 * Signature: (Ljava/lang/String;)Lcom/wizzer/mle/studio/dwp/domain/DwpTable;
 */
JNIEXPORT jobject JNICALL Java_com_wizzer_mle_studio_dwp_DwpReader_buildTableFromNativeDwp
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_wizzer_mle_studio_dwp_DwpReader
 * Method:    buildArrayFromNativeDwp
 * Signature: (Ljava/lang/String;)[B
 */
JNIEXPORT jbyteArray JNICALL Java_com_wizzer_mle_studio_dwp_DwpReader_buildArrayFromNativeDwp
  (JNIEnv *, jobject, jstring);

/*
 * Class:     com_wizzer_mle_studio_dwp_DwpReader
 * Method:    buildTableFromNativeData
 * Signature: ([B)Lcom/wizzer/mle/studio/dwp/domain/DwpTable;
 */
JNIEXPORT jobject JNICALL Java_com_wizzer_mle_studio_dwp_DwpReader_buildTableFromNativeData
  (JNIEnv *, jobject, jbyteArray);

#ifdef __cplusplus
}
#endif
#endif
