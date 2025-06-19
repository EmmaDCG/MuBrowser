package com.mu.game.model.unit.player.dailyreceive;

import com.mu.executor.Executor;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareTemplate;
import com.mu.game.dungeon.imp.bigdevil.BigDevilTopReward;
import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.top.DungeonTopManager;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class DailyReceiveLogManager {
   public static final int Daily_BigDevil = 1;
   private HashMap logMap = new HashMap();
   private Player owner;

   public DailyReceiveLogManager(Player owner) {
      this.owner = owner;
   }

   public void addDailyLog(DailyReceiveLog log) {
      this.logMap.put(log.getType(), log);
   }

   public DailyReceiveLog removeLog(int type) {
      return (DailyReceiveLog)this.logMap.remove(type);
   }

   public DailyReceiveLog getLog(int type) {
      return (DailyReceiveLog)this.logMap.get(type);
   }

   public void destroy() {
      if (this.logMap != null) {
         this.logMap.clear();
         this.logMap = null;
      }

      this.owner = null;
   }

   public boolean bigdevilTopCanReceive() {
      DailyReceiveLog log = this.getLog(1);
      return log == null;
   }

   public synchronized int[] receiveBigDevilTopReward(BigDevilSquareTemplate template) {
      int top = DungeonTopManager.getBigDevilPlayerTop(this.owner);
      BigDevilTopReward br = template.getTopReward(top);
      if (br != null) {
         ArrayList dataList = br.getRewardDataList();
         if (dataList.size() > 0) {
            OperationResult r = this.owner.getItemManager().addItem((List)dataList);
            if (r.getResult() == 1) {
               DailyReceiveLog log = new DailyReceiveLog(1);
               log.setDay(getReceiveDay(1));
               log.setHour(DailyReceiveType.Daily_BigDevil.getHour());
               log.setTimes(1);
               this.addDailyLog(log);
               WriteOnlyPacket packet = Executor.SaveDailyLogs.toPacket(this.owner.getID(), log);
               this.owner.writePacket(packet);
               packet.destroy();
               packet = null;
               return new int[]{1, br.getSort()};
            }

            SystemMessage.writeMessage(this.owner, r.getResult());
         }
      }

      return new int[1];
   }

   public int getBigdeivlTopNextTime() {
      if (this.bigdevilTopCanReceive()) {
         return 0;
      } else {
         Calendar calendar = Calendar.getInstance();
         DailyReceiveLog log = this.getLog(1);
         calendar.setTime(Time.getDate(String.valueOf(log.getDay()), "yyyyMMdd"));
         calendar.add(5, 1);
         calendar.add(11, DailyReceiveType.Daily_BigDevil.getHour());
         return (int)((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000L);
      }
   }

   public static long getReceiveDay(int type) {
      DailyReceiveType dr = DailyReceiveType.getReceiveType(type);
      if (dr == null) {
         return -1L;
      } else {
         Calendar calendar = Calendar.getInstance();
         calendar.set(5, 0);
         calendar.set(12, 0);
         calendar.set(13, 0);
         calendar.add(11, dr.getHour());
         Date shouldReceiveDate = calendar.getTime();
         Date now = new Date(System.currentTimeMillis());
         if (now.after(shouldReceiveDate)) {
            return Time.getDayLong();
         } else {
            Calendar preCalendar = Calendar.getInstance();
            preCalendar.add(6, -1);
            return Time.getDayLong(preCalendar);
         }
      }
   }
}
