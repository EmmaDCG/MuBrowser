package com.mu.game.model.item.operation.imp;

import com.mu.game.model.equip.newStone.StoneDataManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemStone;
import com.mu.game.model.item.operation.ItemOperation;
import com.mu.game.model.item.operation.OperationResult;

public class UpdateStoneOperation extends ItemOperation {
   private Item item;
   private int id;
   private boolean mosaic;

   public UpdateStoneOperation(Item item, int id, boolean mosaic) {
      this.item = item;
      this.id = id;
      this.mosaic = mosaic;
   }

   public OperationResult execute() throws Exception {
      if (this.mosaic) {
         int equipStatID = StoneDataManager.getRndEquipStatID(this.id);
         this.item.addStone(new ItemStone(this.id, equipStatID, this.item.getStones().size()), true);
      } else if (this.id == -1) {
         this.item.removeAllStone();
      } else {
         this.item.removeStone(this.id);
      }

      this.updateItemOtherOperation(this.item, 21, 9);
      return new OperationResult(1, this.item.getID());
   }
}
