package jedd;

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.17u-20030723-1531
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */


public class JeddNative {
  public static void setBdd_errno(String value) {
    JeddNativeJNI.set_bdd_errno(value);
  }

  public static String getBdd_errno() {
    return JeddNativeJNI.get_bdd_errno();
  }

  public static void init() {
    JeddNativeJNI.init();
  }

  public static void addBits(int bits) {
    JeddNativeJNI.addBits(bits);
  }

  public static int numBits() {
    return JeddNativeJNI.numBits();
  }

  public static void addRef(int bdd) {
    JeddNativeJNI.addRef(bdd);
  }

  public static void delRef(int bdd) {
    JeddNativeJNI.delRef(bdd);
  }

  public static int literal(int n, int[] bits) {
    return JeddNativeJNI.literal(n, bits);
  }

  public static int falseBDD() {
    return JeddNativeJNI.falseBDD();
  }

  public static int trueBDD() {
    return JeddNativeJNI.trueBDD();
  }

  public static int replace(int r, int n, int[] from, int[] to) {
    return JeddNativeJNI.replace(r, n, from, to);
  }

  public static int relprod(int r1, int r2, int n, int[] domains) {
    return JeddNativeJNI.relprod(r1, r2, n, domains);
  }

  public static int project(int r, int n, int[] toRemove) {
    return JeddNativeJNI.project(r, n, toRemove);
  }

  public static int or(int r1, int r2) {
    return JeddNativeJNI.or(r1, r2);
  }

  public static int and(int r1, int r2) {
    return JeddNativeJNI.and(r1, r2);
  }

  public static int minus(int r1, int r2) {
    return JeddNativeJNI.minus(r1, r2);
  }

  public static void setOrder(int n, int[] level2var) {
    JeddNativeJNI.setOrder(n, level2var);
  }

  public static void allCubes(int r, int[] cubes) {
    JeddNativeJNI.allCubes(r, cubes);
  }

  public static int numNodes(int r) {
    return JeddNativeJNI.numNodes(r);
  }

  public static int numPaths(int r) {
    return JeddNativeJNI.numPaths(r);
  }

  public static int satCount(int r, int vars) {
    return JeddNativeJNI.satCount(r, vars);
  }

  public static void dump(int r, int n, int[] bits) {
    JeddNativeJNI.dump(r, n, bits);
  }

  public static void dumpdot(int r) {
    JeddNativeJNI.dumpdot(r);
  }

  public static void info() {
    JeddNativeJNI.info();
  }

  public static void reportOrdering(int n, int[] vars) {
    JeddNativeJNI.reportOrdering(n, vars);
  }

  public static void gbc() {
    JeddNativeJNI.gbc();
  }

  public static void getShape(int bdd, int[] shape) {
    JeddNativeJNI.getShape(bdd, shape);
  }

}
