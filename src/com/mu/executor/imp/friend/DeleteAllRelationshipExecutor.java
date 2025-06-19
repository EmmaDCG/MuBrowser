package com.mu.executor.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class DeleteAllRelationshipExecutor extends Executable {
   public DeleteAllRelationshipExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      FriendDBManager.delAllRelationship(packet.readLong(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeLong(((Long)obj[1]).longValue());
   }

   public static void delRelationship(Player player, long fid) {
      WriteOnlyPacket packet = Executor.DelRelationship.toPacket(player.getID(), fid);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
