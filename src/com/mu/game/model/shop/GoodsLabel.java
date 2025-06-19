package com.mu.game.model.shop;

public class GoodsLabel {
   private int labelID;
   private String labelName;
   private int labelSort;

   public GoodsLabel(int labelID, String labelName, int labelSort) {
      this.labelID = labelID;
      this.labelName = labelName;
      this.labelSort = labelSort;
   }

   public int getLabelID() {
      return this.labelID;
   }

   public void setLabelID(int labelID) {
      this.labelID = labelID;
   }

   public String getLabelName() {
      return this.labelName;
   }

   public void setLabelName(String labelName) {
      this.labelName = labelName;
   }

   public int getLabelSort() {
      return this.labelSort;
   }

   public void setLabelSort(int labelSort) {
      this.labelSort = labelSort;
   }
}
