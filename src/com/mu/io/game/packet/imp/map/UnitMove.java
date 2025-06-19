package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.MapUnit;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.awt.Point;

public class UnitMove extends WriteOnlyPacket {
   public UnitMove(MapUnit unit, Point[] path) {
      super(10100);

      try {
         this.writeByte(unit.getType());
         this.writeDouble((double)unit.getID());
         this.writeShort(path.length);

         for(int i = 0; i < path.length; ++i) {
            this.writeInt(path[i].x);
            this.writeInt(path[i].y);
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
