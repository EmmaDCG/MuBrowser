package com.mu.io.game.packet.imp.title;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PlayerChangeTitle extends WriteOnlyPacket {
   public PlayerChangeTitle() {
      super(15004);
   }

   public static void change(Player player, int tid) {
      try {
         PlayerChangeTitle pt = new PlayerChangeTitle();
         pt.writeDouble((double)player.getID());
         pt.writeByte(tid);
         player.getMap().sendPacketToAroundPlayer(pt, player, true);
         pt.destroy();
         pt = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
