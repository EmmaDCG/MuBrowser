package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.CenterManager;
import com.mu.game.model.transaction.RequestTransaction;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import com.mu.utils.geom.MathUtil;

public class InitTransaction extends ReadAndWritePacket {
   boolean open = false;

   public InitTransaction(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (player.getEvil() > 0) {
         SystemMessage.writeMessage(player, 15031);
      } else {
         long rid = (long)this.readDouble();
         Player targetPlayer = CenterManager.getPlayerByRoleID(rid);
         if (targetPlayer == null) {
            SystemMessage.writeMessage(player, 1021);
         } else if (targetPlayer.getEvil() > 0) {
            SystemMessage.writeMessage(player, 15032);
         } else {
            int result = this.checkSelf(player, targetPlayer.getID());
            if (result != 1) {
               SystemMessage.writeMessage(player, result);
            } else if (targetPlayer.getMap().equals(player.getMap()) && MathUtil.getDistance(targetPlayer.getPosition(), player.getPosition()) <= 10000) {
               result = this.checkTarget(player, targetPlayer);
               if (result != 1) {
                  SystemMessage.writeMessage(player, result);
               } else {
                  RequestTransaction action = new RequestTransaction(player.getID(), targetPlayer.getID());
                  TransactionManager.addRequest(action);
                  ReceiveTransactionRequest.receiveRequest(player.getID(), player.getName(), action.getTime(), targetPlayer);
               }
            } else {
               SystemMessage.writeMessage(player, 15011);
            }
         }
      }
   }

   private int checkSelf(Player self, long targetID) {
      if (self.isTransaction()) {
         return 15017;
      } else if (self.getID() == targetID) {
         return 15019;
      } else {
         RequestTransaction selfAction = TransactionManager.getRequestTranseactionByInitiator(self.getID());
         return selfAction != null && selfAction.getTargetID() == targetID ? 15020 : 1;
      }
   }

   private int checkTarget(Player self, Player target) {
      if (target.isTransaction()) {
         return 15018;
      } else {
         RequestTransaction targetAction = TransactionManager.getRequestTranseactionByTarget(target.getID());
         if (targetAction != null) {
            if (!targetAction.isTimeOut()) {
               return 15010;
            }

            TransactionManager.clearTransactionRequest(targetAction);
         }

         return 1;
      }
   }
}
