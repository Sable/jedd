/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 1.3.17u-20030723-1531
 * 
 * This file is not intended to be easily readable and contains a number of 
 * coding conventions designed to improve portability and efficiency. Do not make
 * changes to this file unless you know what you are doing--modify the SWIG 
 * interface file instead. 
 * ----------------------------------------------------------------------------- */


#if defined(__GNUC__)
    typedef long long __int64; /*For gcc on Windows */
#endif
#include <jni.h>
#include <stdlib.h>
#include <string.h>


/* Support for throwing Java exceptions */
typedef enum {
  SWIG_JavaOutOfMemoryError = 1, 
  SWIG_JavaIOException, 
  SWIG_JavaRuntimeException, 
  SWIG_JavaIndexOutOfBoundsException,
  SWIG_JavaArithmeticException,
  SWIG_JavaIllegalArgumentException,
  SWIG_JavaNullPointerException,
  SWIG_JavaUnknownError
} SWIG_JavaExceptionCodes;

typedef struct {
  SWIG_JavaExceptionCodes code;
  const char *java_exception;
} SWIG_JavaExceptions_t;

#if defined(SWIG_NOINCLUDE)
void SWIG_JavaThrowException(JNIEnv *jenv, SWIG_JavaExceptionCodes code, const char *msg);
#else


void SWIG_JavaThrowException(JNIEnv *jenv, SWIG_JavaExceptionCodes code, const char *msg) {
  jclass excep;
  static const SWIG_JavaExceptions_t java_exceptions[] = {
    { SWIG_JavaOutOfMemoryError, "java/lang/OutOfMemoryError" },
    { SWIG_JavaIOException, "java/io/IOException" },
    { SWIG_JavaRuntimeException, "java/lang/RuntimeException" },
    { SWIG_JavaIndexOutOfBoundsException, "java/lang/IndexOutOfBoundsException" },
    { SWIG_JavaArithmeticException, "java/lang/ArithmeticException" },
    { SWIG_JavaIllegalArgumentException, "java/lang/IllegalArgumentException" },
    { SWIG_JavaNullPointerException, "java/lang/NullPointerException" },
    { SWIG_JavaUnknownError,  "java/lang/UnknownError" },
    { (SWIG_JavaExceptionCodes)0,  "java/lang/UnknownError" } };
  const SWIG_JavaExceptions_t *except_ptr = java_exceptions;

  while (except_ptr->code != code && except_ptr->code)
    except_ptr++;

  (*jenv)->ExceptionClear(jenv);
  excep = (*jenv)->FindClass(jenv, except_ptr->java_exception);
  if (excep)
    (*jenv)->ThrowNew(jenv, excep, msg);
}


#endif


#if defined(SWIG_NOINCLUDE) || defined(SWIG_NOARRAYS)


int SWIG_JavaArrayInSchar (JNIEnv *jenv, jbyte **jarr, signed char **carr, jbyteArray input);
void SWIG_JavaArrayArgoutSchar (JNIEnv *jenv, jbyte *jarr, signed char *carr, jbyteArray input);
jbyteArray SWIG_JavaArrayOutSchar (JNIEnv *jenv, signed char *result, jsize sz);


int SWIG_JavaArrayInUchar (JNIEnv *jenv, jshort **jarr, unsigned char **carr, jshortArray input);
void SWIG_JavaArrayArgoutUchar (JNIEnv *jenv, jshort *jarr, unsigned char *carr, jshortArray input);
jshortArray SWIG_JavaArrayOutUchar (JNIEnv *jenv, unsigned char *result, jsize sz);


int SWIG_JavaArrayInShort (JNIEnv *jenv, jshort **jarr, short **carr, jshortArray input);
void SWIG_JavaArrayArgoutShort (JNIEnv *jenv, jshort *jarr, short *carr, jshortArray input);
jshortArray SWIG_JavaArrayOutShort (JNIEnv *jenv, short *result, jsize sz);


int SWIG_JavaArrayInUshort (JNIEnv *jenv, jint **jarr, unsigned short **carr, jintArray input);
void SWIG_JavaArrayArgoutUshort (JNIEnv *jenv, jint *jarr, unsigned short *carr, jintArray input);
jintArray SWIG_JavaArrayOutUshort (JNIEnv *jenv, unsigned short *result, jsize sz);


int SWIG_JavaArrayInInt (JNIEnv *jenv, jint **jarr, int **carr, jintArray input);
void SWIG_JavaArrayArgoutInt (JNIEnv *jenv, jint *jarr, int *carr, jintArray input);
jintArray SWIG_JavaArrayOutInt (JNIEnv *jenv, int *result, jsize sz);


int SWIG_JavaArrayInUint (JNIEnv *jenv, jlong **jarr, unsigned int **carr, jlongArray input);
void SWIG_JavaArrayArgoutUint (JNIEnv *jenv, jlong *jarr, unsigned int *carr, jlongArray input);
jlongArray SWIG_JavaArrayOutUint (JNIEnv *jenv, unsigned int *result, jsize sz);


int SWIG_JavaArrayInLong (JNIEnv *jenv, jint **jarr, long **carr, jintArray input);
void SWIG_JavaArrayArgoutLong (JNIEnv *jenv, jint *jarr, long *carr, jintArray input);
jintArray SWIG_JavaArrayOutLong (JNIEnv *jenv, long *result, jsize sz);


int SWIG_JavaArrayInUlong (JNIEnv *jenv, jlong **jarr, unsigned long **carr, jlongArray input);
void SWIG_JavaArrayArgoutUlong (JNIEnv *jenv, jlong *jarr, unsigned long *carr, jlongArray input);
jlongArray SWIG_JavaArrayOutUlong (JNIEnv *jenv, unsigned long *result, jsize sz);


int SWIG_JavaArrayInLonglong (JNIEnv *jenv, jlong **jarr, jlong **carr, jlongArray input);
void SWIG_JavaArrayArgoutLonglong (JNIEnv *jenv, jlong *jarr, jlong *carr, jlongArray input);
jlongArray SWIG_JavaArrayOutLonglong (JNIEnv *jenv, jlong *result, jsize sz);


int SWIG_JavaArrayInFloat (JNIEnv *jenv, jfloat **jarr, float **carr, jfloatArray input);
void SWIG_JavaArrayArgoutFloat (JNIEnv *jenv, jfloat *jarr, float *carr, jfloatArray input);
jfloatArray SWIG_JavaArrayOutFloat (JNIEnv *jenv, float *result, jsize sz);


int SWIG_JavaArrayInDouble (JNIEnv *jenv, jdouble **jarr, double **carr, jdoubleArray input);
void SWIG_JavaArrayArgoutDouble (JNIEnv *jenv, jdouble *jarr, double *carr, jdoubleArray input);
jdoubleArray SWIG_JavaArrayOutDouble (JNIEnv *jenv, double *result, jsize sz);


#else


/* signed char[] support */
int SWIG_JavaArrayInSchar (JNIEnv *jenv, jbyte **jarr, signed char **carr, jbyteArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetByteArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (signed char*) calloc(sz, sizeof(signed char)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (signed char)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutSchar (JNIEnv *jenv, jbyte *jarr, signed char *carr, jbyteArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jbyte)carr[i];
  (*jenv)->ReleaseByteArrayElements(jenv, input, jarr, 0);
}

jbyteArray SWIG_JavaArrayOutSchar (JNIEnv *jenv, signed char *result, jsize sz) {
  jbyte *arr;
  int i;
  jbyteArray jresult = (*jenv)->NewByteArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetByteArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jbyte)result[i];
  (*jenv)->ReleaseByteArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* unsigned char[] support */
int SWIG_JavaArrayInUchar (JNIEnv *jenv, jshort **jarr, unsigned char **carr, jshortArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetShortArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (unsigned char*) calloc(sz, sizeof(unsigned char)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (unsigned char)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutUchar (JNIEnv *jenv, jshort *jarr, unsigned char *carr, jshortArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jshort)carr[i];
  (*jenv)->ReleaseShortArrayElements(jenv, input, jarr, 0);
}

jshortArray SWIG_JavaArrayOutUchar (JNIEnv *jenv, unsigned char *result, jsize sz) {
  jshort *arr;
  int i;
  jshortArray jresult = (*jenv)->NewShortArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetShortArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jshort)result[i];
  (*jenv)->ReleaseShortArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* short[] support */
int SWIG_JavaArrayInShort (JNIEnv *jenv, jshort **jarr, short **carr, jshortArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetShortArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (short*) calloc(sz, sizeof(short)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (short)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutShort (JNIEnv *jenv, jshort *jarr, short *carr, jshortArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jshort)carr[i];
  (*jenv)->ReleaseShortArrayElements(jenv, input, jarr, 0);
}

jshortArray SWIG_JavaArrayOutShort (JNIEnv *jenv, short *result, jsize sz) {
  jshort *arr;
  int i;
  jshortArray jresult = (*jenv)->NewShortArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetShortArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jshort)result[i];
  (*jenv)->ReleaseShortArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* unsigned short[] support */
int SWIG_JavaArrayInUshort (JNIEnv *jenv, jint **jarr, unsigned short **carr, jintArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetIntArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (unsigned short*) calloc(sz, sizeof(unsigned short)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (unsigned short)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutUshort (JNIEnv *jenv, jint *jarr, unsigned short *carr, jintArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jint)carr[i];
  (*jenv)->ReleaseIntArrayElements(jenv, input, jarr, 0);
}

jintArray SWIG_JavaArrayOutUshort (JNIEnv *jenv, unsigned short *result, jsize sz) {
  jint *arr;
  int i;
  jintArray jresult = (*jenv)->NewIntArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetIntArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jint)result[i];
  (*jenv)->ReleaseIntArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* int[] support */
int SWIG_JavaArrayInInt (JNIEnv *jenv, jint **jarr, int **carr, jintArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetIntArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (int*) calloc(sz, sizeof(int)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (int)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutInt (JNIEnv *jenv, jint *jarr, int *carr, jintArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jint)carr[i];
  (*jenv)->ReleaseIntArrayElements(jenv, input, jarr, 0);
}

jintArray SWIG_JavaArrayOutInt (JNIEnv *jenv, int *result, jsize sz) {
  jint *arr;
  int i;
  jintArray jresult = (*jenv)->NewIntArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetIntArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jint)result[i];
  (*jenv)->ReleaseIntArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* unsigned int[] support */
int SWIG_JavaArrayInUint (JNIEnv *jenv, jlong **jarr, unsigned int **carr, jlongArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetLongArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (unsigned int*) calloc(sz, sizeof(unsigned int)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (unsigned int)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutUint (JNIEnv *jenv, jlong *jarr, unsigned int *carr, jlongArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jlong)carr[i];
  (*jenv)->ReleaseLongArrayElements(jenv, input, jarr, 0);
}

jlongArray SWIG_JavaArrayOutUint (JNIEnv *jenv, unsigned int *result, jsize sz) {
  jlong *arr;
  int i;
  jlongArray jresult = (*jenv)->NewLongArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetLongArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jlong)result[i];
  (*jenv)->ReleaseLongArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* long[] support */
int SWIG_JavaArrayInLong (JNIEnv *jenv, jint **jarr, long **carr, jintArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetIntArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (long*) calloc(sz, sizeof(long)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (long)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutLong (JNIEnv *jenv, jint *jarr, long *carr, jintArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jint)carr[i];
  (*jenv)->ReleaseIntArrayElements(jenv, input, jarr, 0);
}

jintArray SWIG_JavaArrayOutLong (JNIEnv *jenv, long *result, jsize sz) {
  jint *arr;
  int i;
  jintArray jresult = (*jenv)->NewIntArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetIntArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jint)result[i];
  (*jenv)->ReleaseIntArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* unsigned long[] support */
int SWIG_JavaArrayInUlong (JNIEnv *jenv, jlong **jarr, unsigned long **carr, jlongArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetLongArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (unsigned long*) calloc(sz, sizeof(unsigned long)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (unsigned long)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutUlong (JNIEnv *jenv, jlong *jarr, unsigned long *carr, jlongArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jlong)carr[i];
  (*jenv)->ReleaseLongArrayElements(jenv, input, jarr, 0);
}

jlongArray SWIG_JavaArrayOutUlong (JNIEnv *jenv, unsigned long *result, jsize sz) {
  jlong *arr;
  int i;
  jlongArray jresult = (*jenv)->NewLongArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetLongArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jlong)result[i];
  (*jenv)->ReleaseLongArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* jlong[] support */
int SWIG_JavaArrayInLonglong (JNIEnv *jenv, jlong **jarr, jlong **carr, jlongArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetLongArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (jlong*) calloc(sz, sizeof(jlong)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (jlong)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutLonglong (JNIEnv *jenv, jlong *jarr, jlong *carr, jlongArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jlong)carr[i];
  (*jenv)->ReleaseLongArrayElements(jenv, input, jarr, 0);
}

jlongArray SWIG_JavaArrayOutLonglong (JNIEnv *jenv, jlong *result, jsize sz) {
  jlong *arr;
  int i;
  jlongArray jresult = (*jenv)->NewLongArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetLongArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jlong)result[i];
  (*jenv)->ReleaseLongArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* float[] support */
int SWIG_JavaArrayInFloat (JNIEnv *jenv, jfloat **jarr, float **carr, jfloatArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetFloatArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (float*) calloc(sz, sizeof(float)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (float)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutFloat (JNIEnv *jenv, jfloat *jarr, float *carr, jfloatArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jfloat)carr[i];
  (*jenv)->ReleaseFloatArrayElements(jenv, input, jarr, 0);
}

jfloatArray SWIG_JavaArrayOutFloat (JNIEnv *jenv, float *result, jsize sz) {
  jfloat *arr;
  int i;
  jfloatArray jresult = (*jenv)->NewFloatArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetFloatArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jfloat)result[i];
  (*jenv)->ReleaseFloatArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


/* double[] support */
int SWIG_JavaArrayInDouble (JNIEnv *jenv, jdouble **jarr, double **carr, jdoubleArray input) {
  int i;
  jsize sz;
  if (!input) {
    SWIG_JavaThrowException(jenv, SWIG_JavaNullPointerException, "null array");
    return 0;
  }
  sz = (*jenv)->GetArrayLength(jenv, input);
  *jarr = (*jenv)->GetDoubleArrayElements(jenv, input, 0);
  if (!*jarr)
    return 0; 
  *carr = (double*) calloc(sz, sizeof(double)); 
  if (!*carr) {
    SWIG_JavaThrowException(jenv, SWIG_JavaOutOfMemoryError, "array memory allocation failed");
    return 0;
  }
  for (i=0; i<sz; i++)
    (*carr)[i] = (double)(*jarr)[i];
  return 1;
}

void SWIG_JavaArrayArgoutDouble (JNIEnv *jenv, jdouble *jarr, double *carr, jdoubleArray input) {
  int i;
  jsize sz;
  sz = (*jenv)->GetArrayLength(jenv, input);
  for (i=0; i<sz; i++)
    jarr[i] = (jdouble)carr[i];
  (*jenv)->ReleaseDoubleArrayElements(jenv, input, jarr, 0);
}

jdoubleArray SWIG_JavaArrayOutDouble (JNIEnv *jenv, double *result, jsize sz) {
  jdouble *arr;
  int i;
  jdoubleArray jresult = (*jenv)->NewDoubleArray(jenv, sz);
  if (!jresult)
    return NULL;
  arr = (*jenv)->GetDoubleArrayElements(jenv, jresult, 0);
  if (!arr)
    return NULL;
  for (i=0; i<sz; i++)
    arr[i] = (jdouble)result[i];
  (*jenv)->ReleaseDoubleArrayElements(jenv, jresult, arr, 0);
  return jresult;
}


#endif


#define  SWIG_MemoryError    1
#define  SWIG_IOError        2
#define  SWIG_RuntimeError   3
#define  SWIG_IndexError     4
#define  SWIG_TypeError      5
#define  SWIG_DivisionByZero 6
#define  SWIG_OverflowError  7
#define  SWIG_SyntaxError    8
#define  SWIG_ValueError     9
#define  SWIG_SystemError   10
#define  SWIG_UnknownError  99


static void SWIG_JavaException(JNIEnv *jenv, int code, const char *msg) {
  SWIG_JavaExceptionCodes exception_code = SWIG_JavaUnknownError;
  switch(code) {
  case SWIG_MemoryError:
    exception_code = SWIG_JavaOutOfMemoryError;
    break;
  case SWIG_IOError:
    exception_code = SWIG_JavaIOException;
    break;
  case SWIG_SystemError:
  case SWIG_RuntimeError:
    exception_code = SWIG_JavaRuntimeException;
    break;
  case SWIG_OverflowError:
  case SWIG_IndexError:
    exception_code = SWIG_JavaIndexOutOfBoundsException;
    break;
  case SWIG_DivisionByZero:
    exception_code = SWIG_JavaArithmeticException;
    break;
  case SWIG_SyntaxError:
  case SWIG_ValueError:
  case SWIG_TypeError:
    exception_code = SWIG_JavaIllegalArgumentException;
    break;
  case SWIG_UnknownError:
  default:
    exception_code = SWIG_JavaUnknownError;
    break;
  }
  SWIG_JavaThrowException(jenv, exception_code, msg);
}
#define SWIG_exception(code, msg) { SWIG_JavaException(jenv, code, msg); }

extern char const *bdd_errno;
extern void init();
extern void addBits(int);
extern int numBits();
extern void addRef(int);
extern void delRef(int);
extern int literal(int,int []);
extern int falseBDD();
extern int trueBDD();
extern int replace(int,int,int [],int []);
extern int relprod(int,int,int,int []);
extern int project(int,int,int []);
extern int or(int,int);
extern int and(int,int);
extern int minus(int,int);
extern void setOrder(int,int []);
extern void allCubes(int,int []);
extern int numNodes(int);
extern int numPaths(int);
extern void dump(int,int,int []);
extern void dumpdot(int);
extern void info();
extern void reportOrdering(int,int []);
extern void gbc();

#include "jedd.h"

#ifdef __cplusplus
extern "C" {
#endif
JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_set_1bdd_1errno(JNIEnv *jenv, jclass jcls, jstring jarg1) {
    char *arg1 ;
    
    (void)jenv;
    (void)jcls;
    {
        arg1 = 0;
        if (jarg1) {
            arg1 = (char *)(*jenv)->GetStringUTFChars(jenv, jarg1, 0);
            if (!arg1) return ;
        }
    }
    {
        {
            bdd_errno = (char const *) malloc(strlen(arg1)+1);
            strcpy((char*)bdd_errno,arg1);
        }
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    {
        if (arg1) (*jenv)->ReleaseStringUTFChars(jenv, jarg1, arg1); 
    }
}


JNIEXPORT jstring JNICALL Java_jedd_JeddNativeJNI_get_1bdd_1errno(JNIEnv *jenv, jclass jcls) {
    jstring jresult = 0 ;
    char *result;
    
    (void)jenv;
    (void)jcls;
    {
        result = (char *)bdd_errno;
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    {
        if(result) jresult = (*jenv)->NewStringUTF(jenv, result); 
    }
    return jresult;
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_init(JNIEnv *jenv, jclass jcls) {
    (void)jenv;
    (void)jcls;
    {
        init();
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_addBits(JNIEnv *jenv, jclass jcls, jint jarg1) {
    int arg1 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    {
        addBits(arg1);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_numBits(JNIEnv *jenv, jclass jcls) {
    jint jresult = 0 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    {
        result = (int)numBits();
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_addRef(JNIEnv *jenv, jclass jcls, jint jarg1) {
    int arg1 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    {
        addRef(arg1);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_delRef(JNIEnv *jenv, jclass jcls, jint jarg1) {
    int arg1 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    {
        delRef(arg1);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_literal(JNIEnv *jenv, jclass jcls, jint jarg1, jintArray jarg2) {
    jint jresult = 0 ;
    int arg1 ;
    int *arg2 ;
    int result;
    jint *jarr2 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr2, &arg2, jarg2)) return 0; 
    {
        result = (int)literal(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    SWIG_JavaArrayArgoutInt(jenv, jarr2, arg2, jarg2); 
    free(arg2); 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_falseBDD(JNIEnv *jenv, jclass jcls) {
    jint jresult = 0 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    {
        result = (int)falseBDD();
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_trueBDD(JNIEnv *jenv, jclass jcls) {
    jint jresult = 0 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    {
        result = (int)trueBDD();
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_replace(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2, jintArray jarg3, jintArray jarg4) {
    jint jresult = 0 ;
    int arg1 ;
    int arg2 ;
    int *arg3 ;
    int *arg4 ;
    int result;
    jint *jarr3 ;
    jint *jarr4 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr3, &arg3, jarg3)) return 0; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr4, &arg4, jarg4)) return 0; 
    {
        result = (int)replace(arg1,arg2,arg3,arg4);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    SWIG_JavaArrayArgoutInt(jenv, jarr3, arg3, jarg3); 
    SWIG_JavaArrayArgoutInt(jenv, jarr4, arg4, jarg4); 
    free(arg3); 
    free(arg4); 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_relprod(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2, jint jarg3, jintArray jarg4) {
    jint jresult = 0 ;
    int arg1 ;
    int arg2 ;
    int arg3 ;
    int *arg4 ;
    int result;
    jint *jarr4 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    arg3 = (int)jarg3; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr4, &arg4, jarg4)) return 0; 
    {
        result = (int)relprod(arg1,arg2,arg3,arg4);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    SWIG_JavaArrayArgoutInt(jenv, jarr4, arg4, jarg4); 
    free(arg4); 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_project(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2, jintArray jarg3) {
    jint jresult = 0 ;
    int arg1 ;
    int arg2 ;
    int *arg3 ;
    int result;
    jint *jarr3 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr3, &arg3, jarg3)) return 0; 
    {
        result = (int)project(arg1,arg2,arg3);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    SWIG_JavaArrayArgoutInt(jenv, jarr3, arg3, jarg3); 
    free(arg3); 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_or(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2) {
    jint jresult = 0 ;
    int arg1 ;
    int arg2 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    {
        result = (int)or(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_and(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2) {
    jint jresult = 0 ;
    int arg1 ;
    int arg2 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    {
        result = (int)and(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_minus(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2) {
    jint jresult = 0 ;
    int arg1 ;
    int arg2 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    {
        result = (int)minus(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_setOrder(JNIEnv *jenv, jclass jcls, jint jarg1, jintArray jarg2) {
    int arg1 ;
    int *arg2 ;
    jint *jarr2 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr2, &arg2, jarg2)) return ; 
    {
        setOrder(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    SWIG_JavaArrayArgoutInt(jenv, jarr2, arg2, jarg2); 
    free(arg2); 
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_allCubes(JNIEnv *jenv, jclass jcls, jint jarg1, jintArray jarg2) {
    int arg1 ;
    int *arg2 ;
    jint *jarr2 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr2, &arg2, jarg2)) return ; 
    {
        allCubes(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    SWIG_JavaArrayArgoutInt(jenv, jarr2, arg2, jarg2); 
    free(arg2); 
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_numNodes(JNIEnv *jenv, jclass jcls, jint jarg1) {
    jint jresult = 0 ;
    int arg1 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    {
        result = (int)numNodes(arg1);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT jint JNICALL Java_jedd_JeddNativeJNI_numPaths(JNIEnv *jenv, jclass jcls, jint jarg1) {
    jint jresult = 0 ;
    int arg1 ;
    int result;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    {
        result = (int)numPaths(arg1);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    jresult = (jint)result; 
    return jresult;
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_dump(JNIEnv *jenv, jclass jcls, jint jarg1, jint jarg2, jintArray jarg3) {
    int arg1 ;
    int arg2 ;
    int *arg3 ;
    jint *jarr3 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    arg2 = (int)jarg2; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr3, &arg3, jarg3)) return ; 
    {
        dump(arg1,arg2,arg3);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    SWIG_JavaArrayArgoutInt(jenv, jarr3, arg3, jarg3); 
    free(arg3); 
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_dumpdot(JNIEnv *jenv, jclass jcls, jint jarg1) {
    int arg1 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    {
        dumpdot(arg1);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_info(JNIEnv *jenv, jclass jcls) {
    (void)jenv;
    (void)jcls;
    {
        info();
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_reportOrdering(JNIEnv *jenv, jclass jcls, jint jarg1, jintArray jarg2) {
    int arg1 ;
    int *arg2 ;
    jint *jarr2 ;
    
    (void)jenv;
    (void)jcls;
    arg1 = (int)jarg1; 
    if (!SWIG_JavaArrayInInt(jenv, &jarr2, &arg2, jarg2)) return ; 
    {
        reportOrdering(arg1,arg2);
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
    SWIG_JavaArrayArgoutInt(jenv, jarr2, arg2, jarg2); 
    free(arg2); 
}


JNIEXPORT void JNICALL Java_jedd_JeddNativeJNI_gbc(JNIEnv *jenv, jclass jcls) {
    (void)jenv;
    (void)jcls;
    {
        gbc();
        
        if( bdd_errno ) {
            SWIG_exception(SWIG_RuntimeError, bdd_errno);
            bdd_errno = NULL;
        }
    }
}


#ifdef __cplusplus
}
#endif
