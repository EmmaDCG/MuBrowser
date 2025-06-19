package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SavePreviewExecutor extends Executable {
   public SavePreviewExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      int preview = packet.readShort();
      PlayerDBManager.savePreview(rid, preview);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int preview = ((Integer)obj[1]).intValue();
      packet.writeLong(rid);
      packet.writeShort(preview);
   }
}
