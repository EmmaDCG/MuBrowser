package com.mu.game.dungeon.imp.luolan;

import com.mu.game.model.unit.material.MaterialGroup;
import java.awt.Point;

public class LuolanMaterialGroup extends MaterialGroup {
   private Point findwayPoint = null;

   public Point getFindwayPoint() {
      return this.findwayPoint;
   }

   public void setFindwayPoint(Point findwayPoint) {
      this.findwayPoint = findwayPoint;
   }
}
