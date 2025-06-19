package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RequestDungeonInfo extends ReadAndWritePacket {
   public RequestDungeonInfo(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public RequestDungeonInfo() {
      super(12001, (byte[])null);
   }

   public void process() throws Exception {
      int id = this.readUnsignedByte();
      Player player = this.getPlayer();
      writeDunInfo(player, id);
   }

   public static void writeDunInfo(Player player, int id) {
      try {
         DungeonTemplateFactory.getTemplate(id).writeDungeonInfo(player);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
