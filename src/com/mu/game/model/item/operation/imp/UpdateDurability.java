package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;
import java.util.HashMap;
import java.util.Iterator;

public class UpdateDurability extends ItemOperation {
   private HashMap itemMap = null;

   public UpdateDurability(HashMap itemMap) {
      this.itemMap = itemMap;
   }

   public OperationResult execute() throws Exception {
      Iterator var2 = this.itemMap.keySet().iterator();

      while(var2.hasNext()) {
         Item item = (Item)var2.next();
         int dura = item.getDurability() + ((Integer)this.itemMap.get(item)).intValue();
         if (dura > item.getMaxDurability()) {
            dura = item.getMaxDurability();
         }

         if (dura < 0) {
            dura = 0;
         }

         item.setDurability(dura);
         this.doUpdateItemAction(item, this.getContainer(item.getContainerType()), 11, 0, 25, item.getContainerType());
      }

      return new OperationResult(1, -1L);
   }
}
