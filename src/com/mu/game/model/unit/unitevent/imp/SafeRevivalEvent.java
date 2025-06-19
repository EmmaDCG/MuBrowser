package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.player.PlayerRevival;

public class SafeRevivalEvent extends Event {
   private Map targeMap = null;
   private int time = 10;

   public SafeRevivalEvent(Player owner, Map targetMap, int countDown) {
      super(owner);
      this.checkrate = 200;
      this.targeMap = targetMap;
      this.time = countDown;
   }

   public void work(long now) throws Exception {
      if (!((Player)this.getOwner()).isDie()) {
         this.setEnd(true);
      } else {
         if (now - ((Player)this.getOwner()).getDieTime() >= (long)this.time * 1000L) {
            Player player = (Player)this.getOwner();
            player.revival();
            PlayerRevival revival = new PlayerRevival(player.getID());
            player.writePacket(revival);
            Map map = player.getMap();
            if (this.targeMap == null) {
               this.targeMap = map;
            }

            player.switchMap(this.targeMap, this.targeMap.getDefaultPoint((Player)this.getOwner()));
            revival.destroy();
            revival = null;
            this.setEnd(true);
         }

      }
   }

   public Status getStatus() {
      return Status.SAFEREVIVAL;
   }

   public void destroy() {
      super.destroy();
      this.targeMap = null;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
