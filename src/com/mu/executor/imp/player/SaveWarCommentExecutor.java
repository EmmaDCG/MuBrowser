package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveWarCommentExecutor extends Executable {
   public SaveWarCommentExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveWarComment(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int wc = ((Integer)obj[1]).intValue();
      long time = ((Long)obj[2]).longValue();
      packet.writeLong(rid);
      packet.writeShort(wc);
      packet.writeLong(time);
   }
}
