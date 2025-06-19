package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.Task;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class OpenZXDetail extends ReadAndWritePacket {
   public OpenZXDetail(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int id = this.readInt();
      Player player = this.getPlayer();
      if (player != null) {
         Task task = player.getTaskManager().getCurZXTask();
         if (id == task.getId()) {
            TaskInform.sendMsgZXDetail(player, task);
         }
      }
   }
}
