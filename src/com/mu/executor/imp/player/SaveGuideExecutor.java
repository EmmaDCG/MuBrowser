package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveGuideExecutor extends Executable {
   public SaveGuideExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveArrowGuide(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int id = ((Integer)obj[1]).intValue();
      int times = ((Integer)obj[2]).intValue();
      packet.writeLong(rid);
      packet.writeByte(id);
      packet.writeByte(times);
   }
}
