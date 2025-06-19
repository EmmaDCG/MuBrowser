package com.mu.io.game.packet.imp.rewardhall.sign;

import com.mu.game.model.rewardhall.RewardHallConfigManager;
import com.mu.game.model.rewardhall.RewardItemData;
import com.mu.game.model.rewardhall.sign.PlayerSignManager;
import com.mu.game.model.rewardhall.sign.SignRewardData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class SignInform extends ReadAndWritePacket {
   public SignInform(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      sendMsgSignInform(this.getPlayer());
      sendMsgSignRewardInform(this.getPlayer());
   }

   public static void sendMsgSignInform(Player player) {
      try {
         SignInform packet = new SignInform(46101, (byte[])null);
         PlayerSignManager manager = player.getSignManager();
         Calendar c = Calendar.getInstance();
         c.set(8, 0);
         c.set(7, 1);
         Calendar start = manager.getRoundStart();
         int startYear = start.get(1);
         int startDay = start.get(6);
         Calendar cur = Calendar.getInstance();
         int curYear = cur.get(1);
         int curDay = cur.get(6);
         Calendar[] timeSet = manager.getTimeSet();
         packet.writeByte(timeSet.length);
         packet.writeBoolean(manager.isSigned());
         int size = 42;
         packet.writeByte(size);
         int i = 0;

         while(i < size) {
            packet.writeDouble((double)c.getTimeInMillis());
            int year = c.get(1);
            int day = c.get(6);
            if (year < startYear || year == startYear && day < startDay || year > curYear || year == curYear && day > curDay) {
               packet.writeByte(0);
            } else {
               boolean sign = false;

               for(int j = 0; j < timeSet.length; ++j) {
                  if (timeSet[j].get(1) == year && timeSet[j].get(6) == day) {
                     packet.writeByte(2);
                     sign = true;
                  }
               }

               if (!sign) {
                  packet.writeByte(year == curYear && day == curDay ? 0 : 1);
               }
            }

            ++i;
            c.add(5, 1);
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var17) {
         var17.printStackTrace();
      }

   }

   public static void main(String[] args) {
      Calendar c = Calendar.getInstance();
      c.add(5, 400);
      System.out.println((new SimpleDateFormat("yyyy/MM/dd")).format(c.getTime()));
   }

   public static void sendMsgSignRewardInform(Player player) {
      try {
         SignInform packet = new SignInform(46102, (byte[])null);
         PlayerSignManager manager = player.getSignManager();
         int round = manager.getRound();
         int count = manager.getTimeSet().length;
         packet.writeByte(RewardHallConfigManager.getSignRewardSize());
         Iterator it = RewardHallConfigManager.getSignRewardIterator();

         while(it.hasNext()) {
            SignRewardData data = (SignRewardData)it.next();
            packet.writeByte(data.getId());
            packet.writeUTF(data.getName());
            packet.writeByte(data.getCount() > count ? 0 : (!manager.getRewardList().contains(data) ? 1 : 2));
            List rewardList = data.getRewardList(player, round);
            packet.writeByte(rewardList.size());

            for(int i = 0; i < rewardList.size(); ++i) {
               GetItemStats.writeItem(((RewardItemData)rewardList.get(i)).getItem(), packet);
            }

            List vipRewardList = data.getVIPRewardList(player, round);
            packet.writeByte(vipRewardList.size());

            for(int i = 0; i < vipRewardList.size(); ++i) {
               GetItemStats.writeItem(((RewardItemData)vipRewardList.get(i)).getItem(), packet);
            }
         }

         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var10) {
         var10.printStackTrace();
      }

   }

   public static void sendSignRewardState(Player player, int id, int state) {
      try {
         SignInform packet = new SignInform(46106, (byte[])null);
         packet.writeByte(id);
         packet.writeByte(state);
         player.writePacket(packet);
         packet.destroy();
         packet = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
