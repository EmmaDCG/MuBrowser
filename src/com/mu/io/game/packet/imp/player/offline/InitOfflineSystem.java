package com.mu.io.game.packet.imp.player.offline;

import com.mu.game.dungeon.DungeonTemplate;
import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.offline.DunRecoveryInfo;
import com.mu.game.model.unit.player.offline.OfflineManager;
import com.mu.game.model.unit.player.offline.PlayerDunRecover;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Time;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

public class InitOfflineSystem extends ReadAndWritePacket {
   public InitOfflineSystem(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitOfflineSystem() {
      super(10049, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (FunctionOpenManager.isOpen(player, 21)) {
         OfflineManager manager = player.getOffLineManager();
         long begin = this.readLong();
         long end = this.readLong();
         long openTime = player.getFunctionOpenTime(21);
         if (openTime == 0L) {
            end = begin + 86400000L;
         } else {
            end = Math.max(end, openTime);
         }

         int size = this.readByte();
         HashMap finishMap = new HashMap();

         for(int i = 0; i < size; ++i) {
            int dunId = this.readByte();
            int times = this.readInt();
            long day = this.readLong();
            HashMap map = (HashMap)finishMap.get(Integer.valueOf(dunId));
            if (map == null) {
               map = new HashMap();
               finishMap.put(Integer.valueOf(dunId), map);
            }

            map.put(day, times);
         }

         long today = Time.getDayLong();
         Iterator it = OfflineManager.getInfoMap().values().iterator();

         while(true) {
            while(true) {
               PlayerDunRecover todayRecover;
               DunRecoveryInfo info;
               do {
                  if (!it.hasNext()) {
                     finishMap.clear();
                     long loginTime = player.getLoginTime();
                     long logoutTime = player.getLogoutTime();
                     manager.initOfflineBuff(loginTime - logoutTime);
                     return;
                  }

                  info = (DunRecoveryInfo)it.next();
                  todayRecover = manager.getTodayRecover(info.getDunId());
               } while(todayRecover != null);

               PlayerDunRecover lastRecover = manager.getLastRecover(info.getDunId());
               DungeonTemplate template = DungeonTemplateFactory.getTemplate(info.getDunId());
               Calendar beginCal = Calendar.getInstance();
               beginCal.setTimeInMillis(begin);
               Calendar endCal = Calendar.getInstance();
               endCal.setTimeInMillis(end);
               long beginDay = Time.getDayLong(beginCal);
               long endDay = Time.getDayLong(endCal);
               PlayerDunRecover recover = new PlayerDunRecover(info.getDunId());
               recover.setRecoverDay(today);
               manager.addRecover(recover);

               while(beginDay >= endDay) {
                  int finishTimes = 0;
                  HashMap map = (HashMap)finishMap.get(info.getDunId());
                  if (map != null) {
                     Integer in = (Integer)map.get(beginDay);
                     finishTimes = in == null ? 0 : in.intValue();
                  }

                  int additionTimes = template.getMaxTimes() - finishTimes;
                  if (additionTimes < 0) {
                     additionTimes = 0;
                  }

                  recover.setRemainderTimes(recover.getRemainderTimes() + additionTimes);
                  if (recover.getRemainderTimes() > info.getMaxTimes()) {
                     recover.setRemainderTimes(info.getMaxTimes());
                     break;
                  }

                  if (lastRecover != null && lastRecover.getRecoverDay() == beginDay) {
                     recover.setRemainderTimes(recover.getRemainderTimes() + lastRecover.getRemainderTimes());
                     if (recover.getRemainderTimes() > info.getMaxTimes()) {
                        recover.setRemainderTimes(info.getMaxTimes());
                     }
                     break;
                  }

                  beginCal.add(5, -1);
                  beginDay = Time.getDayLong(beginCal);
               }
            }
         }
      }
   }
}
