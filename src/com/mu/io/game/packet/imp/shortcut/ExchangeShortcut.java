package com.mu.io.game.packet.imp.shortcut;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.shortcut.Shortcut;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ExchangeShortcut extends ReadAndWritePacket {
   public ExchangeShortcut(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ExchangeShortcut(int fromp, int targetp) {
      super(10214, (byte[])null);

      try {
         this.writeByte(fromp);
         this.writeByte(targetp);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player, int fromp, int targetp) {
      ExchangeShortcut esc = new ExchangeShortcut(fromp, targetp);
      player.writePacket(esc);
      esc.destroy();
      esc = null;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int fp = this.readByte();
      int tp = this.readByte();
      Shortcut shortcut = player.getShortcut();
      int result = shortcut.changePosition(fp, tp);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }
}
