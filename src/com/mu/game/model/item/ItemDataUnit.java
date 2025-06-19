package com.mu.game.model.item;

public class ItemDataUnit {
   private int modelID;
   private int count;
   private boolean isBind;
   private long expireTime;
   private int source;
   private int statRuleID;
   private boolean hide;
   private long actualExpireTime;

   public ItemDataUnit(int modelID, int count, boolean isBind) {
      this(modelID, count);
      this.isBind = isBind;
   }

   public ItemDataUnit(int modelID, int count) {
      this.isBind = false;
      this.expireTime = -1L;
      this.hide = false;
      this.actualExpireTime = -1L;
      this.modelID = modelID;
      this.count = count;
   }

   public boolean createBonus() {
      return true;
   }

   public ItemDataUnit cloneUnit() {
      ItemDataUnit unit = new ItemDataUnit(this.modelID, this.count, this.isBind);
      unit.setExpireTime(this.expireTime);
      unit.setSource(this.source);
      unit.setStatRuleID(this.statRuleID);
      return unit;
   }

   public void setExpireTime(long expireTime) {
      this.expireTime = expireTime;
      if (expireTime == -1L) {
         this.setActualExpireTime(-1L);
      } else {
         this.setActualExpireTime(System.currentTimeMillis() + expireTime);
      }

   }

   private void setActualExpireTime(long actualExpireTime) {
      this.actualExpireTime = actualExpireTime;
   }

   public long getActualExpireTime() {
      return this.actualExpireTime;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public boolean isBind() {
      return this.isBind;
   }

   public void setBind(boolean isBind) {
      this.isBind = isBind;
   }

   public long getExpireTime() {
      return this.expireTime;
   }

   public int getStatRuleID() {
      return this.statRuleID;
   }

   public void setStatRuleID(int statRuleID) {
      this.statRuleID = statRuleID;
   }

   public boolean isHide() {
      return this.hide;
   }

   public void setHide(boolean hide) {
      this.hide = hide;
   }

   public int getSource() {
      return this.source;
   }

   public void setSource(int source) {
      this.source = source;
   }
}
