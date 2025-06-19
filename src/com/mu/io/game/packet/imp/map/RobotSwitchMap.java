package com.mu.io.game.packet.imp.map;

import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.awt.Point;

public class RobotSwitchMap extends ReadAndWritePacket {
   public RobotSwitchMap(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int targetMapID = this.readInt();
      Map map = MapConfig.getLineMap(targetMapID).getAutoSwitchMap();
      if (map != null) {
         this.writeInt(targetMapID);
         this.getPlayer().writePacket(this);
         Point point = new Point(MapConfig.getMapData(targetMapID).getDefaultX(), MapConfig.getMapData(targetMapID).getDefaultY());
         map.switchMap(this.getPlayer(), map, point);
      }

   }
}
