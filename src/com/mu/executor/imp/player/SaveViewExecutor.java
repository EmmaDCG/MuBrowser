package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.top.TopManager;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveViewExecutor extends Executable {
   public SaveViewExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long id = packet.readLong();
      byte[] bytes = new byte[packet.remaining()];
      packet.readBytes(bytes);
      PlayerDBManager.saveRoleView(id, bytes);
      if (TopManager.hasView(id)) {
         TopManager.addView(id, bytes);
      }

      bytes = null;
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      packet.writeLong(player.getID());
      packet.writeBytes(player.getView());
   }
}
