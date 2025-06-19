package com.mu.executor.imp.task;

import com.mu.db.manager.TaskDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.io.IOException;

public class DeleteTaskClazzExecutor extends Executable {
   public DeleteTaskClazzExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int clazz = packet.readInt();
      TaskDBManager.deleteClazz(roleId, clazz);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         packet.writeLong(((Long)obj[0]).longValue());
         packet.writeInt(((Integer)obj[1]).intValue());
      } catch (IOException var4) {
         var4.printStackTrace();
      }

   }
}
