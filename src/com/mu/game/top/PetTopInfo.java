package com.mu.game.top;

import com.mu.config.MessageText;
import com.mu.game.model.pet.PetConfigManager;
import com.mu.game.model.pet.PetRank;

public class PetTopInfo extends TopInfo implements Comparable {
   private int petRank;
   private int petLevel;
   private long levelUpTime;
   private long rankTime;

   public int getPetRank() {
      return this.petRank;
   }

   public void setPetRank(int petRank) {
      this.petRank = petRank;
   }

   public int getPetLevel() {
      return this.petLevel;
   }

   public void setPetLevel(int petLevel) {
      this.petLevel = petLevel;
   }

   public long getLevelUpTime() {
      return this.levelUpTime;
   }

   public void setLevelUpTime(long levelUpTime) {
      this.levelUpTime = levelUpTime;
   }

   public long getRankTime() {
      return this.rankTime;
   }

   public void setRankTime(long rankTime) {
      this.rankTime = rankTime;
   }

   public String getvariable() {
      PetRank pr = PetConfigManager.getRank(this.petRank);
      return pr == null ? MessageText.getText(1039) : pr.getName();
   }

   public int compareTo(PetTopInfo o) {
      if (this.getPetRank() != o.getPetRank()) {
         return o.getPetRank() - this.getPetRank();
      } else if (this.getPetLevel() != o.getPetLevel()) {
         return o.getPetLevel() - this.getPetLevel();
      } else {
         return o.getRankTime() >= this.getRankTime() ? -1 : 1;
      }
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
