package com.mu.io.game.packet.imp.player;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.awt.Point;

public class Move extends ReadAndWritePacket {
   public Move(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      Point[] points = new Point[this.readUnsignedShort()];

      for(int i = 0; i < points.length; ++i) {
         int x = this.readInt();
         int y = this.readInt();
         if (x < map.getLeft() || x > map.getRight() || y < map.getBottom() || y > map.getTop()) {
            return;
         }

         points[i] = new Point(x, y);
      }

      player.startMove(points, System.currentTimeMillis() - (long)player.getClientDelay());
   }
}
