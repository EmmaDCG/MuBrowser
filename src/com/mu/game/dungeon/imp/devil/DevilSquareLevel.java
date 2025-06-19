package com.mu.game.dungeon.imp.devil;

import com.mu.game.dungeon.DungeonLevel;
import com.mu.game.dungeon.DungeonReward;
import java.util.ArrayList;
import java.util.HashMap;

public class DevilSquareLevel extends DungeonLevel {
   private ArrayList group1 = new ArrayList();
   private ArrayList group2 = new ArrayList();
   private ArrayList group3 = new ArrayList();
   private ArrayList group4 = new ArrayList();
   private HashMap rewardMap = new HashMap();

   public ArrayList getMonsterGroup(int batch) {
      switch(batch) {
      case 1:
         return this.group1;
      case 2:
         return this.group2;
      case 3:
         return this.group3;
      case 4:
         return this.group4;
      default:
         return null;
      }
   }

   public void addMonsterGroup(DevilMonsterGroup group, int batch) {
      switch(batch) {
      case 1:
         this.group1.add(group);
         break;
      case 2:
         this.group2.add(group);
         break;
      case 3:
         this.group3.add(group);
         break;
      case 4:
         this.group4.add(group);
      }

   }

   public void addReward(int rank, DungeonReward reward) {
      this.rewardMap.put(rank, reward);
   }

   public DungeonReward getReward(int rank) {
      DungeonReward reward = (DungeonReward)this.rewardMap.get(rank);
      return reward == null ? (DungeonReward)this.rewardMap.get(Integer.valueOf(0)) : reward;
   }
}
