package com.mu.game.model.unit.robot;

import com.mu.game.IDFactory;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.container.imp.Equipment;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.map.AStar;
import com.mu.game.model.map.Map;
import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.properties.levelData.PlayerLevelData;
import com.mu.game.model.shield.PlayerShieldManager;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.monster.Monster;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.RoleInfo;
import com.mu.game.model.unit.player.pkMode.PKMode;
import com.mu.game.model.unit.player.title.TitleManager;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.game.model.unit.skill.model.ProfessionSkills;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.game.model.unit.unitevent.imp.RobotAiEvent;
import com.mu.game.model.unit.unitevent.imp.RobotAttackEvent;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.mall.PopText;
import com.mu.utils.Rnd;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;

public class Robot extends Player {
   private RobotInfo info = null;
   private Creature followTarget = null;
   protected Skill nextSkill = null;
   private long lastSpeekTime = System.currentTimeMillis();
   private int speekSpace = Rnd.get(10000, 15000);
   protected ScheduledFuture moveFuture;
   protected boolean canSpeek = false;

   public Robot(RobotInfo info, Map map) {
      this.setID(IDFactory.getTemporaryID());
      this.setName(info.getName().trim());
      this.setMap(map);
      this.info = info;
      this.init();
   }

   public void writePacket(WriteOnlyPacket packet) {
   }

   public void setFollowTarget(Creature followTarget) {
      this.followTarget = followTarget;
   }

   public boolean isInHanging() {
      return true;
   }

   public void initManager() {
      this.equipment = new Equipment(this);
      this.externals = new HashMap();
      this.pkMode = new PKMode();
      this.petManager = new PlayerPetManager(this);
      this.shieldManager = new PlayerShieldManager(this);
      this.titleManager = new TitleManager(this);
   }

   public boolean beAttacked(Creature attacker, AttackResult result) {
      if (!this.isEnterMap()) {
         return false;
      } else {
         this.setLastFightingTime(System.currentTimeMillis());
         if (!this.isDestroy() && !this.isDie()) {
            if (!this.isCanBeAttacked()) {
               return false;
            } else {
               if (this.isDamageImmunity()) {
                  result.setType(5);
               }

               this.addHateByHurt(attacker, result, 1);
               return true;
            }
         } else {
            return false;
         }
      }
   }

   public void setAg(int ag) {
      super.setAg(100);
   }

   public int getAg() {
      return 100;
   }

   public Creature getFollowTarget() {
      return this.followTarget;
   }

   public int getRadius() {
      return this.info.getRadius();
   }

   public void init() {
      this.initManager();
      this.setLevel(this.info.getLevel());
      this.setProfessionID(this.info.getPro());
      Profession pf = Profession.getProfession(this.info.getPro());
      this.setProType(pf.getProType());
      this.setProLevel(pf.getProLevel());
      RoleInfo ri = new RoleInfo();
      PlayerLevelData data = PlayerLevelData.getLevelData(pf.getProType(), this.info.getLevel());
      ri.setProType(pf.getProType());
      ri.setLevel(this.info.getLevel());
      ri.setCurrentHp(data.getMaxHp());
      ri.setCurrentMp(data.getMaxMp());
      ri.setCurrentAp(data.getMaxAP());
      ri.setCurrentAg(100);
      this.initBasisData(ri);
      this.setBornPoint(this.info.getPoint());
      Iterator var5 = this.info.getEquips().iterator();

      while(var5.hasNext()) {
         int[] in = (int[])var5.next();
         int model = in[0];
         int slot = in[1];
         int star = in[2];
         if (ItemModel.getModel(model) != null) {
            Item d1 = ItemTools.createItem(model, 1, 2);
            d1.setContainerType(0);
            d1.setSlot(slot);
            d1.setStarLevel(star);
            this.getEquipment().loadItem(d1);
         }
      }

      this.setHp(data.getMaxHp());
      this.setMp(data.getMaxMp());
      this.initSkill();
      this.setEnterMap(true);
      this.getEquipment().onLoadApplyEquipmentStats();
      this.addMomentEvent(new RobotAiEvent(this));
   }

   private void initSkill() {
      SkillManager manager = this.getSkillManager();
      ArrayList skillList = this.info.getSkills();

      int skillID;
      SkillDBEntry entry;
      for(skillID = 0; skillID < skillList.size(); ++skillID) {
         entry = new SkillDBEntry();
         entry.setSkillID(((Integer)skillList.get(skillID)).intValue());
         entry.setLevel(1);
         manager.loadSkill(entry);
      }

      skillID = ProfessionSkills.getCommonSkill(this.getProType());
      if (skillID != -1 && !manager.hasSkill(skillID)) {
         entry = new SkillDBEntry();
         entry.setLevel(1);
         entry.setSkillID(skillID);
         manager.loadSkill(entry);
      }

   }

   public void searchTarget() {
      this.nextSkill = this.getRandomSkill();
      if (this.nextSkill != null) {
         ArrayList followList = new ArrayList();
         Iterator it = this.getMap().getMonsterMap().values().iterator();

         while(it.hasNext()) {
            Monster m = (Monster)it.next();
            if (!m.isDie() && !m.isDestroy() && m.isCanBeAttacked()) {
               int distance = MathUtil.getDistance(m.getPosition(), this.getBornPoint());
               if (distance <= this.getRadius() + this.nextSkill.getDistance()) {
                  followList.add(m);
               }
            }
         }

         if (followList.size() > 0) {
            this.followTarget = (Creature)followList.get(Rnd.get(followList.size()));
            this.doFollow();
         }

         followList.clear();
         followList = null;
      }
   }

   public void doFollow() {
      if (this.nextSkill != null) {
         int distance = MathUtil.getDistance(this.getPosition(), this.followTarget.getPosition());
         if (distance <= this.nextSkill.getDistance() + 300) {
            this.doStatusEvent(new RobotAttackEvent(this, this.followTarget));
         } else if (!this.isFindWay) {
            this.startMove(new Point[]{this.getPosition(), this.getTargetPoint(this.nextSkill.getDistance())}, System.currentTimeMillis());
         } else {
            this.setMoveFuture(AStar.pursue(this, this.followTarget, this.getTargetPoint(this.nextSkill.getDistance())));
         }
      }

   }

   public void setMoveFuture(ScheduledFuture moveFuture) {
      if (this.moveFuture != null && !this.moveFuture.isCancelled()) {
         this.moveFuture.cancel(true);
      }

      this.moveFuture = moveFuture;
   }

   protected Point getTargetPoint(int length) {
      int x1 = this.getX();
      int y1 = this.getY();
      int x2 = this.followTarget.getX();
      int y2 = this.followTarget.getY();
      int distance = MathUtil.getDistance(this.followTarget.getPosition(), this.getPosition());
      int maxDistance = length - Rnd.get(0, 50);
      if (maxDistance < 10) {
         maxDistance = 10;
      }

      if (distance <= maxDistance) {
         return new Point(x1, y1);
      } else {
         int distance2 = distance - maxDistance;
         float rate = (float)distance2 / (float)distance;
         int x = (int)((float)x1 + (float)(x2 - x1) * rate);
         int y = (int)((float)y1 + (float)(y2 - y1) * rate);
         return new Point(x, y);
      }
   }

   public Skill getRandomSkill() {
      Skill skill = null;
      byte count = 3;

      do {
         if (count <= 0) {
            return skill;
         }

         int skillId = -1;
         int size = this.info.getSkills().size();
         if (size > 0) {
            skillId = ((Integer)this.info.getSkills().get(Rnd.get(size))).intValue();
         }

         skill = this.getSkillManager().getSkill(skillId);
      } while(skill == null);

      return skill;
   }

   public int getMinAtk() {
      return 1;
   }

   public int getMaxAtk() {
      return 2;
   }

   public int decreaseHp(Creature attacker, AttackResult result) {
      int r = result.getActualDamage();
      return r;
   }

   public void destroy() {
      if (!this.isDestroy()) {
         this.setDestroy(true);
         if (this.equipment != null) {
            this.equipment.destroy();
            this.equipment = null;
         }

         if (this.externals != null) {
            this.externals.clear();
            this.externals = null;
         }

         if (this.pkMode != null) {
            this.pkMode = null;
         }

         if (this.petManager != null) {
            this.petManager.destroy();
            this.petManager = null;
         }

         if (this.shieldManager != null) {
            this.shieldManager = null;
         }

         if (this.titleManager != null) {
            this.titleManager.destroy();
            this.titleManager = null;
         }

         if (this.moveFuture != null && !this.moveFuture.isCancelled()) {
            this.moveFuture.cancel(true);
            this.moveFuture = null;
         }

         this.doSuperDestroy();
      }
   }

   public int decreaseMp(int value) {
      return value;
   }

   public int getUnitType() {
      return 9;
   }

   public boolean isRobot() {
      return true;
   }

   public int[] getVipIcons() {
      return new int[]{-1, -1};
   }

   public void doWork(Map map, long now) {
      super.doWork(map, now);
      if (this.canSpeek && now - this.lastSpeekTime > (long)this.speekSpace * 1L) {
         String text = this.info.getRndWord();
         if (text != null) {
            this.lastSpeekTime = now;
            this.speekSpace = Rnd.get(10000, 15000);
            PopText.pop(this, text);
         }
      }

   }
}
