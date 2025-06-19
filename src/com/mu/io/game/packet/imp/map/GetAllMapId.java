package com.mu.io.game.packet.imp.map;

import com.mu.config.Global;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import java.util.ArrayList;
import java.util.Iterator;

public class GetAllMapId extends ReadAndWritePacket {
   public GetAllMapId(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ArrayList list = new ArrayList();
      Iterator it;
      MapData md;
      if (Global.isInterServiceServer()) {
         it = MapConfig.getAllMapData().values().iterator();

         while(it.hasNext()) {
            md = (MapData)it.next();
            if (md.getInterMapType() == 2) {
               list.add(md.getMapID());
            }
         }
      } else {
         it = MapConfig.getAllMapData().values().iterator();

         while(it.hasNext()) {
            md = (MapData)it.next();
            if (md.getInterMapType() == 1) {
               list.add(md.getMapID());
            }
         }
      }

      this.writeByte(list.size());
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         int i = ((Integer)var6.next()).intValue();
         this.writeShort(i);
      }

      player.writePacket(this);
      list.clear();
   }
}
