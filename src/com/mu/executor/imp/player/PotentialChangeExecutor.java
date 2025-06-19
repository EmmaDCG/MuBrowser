package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class PotentialChangeExecutor extends Executable {
   public PotentialChangeExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.savePotentialChange(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) {
      try {
         Player player = (Player)obj[0];
         packet.writeLong(player.getID());
         packet.writeInt(player.getPotential());
         packet.writeInt(player.getPoSTR());
         packet.writeInt(player.getPoDEX());
         packet.writeInt(player.getPoCON());
         packet.writeInt(player.getPoINT());
         packet.writeInt(player.getProperty().getLoadedStat(StatEnum.STR));
         packet.writeInt(player.getProperty().getLoadedStat(StatEnum.DEX));
         packet.writeInt(player.getProperty().getLoadedStat(StatEnum.CON));
         packet.writeInt(player.getProperty().getLoadedStat(StatEnum.INT));
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
