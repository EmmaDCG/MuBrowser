package com.mu.io.game.packet.imp.attack;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.awt.Point;

public class CreaturePositionCorrect extends WriteOnlyPacket {
   public CreaturePositionCorrect(MapUnit creature, int x, int y) {
      super(32007);

      try {
         this.writeByte(creature.getType());
         this.writeDouble((double)creature.getID());
         this.writeInt(x);
         this.writeInt(y);
         this.writeByte(creature.getFace()[0]);
         this.writeByte(creature.getFace()[1]);
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }

   public static void correntWhenOutofRange(Player player, Creature target, int x, int y) {
      SelfPositionCorrect.sendToClient(player, x, y);
      Point point = target.getActualPosition();
      CreaturePositionCorrect ct = new CreaturePositionCorrect(target, point.x, point.y);
      player.writePacket(ct);
      ct.destroy();
      ct = null;
   }

   public static void correntWhenTeleport(MapUnit owner, int x, int y) {
      Player player = null;
      if (owner.getType() == 1) {
         player = (Player)owner;
      }

      CreaturePositionCorrect ct = new CreaturePositionCorrect(owner, x, y);
      if (player != null) {
         player.getMap().sendPacketToAroundPlayer(ct, player, false);
         SelfPositionCorrect.sendToClient(player, x, y);
      } else {
         owner.getMap().sendPacketToAroundPlayer(ct, new Point(x, y));
      }

      ct.destroy();
      ct = null;
   }
}
