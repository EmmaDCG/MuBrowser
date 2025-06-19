package com.mu.io.game.packet.imp.activity;

import com.mu.db.manager.ActivityDBManager;
import com.mu.game.model.activity.ActivityLogs;
import com.mu.game.model.activity.ActivityRoleLogs;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.utils.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class InitActivityRoleLogs extends ReadAndWritePacket {
   public InitActivityRoleLogs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitActivityRoleLogs creategetRoleLogs(long rid) {
      InitActivityRoleLogs im = new InitActivityRoleLogs(10800, (byte[])null);
      ArrayList list = ActivityDBManager.getRoleLogs(rid);

      try {
         im.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            ActivityRoleLogs logs = (ActivityRoleLogs)var5.next();
            im.writeInt(logs.getEid());
            im.writeUTF(logs.getDate());
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      return im;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ActivityLogs al = player.getActivityLogs();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         int eid = this.readInt();
         String date = this.readUTF();
         Date d = Time.getDate(date);
         al.addRoleLogs(eid, d);
      }

   }
}
