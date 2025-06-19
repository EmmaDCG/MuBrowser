package com.mu.game.model.shield;

import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import java.util.ArrayList;
import java.util.List;

public class ShieldLevel {
   private int level;
   private int shieldValue;
   private int shieldRecover;
   private int expend;
   private int mallItemId;
   private int zdl;
   private List propertyList = new ArrayList();

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getShieldValue() {
      return this.shieldValue;
   }

   public void setShieldValue(int maxValue) {
      this.shieldValue = maxValue;
      this.propertyList.add(new FinalModify(StatEnum.MAX_SD, this.shieldValue, StatModifyPriority.ADD));
   }

   public int getExpend() {
      return this.expend;
   }

   public void setExpend(int expend) {
      this.expend = expend;
   }

   public int getShieldRecover() {
      return this.shieldRecover;
   }

   public void setShieldRecover(int shieldRecover) {
      this.shieldRecover = shieldRecover;
      this.propertyList.add(new FinalModify(StatEnum.SD_RECOVER, shieldRecover, StatModifyPriority.ADD));
   }

   public List getPropertyList() {
      return this.propertyList;
   }

   public int getMallItemId() {
      return this.mallItemId;
   }

   public void setMallItemId(int mallItemId) {
      this.mallItemId = mallItemId;
   }

   public int getZDL() {
      return this.zdl;
   }

   public void setZDL(int zdl) {
      this.zdl = zdl;
      this.propertyList.add(new FinalModify(StatEnum.DOMINEERING, zdl, StatModifyPriority.ADD));
   }
}
