package com.mu.game.model.unit.monster;

import com.mu.config.VariableConstant;
import com.mu.game.IDFactory;
import com.mu.game.model.drop.SystemDropManager;
import com.mu.game.model.map.Map;
import com.mu.game.model.pet.Pet;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.rewardhall.vitality.VitalityTaskType;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.CreatureTemplate;
import com.mu.game.model.unit.ai.AI;
import com.mu.game.model.unit.ai.AIState;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.unitevent.imp.AIEvent;
import com.mu.game.model.unit.unitevent.imp.PopTextEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.attack.CorrectAttributes;
import com.mu.io.game.packet.imp.attack.CreatureDie;
import com.mu.io.game.packet.imp.monster.AroundMonster;
import com.mu.utils.Rnd;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Monster extends Creature {
   public static final int Rank_Normal = 0;
   public static final int Rank_Elite = 1;
   public static final int Rank_Boss = 2;
   private AI ai = null;
   private long revivalTime = -1L;
   private int maxMoveDistance = 200;
   private int moveRadius = 200;
   private int searchDistance = 6000;
   private List drops = null;
   private int dropProtectedTime = 30000;
   private int bossRank = 0;
   private long beKilledTime = -1L;
   private long lastPatrolTime;
   private static final Logger logger = LoggerFactory.getLogger(Monster.class);

   public Monster(long id, Map map) {
      super(id, map);
   }

   public final long getRevivalTime() {
      return this.revivalTime > 0L && this.revivalTime < 3000L ? 3000L : this.revivalTime;
   }

   public final void setRevivalTime(long revivalTime) {
      this.revivalTime = revivalTime;
   }

   public final int getMaxMoveDistance() {
      return this.maxMoveDistance;
   }

   public final void setMaxMoveDistance(int maxMoveDistance) {
      this.maxMoveDistance = maxMoveDistance;
   }

   public int getSearchDistance() {
      return this.searchDistance;
   }

   public void setSearchDistance(int searchDistance) {
      this.searchDistance = searchDistance;
   }

   public void backHome() {
   }

   public void setTemplateId(int templateId) {
      super.setTemplateId(templateId);
      CreatureTemplate ct = this.getTemplate();
      if (ct != null) {
         ArrayList popList = ct.getPopTextList();
         if (popList != null && popList.size() > 0) {
            this.addMomentEvent(new PopTextEvent(this));
         }
      }

   }

   public void fullResume() {
      super.fullResume();
      List correctList = new ArrayList();
      correctList.add(StatEnum.HP);
      correctList.add(StatEnum.MAX_HP);
      CorrectAttributes.sendWhenChange(this, (List)correctList);
      correctList.clear();
      correctList = null;
   }

   public void addDrops(List drops) {
      if (drops != null) {
         if (this.drops == null) {
            this.drops = new ArrayList();
         }

         this.drops.addAll(drops);
      }
   }

   public void resetDropList(List drops) {
      if (this.drops != null) {
         this.drops.clear();
      }

      this.addDrops(drops);
   }

   public List getDrops() {
      return this.drops;
   }

   public int getDropProtectedTime() {
      return this.dropProtectedTime;
   }

   public void setDropProtectedTime(int dropProtectedTime) {
      this.dropProtectedTime = dropProtectedTime;
   }

   public boolean canDrop(Player player) {
      return true;
   }

   public final void setAI(int aiID) {
      if (this.ai != null) {
         this.ai.destroy();
      }

      this.ai = AI.newInstance(this, aiID);
      this.addMomentEvent(new AIEvent(this));
   }

   public AI getAI() {
      return this.ai;
   }

   public boolean hasWalkRoutes() {
      return Rnd.get(1000) > 980;
   }

   public Point getWalkTarget() {
      if (this.getBornCenter() == null) {
         this.setBornCenter(this.getBornPoint());
      }

      int bornX = (int)this.getBornCenter().getX();
      int bornY = (int)this.getBornCenter().getY();
      int radius = this.getMoveRadius();
      int minBornX = Math.max(this.getMap().getLeft(), bornX - radius);
      int maxBornX = Math.min(this.getMap().getLeft() + this.getMap().getWidth(), bornX + radius);
      int x = Rnd.get(minBornX, maxBornX);
      int miny = Math.max(this.getMap().getTop() - this.getMap().getHeight(), bornY - radius);
      int maxy = Math.min(this.getMap().getTop(), bornY + radius);
      int y = Rnd.get(miny, maxy);
      return new Point(x, y);
   }

   public int getWalkInterval() {
      return 20000;
   }

   public boolean hasEnemyAround() {
      double searchDistance = (double)this.getSearchDistance();
      Map currentMap = this.getMap();
      if (currentMap == null) {
         logger.error("当前地图不存在{}" + this.getName());
         return false;
      } else {
         Rectangle curArea = this.getArea();
         if (curArea == null) {
            return false;
         } else {
            Iterator it = this.getMap().getPlayerMap().values().iterator();

            while(true) {
               while(it.hasNext()) {
                  Player player = (Player)it.next();
                  if (!player.isDie() && !player.isDestroy() && !player.getBuffManager().hasBuff(80004)) {
                     double distance = (double)MathUtil.getDistance(this.getPosition(), player.getPosition());
                     if (this.canSee(player) && distance <= searchDistance) {
                        this.getAggroList().addHateBySeeEnemy(player);
                        return true;
                     }

                     if (distance >= 60000.0D) {
                        this.getAggroList().remove(player);
                     } else if (this.canSee(player) && this.getAggroList().isHate(player)) {
                        return true;
                     }
                  } else {
                     player.getAggroList().clear();
                  }
               }

               return false;
            }
         }
      }
   }

   public boolean canSee(Player player) {
      return true;
   }

   public final int getMoveRadius() {
      return this.moveRadius;
   }

   public final void setMoveRadius(int moveRadius) {
      this.moveRadius = moveRadius;
   }

   public int getType() {
      return 2;
   }

   public WriteOnlyPacket createAroundSelfPacket(Player viewer) {
      return new AroundMonster(this);
   }

   public void switchArea(Rectangle newArea, Rectangle oldArea) {
      this.toNewArea(newArea, oldArea);
   }

   public boolean doAttack(Creature target, AttackResult result, boolean handleMotion) {
      boolean attackSuccess = target.beAttacked(this, result);
      if (attackSuccess) {
         target.hpReduceForDamage(this, result);
      }

      return attackSuccess;
   }

   public int canBeAttackedByPlayer(Player attacker) {
      return this.isCanBeAttacked() ? 1 : 0;
   }

   public boolean hasAttackedMarkForShow(Player observer) {
      return true;
   }

   public boolean beAttacked(Creature attacker, AttackResult result) {
      if (!this.isDestroy() && !this.isDie()) {
         this.addHateByHurt(attacker, result, 1);
         if (!this.isDie()) {
            AI ai = this.getAI();
            if (ai != null) {
               ai.startAIStateHandler(AIState.AS_PURSUE);
               ai = null;
            }
         }

         if (this.bossRank == 2) {
            Player player = null;
            if (attacker.getUnitType() == 1) {
               player = (Player)attacker;
            } else if (attacker.getUnitType() == 4) {
               player = ((Pet)attacker).getOwner();
            }

            if (player != null && player.getBuffManager().hasBuff(80008)) {
               player.getBuffManager().endBuff(80008, true);
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public void revival() {
      super.revival();
      Map map = this.getMap();
      this.setPosition(this.getBornPoint().x, this.getBornPoint().y);
      map.removeMonster(this);
      this.setID(IDFactory.getTemporaryID());
      map.addMonster(this);
      this.idle();
   }

   public void beKilled(Creature attacker, AttackResult result) {
      if (!this.isDie() && !this.isDestroy()) {
         this.setBekilledTime(System.currentTimeMillis());
         int delay = VariableConstant.DefaultDeathDelay;
         super.beKilled(attacker, result);
         AI ai = this.getAI();
         if (ai != null) {
            ai.startAIStateHandler(AIState.AS_DEATH);
         }

         Player player = null;
         if (attacker.getUnitType() == 1) {
            player = (Player)attacker;
         } else if (attacker.getUnitType() == 4) {
            player = ((Pet)attacker).getOwner();
         }

         if (player != null) {
            player.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.KillMonster, this.getTemplateId());
            this.teamCheckKillMonsterTask(player);
            player.getTaskManager().onEventCheckCount(TargetType.CountType.KillMonster);
            player.getVitalityManager().onTaskEvent(VitalityTaskType.KILL_MONSTER, 0, 1);
            player.getDropManager().addKillNumber(this.getTemplateId());
            this.dropItem(player, delay);
            this.addExpToPlayer(player);
            player.getTriggerManager().handleKillMonster(this);
         }

         this.getAggroList().clear();
         CreatureDie.sendToClient(this);
      }
   }

   public void dropItem(Player player, int delay) {
      SystemDropManager.dropWhenMonsterBeKill(this, player, delay, false);
   }

   private void teamCheckKillMonsterTask(Player player) {
      Team team = player.getCurrentTeam();
      if (team != null) {
         Iterator var4 = team.getMateList().iterator();

         while(var4.hasNext()) {
            Teammate mate = (Teammate)var4.next();
            if (mate.getId() == player.getID()) {
               return;
            }

            Player other = mate.getPlayer();
            if (other == null || MathUtil.getDistance(player.getPosition(), other.getPosition()) > 10000) {
               return;
            }

            other.getTaskManager().onEventCheckSpecify(TargetType.SpecifyType.KillMonster, this.getTemplateId());
         }

      }
   }

   public long addExpToPlayer(Player player) {
      long exp = this.getFinallyExp(player);
      if (player.getPetManager().isOpen()) {
         player.getPetManager().increaseExp((long)this.getRewardExp(), true);
      }

      return PlayerManager.addExp(player, exp, this.getID());
   }

   private long getFixedExp(Player player, long rewardExp) {
      int lv = player.getLevel() - this.getLevel();
      if (lv < 1) {
         return rewardExp;
      } else {
         int rate = PlayerLevelData.getRate(lv);
         long tmp = rewardExp * (long)rate / 1000L;
         return tmp < 1L ? 1L : tmp;
      }
   }

   public long getFinallyExp(Player player) {
      long exp = 1L * (long)this.getRewardExp();
      exp = this.getFixedExp(player, exp);
      int expBonus = player.getStatValue(StatEnum.EXP_BONUS) + player.getTeamExpBonus();
      if (expBonus > 0) {
         exp += exp * (long)expBonus / 100000L;
      }

      return exp;
   }

   public void setBossRank(int rank) {
      this.bossRank = rank;
   }

   public int getBossRank() {
      return this.bossRank;
   }

   public boolean isBoss() {
      return this.getBossRank() == 2;
   }

   public void setBekilledTime(long time) {
      this.beKilledTime = time;
   }

   public long getBeKilledTime() {
      return this.beKilledTime;
   }

   public int getNextRevivalTime() {
      if (this.revivalTime > 0L && this.beKilledTime > 0L) {
         int nextTime = (int)(this.revivalTime - (System.currentTimeMillis() - this.beKilledTime)) / 1000;
         return nextTime > 0 ? nextTime : 0;
      } else {
         return 0;
      }
   }

   public long getNextRevivalTimeLong() {
      if (!this.isDestroy() && !this.isDie()) {
         return 0L;
      } else if (this.revivalTime > 0L && this.beKilledTime > 0L) {
         long time = this.beKilledTime + this.revivalTime;
         return time > 0L ? time : 0L;
      } else {
         return 0L;
      }
   }

   public int getDieMusic() {
      return this.getTemplate().getDieMusic();
   }

   public boolean hasDropPunish() {
      return true;
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.setDestroy(true);
         super.destroy();
      }
   }

   public long getLastPatrolTime() {
      return this.lastPatrolTime;
   }

   public void setLastPatrolTime(long lastPatrolTime) {
      this.lastPatrolTime = lastPatrolTime;
   }
}
