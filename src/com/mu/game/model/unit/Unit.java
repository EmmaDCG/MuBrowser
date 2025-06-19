package com.mu.game.model.unit;

public abstract class Unit {
   public static final int Unit_Player = 1;
   public static final int Unit_Monster = 2;
   public static final int Unit_Npc = 3;
   public static final int Unit_Pet = 4;
   public static final int Unit_DropItem = 5;
   public static final int Unit_Transpoint = 6;
   public static final int Unit_Material = 7;
   public static final int Unit_Panda = 8;
   public static final int Unit_Robot = 9;
   public static final int Unit_Item = 99;
   private String name;
   private long id;
   private boolean isDestroy;

   public Unit(long id) {
      this.id = id;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setID(long id) {
      this.id = id;
   }

   public long getID() {
      return this.id;
   }

   public void setDestroy(boolean b) {
      this.isDestroy = b;
   }

   public boolean isDestroy() {
      return this.isDestroy;
   }

   public abstract void destroy();

   public abstract int getType();

   public int getUnitType() {
      return this.getType();
   }
}
