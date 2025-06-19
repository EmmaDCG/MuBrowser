package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.ArrayList;

public class SelfExternal extends WriteOnlyPacket {
   public SelfExternal() {
      super(10209);
   }

   public static void sendToClient(Player player) {
      SelfExternal se = new SelfExternal();

      try {
         ArrayList externalList = player.getCurrentExternal();
         ExternalChange.writeRoleExternal(externalList, se);
         externalList.clear();
         externalList = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      player.writePacket(se);
      se.destroy();
      se = null;
   }

   public static void sendToClient(Player player, ArrayList list) {
      SelfExternal se = new SelfExternal();

      try {
         ExternalChange.writeRoleExternal(list, se);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      player.writePacket(se);
      se.destroy();
      se = null;
   }
}
