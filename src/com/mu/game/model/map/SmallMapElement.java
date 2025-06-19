package com.mu.game.model.map;

public class SmallMapElement {
   public static final int Type_Npc = 0;
   public static final int Type_Monster = 1;
   public static final int Type_Elite = 2;
   public static final int Type_Boss = 3;
   public static final int Type_Gold = 4;
   private int type;
   private String name;
   private int x;
   private int y;
   private long id;

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type >= 0 && type <= 4 ? type : 0;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public int getX() {
      return this.x;
   }

   public void setX(int x) {
      this.x = x;
   }

   public int getY() {
      return this.y;
   }

   public void setY(int y) {
      this.y = y;
   }

   public long getId() {
      return this.id;
   }

   public void setId(long id) {
      this.id = id;
   }
}
