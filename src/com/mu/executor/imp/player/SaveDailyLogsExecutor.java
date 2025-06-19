package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.dailyreceive.DailyReceiveLog;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveDailyLogsExecutor extends Executable {
   public SaveDailyLogsExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveDailyLogs(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      DailyReceiveLog log = (DailyReceiveLog)obj[1];
      packet.writeLong(rid);
      packet.writeShort(log.getType());
      packet.writeShort(log.getTimes());
      packet.writeLong(log.getDay());
      packet.writeByte(log.getHour());
   }
}
