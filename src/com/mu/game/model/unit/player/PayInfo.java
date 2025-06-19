package com.mu.game.model.unit.player;

import com.mu.utils.Time;

public class PayInfo {
   private long payTime;
   private long payDay;
   private int ingot;

   public PayInfo(long payTime, int ingot) {
      this.payTime = payTime;
      this.ingot = ingot;
      this.payDay = Time.getDayLong(payTime);
   }

   public long getPayTime() {
      return this.payTime;
   }

   public long getPayDay() {
      return this.payDay;
   }

   public int getIngot() {
      return this.ingot;
   }
}
