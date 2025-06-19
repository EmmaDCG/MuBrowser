package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class UpdateBindOperation extends ItemOperation {
   private Item item;
   private boolean bind;
   private int source;

   public UpdateBindOperation(Item item, boolean bind, int source) {
      this.item = item;
      this.bind = bind;
      this.source = source;
   }

   public OperationResult execute() throws Exception {
      if (this.item != null && this.item.getCount() >= 1) {
         this.item.setBind(this.bind);
         Container targetContainer = this.getContainer(this.item.getContainerType());
         this.doUpdateItemAction(this.item, targetContainer, 3, 0, this.source, this.item.getContainerType());
         return new OperationResult(1, this.item.getID());
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public void destroy() {
      this.item = null;
   }
}
