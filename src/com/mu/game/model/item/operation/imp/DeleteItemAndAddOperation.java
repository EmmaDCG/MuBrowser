package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.container.Container;
import com.mu.game.model.item.operation.OperationResult;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class DeleteItemAndAddOperation extends AddItemByModelOperation {
   private HashMap delMap = null;
   private int source;
   private int delType;

   public DeleteItemAndAddOperation(HashMap delMap, int source, List unitList) {
      super(unitList);
      this.delMap = delMap;
      this.source = source;
   }

   public OperationResult execute() throws Exception {
      if (this.delMap != null && this.delMap.size() >= 1 || this.getUnitList() != null && this.getUnitList().size() >= 1) {
         if (this.delMap != null) {
            this.result = this.hasItem(this.delMap);
            if (this.result != 1) {
               return new OperationResult(this.result, this.itemID);
            }
         }

         if (this.getUnitList() != null && this.getUnitList().size() > 0) {
            this.addItem();
         } else {
            this.doOther();
         }

         return new OperationResult(this.result, this.itemID);
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   public void doOther() {
      if (this.delMap != null) {
         this.delItem(this.delMap);
      }

   }

   protected void delItem(HashMap delMap) {
      int i = 0;

      for(Iterator var4 = delMap.entrySet().iterator(); var4.hasNext(); ++i) {
         Entry entry = (Entry)var4.next();
         Item item = (Item)entry.getKey();
         int count = ((Integer)entry.getValue()).intValue();
         Container container = this.getContainer(item.getContainerType());
         this.doRemoveItemAction(item, count, container, this.source);
         if (i == 0) {
            this.itemID = item.getID();
         }
      }

   }

   protected int hasItem(HashMap delItems) {
      if (delItems != null && delItems.size() >= 1) {
         Iterator var3 = delItems.entrySet().iterator();

         Item item;
         int count;
         do {
            if (!var3.hasNext()) {
               return 1;
            }

            Entry entry = (Entry)var3.next();
            item = (Item)entry.getKey();
            count = ((Integer)entry.getValue()).intValue();
            Container contaier = this.getContainer(item.getContainerType());
            if (contaier == null) {
               return 3002;
            }

            if (!contaier.hasItem(item.getID())) {
               return 3002;
            }
         } while(item.getCount() >= count);

         return 3001;
      } else {
         return 3002;
      }
   }
}
