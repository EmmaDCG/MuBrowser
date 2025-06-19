package com.mu.executor.imp.dun;

import com.mu.db.manager.DungeonDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveDunLogExecutor extends Executable {
   public SaveDunLogExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      DungeonDBManager.saveLog(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      DunLogs logs = (DunLogs)obj[1];
      packet.writeLong(rid);
      packet.writeByte(logs.getDunId());
      packet.writeShort(logs.getSmallId());
      packet.writeInt(logs.getFinishTimes());
      packet.writeLong(logs.getLastFinishTime());
      packet.writeLong(logs.getSaveDay());
      packet.writeLong(logs.getBaseExp());
      packet.writeInt(logs.getBaseMoney());
      packet.writeBoolean(logs.isHasReceived());
      packet.writeInt(logs.getVipLevel());
   }
}
