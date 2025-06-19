package com.mu.game.model.transaction;

import com.mu.game.model.item.Item;

public class TransactionList {
   public static final int MaxSize = 6;
   long PlayerID;
   Item[] items = new Item[6];
   int money;
   boolean isLocked = false;
   boolean isConfirm = false;
   private int currentIngot = 0;
   private int currentMoney = 0;

   public TransactionList(long pid) {
      this.PlayerID = pid;
   }

   public void destroy() {
      this.items = null;
   }

   public Item[] getItems() {
      return this.items;
   }

   public void setItems(Item[] items) {
      this.items = items;
   }

   public int getCurrentIngot() {
      return this.currentIngot;
   }

   public void setCurrentIngot(int currentIngot) {
      this.currentIngot = currentIngot;
   }

   public int getCurrentMoney() {
      return this.currentMoney;
   }

   public void setCurrentMoney(int currentMoney) {
      this.currentMoney = currentMoney;
   }

   public int getMoney() {
      return this.money;
   }

   public int setMoney(int money) {
      if (this.isLocked) {
         return 15001;
      } else {
         this.money = money;
         return 1;
      }
   }

   public int confirm(TransactionList otherList) {
      if (!this.isLocked) {
         return 15002;
      } else if (!otherList.isLocked) {
         return 15003;
      } else {
         this.isConfirm = true;
         return 1;
      }
   }

   public int unConfirm() {
      this.isConfirm = false;
      return 1;
   }

   public boolean isConfirm() {
      return this.isConfirm;
   }

   public long getPlayerID() {
      return this.PlayerID;
   }

   public int[] addItem(Item item) {
      if (this.isLocked) {
         return new int[]{15001};
      } else {
         int index;
         for(index = 0; index < this.items.length; ++index) {
            if (this.items[index] != null && this.items[index].getID() == item.getID()) {
               return new int[]{15005};
            }
         }

         index = -1;

         for(int i = 0; i < this.items.length; ++i) {
            if (this.items[i] == null) {
               this.items[i] = item;
               index = i;
               break;
            }
         }

         if (index == -1) {
            return new int[]{15030};
         } else {
            Item tmpItem = item.cloneItem(2);
            tmpItem.setId(item.getID());
            TransactionManager.addTransactionItem(tmpItem);
            return new int[]{1, index};
         }
      }
   }

   public synchronized int[] removeItem(long itemID) {
      if (this.isLocked) {
         return new int[]{15001, -1};
      } else {
         boolean hasItem = false;
         int index = -1;

         for(int i = 0; i < this.items.length; ++i) {
            Item item = this.items[i];
            if (item != null && item.getID() == itemID) {
               hasItem = true;
               index = i;
               break;
            }
         }

         if (hasItem) {
            this.items[index] = null;
            TransactionManager.removeTransactionItem(itemID);
            return new int[]{1, index};
         } else {
            return new int[]{15033, -1};
         }
      }
   }

   public boolean isLocked() {
      return this.isLocked;
   }

   public void setLocked(boolean isLocked) {
      this.isLocked = isLocked;
   }
}
