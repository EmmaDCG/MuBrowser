package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.SpiritDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;

public class InitLuckyTurnTable extends ReadAndWritePacket {
   public InitLuckyTurnTable(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitLuckyTurnTable(int turnTableCount) {
      super(59013, (byte[])null);

      try {
         this.writeByte(turnTableCount);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static WriteOnlyPacket getInitLuckyTurnTable(String userName, int serverID) {
      int turnTableCount = SpiritDBManager.searchLuckyTurnTable(userName, serverID);
      WriteOnlyPacket packet = new InitLuckyTurnTable(turnTableCount);
      return packet;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int count = this.readByte();
      player.setTurnTableCount(count);
   }
}
