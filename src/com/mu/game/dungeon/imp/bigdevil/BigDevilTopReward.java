package com.mu.game.dungeon.imp.bigdevil;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import java.util.ArrayList;

public class BigDevilTopReward implements Comparable {
   private int sort;
   private int minTop;
   private int maxTop;
   private String name;
   private ArrayList rewardItemList = new ArrayList();
   private ArrayList rewardDataList = new ArrayList();

   public int getMinTop() {
      return this.minTop;
   }

   public void setMinTop(int min) {
      this.minTop = min;
   }

   public int getMaxTop() {
      return this.maxTop;
   }

   public void setMaxTop(int max) {
      this.maxTop = max;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public ArrayList getRewardItemList() {
      return this.rewardItemList;
   }

   public void addRewardItem(Item item) {
      this.rewardItemList.add(item);
   }

   public ArrayList getRewardDataList() {
      return this.rewardDataList;
   }

   public void addRewardData(ItemDataUnit data) {
      this.rewardDataList.add(data);
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public int compareTo(BigDevilTopReward o) {
      return this.sort - o.getSort();
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
