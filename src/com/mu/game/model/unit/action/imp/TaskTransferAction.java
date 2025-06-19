package com.mu.game.model.unit.action.imp;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.task.TaskPushFly;

public class TaskTransferAction extends XmlAction {
   private int index = 1;

   public TaskTransferAction(int id) {
      super(id);
   }

   public void doAction(Player player) {
      TaskPushFly.pushFly(player, this.getId(), this.index);
   }

   public void destroy() {
   }

   public void initAction(String value) {
      this.index = Integer.parseInt(value);
   }

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }
}
