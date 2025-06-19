package com.mu.io.game.packet.imp.map;

import com.mu.config.Global;
import com.mu.game.dungeon.map.DungeonMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.MD5;
import java.awt.Point;

public class TransferMap extends ReadAndWritePacket {
   public TransferMap(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public TransferMap() {
      super(10106, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map curMap = player.getMap();
      if (curMap != null && curMap.canTransmit()) {
         if (player.isFighting()) {
            SystemMessage.writeMessage(player, 1047);
         } else {
            DungeonMap map = player.getDungeonMap();
            if (map != null && map.getDungeon().isSwitchMapMustQuit()) {
               SystemMessage.writeMessage(player, 1028);
            } else {
               int mapID = this.readUnsignedShort();
               String tag = this.readUTF();
               if (MD5.md5s(Global.getGameverifykey() + mapID).equals(tag)) {
                  int result = 1;
                  MapData data = MapConfig.getMapData(mapID);
                  if (data != null) {
                     if (!player.getMap().switchMap(player, mapID, new Point(data.getDefaultX(), data.getDefaultY()))) {
                        result = 2;
                     }
                  } else {
                     result = 0;
                  }

                  if (result == 0) {
                     SystemMessage.writeMessage(player, 1025);
                  }

                  if (result != 2) {
                     MapSwitchRequest.trans(player, mapID, result == 1);
                  }

               }
            }
         }
      }
   }

   public static void playerSwitchMapDefault(Player player, int mapID, int x, int y) {
      try {
         TransferMap sm = new TransferMap();
         sm.writeBoolean(true);
         sm.writeShort(mapID);
         sm.writeInt(x);
         sm.writeInt(y);
         player.writePacket(sm);
         sm.destroy();
         sm = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
