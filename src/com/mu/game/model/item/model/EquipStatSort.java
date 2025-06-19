package com.mu.game.model.item.model;

public class EquipStatSort {
   private int sortID;
   private String name;
   private int font;

   public EquipStatSort(int sortID, String name, int font) {
      this.sortID = sortID;
      this.name = name;
      this.font = font;
   }

   public int getSortID() {
      return this.sortID;
   }

   public void setSortID(int sortID) {
      this.sortID = sortID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getFont() {
      return this.font;
   }

   public void setFont(int font) {
      this.font = font;
   }
}
