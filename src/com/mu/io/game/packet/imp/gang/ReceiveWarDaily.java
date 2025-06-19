package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ReceiveWarDaily extends ReadAndWritePacket {
   public ReceiveWarDaily(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ReceiveWarDaily(boolean b, int status) {
      super(10630, (byte[])null);

      try {
         this.writeBoolean(b);
         this.writeByte(status);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         gang.receiveWarDaily(player);
      }

   }

   public static void receiveResult(Player player, boolean b, int receiveStatus) {
      ReceiveWarDaily rw = new ReceiveWarDaily(b, receiveStatus);
      player.writePacket(rw);
      rw.destroy();
      rw = null;
   }
}
