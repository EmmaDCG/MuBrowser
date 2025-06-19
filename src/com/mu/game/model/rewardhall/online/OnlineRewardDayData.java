package com.mu.game.model.rewardhall.online;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;

public class OnlineRewardDayData {
   private int id;
   private int seconds;
   private ItemDataUnit idu;
   private Item item;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getSeconds() {
      return this.seconds;
   }

   public void setSeconds(int seconds) {
      this.seconds = seconds;
   }

   public void setIDU(ItemDataUnit idu) {
      Item item = ItemTools.createItem(2, idu);
      if (item == null) {
         (new Exception()).printStackTrace();
      } else {
         ItemTools.setSystemExpire(item, idu.getExpireTime());
         this.idu = idu;
         this.item = item;
      }
   }

   public ItemDataUnit getIDU() {
      return this.idu;
   }

   public Item getItem() {
      return this.item;
   }
}
