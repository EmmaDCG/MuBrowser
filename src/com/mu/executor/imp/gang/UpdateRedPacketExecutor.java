package com.mu.executor.imp.gang;

import com.mu.db.manager.GangDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.gang.RedPacket;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateRedPacketExecutor extends Executable {
   public UpdateRedPacketExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      GangDBManager.updateRedPacket(packet.readLong(), packet.readUTF(), packet.readBoolean());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long packetId = ((Long)obj[0]).longValue();
      String left = (String)obj[1];
      boolean isOver = ((Boolean)obj[2]).booleanValue();
      packet.writeLong(packetId);
      packet.writeUTF(left);
      packet.writeBoolean(isOver);
   }

   public static synchronized void updateRedPacket(Player player, RedPacket rp) {
      WriteOnlyPacket packet = Executor.GangUpdateRedPacket.toPacket(rp.getPacketId(), rp.getLeftIngotStr(), rp.isOver());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
