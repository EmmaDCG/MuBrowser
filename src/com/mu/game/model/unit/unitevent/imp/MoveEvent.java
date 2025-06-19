package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.move.MoveLine;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import java.awt.Point;
import java.awt.Rectangle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoveEvent extends Event {
   private MoveLine[] moveLines;
   private int currentLineIndex = 0;
   private double currentLineMoveLength = 0.0D;
   private double currentLineX = 0.0D;
   private double currentLineY = 0.0D;
   private long lastMoveTime;
   private boolean isStart = false;
   private long beginMoveTime = 0L;
   private static Logger logger = LoggerFactory.getLogger(MoveEvent.class);

   public MoveEvent(MapUnit owner, MoveLine[] movelines, long moveTime) {
      super(owner);
      this.checkrate = 100;
      this.moveLines = movelines;
      this.lastMoveTime = this.beginMoveTime = moveTime;
      this.currentLineX = (double)movelines[0].getX1();
      this.currentLineY = (double)movelines[0].getY1();
   }

   public void work(long now) throws Exception {
      if (this.getOwner().operationInLimit(this.getOperationEnum())) {
         this.getOwner().idle();
      } else {
         Map map = this.getOwner().getMap();
         if (!this.getOwner().isDestroy()) {
            if (!this.isStart) {
               this.getOwner().broadcastWhenStartMove();
               this.isStart = true;
            }

            long moveTime = now - this.lastMoveTime;
            int oldLandscape = map.getUnitLandscape(this.getOwner());
            double moveDistacne = (double)((float)moveTime * this.getOwner().getSpeed());
            boolean isEnd = true;

            int x;
            for(x = this.currentLineIndex; x < this.moveLines.length; ++x) {
               MoveLine line = this.moveLines[x];
               if (this.currentLineMoveLength + moveDistacne <= line.getLength()) {
                  this.currentLineMoveLength += moveDistacne;
                  double rate = this.currentLineMoveLength / line.getLength();
                  this.currentLineX = (double)line.getX1() + (double)line.getxStep() * rate;
                  this.currentLineY = (double)line.getY1() + (double)line.getyStep() * rate;
                  isEnd = false;
                  break;
               }

               if (this.currentLineIndex == this.moveLines.length - 1) {
                  break;
               }

               moveDistacne = this.currentLineMoveLength + moveDistacne - line.getLength();
               ++this.currentLineIndex;
               this.currentLineMoveLength = 0.0D;
               this.currentLineX = (double)this.moveLines[this.currentLineIndex].getX1();
               this.currentLineY = (double)this.moveLines[this.currentLineIndex].getY1();
            }

            this.lastMoveTime = now;
            if (isEnd) {
               MoveLine line = this.moveLines[this.moveLines.length - 1];
               this.currentLineX = (double)line.getX2();
               this.currentLineY = (double)line.getY2();
               this.getOwner().idle();
            }

            x = (int)this.currentLineX;
            int y = (int)this.currentLineY;
            Rectangle newArea = map.getArea(x, y);
            Rectangle oldRectangle = this.getOwner().getArea();
            this.getOwner().setPosition(x, y);
            if (newArea != null && !newArea.equals(oldRectangle)) {
               this.getOwner().switchArea(newArea, oldRectangle);
            }

            if (map.isCheckLandscapeChange() && this.getOwner().getType() == 1) {
               int newLandscape = map.getUnitLandscape(this.getOwner());
               if (newLandscape != oldLandscape) {
                  map.unitLandscapeChanged(newLandscape, this.getOwner());
               }
            }

            this.getOwner().handleMove(now);
         }
      }
   }

   public Point[] getMovePath() {
      Point[] points = new Point[this.moveLines.length - this.currentLineIndex + 1];
      points[0] = new Point((int)this.currentLineX, (int)this.currentLineY);
      int i = this.currentLineIndex;

      for(int j = 1; i < this.moveLines.length; ++j) {
         points[j] = new Point(this.moveLines[i].getX2(), this.moveLines[i].getY2());
         ++i;
      }

      return points;
   }

   public Point getActualPosition() {
      long now = System.currentTimeMillis();
      if (now - this.lastCheckTime > 20L) {
         try {
            this.work(now);
            this.lastCheckTime = now;
         } catch (Exception var4) {
            var4.printStackTrace();
         }
      }

      return this.getOwner().getPosition();
   }

   public Point getEndPoint() {
      MoveLine line = this.moveLines[this.moveLines.length - 1];
      return new Point(line.getX2(), line.getY2());
   }

   public Status getStatus() {
      return Status.MOVE;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.MOVE;
   }

   public void destroy() {
      super.destroy();
      this.moveLines = null;
   }
}
