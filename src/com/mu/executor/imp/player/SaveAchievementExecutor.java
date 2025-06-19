package com.mu.executor.imp.player;

import com.mu.db.manager.AchievementDBManager;
import com.mu.executor.Executable;
import com.mu.executor.Executor;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.exe.ExecutePacket;
import com.mu.io.game2gateway.packet.Game2GatewayPacket;
import java.util.ArrayList;

public class SaveAchievementExecutor extends Executable {
   public SaveAchievementExecutor(int type) {
      super(type);
   }

   public void execute(Game2GatewayPacket packet) throws Exception {
      long rid = packet.readLong();
      int size = packet.readShort();
      ArrayList list = new ArrayList();

      for(int i = 0; i < size; ++i) {
         list.add(new int[]{packet.readShort(), packet.readInt()});
      }

      AchievementDBManager.saveAchievement(rid, list);
      list.clear();
      list = null;
   }

   public void toPacket(ExecutePacket packet, Object... obj) throws Exception {
      long rid = ((Long)obj[0]).longValue();
      ArrayList list = (ArrayList)obj[1];
      packet.writeLong(rid);
      int size = list.size();
      packet.writeShort(size);

      for(int i = 0; i < size; ++i) {
         int[] in = (int[])list.get(i);
         packet.writeShort(in[0]);
         packet.writeInt(in[1]);
      }

   }

   public static void saveOneAchievement(Player player, int id, int num) {
      ArrayList list = new ArrayList();
      list.add(new int[]{id, num});
      WriteOnlyPacket packet = Executor.SaveAchievement.toPacket(player.getID(), list);
      player.writePacket(packet);
      packet.destroy();
      packet = null;
      list.clear();
      list = null;
   }
}
