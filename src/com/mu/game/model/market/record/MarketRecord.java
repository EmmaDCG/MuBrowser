package com.mu.game.model.market.record;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.MarketItem;
import com.mu.game.model.unit.player.Player;

public class MarketRecord {
   private long goodID;
   private Item item;
   private long salerID;
   private String salerName;
   private long buyerID;
   private String buyerName;
   private long transactionTime;
   private int count;
   private int gainMoney;
   private int tax;

   public static MarketRecord createRecord(MarketItem mItem, Player buyer, int tax) {
      MarketRecord record = new MarketRecord();
      Item item = mItem.getItem();
      Item tmpItem = item.cloneItem(2);
      tmpItem.setId(item.getID());
      record.setItem(tmpItem);
      record.setGoodID(item.getID());
      record.setSalerID(mItem.getRoleID());
      record.setSalerName(mItem.getOwnerName());
      record.setBuyerID(buyer.getID());
      record.setBuyerName(buyer.getName());
      record.setTransactionTime(System.currentTimeMillis());
      record.setCount(mItem.getItem().getCount());
      int price = mItem.getPrice();
      record.setGainMoney(price - tax);
      record.setTax(tax);
      return record;
   }

   public void destroy() {
      this.item.destroy();
      this.item = null;
   }

   public long getGoodID() {
      return this.goodID;
   }

   public void setGoodID(long goodID) {
      this.goodID = goodID;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public long getSalerID() {
      return this.salerID;
   }

   public void setSalerID(long salerID) {
      this.salerID = salerID;
   }

   public String getSalerName() {
      return this.salerName;
   }

   public void setSalerName(String salerName) {
      this.salerName = salerName;
   }

   public long getBuyerID() {
      return this.buyerID;
   }

   public void setBuyerID(long buyerID) {
      this.buyerID = buyerID;
   }

   public String getBuyerName() {
      return this.buyerName;
   }

   public void setBuyerName(String buyerName) {
      this.buyerName = buyerName;
   }

   public long getTransactionTime() {
      return this.transactionTime;
   }

   public void setTransactionTime(long transactionTime) {
      this.transactionTime = transactionTime;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getGainMoney() {
      return this.gainMoney;
   }

   public void setGainMoney(int gainMoney) {
      this.gainMoney = gainMoney;
   }

   public int getTax() {
      return this.tax;
   }

   public void setTax(int tax) {
      this.tax = tax;
   }
}
