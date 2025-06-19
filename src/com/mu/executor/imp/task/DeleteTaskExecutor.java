package com.mu.executor.imp.task;

import com.mu.db.manager.TaskDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.io.IOException;

public class DeleteTaskExecutor extends Executable {
   public DeleteTaskExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int taskId = packet.readInt();
      TaskDBManager.delete(roleId, taskId);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         long roleId = ((Long)obj[0]).longValue();
         int taskId = ((Integer)obj[1]).intValue();
         packet.writeLong(roleId);
         packet.writeInt(taskId);
      } catch (IOException var6) {
         var6.printStackTrace();
      }

   }
}
