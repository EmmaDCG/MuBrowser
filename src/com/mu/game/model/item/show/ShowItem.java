package com.mu.game.model.item.show;

import com.mu.game.model.item.Item;

public class ShowItem {
   private Item item;
   private long showTime;

   public ShowItem(Item item) {
      this.item = item;
      this.showTime = System.currentTimeMillis();
   }

   public boolean isTimeOver(long now) {
      return this.getShowTime() + 14400000L < now;
   }

   public void destroy() {
      if (this.item != null) {
         this.item.destroy();
         this.item = null;
      }

   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public long getShowTime() {
      return this.showTime;
   }

   public void setShowTime(long showTime) {
      this.showTime = showTime;
   }
}
