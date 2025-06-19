package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DrawZXReward extends ReadAndWritePacket {
   public DrawZXReward(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readInt();
      Player player = this.getPlayer();
      if (player != null) {
         Task task = player.getTaskManager().getCurZXTask();
         if (task != null) {
            if (task.getId() == id) {
               boolean success = player.getTaskManager().submit(id, false);
               if (success) {
                  this.writeInt(id);
                  this.writeByte(2);
                  player.writePacket(this);
                  TaskInform.sendMsgZXDetail(player, player.getTaskManager().getCurZXTask());
               }

            }
         }
      }
   }
}
