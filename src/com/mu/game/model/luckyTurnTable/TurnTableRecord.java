package com.mu.game.model.luckyTurnTable;

public class TurnTableRecord {
   private long time = System.currentTimeMillis();
   private long roleID;
   private String name;
   private int multile;
   private int successIngot;

   public TurnTableRecord(long roleID, String name, int multile, int successIngot) {
      this.roleID = roleID;
      this.name = name;
      this.multile = multile;
      this.successIngot = successIngot;
   }

   public long getTime() {
      return this.time;
   }

   public void setTime(long time) {
      this.time = time;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getMultile() {
      return this.multile;
   }

   public void setMultile(int multile) {
      this.multile = multile;
   }

   public int getSuccessIngot() {
      return this.successIngot;
   }

   public void setSuccessIngot(int successIngot) {
      this.successIngot = successIngot;
   }
}
