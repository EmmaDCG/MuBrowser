package com.mu.io.game.packet.imp.extarget;

import com.mu.db.manager.ExtargetDBManager;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ExTargetInitReceived extends ReadAndWritePacket {
   public ExTargetInitReceived(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ExTargetInitReceived() {
      super(20501, (byte[])null);
   }

   public static ExTargetInitReceived createExTargetReceive(long rid) {
      ArrayList list = ExtargetDBManager.getReceivedList(rid);
      ExTargetInitReceived er = new ExTargetInitReceived();

      try {
         er.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            int i = ((Integer)var5.next()).intValue();
            er.writeInt(i);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      return er;
   }

   public void process() throws Exception {
      ExtargetManager manager = this.getPlayer().getExtargetManager();
      int size = this.readShort();

      for(int i = 0; i < size; ++i) {
         manager.addReceiveWhenInit(this.readInt());
      }

   }
}
