package com.mu.io.game.packet.imp.dm;

import com.mu.game.CenterManager;
import com.mu.game.model.ui.dm.DynamicMenu;
import com.mu.game.model.ui.dm.DynamicMenuManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;

public class UpdateMenu extends WriteOnlyPacket {
   public UpdateMenu() {
      super(11101);
   }

   public static void allPlayerUpdate(int menuId) {
      DynamicMenu menu = DynamicMenuManager.getMenu(menuId);
      if (menu != null) {
         Iterator it = CenterManager.getAllPlayerIterator();

         while(it.hasNext()) {
            Player player = (Player)it.next();
            update(player, menuId);
         }

      }
   }

   public static void update(Player player, int menuId) {
      DynamicMenu menu = DynamicMenuManager.getMenu(menuId);
      if (menu != null) {
         try {
            UpdateMenu um = new UpdateMenu();
            int showNumber = menu.getShowNumber(player);
            um.writeByte(menu.getId());
            um.writeBoolean(menu.isShow(player));
            um.writeBoolean(true);
            um.writeShort(showNumber);
            um.writeBoolean(menu.hasEffect(player, showNumber));
            um.writeByte(menu.getOpenFunctionId());
            player.writePacket(um);
            um.destroy();
            um = null;
         } catch (Exception var5) {
            var5.printStackTrace();
         }

      }
   }

   public static void updateDungeonMenu(Player player, int dunId) {
      int menuId = -1;
      switch(dunId) {
      case 1:
         menuId = 1;
         break;
      case 2:
         menuId = 2;
         break;
      case 3:
         menuId = 7;
         break;
      case 4:
         menuId = 8;
         break;
      case 5:
         menuId = 12;
         break;
      case 6:
         menuId = 13;
      }

      if (menuId != -1) {
         update(player, menuId);
      }

   }
}
