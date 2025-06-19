package com.mu.game.dungeon.imp.redfort;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import java.util.ArrayList;

public class RedFortTopReward {
   private int minTop;
   private int maxTop;
   private ArrayList dataList = new ArrayList();
   private ArrayList itemList = new ArrayList();

   public int getMinTop() {
      return this.minTop;
   }

   public void setMinTop(int minTop) {
      this.minTop = minTop;
   }

   public int getMaxTop() {
      return this.maxTop;
   }

   public void setMaxTop(int maxTop) {
      this.maxTop = maxTop;
   }

   public ArrayList getDataList() {
      return this.dataList;
   }

   public void addData(ItemDataUnit data) {
      this.dataList.add(data);
   }

   public ArrayList getItemList() {
      return this.itemList;
   }

   public void addItem(Item item) {
      this.itemList.add(item);
   }
}
