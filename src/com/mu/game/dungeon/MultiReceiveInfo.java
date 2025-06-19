package com.mu.game.dungeon;

public class MultiReceiveInfo {
   private int index;
   private int times;
   private int ingot;
   private int vipLevel;
   private String receiveName;
   private String receiveTitle;

   public int getIndex() {
      return this.index;
   }

   public void setIndex(int index) {
      this.index = index;
   }

   public int getTimes() {
      return this.times;
   }

   public void setTimes(int times) {
      this.times = times;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public String getReceiveName() {
      return this.receiveName;
   }

   public void setReceiveName(String receiveName) {
      this.receiveName = receiveName;
   }

   public String getReceiveTitle() {
      return this.receiveTitle;
   }

   public void setReceiveTitle(String receiveTitle) {
      this.receiveTitle = receiveTitle;
   }
}
