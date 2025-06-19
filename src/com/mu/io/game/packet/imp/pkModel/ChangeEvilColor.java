package com.mu.io.game.packet.imp.pkModel;

import com.mu.game.model.stats.StatChange;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.pkMode.EvilEnum;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangeEvilColor extends WriteOnlyPacket {
   public ChangeEvilColor(long roleID, int fontID) {
      super(32012);

      try {
         this.writeDouble((double)roleID);
         this.writeByte(fontID);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void sendWhenSelfChange(Player player, EvilEnum evilEnum) {
      evilEnum = evilEnum == EvilEnum.Evil_White ? EvilEnum.Evil_Orange : evilEnum;
      ChangeEvilColor otherColor;
      if (StatChange.isSendStat(player)) {
         otherColor = new ChangeEvilColor(player.getID(), evilEnum.getFont());
         player.writePacket(otherColor);
         otherColor.destroy();
         otherColor = null;
      }

      if (player.isEnterMap()) {
         evilEnum = evilEnum == EvilEnum.Evil_Orange ? EvilEnum.Evil_White : evilEnum;
         otherColor = new ChangeEvilColor(player.getID(), evilEnum.getFont());
         player.getMap().sendPacketToAroundPlayer(otherColor, player, false);
         otherColor.destroy();
         otherColor = null;
      }

   }

   public static void sendWhenSwitchArea(Player player, long otherRoleID, EvilEnum evilEnum) {
      ChangeEvilColor otherColors = new ChangeEvilColor(otherRoleID, evilEnum.getEvilId());
      player.writePacket(otherColors);
      otherColors.destroy();
      otherColors = null;
   }
}
