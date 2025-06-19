package com.mu.executor.imp.player;

import com.mu.db.manager.SpiritDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveLuckyTurnExecutor extends Executable {
   public SaveLuckyTurnExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      SpiritDBManager.saveLuckyTurnTable(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      packet.writeUTF(player.getUserName());
      packet.writeInt(player.getUser().getServerID());
      packet.writeByte(player.getTurnTableCount());
   }
}
