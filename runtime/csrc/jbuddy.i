%module Buddy
%include "arrays_java.i";
%include "typemaps.i"
%{
#include "bdd.h"
#include "fdd.h"
#include "jbuddy.h"
%}
%include "jbuddy.h"
%include "bdd.h"
%include "fdd.h"
