%module Buddy
%include "arrays_java.i";
%include "typemaps.i"
%exception {
  $action
  if (bdd_errno) {
    jclass clazz = (*jenv)->FindClass(jenv, "java/lang/RuntimeException");
    (*jenv)->ThrowNew(jenv, clazz, bdd_errno);
    return $null;
  }
}
%{
#include "bdd.h"
#include "fdd.h"
#include "jbuddy.h"
%}
%include "jbuddy.h"
%include "bdd.h"
%include "fdd.h"
