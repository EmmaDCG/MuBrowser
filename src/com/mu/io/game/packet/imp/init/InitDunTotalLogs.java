package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.DungeonDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.dun.DunLogManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitDunTotalLogs extends ReadAndWritePacket {
   public InitDunTotalLogs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitDunTotalLogs createLogs(long rid) {
      InitDunTotalLogs dl = new InitDunTotalLogs(12022, (byte[])null);
      ArrayList list = DungeonDBManager.getDungeonTotallogs(rid);

      try {
         dl.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            int[] log = (int[])var5.next();
            dl.writeByte(log[0]);
            dl.writeShort(log[1]);
            dl.writeInt(log[2]);
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

         for(int i = 0; i < size; ++i) {
            int dunId = this.readUnsignedByte();
            int smallId = this.readUnsignedShort();
            int finishTimes = this.readInt();
            manager.setTotalNumber(dunId, smallId, finishTimes);
         }

      }
   }
}
