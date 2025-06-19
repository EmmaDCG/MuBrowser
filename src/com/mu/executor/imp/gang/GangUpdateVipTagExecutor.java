package com.mu.executor.imp.gang;

import com.mu.db.manager.GangDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class GangUpdateVipTagExecutor extends Executable {
   public GangUpdateVipTagExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      GangDBManager.updateVipTag(packet.readLong(), packet.readUTF());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long id = ((Long)obj[0]).longValue();
      String tag = (String)obj[1];
      packet.writeLong(id);
      packet.writeUTF(tag);
   }
}
