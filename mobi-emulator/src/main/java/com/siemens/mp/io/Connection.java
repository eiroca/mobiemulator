/*    */ package com.siemens.mp.io;
/*    */ 
/*    */ import com.siemens.mp.NotAllowedException;
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
/*    */ public class Connection
/*    */ {
/*    */   private ConnectionListener listener;
/*    */   
/*    */   public Connection(String s) {}
/*    */   
/*    */   public void send(byte[] abyte0)
/*    */     throws NotAllowedException
/*    */   {}
/*    */   
/*    */   public void setListener(ConnectionListener connectionlistener)
/*    */   {
/* 28 */     this.listener = connectionlistener;
/*    */   }
/*    */   
/*    */   public ConnectionListener getListener()
/*    */   {
/* 33 */     return this.listener;
/*    */   }
/*    */ }


/* Location:              S:\tmp\!\third-party.jar!\com\siemens\mp\io\Connection.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */