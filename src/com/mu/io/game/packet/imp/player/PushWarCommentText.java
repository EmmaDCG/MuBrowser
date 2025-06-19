package com.mu.io.game.packet.imp.player;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PushWarCommentText extends WriteOnlyPacket {
   public PushWarCommentText() {
      super(10025);
   }

   public static void pushText(Player player) {
      try {
         PushWarCommentText pt = new PushWarCommentText();
         pt.writeUTF(player.getWarCommentText());
         pt.writeShort(player.getWarCommentNameIcon());
         player.writePacket(pt);
         pt.destroy();
         pt = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }
}
