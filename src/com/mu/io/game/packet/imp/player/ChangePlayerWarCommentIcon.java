package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangePlayerWarCommentIcon extends WriteOnlyPacket {
   public ChangePlayerWarCommentIcon() {
      super(10028);
   }

   public static void change(Player player) {
      ChangePlayerWarCommentIcon pw = new ChangePlayerWarCommentIcon();

      try {
         pw.writeDouble((double)player.getID());
         pw.writeShort(player.getWarCommentNameIcon());
         player.getMap().sendPacketToAroundPlayer(pw, player, false);
         pw.destroy();
         pw = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
