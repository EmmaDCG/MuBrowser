package com.mu.game.model.unit.player.hang;

import com.mu.game.model.item.Item;
import java.util.HashMap;

public class HangSale {
   private int starLevelIndex;
   private int zhuijiaIndex;
   private int maxStarLevel;
   private int maxZhuijiaLevel;
   private boolean saleJewelry;
   private boolean saleLucky;
   private boolean lowLevelTickets;

   public HangSale() {
      this.setDefault();
   }

   public void loadSale(String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length >= 5) {
         this.setStarLevelIndex(Integer.parseInt(splits[0]));
         this.setZhuijiaIndex(Integer.parseInt(splits[1]));
         this.saleJewelry = Integer.parseInt(splits[2]) == 1;
         this.saleLucky = Integer.parseInt(splits[3]) == 1;
         this.lowLevelTickets = Integer.parseInt(splits[4]) == 1;
      }
   }

   public void setDefault() {
      this.setStarLevelIndex(SaleCondition.getStarLevelDefaultIndex());
      this.setZhuijiaIndex(SaleCondition.getZhuijiaDefaultIndex());
      this.setSaleJewelry(false);
      this.setSaleLucky(false);
      this.setLowLevelTickets(true);
   }

   public boolean suit(Item item, HashMap lowTicketMap) {
      if (this.isLowLevelTickets() && lowTicketMap.containsKey(item.getModelID())) {
         return true;
      } else if (HangConfig.isNoSaleModel(item.getModelID())) {
         return false;
      } else if (item.isEquipment()) {
         if (HangConfig.isNoSaleEquipType(item.getItemType())) {
            return false;
         } else if (item.getStarLevel() >= this.getMaxStarLevel()) {
            return false;
         } else if (item.getZhuijiaLevel() >= this.getMaxZhuijiaLevel()) {
            return false;
         } else if (item.getBonusStatSize(3) > 0) {
            return false;
         } else if (!this.isSaleLucky() && item.getBonusStatSize(2) > 0) {
            return false;
         } else {
            if (!this.isSaleJewelry()) {
               switch(item.getItemType()) {
               case 11:
               case 14:
               case 25:
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public String getHangSaleStr() {
      StringBuffer sb = new StringBuffer();
      sb.append(this.starLevelIndex);
      sb.append(",");
      sb.append(this.zhuijiaIndex);
      sb.append(",");
      sb.append(this.saleJewelry ? 1 : 0);
      sb.append(",");
      sb.append(this.saleLucky ? 1 : 0);
      sb.append(",");
      sb.append(this.lowLevelTickets ? 1 : 0);
      return sb.toString();
   }

   public void setStarLevelIndex(int starLevelIndex) {
      this.starLevelIndex = SaleCondition.correctStarIndex(starLevelIndex);
      this.setMaxStarLevel(SaleCondition.getStarLevel(this.starLevelIndex));
   }

   public void setZhuijiaIndex(int zhuijiaIndex) {
      this.zhuijiaIndex = SaleCondition.correctZhuijiaIndex(zhuijiaIndex);
      this.setMaxZhuijiaLevel(SaleCondition.getZhuijiaLevel(this.zhuijiaIndex));
   }

   private void setMaxStarLevel(int maxStarLevel) {
      this.maxStarLevel = Math.max(1, maxStarLevel);
   }

   private void setMaxZhuijiaLevel(int maxZhuijiaLevel) {
      this.maxZhuijiaLevel = Math.max(1, maxZhuijiaLevel);
   }

   public int getMaxStarLevel() {
      return this.maxStarLevel;
   }

   public int getMaxZhuijiaLevel() {
      return this.maxZhuijiaLevel;
   }

   public boolean isSaleJewelry() {
      return this.saleJewelry;
   }

   public void setSaleJewelry(boolean saleJewelry) {
      this.saleJewelry = saleJewelry;
   }

   public boolean isSaleLucky() {
      return this.saleLucky;
   }

   public void setSaleLucky(boolean saleLucky) {
      this.saleLucky = saleLucky;
   }

   public boolean isLowLevelTickets() {
      return this.lowLevelTickets;
   }

   public void setLowLevelTickets(boolean lowLevelTickets) {
      this.lowLevelTickets = lowLevelTickets;
   }

   public int getStarLevelIndex() {
      return this.starLevelIndex;
   }

   public int getZhuijiaIndex() {
      return this.zhuijiaIndex;
   }
}
