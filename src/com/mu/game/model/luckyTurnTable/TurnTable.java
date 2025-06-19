package com.mu.game.model.luckyTurnTable;

public class TurnTable {
   private int tableID;
   private int source;
   private String name;
   private float multiple;

   public TurnTable(int tableID, int source, String name, float multiple) {
      this.tableID = tableID;
      this.source = source;
      this.name = name;
      this.multiple = multiple;
   }

   public int getTableID() {
      return this.tableID;
   }

   public void setTableID(int tableID) {
      this.tableID = tableID;
   }

   public int getSource() {
      return this.source;
   }

   public void setSource(int source) {
      this.source = source;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public float getMultiple() {
      return this.multiple;
   }

   public void setMultiple(float multiple) {
      this.multiple = multiple;
   }
}
