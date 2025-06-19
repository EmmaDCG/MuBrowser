package com.mu.io.game.packet.imp.guide;

import com.mu.db.manager.PlayerDBManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class InitFunctionOpenTime extends ReadAndWritePacket {
   public InitFunctionOpenTime(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public InitFunctionOpenTime() {
      super(10050, (byte[])null);
   }

   public static InitFunctionOpenTime createFunctionOpenTime(long rid) {
      InitFunctionOpenTime it = new InitFunctionOpenTime();

      try {
         ArrayList list = PlayerDBManager.getFunctionOpen(rid);
         it.writeByte(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            long[] lo = (long[])var5.next();
            it.writeByte((int)lo[0]);
            it.writeLong(lo[1]);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return it;
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int size = this.readUnsignedByte();

      for(int i = 0; i < size; ++i) {
         player.addFunctionOpenTime(this.readUnsignedByte(), this.readLong());
      }

   }
}
