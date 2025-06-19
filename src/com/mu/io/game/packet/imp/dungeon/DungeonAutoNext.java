package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.imp.bloodcastle.BloodCastleMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class DungeonAutoNext extends ReadAndWritePacket {
   public DungeonAutoNext(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      if (map instanceof BloodCastleMap) {
         ((BloodCastleMap)map).doNext(player);
      }

   }
}
