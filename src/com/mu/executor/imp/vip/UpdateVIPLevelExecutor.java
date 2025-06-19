package com.mu.executor.imp.vip;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateVIPLevelExecutor extends Executable {
   public UpdateVIPLevelExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long roleId = packet.readLong();
      int vipLevel = packet.readShort();
      PlayerDBManager.updateVIPLevel(roleId, vipLevel);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      packet.writeLong(player.getID());
      packet.writeShort(player.getVIPLevel());
   }
}
