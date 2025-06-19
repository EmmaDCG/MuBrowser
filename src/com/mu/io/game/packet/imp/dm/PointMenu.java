package com.mu.io.game.packet.imp.dm;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PointMenu extends WriteOnlyPacket {
   public PointMenu() {
      super(11104);
   }

   public static void pointMenu(Player player, int menuId, String msg) {
      try {
         PointMenu pm = new PointMenu();
         pm.writeByte(menuId);
         pm.writeUTF(msg);
         player.writePacket(pm);
         pm.destroy();
         pm = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
