package com.mu.executor.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class DelFriendExecutor extends Executable {
   public DelFriendExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      long fid = packet.readLong();
      int type = packet.readByte();
      FriendDBManager.delFriend(rid, fid, type);
      if (type == 0) {
         FriendDBManager.delFriend(fid, rid, type);
      }

   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeLong(((Long)obj[1]).longValue());
      packet.writeByte(((Integer)obj[2]).intValue());
   }

   public static void delFriend(Player player, long fid, int type) {
      WriteOnlyPacket packet = Executor.DelFriend.toPacket(player.getID(), fid, type);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
