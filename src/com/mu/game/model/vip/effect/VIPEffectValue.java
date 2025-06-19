package com.mu.game.model.vip.effect;

import com.mu.game.model.vip.VIPLevel;

public class VIPEffectValue {
   private VIPEffect ve;
   private VIPLevel vl;
   private int showType;
   private boolean showBool;
   private String showStr;
   private boolean available;
   private boolean booleanValue;
   private int integerValue;

   public VIPEffectValue(VIPEffect ve, VIPLevel vl) {
      this.ve = ve;
      this.vl = vl;
   }

   public void parseConfig(String str1, String str2, String str3, String str4) {
      this.showType = Integer.parseInt(str1);
      switch(this.showType) {
      case 1:
         this.showBool = str2.equals("1");
         break;
      case 2:
         this.showStr = str2;
      }

      this.available = str3.equals("1");
      this.booleanValue = str4.equals("1");
      this.integerValue = Integer.parseInt(str4);
   }

   public VIPEffect getEffect() {
      return this.ve;
   }

   public VIPLevel getLevel() {
      return this.vl;
   }

   public VIPEffect getVe() {
      return this.ve;
   }

   public VIPLevel getVl() {
      return this.vl;
   }

   public int getShowType() {
      return this.showType;
   }

   public boolean isShowBool() {
      return this.showBool;
   }

   public String getShowStr() {
      return this.showStr;
   }

   public boolean isAvailable() {
      return this.available;
   }

   public boolean isBooleanValue() {
      return this.booleanValue;
   }

   public int getIntegerValue() {
      return this.integerValue;
   }
}
