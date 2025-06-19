package com.mu.game.model.item.operation.imp;

import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.item.operation.OperationResult;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AddItemByModelOperation extends AbsAddOrDelItemOperation {
   private List unitList = null;

   public AddItemByModelOperation(List unitList) {
      this.unitList = unitList;
   }

   public OperationResult execute() throws Exception {
      if (this.unitList != null && this.unitList.size() >= 1) {
         this.result = this.addItem();
         return new OperationResult(this.result, this.itemID);
      } else {
         return new OperationResult(3002, -1L);
      }
   }

   protected int addItem() {
      List checkedUnits = new ArrayList();
      this.result = this.checkAddItems(this.unitList, checkedUnits);
      if (this.result == 1) {
         this.result = this.canPutToContainer(checkedUnits);
      }

      if (this.result != 1) {
         return this.result;
      } else {
         this.doOther();
         Iterator var3 = checkedUnits.iterator();

         while(var3.hasNext()) {
            ItemDataUnit unit = (ItemDataUnit)var3.next();
            Storage storage = this.getTargetStorage(ItemModel.getModel(unit.getModelID()).getSort(), unit.getSource());
            long addId = this.addItemDetail(unit, storage);
            if (this.itemID == -1L) {
               this.itemID = addId;
            }
         }

         checkedUnits.clear();
         checkedUnits = null;
         return 1;
      }
   }

   public void doOther() {
   }

   public List getUnitList() {
      return this.unitList;
   }

   public void destroy() {
      super.destroy();
      this.unitList = null;
   }
}
