package com.mu.game.model.unit.player.offline;

public class DunRecoveryInfo {
   private int dunId;
   private int maxTimes;
   private int bindIngot = 5;
   private int ingot = 2;
   private String name;
   private int menuId;

   public int getDunId() {
      return this.dunId;
   }

   public void setDunId(int dunId) {
      this.dunId = dunId;
   }

   public int getMaxTimes() {
      return this.maxTimes;
   }

   public void setMaxTimes(int maxTimes) {
      this.maxTimes = maxTimes;
   }

   public int getBindIngot() {
      return this.bindIngot;
   }

   public void setBindIngot(int bindIngot) {
      this.bindIngot = bindIngot;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public String getName() {
      return this.name;
   }

   public int getMenuId() {
      return this.menuId;
   }

   public void setMenuId(int menuId) {
      this.menuId = menuId;
   }

   public void setName(String name) {
      this.name = name;
   }
}
