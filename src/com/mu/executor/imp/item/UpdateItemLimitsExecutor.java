package com.mu.executor.imp.item;

import com.mu.db.manager.ItemDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateItemLimitsExecutor extends Executable {
   public UpdateItemLimitsExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      ItemDBManager.updateItemLimit(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      int modelID = ((Integer)obj[1]).intValue();
      int count = ((Integer)obj[2]).intValue();
      packet.writeLong(player.getID());
      packet.writeInt(modelID);
      packet.writeInt(count);
   }
}
