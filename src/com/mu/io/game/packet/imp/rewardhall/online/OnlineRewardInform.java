package com.mu.io.game.packet.imp.rewardhall.online;

import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.rewardhall.online.OnlineRewardDayData;
import com.mu.game.model.rewardhall.online.OnlineRewardWeekData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class OnlineRewardInform extends ReadAndWritePacket {
   public OnlineRewardInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendOnlineInform(this.getPlayer());
   }

   public static void sendOnlineInform(Player player) {
      try {
         OnlineRewardInform packet = new OnlineRewardInform(46301, (byte[])null);
         int todayOnlineTime = player.getTodayOnlineTime();
         CopyOnWriteArrayList list = player.getOnlineManager().getDayReceiveList();
         boolean receive = player.getOnlineManager().getWeekReceiveTime() == 0L && player.getOnlineManager().getWeekAccumulative() != 0;
         packet.writeInt(todayOnlineTime);
         packet.writeByte(RewardHallConfigManager.getOnlineRewardSize());
         Iterator it = RewardHallConfigManager.getOnlineRewardIterator();

         while(it.hasNext()) {
            OnlineRewardDayData data = (OnlineRewardDayData)it.next();
            packet.writeByte(data.getId());
            GetItemStats.writeItem(data.getItem(), packet);
            int state = list.contains(data.getId()) ? 2 : (data.getSeconds() <= todayOnlineTime ? 1 : 0);
            packet.writeByte(state);
            if (state == 0) {
               packet.writeInt(Math.max(0, data.getSeconds() - todayOnlineTime));
            }
         }

         OnlineRewardWeekData wd = RewardHallConfigManager.getOnlineRewardWeek();
         packet.writeUTF(wd.getStr1());
         packet.writeUTF(wd.getStr2());
         packet.writeUTF(wd.getStr3());
         packet.writeInt(player.getOnlineManager().getWeekAccumulative());
         packet.writeInt(wd.getAccumulative(player.getOnlineManager().getWeekSeconds()));
         packet.writeBoolean(!receive);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void sendReceiveDayResult(Player player, List list) {
      try {
         OnlineRewardInform packet = new OnlineRewardInform(46302, (byte[])null);
         packet.writeByte(list.size());
         Iterator it = list.iterator();

         while(it.hasNext()) {
            Integer id = (Integer)it.next();
            packet.writeByte(id.intValue());
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
