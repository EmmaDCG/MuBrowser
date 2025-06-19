package com.mu.io.game.packet.imp.monster;

import com.mu.game.dungeon.DungeonManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.map.MapData;
import com.mu.game.model.unit.monster.worldboss.WorldBossData;
import com.mu.game.model.unit.monster.worldboss.WorldBossManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class BossTeleport extends ReadAndWritePacket {
   public BossTeleport(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      if (map != null && map.canTransmit()) {
         int bossId = this.readUnsignedShort();
         WorldBossData data = WorldBossManager.getBossData(bossId);
         if (data != null) {
            if (data.getBossType() != 3) {
               int mapId = data.getMapId();
               int x = data.getX();
               int y = data.getY();
               MapData md = MapConfig.getMapData(mapId);
               if (md == null) {
                  return;
               }

               this.writeShort(mapId);
               this.writeInt(x);
               this.writeInt(y);
               this.writeInt(data.getTemplateId());
               player.writePacket(this);
            } else {
               DungeonManager.createAndEnterDungeon(player, 7, bossId);
            }

         }
      }
   }
}
