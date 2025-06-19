package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class SendRedPacket extends ReadAndWritePacket {
   public SendRedPacket(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public SendRedPacket() {
      super(10638, (byte[])null);
   }

   public static void sendResult(Player player, int redId, int left) {
      try {
         SendRedPacket sp = new SendRedPacket();
         sp.writeByte(redId);
         sp.writeShort(left);
         player.writePacket(sp);
         sp.destroy();
         sp = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int id = this.readByte();
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doOperation(player, 18, Integer.valueOf(id));
      }

   }
}
