package com.mu.executor.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;

public class SaveBlessRecordExecutor extends Executable {
   public SaveBlessRecordExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      FriendDBManager.saveRecord(packet.readLong(), packet.readUTF(), packet.readLong(), packet.readLong(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      packet.writeLong(((Long)obj[0]).longValue());
      packet.writeUTF((String)obj[1]);
      packet.writeLong(((Long)obj[2]).longValue());
      packet.writeLong(((Long)obj[3]).longValue());
      packet.writeLong(((Long)obj[4]).longValue());
   }

   public static void saveRecord(Player player, long rid) {
      WriteOnlyPacket packet = Executor.saveBlessRecord.toPacket(player.getID(), player.getName(), rid, Time.getDayLong(), System.currentTimeMillis());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
