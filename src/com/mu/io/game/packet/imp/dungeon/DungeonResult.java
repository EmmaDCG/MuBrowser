package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class DungeonResult extends WriteOnlyPacket {
   public DungeonResult() {
      super(12008);
   }

   public static void faild(Player player, int templateId) {
      try {
         DungeonResult ds = new DungeonResult();
         ds.writeByte(templateId);
         ds.writeBoolean(false);
         player.writePacket(ds);
         ds.destroy();
         ds = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static DungeonResult getFailResult(int templateId) {
      DungeonResult dr = new DungeonResult();

      try {
         dr.writeByte(templateId);
         dr.writeBoolean(false);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      return dr;
   }
}
