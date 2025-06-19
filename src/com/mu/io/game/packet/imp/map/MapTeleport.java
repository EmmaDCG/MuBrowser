package com.mu.io.game.packet.imp.map;

import com.mu.config.Global;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.MD5;
import java.awt.Point;

public class MapTeleport extends ReadAndWritePacket {
   public MapTeleport(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Map map = player.getMap();
      if (map != null && map.canTransmit() && map.canSwitchByLink()) {
         if (player.isFighting()) {
            SystemMessage.writeMessage(player, 1047);
         } else {
            player.cancelPushTaskFutre();
            int mapId = this.readUnsignedShort();
            int x = this.readInt();
            int y = this.readInt();
            String tag = this.readUTF();
            if (MD5.md5s(x + Global.getGameverifykey() + y).equals(tag)) {
               player.switchMap(mapId, new Point(x, y));
            }

         }
      }
   }
}
