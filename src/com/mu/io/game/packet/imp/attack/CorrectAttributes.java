package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.player.PlayerAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CorrectAttributes extends WriteOnlyPacket {
   public CorrectAttributes(Creature creature, List statlist) {
      super(10248);

      try {
         this.writeByte(creature.getType());
         this.writeDouble((double)creature.getID());
         this.writeShort(statlist.size());
         Iterator var4 = statlist.iterator();

         while(var4.hasNext()) {
            StatEnum stat = (StatEnum)var4.next();
            PlayerAttributes.writeStat(stat, getValue(creature, stat), this);
         }
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static long getValue(Creature creature, StatEnum stat) {
      return creature.getType() == 1 ? PlayerAttributes.getArributeValue((Player)creature, stat) : (long)creature.getStatValue(stat);
   }

   public static void sendWhenChange(Creature creature, List statlist) {
      CorrectAttributes ca = new CorrectAttributes(creature, statlist);
      if (creature.getType() == 1) {
         Player player = (Player)creature;
         player.getMap().sendPacketToAroundPlayer(ca, player, false);
      } else {
         creature.getMap().sendPacketToAroundPlayer(ca, creature.getActualPosition());
      }

      ca.destroy();
      ca = null;
   }

   public static void sendWhenChange(Creature creature, StatEnum stat) {
      List statlist = new ArrayList();
      statlist.add(stat);
      sendWhenChange(creature, (List)statlist);
      statlist.clear();
      statlist = null;
   }

   public static void sendToSelf(Player player, List statList) {
      CorrectAttributes ca = new CorrectAttributes(player, statList);
      ca.destroy();
      ca = null;
   }
}
