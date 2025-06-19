package com.mu.executor.imp.gang;

import com.mu.db.manager.GangDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.gang.RedPacket;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveRedPacketExecutor extends Executable {
   public SaveRedPacketExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      GangDBManager.saveRedPacket(packet.readLong(), packet.readLong(), packet.readLong(), packet.readByte(), packet.readByte(), packet.readLong(), packet.readUTF(), packet.readByte(), packet.readUTF(), packet.readInt());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      RedPacket rp = (RedPacket)obj[0];
      packet.writeLong(rp.getPacketId());
      packet.writeLong(rp.getRoleId());
      packet.writeLong(rp.getGangId());
      packet.writeByte(rp.getRedId());
      packet.writeByte(rp.getRedType());
      packet.writeLong(rp.getSendTime());
      packet.writeUTF(rp.getLeftIngotStr());
      packet.writeByte(0);
      packet.writeUTF(rp.getUserName());
      packet.writeInt(rp.getServerId());
   }

   public static void saveRedPacket(Player player, RedPacket rp) {
      WriteOnlyPacket packet = Executor.GangSaveRedPacket.toPacket(rp);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
