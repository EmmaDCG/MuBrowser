package com.mu.io.game.packet.imp.tanxian;

import com.mu.game.model.tanxian.ClueInfo;
import com.mu.game.model.tanxian.TanXianConfigManager;
import com.mu.game.model.task.Task;
import com.mu.game.model.task.TaskState;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.io.IOException;

public class TanXianStart extends ReadAndWritePacket {
   public TanXianStart(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int type = this.readByte();
      int id = this.readShort();
      Player player = this.getPlayer();
      if (player != null) {
         player.getTanXianManager().start(type, id);
         sendMessageResult(player);
      }
   }

   public static void sendMessageResult(Player player) {
      TanXianStart packet = new TanXianStart(48004, (byte[])null);
      Task task = player.getTaskManager().getCurTXTask();
      boolean success = task != null && task.is(TaskState.RUN);

      try {
         packet.writeBoolean(success);
         if (success) {
            ClueInfo info = TanXianConfigManager.getClueInfoByTask(task.getId());
            packet.writeShort(info.getMapId());
            packet.writeInt(info.getX());
            packet.writeInt(info.getY());
            packet.writeInt(info.getTemplateId());
         }

         player.writePacket(packet);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

      packet.destroy();
      packet = null;
   }
}
