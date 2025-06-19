package com.mu.executor.imp.player;

import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveIngotExecutor extends Executable {
   public SaveIngotExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.savePlayerIngot(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      String userName = (String)obj[0];
      int ingot = ((Integer)obj[1]).intValue();
      int serverId = ((Integer)obj[2]).intValue();
      packet.writeUTF(userName);
      packet.writeInt(ingot);
      packet.writeInt(serverId);
   }

   public static void saveIngot(Player player) {
      WriteOnlyPacket packet = Executor.SaveIngot.toPacket(player.getUserName(), player.getIngot(), player.getUser().getServerID());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
      GlobalLogDBManager.saveIngot(player.getIngot(), player.getUserName(), player.getUser().getServerID());
   }
}
