package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.OperationResult;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class AddAndDeleteModelOperation extends AddItemByModelOperation {
   private HashMap delMap = null;
   private int source;
   private boolean defaultBind;

   public AddAndDeleteModelOperation(HashMap delMap, int source, boolean defaultBind, List unitList) {
      super(unitList);
      this.source = source;
      this.defaultBind = defaultBind;
      this.delMap = delMap;
   }

   public OperationResult execute() throws Exception {
      if ((this.delMap == null || this.delMap.size() < 1) && (this.getUnitList() == null || this.getUnitList().size() < 1)) {
         return new OperationResult(3002, -1L);
      } else {
         if (this.delMap != null) {
            this.result = this.hasItem();
            if (this.result != 1) {
               return new OperationResult(this.result, this.itemID);
            }
         }

         if (this.getUnitList() != null) {
            this.addItem();
         }

         this.doOther();
         return new OperationResult(this.result, this.itemID);
      }
   }

   public void doOther() {
      if (this.delMap != null) {
         this.delItem();
      }

   }

   private int hasItem() {
      boolean hasEnough = this.hasEnoughItem(this.delMap);
      return !hasEnough ? 3001 : 1;
   }

   public void delItem() {
      Iterator it = this.delMap.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         int modelID = ((Integer)entry.getKey()).intValue();
         Storage storage = this.getTargetStorage(ItemModel.getModel(modelID).getSort(), this.source);
         this.deleteItemDetail(modelID, ((Integer)entry.getValue()).intValue(), storage, this.defaultBind, this.source);
      }

   }
}
