package com.mu.executor.imp.player;

import com.mu.db.manager.GlobalLogDBManager;
import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveBindIngotExector extends Executable {
   public SaveBindIngotExector(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.savePlayerBindIngot(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      int bindIngot = ((Integer)obj[0]).intValue();
      long roleID = ((Long)obj[1]).longValue();
      packet.writeInt(bindIngot);
      packet.writeLong(roleID);
   }

   public static void saveBindIngot(Player player) {
      WriteOnlyPacket packet = Executor.SaveBindIngot.toPacket(player.getBindIngot(), player.getID());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
      GlobalLogDBManager.updateBindIngot(player.getID(), player.getBindIngot());
   }
}
