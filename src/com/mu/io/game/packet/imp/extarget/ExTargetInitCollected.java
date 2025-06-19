package com.mu.io.game.packet.imp.extarget;

import com.mu.db.manager.ExtargetDBManager;
import com.mu.game.model.unit.player.extarget.ExtargetManager;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class ExTargetInitCollected extends ReadAndWritePacket {
   public ExTargetInitCollected(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public ExTargetInitCollected() {
      super(20500, (byte[])null);
   }

   public static ExTargetInitCollected createExTargetCollect(long rid) {
      ArrayList list = ExtargetDBManager.getCollectedList(rid);
      ExTargetInitCollected er = new ExTargetInitCollected();

      try {
         er.writeShort(list.size());
         Iterator var5 = list.iterator();

         while(var5.hasNext()) {
            int[] in = (int[])var5.next();
            er.writeInt(in[0]);
            er.writeInt(in[1]);
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
         manager.addCollectWhenInit(this.readInt(), this.readInt());
      }

   }
}
