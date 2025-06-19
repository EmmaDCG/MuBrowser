package com.mu.executor.imp.friend;

import com.mu.db.manager.FriendDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import com.mu.utils.Time;

public class UpdateReceiveDayExecutor extends Executable {
   public UpdateReceiveDayExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      FriendDBManager.updateReceievDay(packet.readLong(), packet.readInt(), packet.readLong());
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      int lucky = ((Integer)obj[1]).intValue();
      long day = ((Long)obj[2]).longValue();
      packet.writeLong(rid);
      packet.writeInt(lucky);
      packet.writeLong(day);
   }

   public static void updateReceiveDay(Player player) {
      WriteOnlyPacket packet = Executor.UpdateReceiveWish.toPacket(player.getID(), player.getFriendManager().getLucky(), Time.getDayLong());
      player.writePacket(packet);
      packet.destroy();
      packet = null;
   }
}
