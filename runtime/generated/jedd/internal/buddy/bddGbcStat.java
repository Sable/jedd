/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.17u-20030723-1531
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package jedd.internal.buddy;


public class bddGbcStat {
  private long swigCPtr;
  protected boolean swigCMemOwn;

  protected bddGbcStat(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected void finalize() {
    delete();
  }

  public void delete() {
    if(swigCPtr != 0 && swigCMemOwn) {
      BuddyJNI.delete_bddGbcStat(swigCPtr);
      swigCMemOwn = false;
    }
    swigCPtr = 0;
  }

  protected static long getCPtr(bddGbcStat obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  public void setNodes(int nodes) {
    BuddyJNI.set_bddGbcStat_nodes(swigCPtr, nodes);
  }

  public int getNodes() {
    return BuddyJNI.get_bddGbcStat_nodes(swigCPtr);
  }

  public void setFreenodes(int freenodes) {
    BuddyJNI.set_bddGbcStat_freenodes(swigCPtr, freenodes);
  }

  public int getFreenodes() {
    return BuddyJNI.get_bddGbcStat_freenodes(swigCPtr);
  }

  public void setTime(int time) {
    BuddyJNI.set_bddGbcStat_time(swigCPtr, time);
  }

  public int getTime() {
    return BuddyJNI.get_bddGbcStat_time(swigCPtr);
  }

  public void setSumtime(int sumtime) {
    BuddyJNI.set_bddGbcStat_sumtime(swigCPtr, sumtime);
  }

  public int getSumtime() {
    return BuddyJNI.get_bddGbcStat_sumtime(swigCPtr);
  }

  public void setNum(int num) {
    BuddyJNI.set_bddGbcStat_num(swigCPtr, num);
  }

  public int getNum() {
    return BuddyJNI.get_bddGbcStat_num(swigCPtr);
  }

  public bddGbcStat() {
    this(BuddyJNI.new_bddGbcStat(), true);
  }

}
