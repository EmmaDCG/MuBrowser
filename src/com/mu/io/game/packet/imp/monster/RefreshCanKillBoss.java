package com.mu.io.game.packet.imp.monster;

import com.mu.game.CenterManager;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;
import java.util.Iterator;

public class RefreshCanKillBoss extends WriteOnlyPacket {
   public RefreshCanKillBoss() {
      super(10303);
   }

   public static void canKillRefresh(ArrayList list, Player player) {
      try {
         RefreshCanKillBoss pk = new RefreshCanKillBoss();
         RequestBossInfo.writeWorldBoss(pk, list, player);
         player.writePacket(pk);
         pk.destroy();
         pk = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void brodcast() {
      ArrayList list = WorldBossManager.getCanKillList();
      Iterator it = CenterManager.getAllPlayerIterator();

      while(it.hasNext()) {
         Player player = (Player)it.next();
         if (!player.isNew()) {
            canKillRefresh(list, player);
         }
      }

      list.clear();
      list = null;
   }
}
