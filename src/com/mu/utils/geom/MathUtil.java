package com.mu.utils.geom;

import java.awt.Point;

public class MathUtil {
   public static int getDistance(Point p1, Point p2) {
      double l1 = (double)(p1.x - p2.x);
      double l2 = (double)(p1.y - p2.y);
      return (int)Math.sqrt(l1 * l1 + l2 * l2);
   }

   public static double getDistance(double x1, double y1, double x2, double y2) {
      double l1 = x1 - x2;
      double l2 = y1 - y2;
      return Math.sqrt(l1 * l1 + l2 * l2);
   }
}
