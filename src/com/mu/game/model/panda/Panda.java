package com.mu.game.model.panda;

import com.mu.game.model.drop.DropItem;
import com.mu.game.model.map.Map;
import com.mu.game.model.unit.MapUnit;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.CreaturePositionCorrect;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.map.UnitMove;
import com.mu.io.game.packet.imp.panda.AroundPanda;
import com.mu.utils.Rnd;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.List;

public class Panda extends MapUnit {
   public Player owner;
   private long lastCheckTime;
   private int checkInterval = 1200;
   private long lastMoneyId;
   private static final int DragDistance = 9000;
   private static final int FllowDistance = 1000;
   private static final int SreachDistance = 6500;

   public Panda(long id, Map map, Player owner) {
      super(id, map);
      this.owner = owner;
   }

   public void hide() {
      Map map = this.getMap();
      if (map != null) {
         map.removePanda(this);
         RemoveUnit leaveMap = new RemoveUnit(new Unit[]{this});
         map.sendPacketToAroundPlayer(leaveMap, this.owner, true);
         leaveMap.destroy();
         leaveMap = null;
      }

   }

   public void doWork(Map map, long now) {
      super.doWork(map, now);
      if (this.isDestroy()) {
         map.removePanda(this);
      } else {
         super.doWork(map, now);
         Player player = this.getOwner();
         if (System.currentTimeMillis() - this.lastCheckTime >= (long)this.checkInterval && player != null && this.getMap() == player.getMap() && this.lastMoneyId != -1L) {
            this.lastCheckTime = System.currentTimeMillis();
            int s_x = this.getActualPosition().x;
            int s_y = this.getActualPosition().y;
            int p_x = player.getActualPosition().x;
            int p_y = player.getActualPosition().y;
            double d_x = (double)(s_x - p_x);
            double d_y = (double)(s_y - p_y);
            double distance = MathUtil.getDistance((double)s_x, (double)s_y, (double)p_x, (double)p_y);
            Point[] path;
            if (distance >= 9000.0D) {
               this.lastMoneyId = 0L;
               Point rndPosition = this.rndPosition(map, p_x, p_y);
               path = new Point[]{rndPosition, this.rndPosition(map, p_x, p_y)};
               this.drag(rndPosition.x, rndPosition.y);
               this.startMove(path, this.lastCheckTime);
            } else {
               DropItem item = map.getDropItem(this.lastMoneyId);
               if (item == null) {
                  List moneyList = map.getDropItemByRange(this.owner, 6500, true);
                  if (!moneyList.isEmpty()) {
                     item = (DropItem)moneyList.get(0);
                     this.lastMoneyId = item.getID();
                     moneyList.clear();
                     path = null;
                  }
               }

               double at_dis;
               if (item != null) {
                  if (!this.isMoving()) {
                     int at_x = item.getPosition().x;
                     int at_y = item.getPosition().y;
                     int at_dx = s_x - at_x;
                     int at_dy = s_y - at_y;
                     at_dis = Math.sqrt((double)(at_dx * at_dx + at_dy * at_dy));
                     int atdis = 100;
                     if (at_dis >= (double)atdis) {
                        int x = (int)((double)at_x + (double)(atdis - 50) / at_dis * (double)at_dx);
                        int y = (int)((double)at_y + (double)(atdis - 50) / at_dis * (double)at_dy);
                        Point moveTarget = this.searchFeasiblePoint(map, x, y, at_x, at_y);
                        if (Math.abs(s_x - moveTarget.x) > 3 || Math.abs(s_y - moveTarget.y) > 3) {
                           this.startMove(new Point[]{new Point(s_x, s_y), moveTarget}, System.currentTimeMillis());
                        }
                     } else {
                        item.pickout(this.owner);
                     }
                  }
               } else if (distance >= 1000.0D) {
                  double move_x = 980.0D / distance * d_x;
                  double move_y = 980.0D / distance * d_y;
                  at_dis = (double)Rnd.get(-100, 100) / 180.0D * 3.141592653589793D;
                  double cos = Math.cos(at_dis);
                  double sin = Math.sin(at_dis);
                  int t_x = (int)((double)p_x + move_x * cos - move_y * sin);
                  int t_y = (int)((double)p_y + move_y * cos + move_x * sin);
                  Point moveTarget = this.searchFeasiblePoint(map, t_x, t_y, p_x, p_y);
                  if (Math.abs(t_x - s_x) > 50 || Math.abs(t_y - s_y) > 50) {
                     this.startMove(new Point[]{new Point(s_x, s_y), moveTarget}, System.currentTimeMillis());
                  }
               }
            }
         }

      }
   }

   public void drag(int x, int y) {
      Rectangle newArea = this.getMap().getArea(x, y);
      Rectangle oldArea = this.getArea();
      this.setPosition(x, y);
      if (newArea != null && !newArea.equals(oldArea)) {
         this.switchArea(newArea, oldArea);
      }

      CreaturePositionCorrect.correntWhenTeleport(this, x, y);
   }

   private Point searchFeasiblePoint(Map map, int s_x, int s_y, int e_x, int e_y) {
      if (!map.isBlocked(s_x, s_y)) {
         return new Point(s_x, s_y);
      } else {
         s_x = map.getTileX(s_x);
         s_y = map.getTileY(s_y);
         e_x = map.getTileX(e_x);
         e_y = map.getTileY(e_y);
         int xu = s_x < e_x ? 1 : -1;
         int yu = s_y < e_y ? 1 : -1;
         int max_x = Math.abs(e_x - s_x);
         int max_y = Math.abs(e_y - s_y);
         int max = Math.max(max_x, max_y);

         for(int i = 1; i < max; ++i) {
            int x = s_x + Math.min(i, max_x) * xu;
            int y = s_y + Math.min(i, max_y) * yu;
            if (!map.tileIsBlocked(x, y)) {
               return new Point(map.getXByTile(x), map.getYByTile(y));
            }
         }

         return new Point(map.getXByTile(e_x), map.getYByTile(e_y));
      }
   }

   public Point rndPosition(Map map, int x, int y) {
      if (map == null) {
         return new Point(x, y);
      } else {
         int maxX = Math.max(map.getLeft(), x + 1000);
         int minX = Math.min(map.getRight(), x - 1000);
         int maxY = Math.max(map.getBottom(), y + 1000);
         int minY = Math.min(map.getTop(), y - 1000);

         for(int i = 0; i < 3; ++i) {
            int rndX = Rnd.get(minX, maxX);
            int rndY = Rnd.get(minY, maxY);
            if (!map.isBlocked(rndX, rndY)) {
               return new Point(rndX, rndY);
            }
         }

         return new Point(x, y);
      }
   }

   public void switchArea(Rectangle newArena, Rectangle oldArea) {
      this.toNewArea(newArena, oldArea);
   }

   public void toNewArea(Rectangle newArea, Rectangle oldArea) {
      if (oldArea != null) {
         UnitMove mm = null;
         WriteOnlyPacket aroundself = this.createAroundSelfPacket(this.getOwner());
         Point[] ru;
         if (this.isMoving()) {
            ru = this.getMovePath();
            if (ru != null) {
               mm = new UnitMove(this, ru);
            }
         }

         RemoveUnit ru2 = new RemoveUnit(new Unit[]{this});
         Iterator it = this.getMap().getPlayerMap().values().iterator();

         while(true) {
            while(true) {
               Player p;
               do {
                  if (!it.hasNext()) {
                     if (mm != null) {
                        mm.destroy();
                        mm = null;
                     }

                     aroundself.destroy();
                     aroundself = null;
                     ru2.destroy();
                     ru = null;
                     return;
                  }

                  p = (Player)it.next();
               } while(!p.isEnterMap());

               boolean inNewArea = newArea.contains(p.getPosition());
               boolean inOldArea = oldArea.contains(p.getPosition());
               if (inNewArea && !inOldArea) {
                  p.writePacket(aroundself);
                  if (mm != null) {
                     p.writePacket(mm);
                  }
               } else if (inOldArea && !inNewArea) {
                  p.writePacket(ru2);
               }
            }
         }
      }
   }

   public float getSpeed() {
      return this.owner.getSpeed();
   }

   public int getType() {
      return 8;
   }

   public Player getOwner() {
      return this.owner;
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return new AroundPanda(this.owner, this);
   }

   public long getLastMoneyId() {
      return this.lastMoneyId;
   }

   public void setLastMoneyId(long lastMoneyId) {
      this.lastMoneyId = lastMoneyId;
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.setDestroy(true);
         this.owner = null;
         super.destroy();
      }
   }
}
