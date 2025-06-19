package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.imp.redfort.RedFortMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RedFortReceive extends ReadAndWritePacket {
   public RedFortReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      if (map instanceof RedFortMap) {
         RedFortMap mr = (RedFortMap)map;
         this.writeBoolean(mr.doReceive(player, 1));
         player.writePacket(this);
      }

   }
}
