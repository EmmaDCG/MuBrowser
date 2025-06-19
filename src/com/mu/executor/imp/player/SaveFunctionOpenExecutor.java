package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveFunctionOpenExecutor extends Executable {
   public SaveFunctionOpenExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveFunctionOpen(packet.readLong(), packet.readShort(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int id = ((Integer)obj[1]).intValue();
      long time = ((Long)obj[2]).longValue();
      packet.writeLong(rid);
      packet.writeShort(id);
      packet.writeLong(time);
   }

   public static void saveFunctionOpen(Player player, int functionId, long time) {
      WriteOnlyPacket packet = Executor.SaveFunctionOpen.toPacket(player.getID(), functionId, time);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
