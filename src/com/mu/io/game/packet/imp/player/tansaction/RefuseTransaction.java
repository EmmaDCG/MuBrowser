package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.CenterManager;
import com.mu.game.model.transaction.RequestTransaction;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;

public class RefuseTransaction extends ReadAndWritePacket {
   public RefuseTransaction(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      RequestTransaction action = TransactionManager.getRequestTranseactionByTarget(player.getID());
      if (action != null) {
         TransactionManager.clearTransactionRequest(action);
         Player initiator = CenterManager.getPlayerByRoleID(action.getInitiatorID());
         if (initiator != null) {
            this.writeUTF(player.getName());
            initiator.writePacket(this);
         }
      }

   }
}
