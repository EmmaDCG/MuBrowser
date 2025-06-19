package com.mu.io.game.packet.imp.player;

import com.mu.db.manager.PayDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.User;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitPay extends ReadAndWritePacket {
   public InitPay(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public static InitPay getPayList(String userName, int serverId) {
      InitPay ip = new InitPay(10036, (byte[])null);
      ArrayList list = PayDBManager.getPayList(userName, serverId);

      try {
         ip.writeInt(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            long[] l = (long[])var5.next();
            ip.writeLong(l[0]);
            ip.writeInt((int)l[1]);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      list = null;
      return ip;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      User user = player.getUser();
      int size = this.readInt();

      for(int i = 0; i < size; ++i) {
         long day = this.readLong();
         int ingot = this.readInt();
         user.addPay(ingot, day);
      }

   }
}
