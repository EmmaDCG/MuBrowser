package com.mu.game.model.equip.external;

public class ArmorEffectAtom {
   private int starLevel;
   private int chestID;
   private int footID;

   public ArmorEffectAtom(int starLevel, int chestID, int footID) {
      this.starLevel = starLevel;
      this.chestID = chestID;
      this.footID = footID;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getChestID() {
      return this.chestID;
   }

   public void setChestID(int chestID) {
      this.chestID = chestID;
   }

   public int getFootID() {
      return this.footID;
   }

   public void setFootID(int footID) {
      this.footID = footID;
   }
}
