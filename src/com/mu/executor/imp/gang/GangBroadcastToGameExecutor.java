package com.mu.executor.imp.gang;

import com.mu.executor.Executable;
import com.mu.game.model.gang.Gang;
import com.mu.game.model.gang.GangManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class GangBroadcastToGameExecutor extends Executable {
   public GangBroadcastToGameExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long gangId = packet.readLong();
      int opCode = packet.readUnsignedShort();
      byte[] bytes = new byte[packet.remaining()];
      WriteOnlyPacket wp = new WriteOnlyPacket(opCode);
      wp.writeBytes(bytes);
      Gang gang = GangManager.getGang(gangId);
      if (gang != null) {
         gang.doBroadcastOnlyGame(wp);
      }

      wp.destroy();
      wp = null;
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long gangId = ((Long)obj[0]).longValue();
      WriteOnlyPacket wp = (WriteOnlyPacket)obj[1];
      packet.writeLong(gangId);
      packet.writeShort(wp.getOpcode());
      packet.writeBytes(wp.getWriteBuf(), 0, wp.getWriteLength());
   }
}
