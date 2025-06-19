package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.CenterManager;
import com.mu.game.model.transaction.Transaction;
import com.mu.game.model.transaction.TransactionList;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class DeleteItemByPlayer extends ReadAndWritePacket {
   public DeleteItemByPlayer(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      long itemID = (long)this.readDouble();
      Transaction action = TransactionManager.getTransAcTransaction(player.getID());
      if (action == null) {
         SystemMessage.writeMessage(player, 15009);
      } else {
         Player target = CenterManager.getPlayerByRoleID(action.getOtherID(player.getID()));
         if (target == null) {
            SystemMessage.writeMessage(player, 1021);
         } else {
            TransactionList actionList = action.getTransactionList(player.getID());
            int[] result = actionList.removeItem(itemID);
            if (result[0] != 1) {
               SystemMessage.writeMessage(player, result[0]);
            } else {
               DeleteItemByServer.deletItem(player, target, result[1], itemID);
            }
         }
      }
   }
}
