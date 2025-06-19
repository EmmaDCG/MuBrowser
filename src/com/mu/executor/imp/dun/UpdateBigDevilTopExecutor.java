package com.mu.executor.imp.dun;

import com.mu.db.manager.TopDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateBigDevilTopExecutor extends Executable {
   public UpdateBigDevilTopExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      TopDBManager.updateBigDevilTop(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeUTF((String)obj[1]);
      packet.writeByte(6);
      packet.writeByte(((Integer)obj[2]).intValue());
      packet.writeLong(((Long)obj[3]).longValue());
      packet.writeLong(((Long)obj[4]).longValue());
   }

   public static void updateTop(Player player, long exp, int level, long time) {
      WriteOnlyPacket packet = Executor.UpdateBigDevilTop.toPacket(player.getID(), player.getName(), level, exp, time);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
