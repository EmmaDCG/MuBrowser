package com.mu.io.game.packet.imp.player.hangset;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class StartHang extends ReadAndWritePacket {
   public StartHang(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public StartHang(boolean inHanging) {
      super(10013, (byte[])null);

      try {
         this.writeBoolean(inHanging);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      boolean canHang = true;
      if (canHang) {
         player.getGameHang().setInHanging(true);
         player.setNeedtoPopPKEvil(true);
      } else {
         player.getGameHang().setInHanging(false);
      }

      this.writeBoolean(player.getGameHang().isInHanging());
      player.writePacket(this);
      if (!canHang) {
         SystemMessage.writeMessage(player, 1024);
      }

   }

   public static void start(Player player) {
      try {
         player.getGameHang().setInHanging(true);
         StartHang hang = new StartHang(true);
         player.writePacket(hang);
         hang.destroy();
         hang = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void stop(Player player) {
      try {
         player.getGameHang().setInHanging(false);
         StartHang hang = new StartHang(false);
         player.writePacket(hang);
         hang.destroy();
         hang = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public static void sendToClient(Player player) {
      try {
         StartHang hang = new StartHang(player.getGameHang().isInHanging());
         player.writePacket(hang);
         hang.destroy();
         hang = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
