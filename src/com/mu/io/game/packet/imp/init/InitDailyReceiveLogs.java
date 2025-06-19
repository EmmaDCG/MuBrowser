package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dailyreceive.DailyReceiveLog;
import com.mu.game.model.unit.player.dailyreceive.DailyReceiveLogManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitDailyReceiveLogs extends ReadAndWritePacket {
   public InitDailyReceiveLogs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitDailyReceiveLogs createLogs(long rid) {
      InitDailyReceiveLogs dl = new InitDailyReceiveLogs(10033, (byte[])null);
      ArrayList list = PlayerDBManager.getDailyReceiveLog(rid);

      try {
         dl.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            DailyReceiveLog log = (DailyReceiveLog)var5.next();
            dl.writeShort(log.getType());
            dl.writeShort(log.getTimes());
            dl.writeLong(log.getDay());
            dl.writeByte(log.getHour());
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      return dl;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      DailyReceiveLogManager manager = player.getDailyReceiveManager();
      if (manager != null) {
         int size = this.readUnsignedShort();

         for(int i = 0; i < size; ++i) {
            int type = this.readShort();
            int times = this.readUnsignedShort();
            long day = this.readLong();
            int hour = this.readByte();
            DailyReceiveLog log = new DailyReceiveLog(type);
            log.setDay(day);
            log.setHour(hour);
            log.setTimes(times);
            manager.addDailyLog(log);
         }

      }
   }
}
