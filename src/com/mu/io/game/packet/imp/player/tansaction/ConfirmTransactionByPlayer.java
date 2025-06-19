package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.CenterManager;
import com.mu.game.model.transaction.Transaction;
import com.mu.game.model.transaction.TransactionList;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class ConfirmTransactionByPlayer extends ReadAndWritePacket {
   public ConfirmTransactionByPlayer(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      Transaction action = TransactionManager.getTransAcTransaction(player.getID());
      if (action == null) {
         this.writeErrorMsg(player, false);
      } else {
         Player target = CenterManager.getPlayerByRoleID(action.getOtherID(player.getID()));
         if (target == null) {
            this.writeErrorMsg(player, false);
         } else {
            TransactionList actionList = action.getTransactionList(player.getID());
            TransactionList otherList = action.getTransactionList(target.getID());
            int result = actionList.confirm(otherList);
            if (result != 1) {
               this.writeErrorMsg(player, false);
            } else {
               this.writeErrorMsg(player, true);
               TransactionManager.doTransaction(player.getID());
            }
         }
      }
   }

   public void writeErrorMsg(Player player, boolean b) throws Exception {
      this.writeBoolean(b);
      player.writePacket(this);
   }
}
