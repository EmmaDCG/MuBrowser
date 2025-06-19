package com.mu.io.game.packet.imp.dm;

import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;

public class AddMenu extends WriteOnlyPacket {
   public AddMenu() {
      super(11100);
   }

   public static void pushAllMenu(Player player) {
      AddMenu am = new AddMenu();

      try {
         HashMap menuMap = DynamicMenuManager.getMenuMap();
         am.writeByte(menuMap.size());
         Iterator it = menuMap.values().iterator();

         while(it.hasNext()) {
            DynamicMenu menu = (DynamicMenu)it.next();
            am.writeByte(menu.getId());
            am.writeByte(menu.getPosition());
            am.writeByte(menu.getRow());
            am.writeShort(menu.getIcons());
            int showNumber = menu.getShowNumber(player);
            am.writeBoolean(menu.isShow(player));
            am.writeBoolean(true);
            am.writeShort(showNumber);
            am.writeBoolean(menu.hasEffect(player, showNumber));
            am.writeByte(menu.getOpenFunctionId());
            am.writeUTF(menu.getTips());
            am.writeByte(menu.getHg());
            am.writeByte(menu.getSg());
         }

         player.writePacket(am);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      am.destroy();
      am = null;
   }
}
