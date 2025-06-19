package com.mu.io.game.packet.imp.material;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class CollectResult extends WriteOnlyPacket {
   public CollectResult() {
      super(10703);
   }

   public static void sendResult(Player player, int templateId, boolean bool) {
      try {
         CollectResult cr = new CollectResult();
         cr.writeInt(templateId);
         cr.writeBoolean(bool);
         player.writePacket(cr);
         cr.destroy();
         cr = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
