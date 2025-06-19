package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.SpiritDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;

public class InitHallows extends ReadAndWritePacket {
   public InitHallows(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static WriteOnlyPacket getInitSpirit(long roleID) {
      int[] entries = SpiritDBManager.searchHallows(roleID);
      WriteOnlyPacket packet = new InitHallows(entries);
      return packet;
   }

   public InitHallows(int[] entries) {
      super(59012, (byte[])null);

      try {
         this.writeShort(entries[0]);
         this.writeShort(entries[1]);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int rank = this.readShort();
      int level = this.readShort();
      player.getHallowsManager().init(rank, level);
   }
}
