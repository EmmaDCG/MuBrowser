package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class SwitchMap extends WriteOnlyPacket {
   public SwitchMap() {
      super(10107);
   }

   public static void playerSwitchMapDefault(Player player, int mapID, int x, int y) {
      try {
         SwitchMap sm = new SwitchMap();
         sm.writeBoolean(true);
         sm.writeShort(mapID);
         sm.writeInt(x);
         sm.writeInt(y);
         player.writePacket(sm);
         sm.destroy();
         sm = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
