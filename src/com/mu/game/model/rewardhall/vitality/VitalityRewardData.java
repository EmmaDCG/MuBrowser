package com.mu.game.model.rewardhall.vitality;

import java.util.ArrayList;
import java.util.List;

public class VitalityRewardData {
   private int id;
   private int vitality;
   List rewardList = new ArrayList();

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getVitality() {
      return this.vitality;
   }

   public void setVitality(int vitality) {
      this.vitality = vitality;
   }

   public List getRewardList() {
      return this.rewardList;
   }

   public void setRewardList(List rewardList) {
      this.rewardList = rewardList;
   }
}
