package com.mu.io.game.packet.imp.map;

import com.mu.game.model.map.LineMap;
import com.mu.game.model.map.Map;
import com.mu.game.model.map.MapConfig;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class SwitchLine extends ReadAndWritePacket {
   public SwitchLine(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public SwitchLine() {
      super(10112, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.isInDungeon()) {
         SystemMessage.writeMessage(player, 1027);
      } else if (player.isFighting()) {
         SystemMessage.writeMessage(player, 1046);
      } else {
         Map map = player.getMap();
         int line = this.readByte();
         int curLine = map.getLine();
         if (line != curLine) {
            LineMap lm = MapConfig.getLineMap(map.getID());
            if (line >= 0 && line < lm.getAllMaps().length) {
               if (!player.isInDungeon()) {
                  player.switchMap(lm.getMapByLine(line), player.getPosition());
               }
            }
         }
      }
   }

   public static void pushCurrentLine(Player player) {
      SwitchLine sl = new SwitchLine();

      try {
         Map map = player.getMap();
         if (map != null) {
            sl.writeByte(MapConfig.getLineMap(map.getID()).getAllMaps().length);
            sl.writeByte(map.getLine());
            player.writePacket(sl);
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      } finally {
         sl.destroy();
         sl = null;
      }

   }
}
