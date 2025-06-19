package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class MoveItemFromDeport extends ReadAndWritePacket {
   public MoveItemFromDeport(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int slot = this.readShort();
      OperationResult result = player.getItemManager().moveToOtherContainer(4, 1, itemID, slot);
      if (result.getResult() != 1 && result.getResult() != 2009) {
         SystemMessage.writeMessage(player, result.getResult());
      }

   }
}
