package jedd;

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.17u-20030723-1531
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */


class JeddNativeJNI {
  public final static native void set_bdd_errno(String jarg1);
  public final static native String get_bdd_errno();
  public final static native void init();
  public final static native void addBits(int jarg1);
  public final static native int numBits();
  public final static native void addRef(int jarg1);
  public final static native void delRef(int jarg1);
  public final static native int literal(int jarg1, int[] jarg2);
  public final static native int falseBDD();
  public final static native int trueBDD();
  public final static native int replace(int jarg1, int jarg2, int[] jarg3, int[] jarg4);
  public final static native int replacepair(int jarg1, int jarg2);
  public final static native int relprod(int jarg1, int jarg2, int jarg3, int[] jarg4);
  public final static native int project(int jarg1, int jarg2, int[] jarg3);
  public final static native int or(int jarg1, int jarg2);
  public final static native int and(int jarg1, int jarg2);
  public final static native int minus(int jarg1, int jarg2);
  public final static native void setOrder(int jarg1, int[] jarg2);
  public final static native void allCubes(int jarg1, int[] jarg2);
  public final static native int numNodes(int jarg1);
  public final static native int numPaths(int jarg1);
  public final static native int satCount(int jarg1, int jarg2);
  public final static native void dump(int jarg1, int jarg2, int[] jarg3);
  public final static native void dumpdot(int jarg1);
  public final static native void info();
  public final static native void reportOrdering(int jarg1, int[] jarg2);
  public final static native void gbc();
  public final static native void getShape(int jarg1, int[] jarg2);
  public final static native int makecube(int jarg1, int[] jarg2);
  public final static native int relprodcube(int jarg1, int jarg2, int jarg3);
  public final static native int makepair(int jarg1, int[] jarg2, int jarg3, int[] jarg4);
}
