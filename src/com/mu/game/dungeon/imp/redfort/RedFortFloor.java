package com.mu.game.dungeon.imp.redfort;

import java.awt.Point;

public class RedFortFloor {
   private int floor;
   private Point[] bornPoints;
   private int mapId;
   private int duration;

   public RedFortFloor(int floor) {
      this.floor = floor;
   }

   public Point[] getBornPoints() {
      return this.bornPoints;
   }

   public void setBornPoints(Point[] bornPoints) {
      this.bornPoints = bornPoints;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
   }

   public int getFloor() {
      return this.floor;
   }

   public int getDuration() {
      return this.duration;
   }

   public void setDuration(int duration) {
      this.duration = duration;
   }
}
