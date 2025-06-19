package com.mu.io.game.packet.imp.gang;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.io.IOException;

public class ReceiveWelfare extends ReadAndWritePacket {
   public ReceiveWelfare(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ReceiveWelfare(boolean isReceived) {
      super(10622, (byte[])null);

      try {
         this.writeBoolean(isReceived);
      } catch (IOException var3) {
         var3.printStackTrace();
      }

   }

   public static void receiveResult(Player player, boolean isReceived) {
      ReceiveWelfare rw = new ReceiveWelfare(isReceived);
      player.writePacket(rw);
      rw.destroy();
      rw = null;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Gang gang = player.getGang();
      if (gang != null) {
         gang.doOperation(this.getPlayer(), 9);
      }
   }
}
