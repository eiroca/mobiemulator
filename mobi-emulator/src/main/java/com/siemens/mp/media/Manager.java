/*    */ package com.siemens.mp.media;
/*    */ 
/*    */ import com.siemens.mp.media.protocol.DataSource;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
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
/*    */   public static final String TONE_DEVICE_LOCATOR = "device://tone";
/*    */   public static final String MIDI_DEVICE_LOCATOR = "device://midi";
/*    */   
/*    */   public static String[] getSupportedContentTypes(String s)
/*    */   {
/* 26 */     return new String[] {
/* 27 */       s };
/*    */   }
/*    */   
/*    */ 
/*    */   public static String[] getSupportedProtocols(String s)
/*    */   {
/* 33 */     return new String[] {
/* 34 */       s };
/*    */   }
/*    */   
/*    */ 
/*    */   public static Player createPlayer(String s)
/*    */     throws IOException, MediaException
/*    */   {
/* 41 */     return null;
/*    */   }
/*    */   
/*    */   public static Player createPlayer(InputStream inputstream, String s)
/*    */     throws IOException, MediaException
/*    */   {
/* 47 */     return null;
/*    */   }
/*    */   
/*    */   public static Player createPlayer(DataSource datasource)
/*    */     throws IOException, MediaException
/*    */   {
/* 53 */     return null;
/*    */   }
/*    */   
/*    */ 
/*    */   public static void playTone(int i, int j, int k)
/*    */     throws MediaException
/*    */   {}
/*    */   
/*    */   public static TimeBase getSystemTimeBase()
/*    */   {
/* 63 */     return null;
/*    */   }
/*    */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\media\Manager.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */