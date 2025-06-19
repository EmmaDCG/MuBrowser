package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class SubmitTask extends ReadAndWritePacket {
   public SubmitTask(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int taskId = this.readInt();
      Player player = this.getPlayer();
      PlayerTaskManager ptm = null;
      if (player != null && (ptm = player.getTaskManager()) != null) {
         if (ptm.submit(taskId, false)) {
            this.writeInt(taskId);
            player.writePacket(this);
         }

      }
   }
}
