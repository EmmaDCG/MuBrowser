package com.mu.executor.imp.task;

import com.mu.db.manager.TaskDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.task.Task;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateTaskExecutor extends Executable {
   public UpdateTaskExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int taskId = packet.readInt();
      int state = packet.readByte();
      String rateStr = packet.readUTF();
      TaskDBManager.update(roleId, taskId, state, rateStr);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Task task = (Task)obj[0];
      packet.writeLong(task.getOwner().getID());
      packet.writeInt(task.getId());
      packet.writeByte(task.getState().getValue());
      packet.writeUTF(task.getRateStr());
   }
}
