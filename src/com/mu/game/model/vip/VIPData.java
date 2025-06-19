package com.mu.game.model.vip;

public class VIPData {
   public static final long day_unit = 86400000L;
   private int id;
   private String name;
   private int image;
   private String slStr;
   private String cfStr;
   private String qlStr;
   private String mwStr;
   private String xxStr;
   private int exp;
   private int price;
   private int timeDay;
   private String timeStr;
   private int baseLv;
   private int itemId;

   public VIPData(int id, String name) {
      this.id = id;
      this.name = name;
   }

   public int getId() {
      return this.id;
   }

   public String getName() {
      return this.name;
   }

   public void setPrice(int price) {
      this.price = price;
   }

   public int getPrice() {
      return this.price;
   }

   public void setTimeStr(String timeStr) {
      this.timeStr = timeStr;
   }

   public int getTimeDay() {
      return this.timeDay;
   }

   public String getTimeStr() {
      return this.timeStr;
   }

   public void setImage(int image) {
      this.image = image;
   }

   public int getImage() {
      return this.image;
   }

   public String getSlStr() {
      return this.slStr;
   }

   public void setSlStr(String slStr) {
      this.slStr = slStr;
   }

   public String getCfStr() {
      return this.cfStr;
   }

   public void setCfStr(String cfStr) {
      this.cfStr = cfStr;
   }

   public String getQlStr() {
      return this.qlStr;
   }

   public void setQlStr(String qlStr) {
      this.qlStr = qlStr;
   }

   public String getMwStr() {
      return this.mwStr;
   }

   public void setMwStr(String mwStr) {
      this.mwStr = mwStr;
   }

   public int getExp() {
      return this.exp;
   }

   public void setExp(int exp) {
      this.exp = exp;
   }

   public int getBaseLv() {
      return this.baseLv;
   }

   public void setBaseLv(int baseLv) {
      this.baseLv = baseLv;
   }

   public void setTimeDay(int timeDay) {
      this.timeDay = timeDay;
   }

   public String getXxStr() {
      return this.xxStr;
   }

   public void setXxStr(String xxStr) {
      this.xxStr = xxStr;
   }

   public int getItemId() {
      return this.itemId;
   }

   public void setItemId(int itemId) {
      this.itemId = itemId;
   }
}
