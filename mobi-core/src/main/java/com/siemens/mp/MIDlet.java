/*    */ package com.siemens.mp;

/*    */
/*    */ import javax.microedition.io.ConnectionNotFoundException;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public class MIDlet
/*    */ {

  /*    */ private static String[] supportedProtocols;

  /*    */
  /*    */ public static void notifyDestroyed() {
  }

  /*    */
  /*    */ public static void notifyPaused() {
  }

  /*    */
  /*    */ public static String getAppProperty(final String s)
  /*    */ {
    /* 31 */ return s;
    /*    */ }

  /*    */
  /*    */ public static final boolean platformRequest(final String s)
      /*    */ throws ConnectionNotFoundException, NotAllowedException
  /*    */ {
    /* 37 */ return false;
    /*    */ }

  /*    */
  /*    */ public static String[] getSupportedProtocols()
  /*    */ {
    /* 42 */ return MIDlet.supportedProtocols;
    /*    */ }
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\MIDlet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
