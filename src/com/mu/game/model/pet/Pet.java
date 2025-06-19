package com.mu.game.model.pet;

import com.mu.game.model.map.Map;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.Unit;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.CreatureDie;
import com.mu.io.game.packet.imp.attack.CreaturePositionCorrect;
import com.mu.io.game.packet.imp.map.RemoveUnit;
import com.mu.io.game.packet.imp.map.UnitMove;
import com.mu.io.game.packet.imp.pet.AroundPet;
import com.mu.io.game.packet.imp.pkModel.ChangePkView;
import com.mu.utils.Rnd;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;

public class Pet extends Creature {
   private Player owner;
   private int rank;
   private Creature lastAttackTarget;
   private long lastCheckTime;
   private int checkInterval = 200;

   public Pet(long id, Map map, Player owner) {
      super(id, map);
      this.owner = owner;
   }

   public PetRank getData() {
      return PetConfigManager.getRank(this.rank);
   }

   public boolean doAttack(Creature target, AttackResult result, boolean handleMotion) {
      boolean attackSuccess = target.beAttacked(this, result);
      if (attackSuccess) {
         target.hpReduceForDamage(this, result);
      }

      return attackSuccess;
   }

   public boolean isCanBeAttacked() {
      return false;
   }

   public int canBeAttackedByPlayer(Player attacker) {
      return this.owner.canBeAttackedByPlayer(attacker);
   }

   public boolean hasAttackedMarkForShow(Player observer) {
      return this.owner.hasAttackedMarkForShow(observer);
   }

   public void switchArea(Rectangle newArena, Rectangle oldArea) {
      this.toNewArea(newArena, oldArea);
   }

   public void toNewArea(Rectangle newArea, Rectangle oldArea) {
      if (oldArea != null) {
         UnitMove mm = null;
         Point[] pkView1;
         if (this.isMoving()) {
            pkView1 = this.getMovePath();
            if (pkView1 != null) {
               mm = new UnitMove(this, pkView1);
            }
         }

         ChangePkView pkView = new ChangePkView(this, true);
         RemoveUnit ru = new RemoveUnit(new Unit[]{this});
         Iterator it = this.getMap().getPlayerMap().values().iterator();

         while(true) {
            while(it.hasNext()) {
               Player p = (Player)it.next();
               boolean inNewArea = newArea.contains(p.getPosition());
               boolean inOldArea = oldArea.contains(p.getPosition());
               if (inNewArea && !inOldArea) {
                  if (p.isEnterMap()) {
                     WriteOnlyPacket aroundself = this.createAroundSelfPacket(p);
                     p.writePacket(aroundself);
                     aroundself.destroy();
                     aroundself = null;
                     boolean canPkView = ChangePkView.canSeeKillView(p, this.getOwner());
                     if (canPkView) {
                        p.writePacket(pkView);
                     }

                     if (mm != null) {
                        p.writePacket(mm);
                     }
                  }
               } else if (inOldArea && !inNewArea) {
                  p.writePacket(ru);
               }
            }

            pkView.destroy();
            pkView = null;
            if (mm != null) {
               mm.destroy();
               mm = null;
            }

            ru.destroy();
            ru = null;
            return;
         }
      }
   }

   public void doWork(Map map, long now) {
      if (!this.isDestroy() && this.getOwner() != null && map.equals(this.getOwner().getMap())) {
         super.doWork(map, now);
         Player player = this.getOwner();
         if (System.currentTimeMillis() - this.lastCheckTime >= (long)this.checkInterval && player != null && this.getMap() == player.getMap() && !this.isDie()) {
            this.lastCheckTime = System.currentTimeMillis();
            int s_x = this.getActualPosition().x;
            int s_y = this.getActualPosition().y;
            int e_x = player.getActualPosition().x;
            int e_y = player.getActualPosition().y;
            double d_x = (double)(s_x - e_x);
            double d_y = (double)(s_y - e_y);
            double distance = MathUtil.getDistance((double)s_x, (double)s_y, (double)e_x, (double)e_y);
            if (distance >= (double)PetConfigManager.MAX_OWNER_DISTANCE) {
               this.setLastAttackTarget((Creature)null);
               Point rndPosition = this.rndPosition(map, e_x, e_y);
               Point[] path = new Point[]{rndPosition, this.rndPosition(map, e_x, e_y)};
               this.drag(rndPosition.x, rndPosition.y);
               this.startMove(path, this.lastCheckTime);
            } else {
               int at_x;
               int at_y;
               if (this.lastAttackTarget != null) {
                  if (!this.lastAttackTarget.isDie() && !this.lastAttackTarget.isDestroy() && this.lastAttackTarget.getMap() == map && (this.lastAttackTarget.getType() != 1 || ((Player)this.lastAttackTarget).canBeAttackedByPlayer(this.owner) == 1) && (this.lastAttackTarget.getType() != 4 || ((Pet)this.lastAttackTarget).getOwner().canBeAttackedByPlayer(this.owner) == 1)) {
                     if (!this.isMoving()) {
                        at_x = this.lastAttackTarget.getPosition().x;
                        at_y = this.lastAttackTarget.getPosition().y;
                        int at_dx = s_x - at_x;
                        int at_dy = s_y - at_y;
                        double at_dis = Math.sqrt((double)(at_dx * at_dx + at_dy * at_dy));
                        int atdis = this.attackDistance;
                        if (at_dis >= (double)atdis) {
                           int x = (int)((double)at_x + (double)(atdis - 50) / at_dis * (double)at_dx);
                           int y = (int)((double)at_y + (double)(atdis - 50) / at_dis * (double)at_dy);
                           Point moveTarget = this.searchFeasiblePoint(map, x, y, at_x, at_y);
                           if (Math.abs(s_x - moveTarget.x) <= 3 && Math.abs(s_y - moveTarget.y) <= 3) {
                              if (this.getAttackTarget() != this.lastAttackTarget) {
                                 this.attack(this.lastAttackTarget);
                              }
                           } else {
                              this.startMove(new Point[]{new Point(s_x, s_y), moveTarget}, System.currentTimeMillis());
                              System.out.println("move2 " + this.getStatusEvent().getStatus().name() + " end?" + this.getStatusEvent().isEnd());
                              System.out.println("move2 {" + moveTarget.x + "," + moveTarget.y + "}");
                           }
                        } else if (this.getAttackTarget() != this.lastAttackTarget) {
                           this.attack(this.lastAttackTarget);
                        }
                     }
                  } else {
                     this.idle();
                     this.setLastAttackTarget((Creature)null);
                     this.getAggroList().clear();
                  }
               } else if (distance >= 2010.0D && !this.isAttacking()) {
                  at_x = (int)((double)e_x + 2000.0D / distance * d_x);
                  at_y = (int)((double)e_y + 2000.0D / distance * d_y);
                  Point moveTarget = this.searchFeasiblePoint(map, at_x, at_y, e_x, e_y);
                  if ((Math.abs(at_x - s_x) > 10 || Math.abs(at_y - s_y) > 10) && this.getAttackTarget() == null) {
                     this.startMove(new Point[]{new Point(s_x, s_y), moveTarget}, System.currentTimeMillis());
                  }
               }
            }
         }

      } else {
         RemoveUnit rm = new RemoveUnit(new Unit[]{this});
         map.sendPacketToAroundPlayer(rm, this.getPosition());
         rm.destroy();
         rm = null;
         map.removePet(this);
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

   public Point rndPosition(Map map, int x, int y) {
      if (map == null) {
         return new Point(x, y);
      } else {
         int maxX = Math.max(map.getLeft(), x + 2000);
         int minX = Math.min(map.getRight(), x - 2000);
         int maxY = Math.max(map.getBottom(), y + 2000);
         int minY = Math.min(map.getTop(), y - 2000);

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

   public void setLastAttackTarget(Creature lastAttackTarget) {
      if (!(lastAttackTarget instanceof Pet) && (lastAttackTarget == null || this.lastAttackTarget == null || this.lastAttackTarget.isDestroy())) {
         this.lastAttackTarget = lastAttackTarget;
      }

   }

   public Creature getLastAttackTarget() {
      return this.lastAttackTarget;
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return new AroundPet(this.owner, this);
   }

   public int getType() {
      return 4;
   }

   public int getRank() {
      return this.rank;
   }

   public void setRank(int rank) {
      this.rank = rank;
   }

   public Player getOwner() {
      return this.owner;
   }

   public void beKilled(Creature attacker, AttackResult result) {
      super.beKilled(attacker, result);
      CreatureDie.sendToClient(this);
      this.owner.getPetManager().beKilled();
   }

   public long getArributeValue(StatEnum stat) {
      return (long)this.getStatValue(stat);
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.setDestroy(true);
         super.destroy();
         if (this.owner != null) {
            this.owner = null;
         }

      }
   }
}
