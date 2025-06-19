package com.mu.game.model.unit.ai;

import com.mu.game.model.unit.Creature;
import com.mu.utils.geom.MathUtil;

public class AggroInfo {
   public static final int VALID_DISTANCE = 60000;
   private Creature owner;
   private Creature target;
   private int hate;
   private int hurt;
   private boolean invalid;
   private boolean destroy;

   public AggroInfo(Creature owner, Creature target, int hate, int hurt) {
      this.owner = owner;
      this.target = target;
      this.hate = hate;
      this.hurt = hurt;
   }

   public void addAggro(int hate, int hurt) {
      if (this.hate + hate >= Integer.MAX_VALUE) {
         this.hate = 2147483646;
      } else {
         this.hate += hate;
      }

      if (this.hurt + hurt >= Integer.MAX_VALUE) {
         this.hurt = 2147483646;
      } else {
         this.hurt += hurt;
      }

   }

   public void destroy() {
      if (!this.destroy) {
         this.destroy = true;
         this.invalid = true;
         this.owner = null;
         this.target = null;
         this.hate = 0;
         this.hurt = 0;
      }
   }

   public boolean isInvalid() {
      return this.invalid ? this.invalid : (this.invalid = this.target.isDestroy() || this.target.isDie() || this.owner.getMap() != this.target.getMap() || MathUtil.getDistance(this.owner.getPosition(), this.target.getPosition()) >= 60000);
   }

   public int getHate() {
      return this.hate;
   }

   public void setHate(int hate) {
      this.hate = hate;
   }

   public int getHurt() {
      return this.hurt;
   }

   public void setHurt(int hurt) {
      this.hurt = hurt;
   }

   public Creature getOwner() {
      return this.owner;
   }

   public Creature getTarget() {
      return this.target;
   }

   public boolean isDestroy() {
      return this.destroy;
   }
}
