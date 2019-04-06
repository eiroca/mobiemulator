/*    */ package com.siemens.mp.media.protocol;

/*    */ import java.io.IOException;
/*    */
/*    */ import com.siemens.mp.media.Control;
/*    */ import com.siemens.mp.media.Controllable;

/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */
/*    */ public abstract class DataSource
    /*    */ implements Controllable
/*    */ {

  /*    */ private final String locator;

  /*    */
  /*    */ public DataSource(final String s)
  /*    */ {
    /* 22 */ locator = s;
    /*    */ }

  /*    */
  /*    */ public String getLocator()
  /*    */ {
    /* 27 */ return locator;
    /*    */ }

  /*    */
  /*    */ public abstract String getContentType();

  /*    */
  /*    */ public abstract void connect()
      /*    */ throws IOException;

  /*    */
  /*    */ public abstract void disconnect();

  /*    */
  /*    */ public abstract void start()
      /*    */ throws IOException;

  /*    */
  /*    */ public abstract void stop()
      /*    */ throws IOException;

  /*    */
  /*    */ public abstract SourceStream[] getStreams();

  /*    */
  /*    */ @Override
  public abstract Control getControl(String paramString);

  /*    */
  /*    */ @Override
  public abstract Control[] getControls();
  /*    */ }

/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\media\protocol\DataSource.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */
