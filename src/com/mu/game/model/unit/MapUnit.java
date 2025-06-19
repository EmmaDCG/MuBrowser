package com.mu.game.model.unit;

import com.mu.game.model.map.Map;
import com.mu.game.model.unit.move.MoveLine;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.game.model.unit.unitevent.imp.MoveEvent;
import com.mu.game.model.unit.unitevent.imp.NoneEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.map.UnitMove;
import com.mu.utils.Tools;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class MapUnit extends Unit {
   private static final Logger logger = LoggerFactory.getLogger(MapUnit.class);
   private Map map = null;
   private Point position = new Point();
   private int[] face = new int[]{0, 100};
   private boolean shouldDestroy = false;
   private int modelId;
   private Event statusEvent = new NoneEvent(this);
   private Event nextEvent = null;
   private ConcurrentLinkedQueue repeatImmediateEvents = new ConcurrentLinkedQueue();
   protected ConcurrentHashMap momentEvents = new ConcurrentHashMap(8, 0.75F, 2);
   protected ConcurrentHashMap limitOperations = Tools.newConcurrentHashMap2();

   public MapUnit(long id, Map map) {
      super(id);
      this.map = map;
   }

   public abstract void switchArea(Rectangle var1, Rectangle var2);

   public abstract WriteOnlyPacket createAroundSelfPacket(Player var1);

   public final Map getMap() {
      return this.map;
   }

   public final void setMap(Map map) {
      this.map = map;
   }

   public int getModelId() {
      return this.modelId;
   }

   public void setModelId(int modelId) {
      this.modelId = modelId;
   }

   public Rectangle getArea() {
      return this.getMap().getArea(this.getPosition());
   }

   public final void setPosition(Point point) {
      this.position.x = point.x;
      this.position.y = point.y;
   }

   public final void setPosition(int x, int y) {
      this.position.x = x;
      this.position.y = y;
   }

   public final int getX() {
      return this.position.x;
   }

   public final void setX(int x) {
      this.position.x = x;
   }

   public final int getY() {
      return this.position.y;
   }

   public final void setY(int y) {
      this.position.y = y;
   }

   public final Point getPosition() {
      return this.position;
   }

   public void broadcastWhenStartMove() {
      Map map = this.getMap();
      if (map != null) {
         Point[] movePath = this.getMovePath();
         if (movePath != null) {
            UnitMove um = new UnitMove(this, movePath);
            map.sendPacketToAroundPlayer(um, this.getPosition());
            um.destroy();
            um = null;
         } else {
            logger.error("movePath is null");
         }
      } else {
         logger.error("map is null");
      }

   }

   protected final Status getStatus() {
      return this.getStatusEvent().getStatus();
   }

   public final Event getStatusEvent() {
      if (this.statusEvent == null) {
         this.statusEvent = new NoneEvent(this);
      }

      return this.statusEvent;
   }

   protected void doStatusEvent(Event event) {
      if (this.nextEvent != null) {
         this.nextEvent.destroy();
         this.nextEvent = null;
      }

      this.nextEvent = event;
   }

   public boolean isWalking() {
      return false;
   }

   public void doWork(Map map, long now) {
      if (this.isDestroy()) {
         map.removeUnit(this);
         logger.error(this.getID() + "," + this.getType() + "," + this.getName() + " be destroyed" + "\t" + this.getStatus());
      } else if (this.shouldDestroy) {
         this.destroy();
      } else {
         Iterator it = this.momentEvents.values().iterator();

         Event statusEvent;
         while(it.hasNext()) {
            statusEvent = (Event)it.next();

            try {
               if (statusEvent.isEnd()) {
                  statusEvent.destroy();
                  it.remove();
               } else {
                  statusEvent.doWork(now);
               }
            } catch (Exception var9) {
               logger.error("dowork error,momentEvents = {} object = {}", statusEvent.getStatus().getIdentify(), this.getName());
               var9.printStackTrace();
            }
         }

         Event event;
         while(!this.repeatImmediateEvents.isEmpty()) {
            event = (Event)this.repeatImmediateEvents.poll();

            try {
               event.doWork(now);
            } catch (Exception var8) {
               logger.error("dowork error,repeatImmediateEvents = {} object = {}", event.getStatus().getIdentify(), this.getName());
               var8.printStackTrace();
            }

            event.destroy();
            it = null;
         }

         event = this.callNextEvent();
         if (event != null) {
            statusEvent = this.getStatusEvent();
            this.statusEvent = event;
            this.nextEvent = null;
            this.addLimitOperatioin(2, this.statusEvent.getLimitOperations().toArray());
            statusEvent.destroy();
         }

         statusEvent = this.getStatusEvent();

         try {
            statusEvent.doWork(now);
         } catch (Exception var7) {
            logger.error("dowork error,statusEvent = " + statusEvent.getStatus().getIdentify() + " object = " + this.getName() + "\t mapID = " + this.getMap().getID());
            var7.printStackTrace();
         }

      }
   }

   public boolean operationInLimit(OperationEnum pEnum) {
      return this.limitOperations.containsKey(pEnum);
   }

   public void addLimitOperatioin(int type, Object... pEnums) {
      if (pEnums != null) {
         for(int i = 0; i < pEnums.length; ++i) {
            OperationEnum pEnum = (OperationEnum)pEnums[i];
            this.limitOperationCounter(pEnum, true);
         }
      }

   }

   public void removeLimitOperation(int type, Object... pEnums) {
      if (pEnums != null) {
         for(int i = 0; i < pEnums.length; ++i) {
            OperationEnum pEnum = (OperationEnum)pEnums[i];
            this.limitOperationCounter(pEnum, false);
         }
      }

   }

   private void limitOperationCounter(OperationEnum pEnum, boolean add) {
      Integer count = (Integer)this.limitOperations.get(pEnum);
      if (add) {
         if (count != null) {
            this.limitOperations.put(pEnum, count.intValue() + 1);
         } else {
            this.limitOperations.put(pEnum, Integer.valueOf(1));
         }
      } else if (count != null) {
         int value = count.intValue() - 1;
         if (value <= 0) {
            this.limitOperations.remove(pEnum);
         } else {
            this.limitOperations.put(pEnum, value);
         }
      }

   }

   public int getMapID() {
      return this.map.getID();
   }

   private Event callNextEvent() {
      if (this.nextEvent != null) {
         boolean b = this.canDoStatus(this.nextEvent);
         if (b) {
            return this.nextEvent;
         }
      }

      return null;
   }

   public void addRepeatImmediateEvent(Event event) {
      if (this.canDoStatus(event)) {
         this.addLimitOperatioin(2, event.getLimitOperations().toArray());
         this.repeatImmediateEvents.add(event);
      }

   }

   public void addMomentEvent(Event event) {
      Event sameEvent = (Event)this.momentEvents.remove(event.getStatus());
      if (sameEvent != null) {
         sameEvent.destroy();
         sameEvent = null;
      }

      this.momentEvents.put(event.getStatus(), event);
      if (this.canDoStatus(event)) {
         this.addLimitOperatioin(2, event.getLimitOperations().toArray());
      }

   }

   public void endMomentEvent(Status status) {
      Event event = (Event)this.momentEvents.get(status);
      if (event != null) {
         event.setEnd(true);
      }

   }

   public float getSpeed() {
      return 4.0F;
   }

   public void handleMove(long now) {
   }

   public void idle() {
      this.doStatusEvent(new NoneEvent(this));
   }

   public Point[] getMovePath() {
      if (this.getStatus() == Status.MOVE) {
         MoveEvent event = (MoveEvent)this.getStatusEvent();
         return event.getMovePath();
      } else {
         return null;
      }
   }

   public boolean canDoStatus(Event event) {
      return !this.limitOperations.containsKey(event.getOperationEnum());
   }

   public int[] getFace() {
      return this.face;
   }

   public void setFace(int faceX, int faceY) {
      this.face[0] = faceX;
      this.face[1] = faceY;
   }

   public Point getActualPosition() {
      return this.getStatusEvent().getActualPosition();
   }

   public boolean isMoving() {
      return this.getStatus() == Status.MOVE;
   }

   public boolean isIdle() {
      return this.getStatus() == Status.NONE;
   }

   public Point getMoveTarget() {
      return this.getPosition();
   }

   public void startMove(Point[] path, long moveTime) {
      Map map = this.getMap();
      if (map != null) {
         MoveLine[] moveLines = new MoveLine[path.length - 1];

         for(int i = 0; i < path.length - 1; ++i) {
            Point p1 = path[i];
            Point p2 = path[i + 1];
            if (!map.isValidPoint(p2)) {
               moveLines = null;
               return;
            }

            MoveLine line = new MoveLine(p1.x, p1.y, p2.x, p2.y);
            moveLines[i] = line;
         }

         this.doStatusEvent(new MoveEvent(this, moveLines, moveTime));
      }
   }

   public boolean isShouldDestroy() {
      return this.shouldDestroy;
   }

   public void setShouldDestroy(boolean shouldDestroy) {
      this.shouldDestroy = shouldDestroy;
   }

   public Event getMomentEvent(Status sta) {
      return (Event)this.momentEvents.get(sta);
   }

   public void forceIdle() {
      this.statusEvent = new NoneEvent(this);
   }

   public void destroy() {
      this.setDestroy(true);
      if (this.momentEvents != null) {
         Iterator it = this.momentEvents.values().iterator();

         while(it.hasNext()) {
            try {
               ((Event)it.next()).destroy();
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }

         this.momentEvents.clear();
         this.momentEvents = null;
      }

      if (this.repeatImmediateEvents != null) {
         while(!this.repeatImmediateEvents.isEmpty()) {
            Event event = (Event)this.repeatImmediateEvents.poll();
            event.destroy();
         }

         this.repeatImmediateEvents = null;
      }

      if (this.statusEvent != null) {
         this.statusEvent.destroy();
         this.statusEvent = null;
      }

      if (this.nextEvent != null) {
         this.nextEvent.destroy();
         this.nextEvent = null;
      }

      if (this.limitOperations != null) {
         this.limitOperations.clear();
         this.limitOperations = null;
      }

      if (this.map != null) {
         if (this.getUnitType() != 1) {
            this.map.removeUnit(this);
         }

         this.map = null;
      }

   }
}
