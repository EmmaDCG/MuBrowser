package com.mu.io.game.packet.imp.guide;

import com.mu.game.model.fp.FunctionPreviewManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class FunctionPreviewReceive extends ReadAndWritePacket {
   public FunctionPreviewReceive(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      this.writeBoolean(FunctionPreviewManager.receive(player));
      player.writePacket(this);
   }
}
