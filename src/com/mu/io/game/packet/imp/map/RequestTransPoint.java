package com.mu.io.game.packet.imp.map;

import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.tp.TransPoint;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class RequestTransPoint extends ReadAndWritePacket {
   public RequestTransPoint(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long id = (long)this.readDouble();
      if (player.isFighting()) {
         SystemMessage.writeMessage(player, 1047);
      } else {
         TransPoint tp = player.getMap().getTransPoint(id);
         if (tp != null) {
            tp.excute(player);
         }

      }
   }
}
