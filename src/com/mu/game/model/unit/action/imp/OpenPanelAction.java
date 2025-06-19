package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.sys.OpenPanel;

public class OpenPanelAction extends XmlAction {
   private int bigId;
   private int smallId;

   public OpenPanelAction(int id) {
      super(id);
   }

   public void doAction(Player player) {
      OpenPanel.open(player, this.bigId, this.smallId);
   }

   public void destroy() {
   }

   public void initAction(String value) {
      String[] tmp = value.split(",");
      this.bigId = Integer.parseInt(tmp[0]);
      this.smallId = Integer.parseInt(tmp[1]);
   }
}
