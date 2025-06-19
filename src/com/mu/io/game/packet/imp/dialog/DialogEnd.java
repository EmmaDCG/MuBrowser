package com.mu.io.game.packet.imp.dialog;

import com.mu.game.model.task.PlayerTaskManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DialogEnd extends ReadAndWritePacket {
   public DialogEnd(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      long npcId = (long)this.readDouble();
      int taskId = this.readInt();
      Player player = this.getPlayer();
      if (player != null) {
         PlayerTaskManager ptm = player.getTaskManager();
         if (ptm != null) {
            ptm.onEventDialogEnd(taskId, npcId);
         }

         CloseDialog.close(player);
      }
   }
}
