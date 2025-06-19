package com.mu.executor.imp.gang;

import com.mu.db.manager.GangDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.gang.RedPacketReceiveRecord;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveRedPacketReceiveExecutor extends Executable {
   public SaveRedPacketReceiveExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      GangDBManager.saveRedPacketReceive(packet.readLong(), packet.readLong(), packet.readByte(), packet.readByte(), packet.readLong(), packet.readLong(), packet.readInt());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      RedPacketReceiveRecord record = (RedPacketReceiveRecord)obj[0];
      int redId = ((Integer)obj[1]).intValue();
      int redType = ((Integer)obj[2]).intValue();
      packet.writeLong(record.getPacketId());
      packet.writeLong(record.getRid());
      packet.writeByte(redId);
      packet.writeByte(redType);
      packet.writeLong(record.getReceiveTime());
      packet.writeLong(record.getReceiveDay());
      packet.writeInt(record.getReceiveIngot());
   }

   public static void saveReceive(Player player, RedPacketReceiveRecord record, int redId, int redType) {
      WriteOnlyPacket packet = Executor.GangSaveRedPacketRecord.toPacket(record, redId, redType);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
