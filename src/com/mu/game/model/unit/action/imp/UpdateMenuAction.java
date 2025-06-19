package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.UpdateMenu;

public class UpdateMenuAction extends XmlAction {
   private int menuId;

   public UpdateMenuAction(int id) {
      super(id);
   }

   public void doAction(Player player) {
      UpdateMenu.update(player, this.menuId);
   }

   public void destroy() {
   }

   public void initAction(String value) {
      this.menuId = Integer.parseInt(value);
   }
}
