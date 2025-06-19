package com.mu.game.model.unit.move;

import com.mu.utils.Tools;
import com.mu.utils.geom.MathUtil;

public class MoveLine {
   private int x1;
   private int y1;
   private int x2;
   private int y2;
   private int xStep = 0;
   private int yStep = 0;
   private int face = 0;
   private double length;

   public MoveLine(int x1, int y1, int x2, int y2) {
      this.x1 = x1;
      this.y1 = y1;
      this.x2 = x2;
      this.y2 = y2;
      this.length = MathUtil.getDistance((double)x1, (double)y1, (double)x2, (double)y2);
      if (this.length == 0.0D) {
         this.length = 1.0D;
      }

      this.xStep = x2 - x1;
      this.yStep = y2 - y1;
      this.face = Tools.getFace(x1, y1, x2, y2);
   }

   public int getxStep() {
      return this.xStep;
   }

   public int getyStep() {
      return this.yStep;
   }

   public int getX1() {
      return this.x1;
   }

   public int getY1() {
      return this.y1;
   }

   public int getX2() {
      return this.x2;
   }

   public int getY2() {
      return this.y2;
   }

   public double getLength() {
      return this.length;
   }

   public int getFace() {
      return this.face;
   }
}
