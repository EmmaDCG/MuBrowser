package com.mu.utils.geom;

import com.mu.game.model.map.Map;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Arc2D.Double;

public class ShapeUtil {
   public static void main(String[] args) {
   }

   public static Shape createRoundShape(Point point, int radius) {
      Arc2D arc = new Double();
      arc.setArcByCenter(point.getX(), point.getY(), (double)radius, 0.0D, 360.0D, 2);
      return arc;
   }

   public static Shape createSectorShape(Point startPoint, Point targetPonit, int radius, int angExt) {
      Arc2D arc = new Double();
      int startAng = getStartAngle(startPoint, targetPonit, angExt);
      arc.setArcByCenter(startPoint.getX(), startPoint.getY(), (double)radius, (double)startAng, (double)angExt, 2);
      return arc;
   }

   public static int getStartAngle(Point p1, Point p2, int angExt) {
      int angle = getAngle(p1, p2);
      angle += angExt / 2;
      angle %= 360;
      angle = 360 - angle;
      return angle;
   }

   public static Shape createLineShape(Point startPoint, Point targetPoint, double wight, double hight) {
      int angle = getAngle(startPoint, targetPoint);
      double cornerX = startPoint.getX() + Math.sin((double)angle * 3.141592653589793D / 180.0D) * hight / 2.0D;
      double cornerY = startPoint.getY() - Math.cos((double)angle * 3.141592653589793D / 180.0D) * hight / 2.0D;
      Rectangle2D rectangle = new java.awt.geom.Rectangle2D.Double();
      rectangle.setRect(cornerX, cornerY, wight, hight);
      int startAngle = angle;
      if (angle > 180) {
         startAngle = angle - 360;
      }

      AffineTransform af = AffineTransform.getRotateInstance((double)startAngle * 3.141592653589793D / 180.0D, cornerX, cornerY);
      Shape shape = af.createTransformedShape(rectangle);
      return shape;
   }

   public static int getAngle(Point p1, Point p2) {
      if (p1.getX() == p2.getX()) {
         return p1.getY() < p2.getY() ? 90 : 270;
      } else {
         double radians = Math.atan((p2.getY() - p1.getY()) / (p2.getX() - p1.getX()));
         int angle = (int)Math.toDegrees(radians);
         if (radians < 0.0D) {
            if (p1.getX() > p2.getX()) {
               angle += 180;
            } else {
               angle += 360;
            }
         } else if (p1.getX() > p2.getX()) {
            angle += 180;
         }

         return angle;
      }
   }

   public static Point follow(Point p1, Point p2, int range, Map map) {
      int left = map.getLeft();
      int top = map.getTop();
      int mapHight = map.getBottom();
      int mapWidth = map.getRight();
      int x = 0;
      int y = 0;
      double distance = (double)MathUtil.getDistance(p1, p2);
      if (p1.x == p2.x) {
         if (p2.y >= p1.y) {
            y = p2.y - range > 0 ? p2.y - range : 0;
         } else {
            y = p2.y + range > mapHight ? mapHight : p2.y + range;
         }

         x = p1.x;
      } else if (p1.y == p2.y) {
         y = p1.y;
         if (p2.x >= p1.x) {
            x = p2.x - range > 0 ? p2.x - range : 0;
         } else {
            x = p2.x + range > mapWidth ? mapWidth : p2.x + range;
         }
      } else {
         x = (int)((double)p2.x - (double)range / distance * (double)(p2.x - p1.x));
         y = (int)((double)p2.y - (double)range / distance * (double)(p2.y - p1.y));
         if (x < left) {
            x = left;
         }

         if (x > mapWidth) {
            x = mapWidth;
         }

         if (y < top) {
            y = top;
         }

         if (y > mapHight) {
            y = mapHight;
         }
      }

      return new Point(x, y);
   }
}
