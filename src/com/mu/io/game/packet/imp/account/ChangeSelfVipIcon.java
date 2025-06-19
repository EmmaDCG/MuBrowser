package com.mu.io.game.packet.imp.account;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueIcon;
import com.mu.io.game.packet.WriteOnlyPacket;

public class ChangeSelfVipIcon extends WriteOnlyPacket {
   public ChangeSelfVipIcon() {
      super(10046);
   }

   public static void change(Player player, BlueIcon icon) {
      try {
         ChangeSelfVipIcon ci = new ChangeSelfVipIcon();
         ci.writeShort(icon.getIcons()[0]);
         ci.writeShort(icon.getIcons()[1]);
         ci.writeUTF(icon.getPrivilege());
         player.writePacket(ci);
         ci.destroy();
         ci = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
