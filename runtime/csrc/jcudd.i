%module Cudd
%include "arrays_java.i";
%include "typemaps.i"
%{
#include "util.h"
#include "cudd.h"
#include "jcudd.h"
%}
%include "jcudd.h"
%include "cudd.h"

