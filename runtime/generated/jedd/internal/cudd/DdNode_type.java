/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.17u-20030723-1531
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package jedd.internal.cudd;


public class DdNode_type {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected DdNode_type(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected void finalize() {
    delete();
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      CuddJNI.delete_DdNode_type(swigCPtr);
      swigCMemOwn = false;
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(DdNode_type obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public void setValue(double value) {
    CuddJNI.set_DdNode_type_value(swigCPtr, value);
  }

  public double getValue() {
    return CuddJNI.get_DdNode_type_value(swigCPtr);
  }

  public void setKids(DdChildren kids) {
    CuddJNI.set_DdNode_type_kids(swigCPtr, DdChildren.getCPtr(kids));
  }

  public DdChildren getKids() {
    long cPtr = CuddJNI.get_DdNode_type_kids(swigCPtr);
    return (cPtr == 0) ? null : new DdChildren(cPtr, false);
  }

  public DdNode_type() {
    this(CuddJNI.new_DdNode_type(), true);
  }

}
