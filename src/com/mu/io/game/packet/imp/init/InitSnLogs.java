package com.mu.io.game.packet.imp.init;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.gift.GiftSnLogs;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class InitSnLogs extends ReadAndWritePacket {
   public InitSnLogs(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitSnLogs() {
      super(10027, (byte[])null);
   }

   public static WriteOnlyPacket initSnLogs(long roleID) {
      List list = PlayerDBManager.getSnLogs(roleID);
      InitSnLogs il = new InitSnLogs();

      try {
         il.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            int data = ((Integer)var5.next()).intValue();
            il.writeShort(data);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      list.clear();
      list = null;
      return il;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      GiftSnLogs logs = player.getSnLogs();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         logs.addLog(this.readUnsignedShort());
      }

   }
}
