package com.mu.io.game.packet.imp.player.tansaction;

import com.mu.game.model.transaction.Transaction;
import com.mu.game.model.transaction.TransactionList;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;

public class BeginTransaction extends WriteOnlyPacket {
   public BeginTransaction() {
      super(13005);
   }

   public static void startTransaction(Player initiator, Player target) {
      try {
         TransactionList tl1 = new TransactionList(initiator.getID());
         TransactionList tl2 = new TransactionList(target.getID());
         Transaction ta = new Transaction(tl1, tl2);
         tl1.setCurrentIngot(initiator.getIngot());
         tl1.setCurrentMoney(initiator.getMoney());
         tl2.setCurrentIngot(target.getIngot());
         tl2.setCurrentMoney(target.getMoney());
         TransactionManager.addTransaction(initiator.getID(), target.getID(), ta);
         BeginTransaction bt = new BeginTransaction();
         bt.writeDouble((double)target.getID());
         bt.writeUTF(target.getName());
         bt.writeShort(target.getLevel());
         initiator.writePacket(bt);
         bt.destroy();
         bt = null;
         bt = new BeginTransaction();
         bt.writeDouble((double)initiator.getID());
         bt.writeUTF(initiator.getName());
         bt.writeShort(initiator.getLevel());
         target.writePacket(bt);
         bt.destroy();
         bt = null;
         initiator.startTransAction(target);
         target.startTransAction(initiator);
      } catch (Exception var6) {
         var6.printStackTrace();
      }

   }
}
