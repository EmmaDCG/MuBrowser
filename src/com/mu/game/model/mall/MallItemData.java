package com.mu.game.model.mall;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;

public class MallItemData extends ItemDataUnit {
   private int id;
   private Item item;
   private int priceType;
   private int price1;
   private int price2;
   private int vipLevel;

   public void buildItem() {
      Item item = ItemTools.createItem(2, this);
      if (item != null) {
         item.setId((long)this.id);
         ItemTools.setSystemExpire(item, this.getExpireTime());
         item.setMoneyType(this.priceType);
         item.setShopMoney(this.price2);
         item.setBind(this.isBind());
         this.setItem(item);
      }
   }

   public MallItemData(int modelID, int count) {
      super(modelID, count);
      this.setHide(true);
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public int getPriceType() {
      return this.priceType;
   }

   public void setPriceType(int priceType) {
      this.priceType = priceType;
   }

   public int getPrice1() {
      return this.price1;
   }

   public void setPrice1(int price1) {
      this.price1 = price1;
   }

   public int getPrice2() {
      return this.price2;
   }

   public void setPrice2(int price2) {
      this.price2 = price2;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
