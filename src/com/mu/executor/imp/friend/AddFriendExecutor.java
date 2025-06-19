package com.mu.executor.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;

public class AddFriendExecutor extends Executable {
   public AddFriendExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      long fid = packet.readLong();
      int type = packet.readByte();
      int fsid = packet.readInt();
      String fname = packet.readUTF();
      int killTimes = packet.readUnsignedShort();
      FriendDBManager.addFriend(rid, fid, type, fsid, fname, killTimes);
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      long fid = ((Long)obj[1]).longValue();
      int type = ((Integer)obj[2]).intValue();
      int fsid = ((Integer)obj[3]).intValue();
      String fname = (String)obj[4];
      int killTimes = ((Integer)obj[5]).intValue();
      packet.writeLong(rid);
      packet.writeLong(fid);
      packet.writeByte(type);
      packet.writeInt(fsid);
      packet.writeUTF(fname);
      packet.writeShort(killTimes);
   }

   public static void saveFriend(Player player, long fid, int type, int fsid, String name, int killTimes) {
      WriteOnlyPacket packet = Executor.AddFriend.toPacket(player.getID(), fid, type, fsid, name, killTimes);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
