package com.mu.io.game.packet.imp.dungeon;

import com.mu.game.dungeon.DungeonTemplateFactory;
import com.mu.game.dungeon.imp.bigdevil.BigDevilSquareTemplate;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class BigDevilTopRequest extends ReadAndWritePacket {
   public BigDevilTopRequest(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public BigDevilTopRequest() {
      super(12019, (byte[])null);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      ((BigDevilSquareTemplate)DungeonTemplateFactory.getTemplate(6)).writeTopInfo(player);
   }
}
