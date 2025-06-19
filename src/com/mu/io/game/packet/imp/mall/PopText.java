package com.mu.io.game.packet.imp.mall;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.Creature;
import com.mu.io.game.packet.WriteOnlyPacket;

public class PopText extends WriteOnlyPacket {
   public PopText() {
      super(10307);
   }

   public static void pop(Creature creature, String text) {
      PopText pt = new PopText();

      try {
         pt.writeByte(creature.getType());
         pt.writeDouble((double)creature.getID());
         pt.writeUTF(text);
         Map map = creature.getMap();
         if (map != null) {
            map.sendPacketToAroundPlayer(pt, creature.getPosition());
         }

         pt.destroy();
         pt = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
