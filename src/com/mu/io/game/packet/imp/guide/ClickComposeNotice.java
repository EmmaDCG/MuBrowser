package com.mu.io.game.packet.imp.guide;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ClickComposeNotice extends ReadAndWritePacket {
   public ClickComposeNotice(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      player.getArrowGuideManager().pushArrow(12, MessageText.getText(4030));
   }
}
