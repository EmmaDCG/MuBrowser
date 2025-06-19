package com.mu.game.model.restrict;

import com.mu.game.model.item.Item;
import com.mu.game.model.transaction.TransactionManager;
import com.mu.game.model.unit.player.Player;

public class Restriction {
   public static boolean isInTranction(Item item) {
      return item == null ? false : TransactionManager.isTransActionItem(item.getID());
   }

   public static boolean itemOperationPre(Player player) {
      return TransactionManager.isTransaction(player.getID());
   }
}
