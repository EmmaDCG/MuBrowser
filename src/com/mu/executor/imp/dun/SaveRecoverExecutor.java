package com.mu.executor.imp.dun;

import com.mu.db.manager.RecoverDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;

public class SaveRecoverExecutor extends Executable {
   public SaveRecoverExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      RecoverDBManager.saveRecover(packet.readLong(), packet.readUnsignedByte(), packet.readUnsignedByte(), packet.readUnsignedByte(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int dunId = ((Integer)obj[1]).intValue();
      int recoverTimes = ((Integer)obj[2]).intValue();
      int remainderTimes = ((Integer)obj[3]).intValue();
      long time = ((Long)obj[4]).longValue();
      packet.writeLong(rid);
      packet.writeByte(dunId);
      packet.writeByte(recoverTimes);
      packet.writeByte(remainderTimes);
      packet.writeLong(time);
   }

   public static void saveRecover(Player player, int id, int recoverTimes, int remainderTimes) {
      WriteOnlyPacket packet = Executor.SaveDunRecover.toPacket(player.getID(), id, recoverTimes, remainderTimes, Time.getDayLong());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
