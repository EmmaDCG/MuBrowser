package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.tp.TransPoint;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.HashMap;
import java.util.Iterator;

public class AroundTransPoint extends WriteOnlyPacket {
   public AroundTransPoint(HashMap tpMap) {
      super(10102);

      try {
         this.writeByte(tpMap.size());
         Iterator it = tpMap.values().iterator();

         while(it.hasNext()) {
            TransPoint tp = (TransPoint)it.next();
            this.writeDetail(tp);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }

   public AroundTransPoint(TransPoint tp) {
      super(10102);

      try {
         this.writeByte(1);
         this.writeDetail(tp);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   private void writeDetail(TransPoint tp) throws Exception {
      this.writeDouble((double)tp.getID());
      this.writeUTF(tp.getName());
      this.writeInt(tp.getX());
      this.writeInt(tp.getY());
      this.writeByte(tp.getWorldX());
      this.writeByte(tp.getWorldY());
      this.writeShort(1180);
   }
}
