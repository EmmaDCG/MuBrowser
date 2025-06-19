package com.mu.game.model.transaction;

public class RequestTransaction {
   private static final int timeOut = 300000;
   private long initiatorID = 0L;
   private long targetID = 0L;
   private long time = System.currentTimeMillis();

   public RequestTransaction(long initiatorID, long targetID) {
      this.initiatorID = initiatorID;
      this.targetID = targetID;
   }

   public long getInitiatorID() {
      return this.initiatorID;
   }

   public long getTargetID() {
      return this.targetID;
   }

   public long getTime() {
      return this.time;
   }

   public boolean isTimeOut() {
      return System.currentTimeMillis() - this.time > 300000L;
   }
}
