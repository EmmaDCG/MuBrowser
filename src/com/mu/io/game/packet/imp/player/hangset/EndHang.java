package com.mu.io.game.packet.imp.player.hangset;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class EndHang extends ReadAndWritePacket {
   public EndHang(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public EndHang() {
      super(10014, (byte[])null);

      try {
         this.writeBoolean(true);
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      player.getGameHang().setInHanging(false);
   }

   public static void sendToClient(Player player) {
      EndHang hang = new EndHang();
      player.writePacket(hang);
      hang.destroy();
      hang = null;
   }
}
