package com.mu.game.dungeon.imp.bigdevil;

import com.mu.game.model.map.BigMonsterGroup;

public class BigDevilMonsterGroup extends BigMonsterGroup {
   private int batch = 1;
   private int levelId = 1;

   public int getBatch() {
      return this.batch;
   }

   public void setBatch(int batch) {
      this.batch = batch;
   }

   public int getLevelId() {
      return this.levelId;
   }

   public void setLevelId(int levelId) {
      this.levelId = levelId;
   }
}
