package com.mu.game.model.rewardhall;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;

public class RewardItemData extends ItemDataUnit {
   private Item item;

   public RewardItemData(int modelID, int count) {
      super(modelID, count);
      this.setHide(true);
   }

   public void buildItem() {
      Item item = ItemTools.createItem(2, this);
      if (item != null) {
         ItemTools.setSystemExpire(item, this.getExpireTime());
         this.setItem(item);
      }
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }
}
