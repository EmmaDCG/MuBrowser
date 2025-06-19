package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.sevenDay.SevenDayTreasure;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveSevenDayTreasureExecutor extends Executable {
   public SaveSevenDayTreasureExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveSevenDayTreasure(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      Player player = (Player)obj[0];
      SevenDayTreasure sevenManager = player.getSevenManager();
      int loginDays = sevenManager.getLoginTotalday();
      String findIndexStr = sevenManager.getFoundIndexStr();
      packet.writeLong(player.getID());
      packet.writeInt(loginDays);
      packet.writeUTF(findIndexStr);
   }
}
