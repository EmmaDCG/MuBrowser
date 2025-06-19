package com.mu.io.game.packet.imp.player;

import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangePlayerGangName extends WriteOnlyPacket {
   public ChangePlayerGangName(long rid, String name) {
      super(10252);

      try {
         this.writeDouble((double)rid);
         this.writeUTF(name);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void change(Player player) {
      String gangName = "";
      Gang gang = player.getGang();
      if (gang != null) {
         gangName = GangManager.getViewGangName(gang);
      }

      ChangePlayerGangName cn = new ChangePlayerGangName(player.getID(), gangName);
      player.getMap().sendPacketToAroundPlayer(cn, player, true);
      cn.destroy();
      cn = null;
   }

   public static void clearName(Player player) {
      ChangePlayerGangName cn = new ChangePlayerGangName(player.getID(), "");
      player.getMap().sendPacketToAroundPlayer(cn, player, true);
      cn.destroy();
      cn = null;
   }
}
