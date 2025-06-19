package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.CenterManager;
import com.mu.game.model.transaction.Transaction;
import com.mu.game.model.transaction.TransactionList;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class PutMoneyByPlayer extends ReadAndWritePacket {
   boolean open = false;

   public PutMoneyByPlayer(int code, byte[] readData) {
      super(code, readData);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      if (!this.open) {
         SystemMessage.writeMessage(player, "暂不支持金币交易", -1);
      } else {
         int money = this.readInt();
         Transaction action = TransactionManager.getTransAcTransaction(player.getID());
         if (action == null) {
            SystemMessage.writeMessage(player, 15009);
         } else if (player.getMoney() >= money && money >= 0) {
            Player target = CenterManager.getPlayerByRoleID(action.getOtherID(player.getID()));
            if (target == null) {
               SystemMessage.writeMessage(player, 1021);
            } else {
               TransactionList actionList = action.getTransactionList(player.getID());
               int result = actionList.setMoney(money);
               if (result != 1) {
                  SystemMessage.writeMessage(player, result);
               } else {
                  PutMoneyByServer.putMoneyByServer(player, target, money);
               }
            }
         } else {
            SystemMessage.writeMessage(player, 15029);
         }
      }
   }
}
