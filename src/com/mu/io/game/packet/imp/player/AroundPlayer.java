package com.mu.io.game.packet.imp.player;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.buff.AddBuff;
import com.mu.io.game.packet.imp.buff.ClientShowStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AroundPlayer extends WriteOnlyPacket {
   public AroundPlayer(ArrayList otherList, Player viewer) {
      super(10200);

      try {
         this.writeShort((short)otherList.size());
         Iterator var4 = otherList.iterator();

         while(var4.hasNext()) {
            Player p = (Player)var4.next();
            this.writeDetail(p, viewer);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   private void writeDetail(Player player, Player viewer) {
      Map map = player.getMap();

      try {
         map.writeRoleDetail(player, this, viewer);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void writeBuffs(Creature creature, boolean writeStatus, WriteOnlyPacket packet) throws IOException {
      List buffs = creature.getBuffManager().getShowBuffs(false);
      AddBuff.writeBuffs(buffs, packet);
      if (writeStatus) {
         ClientShowStatus.writeStatus(creature, packet);
      }

   }

   public AroundPlayer(Player player, Player viewer) {
      super(10200);

      try {
         this.writeShort(1);
         this.writeDetail(player, viewer);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
