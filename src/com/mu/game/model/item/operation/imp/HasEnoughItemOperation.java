package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.operation.OperationResult;
import java.util.HashMap;

public class HasEnoughItemOperation extends AbsAddOrDelItemOperation {
   public HashMap needItems = null;
   private int result;

   public HasEnoughItemOperation(HashMap needItems) {
      this.needItems = needItems;
   }

   public OperationResult execute() throws Exception {
      boolean flag = this.hasEnoughItem(this.needItems);
      if (!flag) {
         this.result = 3001;
      }

      return new OperationResult(this.result, -1L);
   }
}
