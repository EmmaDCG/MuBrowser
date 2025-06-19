package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ClosePanelByServer extends WriteOnlyPacket {
   public ClosePanelByServer() {
      super(13006);
   }

   public static void closePanel(Player player, String name) {
      ClosePanelByServer cs = new ClosePanelByServer();

      try {
         cs.writeByte(1);
         player.writePacket(cs);
         cs.destroy();
         cs = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
