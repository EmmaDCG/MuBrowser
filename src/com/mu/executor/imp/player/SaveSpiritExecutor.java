package com.mu.executor.imp.player;

import com.mu.db.manager.SpiritDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.spiritOfWar.SpiritManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveSpiritExecutor extends Executable {
   public SaveSpiritExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      SpiritDBManager.saveSpirit(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      SpiritManager manager = player.getSpiritManager();
      long roleID = player.getID();
      int rank = manager.getRank();
      int level = manager.getLevel();
      long exp = manager.getExp();
      int ingotRefineCount = manager.getIngotRefineCount();
      String itemCountStr = manager.getItemCountStr();
      String conStr = manager.getConditionStr();
      packet.writeLong(roleID);
      packet.writeByte(rank);
      packet.writeShort(level);
      packet.writeLong(exp);
      packet.writeInt(ingotRefineCount);
      packet.writeUTF(itemCountStr);
      packet.writeUTF(conStr);
   }
}
