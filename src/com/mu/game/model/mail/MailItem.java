package com.mu.game.model.mail;

import com.mu.game.model.item.Item;

public class MailItem {
   private int index;
   private Item item;

   public MailItem(int index) {
      this.index = index;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public int getIndex() {
      return this.index;
   }

   public Item getItem() {
      return this.item;
   }

   public void destroy() {
      if (this.item != null) {
         this.item.destroy();
         this.item = null;
      }

   }
}
