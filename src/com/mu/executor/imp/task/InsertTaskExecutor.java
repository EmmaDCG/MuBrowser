package com.mu.executor.imp.task;

import com.mu.db.manager.TaskDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.task.Task;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class InsertTaskExecutor extends Executable {
   public InsertTaskExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      boolean deleteClazz = packet.readBoolean();
      long roleId = packet.readLong();
      int taskId = packet.readInt();
      int clazz = packet.readByte();
      int state = packet.readByte();
      String rateStr = packet.readUTF();
      if (deleteClazz) {
         TaskDBManager.insertAndDelete(roleId, taskId, clazz, state, rateStr);
      } else {
         TaskDBManager.insert(roleId, taskId, clazz, state, rateStr);
      }

   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Task task = (Task)obj[0];
         boolean deleteClazz = ((Boolean)obj[1]).booleanValue();
         packet.writeBoolean(deleteClazz);
         packet.writeLong(task.getOwner().getID());
         packet.writeInt(task.getId());
         packet.writeByte(task.getClazz().getValue());
         packet.writeByte(task.getState().getValue());
         packet.writeUTF(task.getRateStr());
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
