package com.mu.game.dungeon.imp.luolan;

import com.mu.game.model.map.BigMonsterGroup;
import java.awt.Point;
import java.util.ArrayList;

public class GateData extends BigMonsterGroup {
   private ArrayList blockList = null;
   private int id;
   private Point findwayPoint = null;

   public ArrayList getBlockList() {
      return this.blockList;
   }

   public void setBlockList(ArrayList blockList) {
      this.blockList = blockList;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public Point getFindwayPoint() {
      return this.findwayPoint;
   }

   public void setFindwayPoint(Point findwayPoint) {
      this.findwayPoint = findwayPoint;
   }
}
