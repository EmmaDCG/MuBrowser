package com.mu.game.model.market;

import com.mu.game.model.item.Item;

public class MarketItem {
   private long itemID;
   private Item item;
   private long shelveTime;
   private long roleID;
   private String ownerName;
   private String userName;
   private int serverID;
   private int averagePrice;
   private int taxRate;

   public MarketItem(Item item) {
      this.itemID = item.getID();
      this.item = item;
      this.averagePrice = Math.max(1, item.getMoney() / item.getCount());
   }

   public boolean isExpire(long now) {
      return this.shelveTime + 259200000L < now;
   }

   public int getPrice() {
      return this.item.getMoney();
   }

   public long getItemID() {
      return this.itemID;
   }

   public void setItemID(long itemID) {
      this.itemID = itemID;
   }

   public Item getItem() {
      return this.item;
   }

   public void setItem(Item item) {
      this.item = item;
   }

   public long getShelveTime() {
      return this.shelveTime;
   }

   public void setShelveTime(long shelveTime) {
      this.shelveTime = shelveTime;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getOwnerName() {
      return this.ownerName;
   }

   public void setOwnerName(String ownerName) {
      this.ownerName = ownerName;
   }

   public String getUserName() {
      return this.userName;
   }

   public void setUserName(String userName) {
      this.userName = userName;
   }

   public int getAveragePrice() {
      return this.averagePrice;
   }

   public int getServerID() {
      return this.serverID;
   }

   public void setServerID(int serverID) {
      this.serverID = serverID;
   }

   public int getTaxRate() {
      return this.taxRate;
   }

   public void setTaxRate(int taxRate) {
      if (taxRate <= 0) {
         taxRate = 20000;
      }

      this.taxRate = taxRate;
   }
}
