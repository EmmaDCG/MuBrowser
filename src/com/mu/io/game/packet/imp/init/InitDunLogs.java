package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.DungeonDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.game.model.unit.player.dun.DunLogs;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Iterator;

public class InitDunLogs extends ReadAndWritePacket {
   public InitDunLogs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitDunLogs createLogs(long rid) {
      InitDunLogs dl = new InitDunLogs(12000, (byte[])null);
      ArrayList list = DungeonDBManager.getDungeonlogs(rid);

      try {
         dl.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            DunLogs log = (DunLogs)var5.next();
            dl.writeByte(log.getDunId());
            dl.writeShort(log.getSmallId());
            dl.writeInt(log.getFinishTimes());
            dl.writeLong(log.getLastFinishTime());
            dl.writeLong(log.getSaveDay());
            dl.writeLong(log.getBaseExp());
            dl.writeInt(log.getBaseMoney());
            dl.writeBoolean(log.isHasReceived());
            dl.writeInt(log.getVipLevel());
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      return dl;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      DunLogManager manager = player.getDunLogsManager();
      if (manager != null) {
         int size = this.readUnsignedShort();
         long today = Time.getDayLong();

         for(int i = 0; i < size; ++i) {
            int dunId = this.readUnsignedByte();
            int smallId = this.readUnsignedShort();
            int finishTimes = this.readInt();
            long lastFinishTime = this.readLong();
            long saveDay = this.readLong();
            if (saveDay == today) {
               long exp = this.readLong();
               int money = this.readInt();
               boolean received = this.readBoolean();
               int vipLevel = this.readInt();
               DunLogs log = new DunLogs(dunId);
               log.setSmallId(smallId);
               log.setFinishTimes(finishTimes);
               log.setLastFinishTime(lastFinishTime);
               log.setSaveDay(saveDay);
               log.setBaseExp(exp);
               log.setBaseMoney(money);
               log.setHasReceived(received);
               log.setVipLevel(vipLevel);
               manager.addLog(log);
            }
         }

      }
   }
}
