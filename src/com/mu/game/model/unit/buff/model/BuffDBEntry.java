package com.mu.game.model.unit.buff.model;

public class BuffDBEntry {
   private int buffModelID;
   private int level;
   private long duration;
   private String propStr;

   public int getBuffModelID() {
      return this.buffModelID;
   }

   public void setBuffModelID(int buffModelID) {
      this.buffModelID = buffModelID;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public long getDuration() {
      return this.duration;
   }

   public void setDuration(long duration) {
      this.duration = duration;
   }

   public String getPropStr() {
      return this.propStr;
   }

   public void setPropStr(String propStr) {
      this.propStr = propStr;
   }
}
