package com.mu.game.dungeon.imp.molian;

import com.mu.game.model.map.BigMonsterGroup;

public class MoLianMonsterGroup extends BigMonsterGroup {
   private int levelId = 1;

   public int getLevelId() {
      return this.levelId;
   }

   public void setLevelId(int levelId) {
      this.levelId = levelId;
   }
}
