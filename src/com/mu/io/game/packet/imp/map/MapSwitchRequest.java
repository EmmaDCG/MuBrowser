package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class MapSwitchRequest extends ReadAndWritePacket {
   public MapSwitchRequest(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public MapSwitchRequest() {
      super(10113, (byte[])null);
   }

   public void process() throws Exception {
   }

   public static final void trans(Player player, int mapId, boolean b) {
      try {
         MapSwitchRequest mr = new MapSwitchRequest();
         mr.writeShort(mapId);
         mr.writeBoolean(b);
         player.writePacket(mr);
         mr.destroy();
         mr = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

   }
}
