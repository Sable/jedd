%module JeddNative
%include "arrays_java.i";
%include "typemaps.i"
%include "exception.i"
%exception {
    $function
    if( bdd_errno ) {
        SWIG_exception(SWIG_RuntimeError, bdd_errno);
        bdd_errno = NULL;
    }
}
%include "jedd.h"
%{
#include "jedd.h"
%}

