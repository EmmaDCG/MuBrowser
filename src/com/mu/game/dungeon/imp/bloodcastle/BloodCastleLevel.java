package com.mu.game.dungeon.imp.bloodcastle;

import com.mu.game.dungeon.DungeonLevel;
import com.mu.game.dungeon.DungeonReward;
import java.util.ArrayList;
import java.util.HashMap;

public class BloodCastleLevel extends DungeonLevel {
   private ArrayList groupBridge = new ArrayList();
   private BloodCastleMonsterGroup groupGate = null;
   private ArrayList groupWizard = new ArrayList();
   private BloodCastleMonsterGroup groupCoffin = null;
   private BloodCastleMonsterGroup groupAngel = null;
   private HashMap rewardMap = new HashMap();

   public void addBridgeMonster(BloodCastleMonsterGroup group) {
      this.groupBridge.add(group);
   }

   public void addWizardMonster(BloodCastleMonsterGroup group) {
      this.groupWizard.add(group);
   }

   public void addReward(int rank, DungeonReward reward) {
      this.rewardMap.put(rank, reward);
   }

   public BloodCastleMonsterGroup getGroupGate() {
      return this.groupGate;
   }

   public void setGroupGate(BloodCastleMonsterGroup groupGate) {
      this.groupGate = groupGate;
   }

   public BloodCastleMonsterGroup getGroupCoffin() {
      return this.groupCoffin;
   }

   public void setGroupCoffin(BloodCastleMonsterGroup groupCoffin) {
      this.groupCoffin = groupCoffin;
   }

   public BloodCastleMonsterGroup getGroupAngel() {
      return this.groupAngel;
   }

   public void setGroupAngel(BloodCastleMonsterGroup groupAngel) {
      this.groupAngel = groupAngel;
   }

   public ArrayList getGroupBridge() {
      return this.groupBridge;
   }

   public ArrayList getGroupWizard() {
      return this.groupWizard;
   }

   public HashMap getRewardMap() {
      return this.rewardMap;
   }

   public DungeonReward getReward(int rank) {
      DungeonReward reward = (DungeonReward)this.rewardMap.get(rank);
      return reward == null ? (DungeonReward)this.rewardMap.get(Integer.valueOf(0)) : reward;
   }
}
