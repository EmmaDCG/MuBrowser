package com.mu.executor.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.friend.Friend;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class UpdateFriendExecutor extends Executable {
   public UpdateFriendExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      FriendDBManager.updateFriend(packet.readLong(), packet.readLong(), packet.readByte(), packet.readInt(), packet.readInt());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      Friend friend = (Friend)obj[1];
      packet.writeLong(rid);
      packet.writeLong(friend.getId());
      packet.writeByte(friend.getType());
      packet.writeInt(friend.getFriendDegree());
      packet.writeInt(friend.getBeKilledTimes());
   }

   public static void updateFriend(Player player, Friend friend) {
      WriteOnlyPacket packet = Executor.UpdateFriend.toPacket(player.getID(), friend);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
