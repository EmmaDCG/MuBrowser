package com.mu.game.model.transaction;

public class Transaction {
   TransactionList initiatorList = null;
   TransactionList targetList = null;
   long time = System.currentTimeMillis();

   public Transaction(TransactionList t1, TransactionList t2) {
      this.initiatorList = t1;
      this.targetList = t2;
   }

   public long getInitiatorID() {
      return this.initiatorList.getPlayerID();
   }

   public long getTargetID() {
      return this.targetList.getPlayerID();
   }

   public TransactionList getInitiatorList() {
      return this.initiatorList;
   }

   public TransactionList getTargetList() {
      return this.targetList;
   }

   public long getTime() {
      return this.time;
   }

   public long getOtherID(long selfID) {
      return selfID == this.getInitiatorID() ? this.getTargetID() : this.getInitiatorID();
   }

   public TransactionList getTransactionList(long selfID) {
      return selfID == this.getInitiatorID() ? this.initiatorList : this.targetList;
   }
}
