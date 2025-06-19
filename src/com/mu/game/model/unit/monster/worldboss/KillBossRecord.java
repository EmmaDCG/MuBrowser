package com.mu.game.model.unit.monster.worldboss;

import com.mu.utils.Time;

public class KillBossRecord {
   private String killerName = null;
   private long killTime = 0L;
   private int bossId = 0;
   private String timeStr = "";

   public String getKillerName() {
      return this.killerName;
   }

   public void setKillerName(String killerName) {
      this.killerName = killerName;
   }

   public long getKillTime() {
      return this.killTime;
   }

   public void setKillTime(long killTime) {
      this.killTime = killTime;
      this.timeStr = Time.getTimeStr(this.killTime, "HH:mm:ss");
   }

   public int getBossId() {
      return this.bossId;
   }

   public void setBossId(int bossId) {
      this.bossId = bossId;
   }

   public String getTimeStr() {
      return this.timeStr;
   }
}
