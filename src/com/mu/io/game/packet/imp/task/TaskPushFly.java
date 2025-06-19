package com.mu.io.game.packet.imp.task;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class TaskPushFly extends WriteOnlyPacket {
   public TaskPushFly() {
      super(40025);
   }

   public static void pushFly(Player player, int taskId, int index) {
      try {
         TaskPushFly pf = new TaskPushFly();
         pf.writeInt(taskId);
         pf.writeByte(index);
         player.writePacket(pf);
         pf.destroy();
         pf = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
