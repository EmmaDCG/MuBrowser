package com.mu.game.model.packet;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.player.PlayerAttributes;

public class CreatureHpMpPacket {
   public static void creatureMpChange(Creature creature) {
      if (creature.getType() == 1) {
         PlayerAttributes.sendToClient((Player)creature, StatEnum.MP);
      }

   }

   public static void creatureSdChange(Creature creature) {
      if (creature.getType() == 1) {
         PlayerAttributes.sendToClient((Player)creature, StatEnum.SD);
      }

   }

   public static void creatureApChange(Creature creature) {
      if (creature.getType() == 1) {
         PlayerAttributes.sendToClient((Player)creature, StatEnum.AP);
      }

   }

   public static void creatureAGChange(Creature creature) {
      if (creature.getType() == 1) {
         PlayerAttributes.sendToClient((Player)creature, StatEnum.AG);
      }

   }
}
