package com.mu.game.dungeon.imp.plot;

import java.util.ArrayList;

public class PlotLevel {
   private int plotId;
   private int taskId;
   private int mapId;
   private int x;
   private int y;
   private ArrayList monsterList = new ArrayList();

   public PlotLevel(int plotId) {
      this.plotId = plotId;
   }

   public int getTaskId() {
      return this.taskId;
   }

   public void setTaskId(int taskId) {
      this.taskId = taskId;
   }

   public int getMapId() {
      return this.mapId;
   }

   public void setMapId(int mapId) {
      this.mapId = mapId;
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

   public int getPlotId() {
      return this.plotId;
   }

   public ArrayList getMonsterList() {
      return this.monsterList;
   }

   public void addMonsterGroup(PlotMonsterGroup group) {
      this.monsterList.add(group);
   }
}
