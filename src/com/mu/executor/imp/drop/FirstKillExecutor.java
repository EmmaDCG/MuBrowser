package com.mu.executor.imp.drop;

import com.mu.db.manager.DropDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class FirstKillExecutor extends Executable {
   public FirstKillExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      DropDBManager.addFirstKill(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int killsign = ((Integer)obj[1]).intValue();
      packet.writeLong(player.getID());
      packet.writeInt(killsign);
      packet.writeInt(1);
   }
}
