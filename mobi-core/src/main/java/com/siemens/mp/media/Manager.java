/*    */ package com.siemens.mp.media;

/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */
/*    */ import com.siemens.mp.media.protocol.DataSource;

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
/*    */ public final class Manager
/*    */ {

  /*    */ public static final String TONE_DEVICE_LOCATOR = "device://tone";
  /*    */ public static final String MIDI_DEVICE_LOCATOR = "device://midi";

  /*    */
  /*    */ public static String[] getSupportedContentTypes(final String s)
  /*    */ {
    /* 26 */ return new String[] {
        /* 27 */ s
    };
    /*    */ }

  /*    */
  /*    */
  /*    */ public static String[] getSupportedProtocols(final String s)
  /*    */ {
    /* 33 */ return new String[] {
        /* 34 */ s
    };
    /*    */ }

  /*    */
  /*    */
  /*    */ public static Player createPlayer(final String s)
      /*    */ throws IOException, MediaException
  /*    */ {
    /* 41 */ return null;
    /*    */ }

  /*    */
  /*    */ public static Player createPlayer(final InputStream inputstream, final String s)
      /*    */ throws IOException, MediaException
  /*    */ {
    /* 47 */ return null;
    /*    */ }

  /*    */
  /*    */ public static Player createPlayer(final DataSource datasource)
      /*    */ throws IOException, MediaException
  /*    */ {
    /* 53 */ return null;
    /*    */ }

  /*    */
  /*    */
  /*    */ public static void playTone(final int i, final int j, final int k)
      /*    */ throws MediaException
  /*    */ {
  }

  /*    */
  /*    */ public static TimeBase getSystemTimeBase()
  /*    */ {
    /* 63 */ return null;
    /*    */ }
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\media\Manager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
