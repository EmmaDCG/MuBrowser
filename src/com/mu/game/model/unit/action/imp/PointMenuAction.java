package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.dm.PointMenu;

public class PointMenuAction extends XmlAction {
   private int menuId;
   private String msg;

   public PointMenuAction(int id) {
      super(id);
   }

   public void doAction(Player player) {
      PointMenu.pointMenu(player, this.menuId, this.msg);
   }

   public void destroy() {
      this.msg = null;
   }

   public void initAction(String value) {
      String[] tmp = value.split("##");
      this.menuId = Integer.parseInt(tmp[0]);
      this.msg = tmp[1];
   }
}
