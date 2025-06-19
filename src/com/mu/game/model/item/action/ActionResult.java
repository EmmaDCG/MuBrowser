package com.mu.game.model.item.action;

public class ActionResult {
   private int result;
   private boolean sendToClient;

   public ActionResult() {
      this(1, true);
   }

   public ActionResult(int result, boolean sendToClient) {
      this.result = result;
      this.sendToClient = sendToClient;
   }

   public int getResult() {
      return this.result;
   }

   public void setResult(int result) {
      this.result = result;
   }

   public boolean isSendToClient() {
      return this.sendToClient;
   }

   public void setSendToClient(boolean sendToClient) {
      this.sendToClient = sendToClient;
   }
}
