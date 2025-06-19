package com.mu.game.dungeon;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import java.util.ArrayList;

public class DungeonReward {
   private long exp;
   private int money;
   private ArrayList itemList = new ArrayList();
   private ArrayList itemDataList = new ArrayList();

   public long getExp() {
      return this.exp;
   }

   public void setExp(long exp) {
      this.exp = exp;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public ArrayList getItemList() {
      return this.itemList;
   }

   public void addItem(Item item) {
      this.itemList.add(item);
   }

   public void addItemData(ItemDataUnit unit) {
      this.itemDataList.add(unit);
   }

   public ArrayList getItemDataList() {
      return this.itemDataList;
   }
}
