package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.MapUnit;
import com.mu.io.game.packet.WriteOnlyPacket;

public class MapChangeLandscape extends WriteOnlyPacket {
   public MapChangeLandscape(MapUnit unit, boolean isWater) {
      super(10111);

      try {
         this.writeByte(unit.getType());
         this.writeDouble((double)unit.getID());
         this.writeBoolean(isWater);
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
