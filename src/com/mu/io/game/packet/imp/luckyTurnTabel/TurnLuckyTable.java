package com.mu.io.game.packet.imp.luckyTurnTabel;

import com.mu.game.model.luckyTurnTable.LuckyTurnTableManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class TurnLuckyTable extends ReadAndWritePacket {
   public TurnLuckyTable(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int[] results = LuckyTurnTableManager.doLuckyTurn(player);
      this.writeBoolean(results[0] == 1);
      if (results[0] == 1) {
         this.writeByte(results[1]);
         this.writeInt(results[2]);
      } else {
         SystemMessage.writeMessage(player, results[0]);
      }

      player.writePacket(this);
      results = null;
   }
}
