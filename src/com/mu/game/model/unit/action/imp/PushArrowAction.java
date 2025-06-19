package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.guide.PushRoleArrow;

public class PushArrowAction extends XmlAction {
   private int arrowId = 0;
   private String content = "";

   public PushArrowAction(int id) {
      super(id);
   }

   public void doAction(Player player) {
      PushRoleArrow.pushArrow(player, this.arrowId, this.content);
   }

   public void destroy() {
      this.content = null;
   }

   public void initAction(String value) {
      String[] tmp = value.split("#");
      this.arrowId = Integer.parseInt(tmp[0]);
      this.content = tmp[1];
   }
}
