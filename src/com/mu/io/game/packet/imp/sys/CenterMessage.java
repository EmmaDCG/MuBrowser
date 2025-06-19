package com.mu.io.game.packet.imp.sys;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class CenterMessage extends WriteOnlyPacket {
   public CenterMessage() {
      super(1009);
   }

   public CenterMessage(String text) {
      super(1009);

      try {
         this.writeUTF(text);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void writeMessage(Player player, String msg) {
      CenterMessage cm = new CenterMessage();

      try {
         cm.writeUTF(msg);
         player.writePacket(cm);
         cm.destroy();
         cm = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
