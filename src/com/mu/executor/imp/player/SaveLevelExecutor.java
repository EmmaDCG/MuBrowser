package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveLevelExecutor extends Executable {
   public SaveLevelExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveLevelChange(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      packet.writeLong(player.getID());
      packet.writeShort(player.getLevel());
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.STR));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.DEX));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.CON));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.INT));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.MAX_HP));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.MAX_MP));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.MAX_SD));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.MAX_AP));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.ATK_MAX));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.ATK_MIN));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.DEF));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.HIT));
      packet.writeInt(player.getProperty().getLoadedStat(StatEnum.AVD));
      packet.writeInt(player.getPotential());
      packet.writeLong(System.currentTimeMillis());
   }
}
