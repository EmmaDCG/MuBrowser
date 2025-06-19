package com.mu.game.model.ui;

public abstract class UI {
   private int id;
   private int x;
   private int y;

   public UI(int id) {
      this.id = id;
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

   public int getId() {
      return this.id;
   }

   public abstract String getTag();
}
