package com.mu.io.game.packet.imp.item;

import com.mu.game.model.item.operation.OperationResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class MoveItemToDeport extends ReadAndWritePacket {
   public MoveItemToDeport(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      int slot = this.readShort();
      OperationResult result = player.getItemManager().moveToOtherContainer(1, 4, itemID, slot);
      int rs = result.getResult();
      if (rs != 1 && rs != 2009) {
         SystemMessage.writeMessage(player, rs);
      }

   }
}
