package com.mu.io.game.packet.imp.task;

import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.task.TaskPosition;
import com.mu.game.model.task.clazz.TaskClazzXS;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.awt.Point;

public class EnterXSMap extends ReadAndWritePacket {
   public EnterXSMap(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      int xsId = this.readInt();
      TaskClazzXS xs = TaskConfigManager.getXS(xsId);
      TaskPosition position = TaskConfigManager.getPosition(xs.getPositionId());
      if (position != null) {
         MapData md = MapConfig.getMapData(position.getMapId());
         if (md != null) {
            this.getPlayer().switchMap(position.getMapId(), new Point(position.getX(), position.getY()));
         }
      }

   }
}
