package com.mu.game.model.map;

public class MapGroup {
   private int id;
   private String name;
   private String des;
   private int backGroup;

   public MapGroup(int id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public int getId() {
      return this.id;
   }

   public int getBackGroup() {
      return this.backGroup;
   }

   public void setBackGroup(int backGroup) {
      this.backGroup = backGroup;
   }
}
