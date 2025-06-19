package com.mu.io.game.packet.imp.gang;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class GangBattleInvitation extends WriteOnlyPacket {
   public GangBattleInvitation(String text) {
      super(10633);

      try {
         this.writeUTF(text);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void push(Player player) {
      GangBattleInvitation gi = getGangBattleInvitation();
      player.writePacket(gi);
      gi.destroy();
      gi = null;
   }

   public static GangBattleInvitation getGangBattleInvitation() {
      String s = DungeonManager.getLuolanManager().getTemplate().getInvitationText();
      GangBattleInvitation gi = new GangBattleInvitation(s);
      return gi;
   }
}
