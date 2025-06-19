package com.mu.executor.imp.player;

import com.mu.db.manager.SpiritDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.hallow.HallowManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveHallowsExecutor extends Executable {
   public SaveHallowsExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      SpiritDBManager.saveHallows(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      HallowManager manager = player.getHallowsManager();
      long roleID = player.getID();
      int rank = manager.getRank();
      int level = manager.getLevel();
      packet.writeLong(roleID);
      packet.writeShort(rank);
      packet.writeShort(level);
   }
}
