package com.mu.executor.imp.log;

import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.schedule.log.ReduceIngotLogTask;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;

public class ReduceBindIngotLogExecutor extends Executable {
   public ReduceBindIngotLogExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ReduceIngotLogTask.addBindIngotLog(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int ingot = ((Integer)obj[1]).intValue();
      int reduceType = ((Integer)obj[2]).intValue();
      String changeDetail = (String)obj[3];
      long roleID = player.getID();
      String userName = player.getUserName();
      String roleName = player.getName();
      int roleServerID = player.getUser().getServerID();
      packet.writeLong(roleID);
      packet.writeUTF(roleName);
      packet.writeUTF(userName);
      packet.writeInt(ingot);
      packet.writeShort(reduceType);
      packet.writeUTF(Time.getTimeStr());
      packet.writeInt(roleServerID);
      packet.writeUTF(changeDetail);
   }
}
