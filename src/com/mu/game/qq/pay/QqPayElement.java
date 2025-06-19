package com.mu.game.qq.pay;

public class QqPayElement {
   private int id;
   private int ingot;
   private int icon;
   private int originalPrice;
   private int blueVipProce;
   private String payItem = null;
   private String des;

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int getIcon() {
      return this.icon;
   }

   public void setIcon(int icon) {
      this.icon = icon;
   }

   public int getOriginalPrice() {
      return this.originalPrice;
   }

   public void setOriginalPrice(int originalPrice) {
      this.originalPrice = originalPrice;
   }

   public int getBlueVipProce() {
      return this.blueVipProce;
   }

   public void setBlueVipProce(int blueVipProce) {
      this.blueVipProce = blueVipProce;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public String getPayItem() {
      if (this.payItem == null) {
         this.payItem = this.ingot + "*" + this.ingot + "*1";
      }

      return this.payItem;
   }
}
