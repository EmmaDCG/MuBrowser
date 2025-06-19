package com.mu.game.model.shop;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;

public class Goods {
   private int modelID;
   private int price;
   private int moneyType;
   private int label;
   private int statRuleID;
   private long expireTime = -1L;
   private boolean bind = false;
   private Item goodItem = null;

   public boolean entity() {
      ItemDataUnit unit = this.getUnit();
      unit.setHide(true);
      this.goodItem = ItemTools.createItem(2, unit);
      if (this.goodItem == null) {
         return false;
      } else {
         ItemTools.setSystemExpire(this.goodItem, this.getExpireTime());
         this.goodItem.setMoneyType(this.moneyType);
         this.goodItem.setShopMoney(this.price);
         return true;
      }
   }

   public long getGoodsID() {
      return this.goodItem.getID();
   }

   public int getModelID() {
      return this.modelID;
   }

   public int getStatRuleID() {
      return this.statRuleID;
   }

   public void setStatRuleID(int statRuleID) {
      this.statRuleID = statRuleID;
   }

   public ItemDataUnit getUnit() {
      ItemDataUnit unit = new ItemDataUnit(this.modelID, 1);
      unit.setBind(this.isBind());
      unit.setStatRuleID(this.statRuleID);
      unit.setExpireTime(this.getExpireTime());
      return unit;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }

   public int getPrice() {
      return this.price;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public int getMoneyType() {
      return this.moneyType;
   }

   public int getLabel() {
      return this.label;
   }

   public void setLabel(int label) {
      this.label = label;
   }

   public void setMoneyType(int moneyType) {
      this.moneyType = moneyType;
   }

   public Item getGoodItem() {
      return this.goodItem;
   }

   public long getExpireTime() {
      return this.expireTime;
   }

   public void setExpireTime(long expireTime) {
      this.expireTime = expireTime;
   }

   public boolean isBind() {
      return this.bind;
   }

   public void setBind(boolean bind) {
      this.bind = bind;
   }
}
