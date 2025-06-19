package com.mu.io.game.packet.imp.guide;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PushRoleArrow extends WriteOnlyPacket {
   public PushRoleArrow() {
      super(10024);
   }

   public static void pushArrow(Player player, int id, String des) {
      try {
         PushRoleArrow pa = new PushRoleArrow();
         pa.writeByte(id);
         pa.writeUTF(des);
         player.writePacket(pa);
         pa.destroy();
         pa = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
