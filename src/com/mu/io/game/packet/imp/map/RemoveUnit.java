package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.Unit;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.Iterator;
import java.util.List;

public class RemoveUnit extends WriteOnlyPacket {
   public RemoveUnit(Unit... units) {
      super(10101);

      try {
         this.writeShort(units.length);

         for(int i = 0; i < units.length; ++i) {
            this.writeByte(units[i].getType());
            this.writeDouble((double)units[i].getID());
         }
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public RemoveUnit(List list) {
      super(10101);

      try {
         this.writeShort(list.size());
         Iterator var3 = list.iterator();

         while(var3.hasNext()) {
            Unit unit = (Unit)var3.next();
            this.writeByte(unit.getType());
            this.writeDouble((double)unit.getID());
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
