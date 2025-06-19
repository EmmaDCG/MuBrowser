package com.mu.executor.imp.player;

import com.mu.db.manager.PlayerDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class SaveRedeemPointExector extends Executable {
   public SaveRedeemPointExector(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      PlayerDBManager.saveRedeemPoint(packet);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      int redeemPoint = ((Integer)obj[0]).intValue();
      long roleID = ((Long)obj[1]).longValue();
      packet.writeLong(roleID);
      packet.writeInt(redeemPoint);
   }

   public static void saveRedeemPoint(Player player) {
      WriteOnlyPacket packet = Executor.SaveRedeemPoint.toPacket(player.getRedeemPoints(), player.getID());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
