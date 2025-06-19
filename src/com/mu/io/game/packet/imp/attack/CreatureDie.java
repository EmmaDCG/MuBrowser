package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class CreatureDie extends WriteOnlyPacket {
   public CreatureDie(Creature creature) {
      super(32004);

      try {
         this.writeByte(creature.getType());
         this.writeDouble((double)creature.getID());
         this.writeShort(creature.getDieMusic());
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendToClient(Creature creature) {
      CreatureDie cd = new CreatureDie(creature);
      Map map = creature.getMap();
      if (map != null) {
         if (creature.getType() == 1) {
            map.sendPacketToAroundPlayer(cd, (Player)creature, false);
         } else {
            map.sendPacketToAroundPlayer(cd, creature.getActualPosition());
         }

         cd.destroy();
         cd = null;
      }
   }
}
