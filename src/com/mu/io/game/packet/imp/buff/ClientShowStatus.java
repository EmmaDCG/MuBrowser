package com.mu.io.game.packet.imp.buff;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.util.concurrent.ConcurrentHashMap;

public class ClientShowStatus extends WriteOnlyPacket {
   public ClientShowStatus(Creature creature) {
      super(31005);

      try {
         this.writeByte(creature.getType());
         this.writeDouble((double)creature.getID());
         writeStatus(creature, this);
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }

   public static void sendToSelf(Player player) {
      ClientShowStatus css = new ClientShowStatus(player);
      player.writePacket(css);
      css.destroy();
      css = null;
   }

   public static void sendToClient(Creature creature) {
      if (DeleteBuff.needToSend(creature)) {
         ClientShowStatus css = new ClientShowStatus(creature);
         creature.getMap().sendPacketToAroundPlayer(css, creature.getPosition());
         css.destroy();
         css = null;
      }
   }

   public static void writeStatus(Creature creature, WriteOnlyPacket packet) {
      try {
         ConcurrentHashMap map = creature.getBuffManager().getPerformStatusMap();
         packet.writeBoolean(!map.containsKey(Integer.valueOf(1)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(2)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(3)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(4)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(5)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(6)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(9)));
         packet.writeBoolean(map.containsKey(Integer.valueOf(8)));
      } catch (Exception var3) {
         var3.printStackTrace();
      }

   }
}
