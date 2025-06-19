package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class AutoUseItemEvent extends Event {
   private static int intervalTime = 5000;
   private long itemID;
   private long directTime;

   public AutoUseItemEvent(Player owner, long itemID) {
      super(owner);
      this.itemID = itemID;
      this.directTime = System.currentTimeMillis();
   }

   public void work(long now) throws Exception {
      if (this.directTime + (long)intervalTime <= now) {
         Item item = ((Player)this.getOwner()).getBackpack().getItemByID(this.itemID);
         if (item == null) {
            this.setEnd(true);
         } else if (!((Player)this.getOwner()).isDie() && !((Player)this.getOwner()).operationInLimit(OperationEnum.USEITEM)) {
            int result = ((Player)this.getOwner()).getItemManager().useItem(item, 1, true).getResult();
            if (result != 3008) {
               this.setEnd(true);
            }

         }
      }
   }

   public Status getStatus() {
      return Status.AutoAssSkill;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
