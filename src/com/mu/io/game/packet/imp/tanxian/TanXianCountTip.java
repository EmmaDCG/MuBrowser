package com.mu.io.game.packet.imp.tanxian;

import com.mu.game.model.tanxian.TanXianConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class TanXianCountTip extends ReadAndWritePacket {
   public TanXianCountTip(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player != null) {
         this.writeUTF(TanXianConfigManager.TANXIAN_COUNT_TIP);
         player.writePacket(this);
      }
   }
}
