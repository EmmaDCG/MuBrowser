package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.CenterManager;
import com.mu.game.model.transaction.RequestTransaction;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.geom.MathUtil;

public class AcceptTransaction extends ReadAndWritePacket {
   public AcceptTransaction(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      RequestTransaction action = TransactionManager.getRequestTranseactionByTarget(player.getID());
      if (action == null) {
         SystemMessage.writeMessage(player, 15010);
      } else if (action.isTimeOut()) {
         SystemMessage.writeMessage(player, 15010);
         TransactionManager.clearTransactionRequest(action);
      } else {
         Player initiator = CenterManager.getPlayerByRoleID(action.getInitiatorID());
         if (initiator == null) {
            SystemMessage.writeMessage(player, 1021);
            TransactionManager.clearTransactionRequest(action);
         } else {
            int result = this.checkInitiator(player, initiator);
            if (result != 1) {
               SystemMessage.writeMessage(player, result);
               TransactionManager.clearTransactionRequest(action);
            } else if (initiator.getMap().equals(player.getMap()) && MathUtil.getDistance(initiator.getPosition(), player.getPosition()) <= 10000) {
               TransactionManager.clearTransactionRequest(action);
               BeginTransaction.startTransaction(initiator, player);
            } else {
               SystemMessage.writeMessage(player, 15011);
               TransactionManager.clearTransactionRequest(action);
            }
         }
      }
   }

   private int checkInitiator(Player self, Player initiator) {
      return initiator.isTransaction() ? 15018 : 1;
   }
}
