package com.mu.game.model.gang;

public class GangLevelData {
   private int level = 1;
   private String name;
   private int maxMember = 10;
   private int money = 0;
   private int bindIngot = 0;
   private int levelUpNeedMoney = 0;
   private int levelUpNeedIngot = 0;
   private int flag = -1;

   public GangLevelData(int level) {
      this.level = level;
   }

   public int getLevel() {
      return this.level;
   }

   public int getMaxMember() {
      return this.maxMember;
   }

   public void setMaxMember(int maxMember) {
      this.maxMember = maxMember;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public int getBindIngot() {
      return this.bindIngot;
   }

   public void setBindIngot(int bindIngot) {
      this.bindIngot = bindIngot;
   }

   public int getLevelUpNeedMoney() {
      return this.levelUpNeedMoney;
   }

   public void setLevelUpNeedMoney(int levelUpNeedMoney) {
      this.levelUpNeedMoney = levelUpNeedMoney;
   }

   public int getLevelUpNeedIngot() {
      return this.levelUpNeedIngot;
   }

   public void setLevelUpNeedIngot(int levelUpNeedIngot) {
      this.levelUpNeedIngot = levelUpNeedIngot;
   }

   public int getFlag() {
      return this.flag;
   }

   public void setFlag(int flag) {
      this.flag = flag;
   }
}
