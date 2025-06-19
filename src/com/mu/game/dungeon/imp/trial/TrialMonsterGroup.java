package com.mu.game.dungeon.imp.trial;

import com.mu.game.model.map.BigMonsterGroup;

public class TrialMonsterGroup extends BigMonsterGroup {
   private int trialLevel = 1;
   private int zoom = 100;
   private boolean isBroadCast;

   public int getTrialLevel() {
      return this.trialLevel;
   }

   public void setTrialLevel(int trialLevel) {
      this.trialLevel = trialLevel;
   }

   public int getZoom() {
      return this.zoom;
   }

   public void setZoom(int zoom) {
      this.zoom = zoom;
   }

   public boolean isBroadCast() {
      return this.isBroadCast;
   }

   public void setBroadCast(boolean isBroadCast) {
      this.isBroadCast = isBroadCast;
   }
}
