package com.mu.game.model.unit.player;

import java.util.HashMap;

public class PlayerTimeLimit {
   private HashMap buttomMessageMap = new HashMap();

   public boolean canPushButtom(int msgId) {
      Long lastTime = (Long)this.buttomMessageMap.get(msgId);
      if (lastTime != null && System.currentTimeMillis() - lastTime.longValue() <= 2000L) {
         return false;
      } else {
         this.buttomMessageMap.put(msgId, System.currentTimeMillis());
         return true;
      }
   }
}
