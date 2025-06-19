package com.mu.executor.imp.buff;

import com.mu.db.manager.BuffDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class DeleteBuffExecutor extends Executable {
   public DeleteBuffExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      BuffDBManager.delBuff(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int buffModelID = ((Integer)obj[1]).intValue();
      packet.writeLong(player.getID());
      packet.writeInt(buffModelID);
   }
}
