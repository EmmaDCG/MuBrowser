package com.mu.game.model.financing;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

public class FinancingItemData {
   private int id;
   private String name;
   private int image1;
   private int image2;
   private int price;
   private int conditionType;
   private HashMap rewardMap = new LinkedHashMap();

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getImage1() {
      return this.image1;
   }

   public void setImage1(int image1) {
      this.image1 = image1;
   }

   public int getImage2() {
      return this.image2;
   }

   public void setImage2(int image2) {
      this.image2 = image2;
   }

   public int getPrice() {
      return this.price;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public void addReward(FinancingItemRewardData data) {
      this.rewardMap.put(data.getId(), data);
   }

   public HashMap getRewardMap() {
      return this.rewardMap;
   }

   public Iterator getRewardIterator() {
      return this.rewardMap.values().iterator();
   }

   public int getRewardSize() {
      return this.rewardMap.size();
   }

   public int getConditionType() {
      return this.conditionType;
   }

   public void setConditionType(int conditionType) {
      this.conditionType = conditionType;
   }
}
