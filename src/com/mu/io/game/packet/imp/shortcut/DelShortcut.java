package com.mu.io.game.packet.imp.shortcut;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class DelShortcut extends ReadAndWritePacket {
   public DelShortcut(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public DelShortcut(int position) {
      super(10212, (byte[])null);

      try {
         this.writeByte(position);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int position = this.readByte();
      int result = player.getShortcut().delShortcut(position);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static void sendToClient(Player player, int position) {
      DelShortcut dsc = new DelShortcut(position);
      player.writePacket(dsc);
      dsc.destroy();
      dsc = null;
   }
}
