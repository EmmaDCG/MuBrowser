package com.mu.game.model.task;

public class TaskPosition {
   private int id;
   private int mapId;
   private int x;
   private int y;

   public TaskPosition(int id, int mapId, int x, int y) {
      this.id = id;
      this.mapId = mapId;
      this.x = x;
      this.y = y;
   }

   public int getId() {
      return this.id;
   }

   public int getMapId() {
      return this.mapId;
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}
