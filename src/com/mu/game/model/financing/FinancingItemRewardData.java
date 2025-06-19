package com.mu.game.model.financing;

import java.util.ArrayList;
import java.util.List;

public class FinancingItemRewardData {
   private FinancingItemData itemData;
   private int id;
   private String name;
   private int image;
   private List rewardList = new ArrayList();
   private int conditionValue;

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

   public int getImage() {
      return this.image;
   }

   public void setImage(int image) {
      this.image = image;
   }

   public int getConditionValue() {
      return this.conditionValue;
   }

   public void setConditionValue(int conditionValue) {
      this.conditionValue = conditionValue;
   }

   public List getRewardList() {
      return this.rewardList;
   }

   public FinancingItemData getItemData() {
      return this.itemData;
   }

   public void setItemData(FinancingItemData itemData) {
      this.itemData = itemData;
   }
}
