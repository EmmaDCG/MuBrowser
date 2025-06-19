package com.mu.executor.imp.gang;

import com.mu.db.manager.GangDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveSummonBossExecutor extends Executable {
   public SaveSummonBossExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      GangDBManager.saveSummonRecord(packet.readLong(), packet.readInt(), packet.readInt(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long gangId = ((Long)obj[0]).longValue();
      int bossId = ((Integer)obj[1]).intValue();
      int times = ((Integer)obj[2]).intValue();
      long day = ((Long)obj[3]).longValue();
      packet.writeLong(gangId);
      packet.writeInt(bossId);
      packet.writeInt(times);
      packet.writeLong(day);
   }

   public static void saveSummon(Player player, long gangId, int bossId, int times, long day) {
      WriteOnlyPacket packet = Executor.GangSaveSummonBoss.toPacket(gangId, bossId, times, day);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
