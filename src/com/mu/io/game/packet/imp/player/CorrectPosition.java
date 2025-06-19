package com.mu.io.game.packet.imp.player;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import java.awt.Point;
import java.awt.Rectangle;

public class CorrectPosition extends WriteOnlyPacket {
   public CorrectPosition() {
      super(10204);
   }

   private void writeInfo(int type, long creatureId, int x, int y, int face) {
      try {
         this.writeByte(type);
         this.writeDouble((double)creatureId);
         this.writeShort(x);
         this.writeShort(y);
         this.writeByte(face);
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }

   public static void correctPosition(Player player) {
      Map map = player.getMap();
      int x = player.getX();
      int y = player.getY();
      if (map.isBlocked(x, y)) {
         Point p = map.searchFeasiblePoint(x, y);
         x = p.x;
         y = p.y;
      }

      Rectangle newArea = map.getArea(x, y);
      Rectangle oldArea = player.getArea();
      if (newArea != null && !newArea.equals(oldArea)) {
         player.switchArea(newArea, oldArea);
      }

      player.setPosition(x, y);
      CorrectPosition cp = new CorrectPosition();
      cp.writeInfo(1, player.getID(), x, y, 0);
      player.writePacket(cp);
      cp.destroy();
      cp = null;
      player.idle();
   }
}
