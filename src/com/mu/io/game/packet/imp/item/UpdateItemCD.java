package com.mu.io.game.packet.imp.item;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class UpdateItemCD extends WriteOnlyPacket {
   public UpdateItemCD(int type, int remainCD) {
      super(20019);

      try {
         this.writeByte(type);
         this.writeInt(remainCD);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public static void sendToClient(Player player, int type, int remainCD) {
      UpdateItemCD uicd = new UpdateItemCD(type, remainCD);
      player.writePacket(uicd);
      uicd.destroy();
      uicd = null;
   }
}
