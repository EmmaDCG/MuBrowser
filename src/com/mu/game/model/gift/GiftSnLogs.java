package com.mu.game.model.gift;

import java.util.HashMap;

public class GiftSnLogs {
   private HashMap receiveMap = new HashMap();

   public void addLog(int id) {
      this.receiveMap.put(id, true);
   }

   public boolean hasReceived(int id) {
      return this.receiveMap.containsKey(id);
   }
}
