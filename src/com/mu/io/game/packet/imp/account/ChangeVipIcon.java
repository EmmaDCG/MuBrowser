package com.mu.io.game.packet.imp.account;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueIcon;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangeVipIcon extends WriteOnlyPacket {
   public ChangeVipIcon() {
      super(10045);
   }

   public static void changIcons(Player player, BlueIcon icon) {
      ChangeVipIcon ci = new ChangeVipIcon();

      try {
         ci.writeDouble((double)player.getID());
         ci.writeShort(icon.getIcons()[0]);
         ci.writeShort(icon.getIcons()[1]);
         player.getMap().sendPacketToAroundPlayer(ci, player, false);
         ci.destroy();
         ci = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
