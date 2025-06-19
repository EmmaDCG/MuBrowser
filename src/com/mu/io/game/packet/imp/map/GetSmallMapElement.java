package com.mu.io.game.packet.imp.map;

import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class GetSmallMapElement extends ReadAndWritePacket {
   public GetSmallMapElement(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public GetSmallMapElement() {
      super(10108, (byte[])null);
   }

   public void process() throws Exception {
      int mapID = this.readShort();
      MapData md = MapConfig.getMapData(mapID);
      if (md != null) {
         Player player = this.getPlayer();
         player.writePacket(md.getSmallMapElementPacket());
      }

   }
}
