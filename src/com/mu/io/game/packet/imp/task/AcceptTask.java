package com.mu.io.game.packet.imp.task;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class AcceptTask extends ReadAndWritePacket {
   public AcceptTask(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int taskId = this.readInt();
      Player player = this.getPlayer();
      PlayerTaskManager ptm = player.getTaskManager();
      ptm.accept(taskId, false);
   }
}
