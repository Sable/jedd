package jedd.internal.buddy;

/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version: 1.3.17u-20030723-1531
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */


public class Buddy {
  public static int bdd_setpairs(bddPair arg0, int[] arg1, int[] arg2, int arg3) {
    return BuddyJNI.bdd_setpairs__SWIG_0(bddPair.getCPtr(arg0), arg1, arg2, arg3);
  }

  public static int bdd_makeset(int[] arg0, int arg1) {
    return BuddyJNI.bdd_makeset__SWIG_0(arg0, arg1);
  }

  public static void bdd_setvarorder(int[] arg0) {
    BuddyJNI.bdd_setvarorder__SWIG_0(arg0);
  }

  public static void allCubes(int r, int[] cubes) {
    BuddyJNI.allCubes(r, cubes);
  }

  public static int nextCube(int r, int n, int[] cube) {
    return BuddyJNI.nextCube(r, n, cube);
  }

  public static int firstCube(int r, int n, int[] cube) {
    return BuddyJNI.firstCube(r, n, cube);
  }

  public static void getShape(int bdd, int[] shape) {
    BuddyJNI.getShape(bdd, shape);
  }

  public static void setBdd_errno(String value) {
    BuddyJNI.set_bdd_errno(value);
  }

  public static String getBdd_errno() {
    return BuddyJNI.get_bdd_errno();
  }

  public static void setuperrorhandler() {
    BuddyJNI.setuperrorhandler();
  }

  public static int bdd_markwidth(int bdd, int var1, int var2) {
    return BuddyJNI.bdd_markwidth(bdd, var1, var2);
  }

  public static int bdd_makenode(long arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_makenode(arg0, arg1, arg2);
  }

  public static SWIGTYPE_p_f_int__void bdd_error_hook(SWIGTYPE_p_f_int__void arg0) {
    long cPtr = BuddyJNI.bdd_error_hook(SWIGTYPE_p_f_int__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_int__void(cPtr, false);
  }

  public static SWIGTYPE_p_f_int_p_struct_s_bddGbcStat__void bdd_gbc_hook(SWIGTYPE_p_f_int_p_struct_s_bddGbcStat__void arg0) {
    long cPtr = BuddyJNI.bdd_gbc_hook(SWIGTYPE_p_f_int_p_struct_s_bddGbcStat__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_int_p_struct_s_bddGbcStat__void(cPtr, false);
  }

  public static SWIGTYPE_p_f_int_int__void bdd_resize_hook(SWIGTYPE_p_f_int_int__void arg0) {
    long cPtr = BuddyJNI.bdd_resize_hook(SWIGTYPE_p_f_int_int__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_int_int__void(cPtr, false);
  }

  public static SWIGTYPE_p_f_int__void bdd_reorder_hook(SWIGTYPE_p_f_int__void arg0) {
    long cPtr = BuddyJNI.bdd_reorder_hook(SWIGTYPE_p_f_int__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_int__void(cPtr, false);
  }

  public static SWIGTYPE_p_f_p_FILE_int__void bdd_file_hook(SWIGTYPE_p_f_p_FILE_int__void arg0) {
    long cPtr = BuddyJNI.bdd_file_hook(SWIGTYPE_p_f_p_FILE_int__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_FILE_int__void(cPtr, false);
  }

  public static int bdd_init(int arg0, int arg1) {
    return BuddyJNI.bdd_init(arg0, arg1);
  }

  public static void bdd_done() {
    BuddyJNI.bdd_done();
  }

  public static int bdd_setvarnum(int arg0) {
    return BuddyJNI.bdd_setvarnum(arg0);
  }

  public static int bdd_extvarnum(int arg0) {
    return BuddyJNI.bdd_extvarnum(arg0);
  }

  public static int bdd_isrunning() {
    return BuddyJNI.bdd_isrunning();
  }

  public static int bdd_setmaxnodenum(int arg0) {
    return BuddyJNI.bdd_setmaxnodenum(arg0);
  }

  public static int bdd_setmaxincrease(int arg0) {
    return BuddyJNI.bdd_setmaxincrease(arg0);
  }

  public static int bdd_setminfreenodes(int arg0) {
    return BuddyJNI.bdd_setminfreenodes(arg0);
  }

  public static int bdd_getnodenum() {
    return BuddyJNI.bdd_getnodenum();
  }

  public static int bdd_getallocnum() {
    return BuddyJNI.bdd_getallocnum();
  }

  public static String bdd_versionstr() {
    return BuddyJNI.bdd_versionstr();
  }

  public static int bdd_versionnum() {
    return BuddyJNI.bdd_versionnum();
  }

  public static void bdd_stats(bddStat arg0) {
    BuddyJNI.bdd_stats(bddStat.getCPtr(arg0));
  }

  public static void bdd_cachestats(bddCacheStat arg0) {
    BuddyJNI.bdd_cachestats(bddCacheStat.getCPtr(arg0));
  }

  public static void bdd_fprintstat(SWIGTYPE_p_FILE arg0) {
    BuddyJNI.bdd_fprintstat(SWIGTYPE_p_FILE.getCPtr(arg0));
  }

  public static void bdd_printstat() {
    BuddyJNI.bdd_printstat();
  }

  public static void bdd_default_gbchandler(int arg0, bddGbcStat arg1) {
    BuddyJNI.bdd_default_gbchandler(arg0, bddGbcStat.getCPtr(arg1));
  }

  public static void bdd_default_errhandler(int arg0) {
    BuddyJNI.bdd_default_errhandler(arg0);
  }

  public static String bdd_errstring(int arg0) {
    return BuddyJNI.bdd_errstring(arg0);
  }

  public static void bdd_clear_error() {
    BuddyJNI.bdd_clear_error();
  }

  public static int bdd_true() {
    return BuddyJNI.bdd_true();
  }

  public static int bdd_false() {
    return BuddyJNI.bdd_false();
  }

  public static int bdd_varnum() {
    return BuddyJNI.bdd_varnum();
  }

  public static int bdd_ithvar(int arg0) {
    return BuddyJNI.bdd_ithvar(arg0);
  }

  public static int bdd_nithvar(int arg0) {
    return BuddyJNI.bdd_nithvar(arg0);
  }

  public static int bdd_var(int arg0) {
    return BuddyJNI.bdd_var(arg0);
  }

  public static int bdd_low(int arg0) {
    return BuddyJNI.bdd_low(arg0);
  }

  public static int bdd_high(int arg0) {
    return BuddyJNI.bdd_high(arg0);
  }

  public static int bdd_varlevel(int arg0) {
    return BuddyJNI.bdd_varlevel(arg0);
  }

  public static int bdd_addref(int arg0) {
    return BuddyJNI.bdd_addref(arg0);
  }

  public static int bdd_delref(int arg0) {
    return BuddyJNI.bdd_delref(arg0);
  }

  public static void bdd_gbc() {
    BuddyJNI.bdd_gbc();
  }

  public static int bdd_scanset(int arg0, SWIGTYPE_p_p_int arg1, SWIGTYPE_p_int arg2) {
    return BuddyJNI.bdd_scanset(arg0, SWIGTYPE_p_p_int.getCPtr(arg1), SWIGTYPE_p_int.getCPtr(arg2));
  }

  public static int bdd_makeset(SWIGTYPE_p_int arg0, int arg1) {
    return BuddyJNI.bdd_makeset__SWIG_1(SWIGTYPE_p_int.getCPtr(arg0), arg1);
  }

  public static bddPair bdd_newpair() {
    long cPtr = BuddyJNI.bdd_newpair();
    return (cPtr == 0) ? null : new bddPair(cPtr, false);
  }

  public static int bdd_setpair(bddPair arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_setpair(bddPair.getCPtr(arg0), arg1, arg2);
  }

  public static int bdd_setpairs(bddPair arg0, SWIGTYPE_p_int arg1, SWIGTYPE_p_int arg2, int arg3) {
    return BuddyJNI.bdd_setpairs__SWIG_1(bddPair.getCPtr(arg0), SWIGTYPE_p_int.getCPtr(arg1), SWIGTYPE_p_int.getCPtr(arg2), arg3);
  }

  public static int bdd_setbddpair(bddPair arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_setbddpair(bddPair.getCPtr(arg0), arg1, arg2);
  }

  public static int bdd_setbddpairs(bddPair arg0, SWIGTYPE_p_int arg1, SWIGTYPE_p_int arg2, int arg3) {
    return BuddyJNI.bdd_setbddpairs(bddPair.getCPtr(arg0), SWIGTYPE_p_int.getCPtr(arg1), SWIGTYPE_p_int.getCPtr(arg2), arg3);
  }

  public static void bdd_resetpair(bddPair arg0) {
    BuddyJNI.bdd_resetpair(bddPair.getCPtr(arg0));
  }

  public static void bdd_freepair(bddPair arg0) {
    BuddyJNI.bdd_freepair(bddPair.getCPtr(arg0));
  }

  public static int bdd_setcacheratio(int arg0) {
    return BuddyJNI.bdd_setcacheratio(arg0);
  }

  public static int bdd_buildcube(int arg0, int arg1, SWIGTYPE_p_int arg2) {
    return BuddyJNI.bdd_buildcube(arg0, arg1, SWIGTYPE_p_int.getCPtr(arg2));
  }

  public static int bdd_ibuildcube(int arg0, int arg1, SWIGTYPE_p_int arg2) {
    return BuddyJNI.bdd_ibuildcube(arg0, arg1, SWIGTYPE_p_int.getCPtr(arg2));
  }

  public static int bdd_not(int arg0) {
    return BuddyJNI.bdd_not(arg0);
  }

  public static int bdd_apply(int arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_apply(arg0, arg1, arg2);
  }

  public static int bdd_and(int arg0, int arg1) {
    return BuddyJNI.bdd_and(arg0, arg1);
  }

  public static int bdd_or(int arg0, int arg1) {
    return BuddyJNI.bdd_or(arg0, arg1);
  }

  public static int bdd_xor(int arg0, int arg1) {
    return BuddyJNI.bdd_xor(arg0, arg1);
  }

  public static int bdd_imp(int arg0, int arg1) {
    return BuddyJNI.bdd_imp(arg0, arg1);
  }

  public static int bdd_biimp(int arg0, int arg1) {
    return BuddyJNI.bdd_biimp(arg0, arg1);
  }

  public static int bdd_ite(int arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_ite(arg0, arg1, arg2);
  }

  public static int bdd_restrict(int arg0, int arg1) {
    return BuddyJNI.bdd_restrict(arg0, arg1);
  }

  public static int bdd_constrain(int arg0, int arg1) {
    return BuddyJNI.bdd_constrain(arg0, arg1);
  }

  public static int bdd_replace(int arg0, bddPair arg1) {
    return BuddyJNI.bdd_replace(arg0, bddPair.getCPtr(arg1));
  }

  public static int bdd_compose(int arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_compose(arg0, arg1, arg2);
  }

  public static int bdd_veccompose(int arg0, bddPair arg1) {
    return BuddyJNI.bdd_veccompose(arg0, bddPair.getCPtr(arg1));
  }

  public static int bdd_simplify(int arg0, int arg1) {
    return BuddyJNI.bdd_simplify(arg0, arg1);
  }

  public static int bdd_exist(int arg0, int arg1) {
    return BuddyJNI.bdd_exist(arg0, arg1);
  }

  public static int bdd_forall(int arg0, int arg1) {
    return BuddyJNI.bdd_forall(arg0, arg1);
  }

  public static int bdd_unique(int arg0, int arg1) {
    return BuddyJNI.bdd_unique(arg0, arg1);
  }

  public static int bdd_appex(int arg0, int arg1, int arg2, int arg3) {
    return BuddyJNI.bdd_appex(arg0, arg1, arg2, arg3);
  }

  public static int bdd_appall(int arg0, int arg1, int arg2, int arg3) {
    return BuddyJNI.bdd_appall(arg0, arg1, arg2, arg3);
  }

  public static int bdd_appuni(int arg0, int arg1, int arg2, int arg3) {
    return BuddyJNI.bdd_appuni(arg0, arg1, arg2, arg3);
  }

  public static int bdd_support(int arg0) {
    return BuddyJNI.bdd_support(arg0);
  }

  public static int bdd_satone(int arg0) {
    return BuddyJNI.bdd_satone(arg0);
  }

  public static int bdd_satoneset(int arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_satoneset(arg0, arg1, arg2);
  }

  public static int bdd_fullsatone(int arg0) {
    return BuddyJNI.bdd_fullsatone(arg0);
  }

  public static void bdd_allsat(int r, SWIGTYPE_p_f_p_char_int__void handler) {
    BuddyJNI.bdd_allsat(r, SWIGTYPE_p_f_p_char_int__void.getCPtr(handler));
  }

  public static double bdd_satcount(int arg0) {
    return BuddyJNI.bdd_satcount(arg0);
  }

  public static double bdd_satcountset(int arg0, int arg1) {
    return BuddyJNI.bdd_satcountset(arg0, arg1);
  }

  public static double bdd_satcountln(int arg0) {
    return BuddyJNI.bdd_satcountln(arg0);
  }

  public static double bdd_satcountlnset(int arg0, int arg1) {
    return BuddyJNI.bdd_satcountlnset(arg0, arg1);
  }

  public static int bdd_nodecount(int arg0) {
    return BuddyJNI.bdd_nodecount(arg0);
  }

  public static int bdd_anodecount(SWIGTYPE_p_int arg0, int arg1) {
    return BuddyJNI.bdd_anodecount(SWIGTYPE_p_int.getCPtr(arg0), arg1);
  }

  public static SWIGTYPE_p_int bdd_varprofile(int arg0) {
    long cPtr = BuddyJNI.bdd_varprofile(arg0);
    return (cPtr == 0) ? null : new SWIGTYPE_p_int(cPtr, false);
  }

  public static double bdd_pathcount(int arg0) {
    return BuddyJNI.bdd_pathcount(arg0);
  }

  public static void bdd_printall() {
    BuddyJNI.bdd_printall();
  }

  public static void bdd_fprintall(SWIGTYPE_p_FILE arg0) {
    BuddyJNI.bdd_fprintall(SWIGTYPE_p_FILE.getCPtr(arg0));
  }

  public static void bdd_fprinttable(SWIGTYPE_p_FILE arg0, int arg1) {
    BuddyJNI.bdd_fprinttable(SWIGTYPE_p_FILE.getCPtr(arg0), arg1);
  }

  public static void bdd_printtable(int arg0) {
    BuddyJNI.bdd_printtable(arg0);
  }

  public static void bdd_fprintset(SWIGTYPE_p_FILE arg0, int arg1) {
    BuddyJNI.bdd_fprintset(SWIGTYPE_p_FILE.getCPtr(arg0), arg1);
  }

  public static void bdd_printset(int arg0) {
    BuddyJNI.bdd_printset(arg0);
  }

  public static int bdd_fnprintdot(String arg0, int arg1) {
    return BuddyJNI.bdd_fnprintdot(arg0, arg1);
  }

  public static void bdd_fprintdot(SWIGTYPE_p_FILE arg0, int arg1) {
    BuddyJNI.bdd_fprintdot(SWIGTYPE_p_FILE.getCPtr(arg0), arg1);
  }

  public static void bdd_printdot(int arg0) {
    BuddyJNI.bdd_printdot(arg0);
  }

  public static int bdd_fnsave(String arg0, int arg1) {
    return BuddyJNI.bdd_fnsave(arg0, arg1);
  }

  public static int bdd_save(SWIGTYPE_p_FILE arg0, int arg1) {
    return BuddyJNI.bdd_save(SWIGTYPE_p_FILE.getCPtr(arg0), arg1);
  }

  public static int bdd_fnload(String arg0, SWIGTYPE_p_int arg1) {
    return BuddyJNI.bdd_fnload(arg0, SWIGTYPE_p_int.getCPtr(arg1));
  }

  public static int bdd_load(SWIGTYPE_p_FILE ifile, SWIGTYPE_p_int arg1) {
    return BuddyJNI.bdd_load(SWIGTYPE_p_FILE.getCPtr(ifile), SWIGTYPE_p_int.getCPtr(arg1));
  }

  public static int bdd_swapvar(int v1, int v2) {
    return BuddyJNI.bdd_swapvar(v1, v2);
  }

  public static void bdd_default_reohandler(int arg0) {
    BuddyJNI.bdd_default_reohandler(arg0);
  }

  public static void bdd_reorder(int arg0) {
    BuddyJNI.bdd_reorder(arg0);
  }

  public static int bdd_reorder_gain() {
    return BuddyJNI.bdd_reorder_gain();
  }

  public static SWIGTYPE_p_f_void__int bdd_reorder_probe(SWIGTYPE_p_f_void__int arg0) {
    long cPtr = BuddyJNI.bdd_reorder_probe(SWIGTYPE_p_f_void__int.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_void__int(cPtr, false);
  }

  public static void bdd_clrvarblocks() {
    BuddyJNI.bdd_clrvarblocks();
  }

  public static int bdd_addvarblock(int arg0, int arg1) {
    return BuddyJNI.bdd_addvarblock(arg0, arg1);
  }

  public static int bdd_intaddvarblock(int arg0, int arg1, int arg2) {
    return BuddyJNI.bdd_intaddvarblock(arg0, arg1, arg2);
  }

  public static void bdd_varblockall() {
    BuddyJNI.bdd_varblockall();
  }

  public static SWIGTYPE_p_f_p_FILE_int__void bdd_blockfile_hook(SWIGTYPE_p_f_p_FILE_int__void arg0) {
    long cPtr = BuddyJNI.bdd_blockfile_hook(SWIGTYPE_p_f_p_FILE_int__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_FILE_int__void(cPtr, false);
  }

  public static int bdd_autoreorder(int arg0) {
    return BuddyJNI.bdd_autoreorder(arg0);
  }

  public static int bdd_autoreorder_times(int arg0, int arg1) {
    return BuddyJNI.bdd_autoreorder_times(arg0, arg1);
  }

  public static int bdd_var2level(int arg0) {
    return BuddyJNI.bdd_var2level(arg0);
  }

  public static int bdd_level2var(int arg0) {
    return BuddyJNI.bdd_level2var(arg0);
  }

  public static int bdd_getreorder_times() {
    return BuddyJNI.bdd_getreorder_times();
  }

  public static int bdd_getreorder_method() {
    return BuddyJNI.bdd_getreorder_method();
  }

  public static void bdd_enable_reorder() {
    BuddyJNI.bdd_enable_reorder();
  }

  public static void bdd_disable_reorder() {
    BuddyJNI.bdd_disable_reorder();
  }

  public static int bdd_reorder_verbose(int arg0) {
    return BuddyJNI.bdd_reorder_verbose(arg0);
  }

  public static void bdd_setvarorder(SWIGTYPE_p_int arg0) {
    BuddyJNI.bdd_setvarorder__SWIG_1(SWIGTYPE_p_int.getCPtr(arg0));
  }

  public static void bdd_printorder() {
    BuddyJNI.bdd_printorder();
  }

  public static void bdd_fprintorder(SWIGTYPE_p_FILE arg0) {
    BuddyJNI.bdd_fprintorder(SWIGTYPE_p_FILE.getCPtr(arg0));
  }

  public static int getBddfalse() {
    return BuddyJNI.get_bddfalse();
  }

  public static int getBddtrue() {
    return BuddyJNI.get_bddtrue();
  }

  public static int fdd_extdomain(SWIGTYPE_p_int arg0, int arg1) {
    return BuddyJNI.fdd_extdomain(SWIGTYPE_p_int.getCPtr(arg0), arg1);
  }

  public static int fdd_overlapdomain(int arg0, int arg1) {
    return BuddyJNI.fdd_overlapdomain(arg0, arg1);
  }

  public static void fdd_clearall() {
    BuddyJNI.fdd_clearall();
  }

  public static int fdd_domainnum() {
    return BuddyJNI.fdd_domainnum();
  }

  public static int fdd_domainsize(int arg0) {
    return BuddyJNI.fdd_domainsize(arg0);
  }

  public static int fdd_varnum(int arg0) {
    return BuddyJNI.fdd_varnum(arg0);
  }

  public static SWIGTYPE_p_int fdd_vars(int arg0) {
    long cPtr = BuddyJNI.fdd_vars(arg0);
    return (cPtr == 0) ? null : new SWIGTYPE_p_int(cPtr, false);
  }

  public static int fdd_ithvar(int arg0, int arg1) {
    return BuddyJNI.fdd_ithvar(arg0, arg1);
  }

  public static int fdd_scanvar(int arg0, int arg1) {
    return BuddyJNI.fdd_scanvar(arg0, arg1);
  }

  public static SWIGTYPE_p_int fdd_scanallvar(int arg0) {
    long cPtr = BuddyJNI.fdd_scanallvar(arg0);
    return (cPtr == 0) ? null : new SWIGTYPE_p_int(cPtr, false);
  }

  public static int fdd_ithset(int arg0) {
    return BuddyJNI.fdd_ithset(arg0);
  }

  public static int fdd_domain(int arg0) {
    return BuddyJNI.fdd_domain(arg0);
  }

  public static int fdd_equals(int arg0, int arg1) {
    return BuddyJNI.fdd_equals(arg0, arg1);
  }

  public static SWIGTYPE_p_f_p_FILE_int__void fdd_file_hook(SWIGTYPE_p_f_p_FILE_int__void arg0) {
    long cPtr = BuddyJNI.fdd_file_hook(SWIGTYPE_p_f_p_FILE_int__void.getCPtr(arg0));
    return (cPtr == 0) ? null : new SWIGTYPE_p_f_p_FILE_int__void(cPtr, false);
  }

  public static void fdd_printset(int arg0) {
    BuddyJNI.fdd_printset(arg0);
  }

  public static void fdd_fprintset(SWIGTYPE_p_FILE arg0, int arg1) {
    BuddyJNI.fdd_fprintset(SWIGTYPE_p_FILE.getCPtr(arg0), arg1);
  }

  public static int fdd_scanset(int arg0, SWIGTYPE_p_p_int arg1, SWIGTYPE_p_int arg2) {
    return BuddyJNI.fdd_scanset(arg0, SWIGTYPE_p_p_int.getCPtr(arg1), SWIGTYPE_p_int.getCPtr(arg2));
  }

  public static int fdd_makeset(SWIGTYPE_p_int arg0, int arg1) {
    return BuddyJNI.fdd_makeset(SWIGTYPE_p_int.getCPtr(arg0), arg1);
  }

  public static int fdd_intaddvarblock(int arg0, int arg1, int arg2) {
    return BuddyJNI.fdd_intaddvarblock(arg0, arg1, arg2);
  }

  public static int fdd_setpair(bddPair arg0, int arg1, int arg2) {
    return BuddyJNI.fdd_setpair(bddPair.getCPtr(arg0), arg1, arg2);
  }

  public static int fdd_setpairs(bddPair arg0, SWIGTYPE_p_int arg1, SWIGTYPE_p_int arg2, int arg3) {
    return BuddyJNI.fdd_setpairs(bddPair.getCPtr(arg0), SWIGTYPE_p_int.getCPtr(arg1), SWIGTYPE_p_int.getCPtr(arg2), arg3);
  }

  // enums and constants
  public final static int bddop_and = BuddyJNI.get_bddop_and();
  public final static int bddop_xor = BuddyJNI.get_bddop_xor();
  public final static int bddop_or = BuddyJNI.get_bddop_or();
  public final static int bddop_nand = BuddyJNI.get_bddop_nand();
  public final static int bddop_nor = BuddyJNI.get_bddop_nor();
  public final static int bddop_imp = BuddyJNI.get_bddop_imp();
  public final static int bddop_biimp = BuddyJNI.get_bddop_biimp();
  public final static int bddop_diff = BuddyJNI.get_bddop_diff();
  public final static int bddop_less = BuddyJNI.get_bddop_less();
  public final static int bddop_invimp = BuddyJNI.get_bddop_invimp();
  public final static int bddop_not = BuddyJNI.get_bddop_not();
  public final static int bddop_simplify = BuddyJNI.get_bddop_simplify();
  public final static int BDD_REORDER_NONE = BuddyJNI.get_BDD_REORDER_NONE();
  public final static int BDD_REORDER_WIN2 = BuddyJNI.get_BDD_REORDER_WIN2();
  public final static int BDD_REORDER_WIN2ITE = BuddyJNI.get_BDD_REORDER_WIN2ITE();
  public final static int BDD_REORDER_SIFT = BuddyJNI.get_BDD_REORDER_SIFT();
  public final static int BDD_REORDER_SIFTITE = BuddyJNI.get_BDD_REORDER_SIFTITE();
  public final static int BDD_REORDER_WIN3 = BuddyJNI.get_BDD_REORDER_WIN3();
  public final static int BDD_REORDER_WIN3ITE = BuddyJNI.get_BDD_REORDER_WIN3ITE();
  public final static int BDD_REORDER_RANDOM = BuddyJNI.get_BDD_REORDER_RANDOM();
  public final static int BDD_REORDER_FREE = BuddyJNI.get_BDD_REORDER_FREE();
  public final static int BDD_REORDER_FIXED = BuddyJNI.get_BDD_REORDER_FIXED();
  public final static int BDD_MEMORY = BuddyJNI.get_BDD_MEMORY();
  public final static int BDD_VAR = BuddyJNI.get_BDD_VAR();
  public final static int BDD_RANGE = BuddyJNI.get_BDD_RANGE();
  public final static int BDD_DEREF = BuddyJNI.get_BDD_DEREF();
  public final static int BDD_RUNNING = BuddyJNI.get_BDD_RUNNING();
  public final static int BDD_FILE = BuddyJNI.get_BDD_FILE();
  public final static int BDD_FORMAT = BuddyJNI.get_BDD_FORMAT();
  public final static int BDD_ORDER = BuddyJNI.get_BDD_ORDER();
  public final static int BDD_BREAK = BuddyJNI.get_BDD_BREAK();
  public final static int BDD_VARNUM = BuddyJNI.get_BDD_VARNUM();
  public final static int BDD_NODES = BuddyJNI.get_BDD_NODES();
  public final static int BDD_OP = BuddyJNI.get_BDD_OP();
  public final static int BDD_VARSET = BuddyJNI.get_BDD_VARSET();
  public final static int BDD_VARBLK = BuddyJNI.get_BDD_VARBLK();
  public final static int BDD_DECVNUM = BuddyJNI.get_BDD_DECVNUM();
  public final static int BDD_REPLACE = BuddyJNI.get_BDD_REPLACE();
  public final static int BDD_NODENUM = BuddyJNI.get_BDD_NODENUM();
  public final static int BDD_ILLBDD = BuddyJNI.get_BDD_ILLBDD();
  public final static int BDD_SIZE = BuddyJNI.get_BDD_SIZE();
  public final static int BVEC_SIZE = BuddyJNI.get_BVEC_SIZE();
  public final static int BVEC_SHIFT = BuddyJNI.get_BVEC_SHIFT();
  public final static int BVEC_DIVZERO = BuddyJNI.get_BVEC_DIVZERO();
  public final static int BDD_ERRNUM = BuddyJNI.get_BDD_ERRNUM();
}
