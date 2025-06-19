package com.mu.game.dungeon.imp.temple;

import com.mu.game.dungeon.imp.devil.DevilMonsterGroup;

public class TempleMonsterGroup extends DevilMonsterGroup {
   private int header = -1;
   private int bossId = 1;

   public int getHeader() {
      return this.header;
   }

   public void setHeader(int header) {
      this.header = header;
   }

   public int getBossId() {
      return this.bossId;
   }

   public void setBossId(int bossId) {
      this.bossId = bossId;
   }
}
