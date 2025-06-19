package com.mu.game.model.unit.player.hang;

public class HangDBEntry {
   private long roleID;
   private String cycles;
   private String pickupOthers;
   private String qualities;
   private String hangSkills;
   private String hangSales;

   public HangDBEntry() {
   }

   public HangDBEntry(long roleID, String cycles, String pickupOthers, String qualities, String hangSkills, String hangSales) {
      this.roleID = roleID;
      this.cycles = cycles;
      this.pickupOthers = pickupOthers;
      this.qualities = qualities;
      this.hangSkills = hangSkills;
      this.hangSales = hangSales;
   }

   public long getRoleID() {
      return this.roleID;
   }

   public void setRoleID(long roleID) {
      this.roleID = roleID;
   }

   public String getCycles() {
      return this.cycles;
   }

   public void setCycles(String cycles) {
      this.cycles = cycles;
   }

   public String getPickupOthers() {
      return this.pickupOthers;
   }

   public void setPickupOthers(String pickupOthers) {
      this.pickupOthers = pickupOthers;
   }

   public String getQualities() {
      return this.qualities;
   }

   public void setQualities(String qualities) {
      this.qualities = qualities;
   }

   public String getHangSkills() {
      return this.hangSkills;
   }

   public void setHangSkills(String hangSkills) {
      this.hangSkills = hangSkills;
   }

   public String getHangSales() {
      return this.hangSales;
   }

   public void setHangSales(String hangSales) {
      this.hangSales = hangSales;
   }
}
