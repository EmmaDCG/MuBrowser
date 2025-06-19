package com.mu.game.model.unit.skill;

import com.mu.config.VariableConstant;
import com.mu.game.model.packet.SkillPacketService;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.manager.SkillFactory;
import com.mu.game.model.unit.skill.model.SkillDBEntry;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.io.game.packet.imp.skill.UpdateSkill;
import java.util.HashMap;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillManager {
   private static Logger logger = LoggerFactory.getLogger(SkillManager.class);
   private int attackSpeed = 2000;
   private float cdRedueceRate = 0.0F;
   private Creature owner;
   protected long commonSkillCoolTime;
   private HashMap skillMap = new HashMap();
   private HashMap properties = new HashMap();
   private Skill autoSkill = null;
   private Skill commonSkill = null;

   public SkillManager(Creature owner) {
      this.owner = owner;
      this.attackSpeed = this.getBasicPublicCD();
   }

   private int getBasicPublicCD() {
      return this.owner.getType() == 1 ? 800 : 2000;
   }

   public void removeAllPetSkill() {
      Iterator var2;
      if (this.skillMap != null) {
         var2 = this.skillMap.values().iterator();

         while(var2.hasNext()) {
            Skill skill = (Skill)var2.next();
            skill.destroy();
         }

         this.skillMap.clear();
      }

      if (this.properties != null) {
         var2 = this.properties.values().iterator();

         while(var2.hasNext()) {
            SkillAdditionalProperty property = (SkillAdditionalProperty)var2.next();
            property.destroy();
         }

         this.properties.clear();
      }

   }

   public void loadSkill(SkillDBEntry entry) {
      Skill skill = SkillFactory.createSkill(entry.getSkillID(), entry.getLevel(), this.owner);
      if (skill == null) {
         logger.error("没有技能 " + entry.getSkillID());
      } else {
         skill.setNextThawTime(System.currentTimeMillis() + (long)entry.getRemainThawTime());
         skill.setSelected(entry.isSelected());
         skill.setPassiveConsume(entry.getPassiveConsume());
         this.addToMap(skill);
         this.effectPassive(skill);
      }
   }

   private void addToMap(Skill skill) {
      this.skillMap.put(skill.getSkillID(), skill);
      if (skill.isSelected()) {
         this.autoSkill = skill;
      }

      if (skill.isCommonSkill()) {
         if (skill.getLevel() < 1) {
            skill.setLevel(1);
            skill.init();
         }

         this.commonSkill = skill;
      }

   }

   public int learnSkill(int skillID) {
      Skill skill = this.getSkill(skillID);
      if (skill == null) {
         skill = SkillFactory.createSkill(skillID, 1, this.getOwner());
         if (skill == null) {
            return 8007;
         }
      } else {
         skill.learnSkill();
      }

      skill.init();
      this.effectPassive(skill);
      return 1;
   }

   private void effectPassive(Skill skill) {
      if (skill.getModel().isPassive() && skill.getLevel() > 0) {
         skill.useSkill(true, this.owner.getPosition(), this.owner);
      }

   }

   public int getSkillLevel(int skillID) {
      Skill skill = (Skill)this.skillMap.get(skillID);
      return skill != null ? skill.getLevel() : 0;
   }

   public void addSkill(Skill skill) {
      if (skill != null) {
         this.addToMap(skill);
      }
   }

   public void setCoolDown(long now, boolean occuPublicTime) {
      if (occuPublicTime) {
         this.commonSkillCoolTime = now + (long)this.attackSpeed;
      }

   }

   public boolean isInCommonCoolTime(long now) {
      return this.commonSkillCoolTime > now;
   }

   public boolean isSkillDisabled(int skillID, long now) {
      SkillModel model = SkillModel.getModel(skillID);
      if (model == null) {
         return true;
      } else if (model.isPublicCoolTime() && this.commonSkillCoolTime > now) {
         return true;
      } else {
         model = null;
         return false;
      }
   }

   public void destroy() {
      Iterator var2;
      if (this.skillMap != null) {
         var2 = this.skillMap.values().iterator();

         while(var2.hasNext()) {
            Skill skill = (Skill)var2.next();
            skill.destroy();
         }

         this.skillMap.clear();
      }

      this.skillMap = null;
      if (this.properties != null) {
         var2 = this.properties.values().iterator();

         while(var2.hasNext()) {
            SkillAdditionalProperty property = (SkillAdditionalProperty)var2.next();
            property.destroy();
         }

         this.properties.clear();
         this.properties = null;
      }

      this.commonSkill = null;
      this.autoSkill = null;
   }

   public void changeAutoSkill(int newAutoSkillID) {
      if (this.autoSkill != null) {
         this.autoSkill.setSelected(false);
         if (this.getOwner().getType() == 1) {
            SkillPacketService.noticeGatewayUpdateSkill((Player)this.getOwner(), this.autoSkill, 2);
         }
      }

      Skill skill = this.getSkill(newAutoSkillID);
      if (skill != null) {
         skill.setSelected(true);
         this.autoSkill = skill;
      }

   }

   public void changeAttackSpeed(int newSpeed) {
      int oldSpeed = this.attackSpeed;
      int diffSpeed = newSpeed - 100;
      float cdRate = VariableConstant.ATK_SPEED_COE * (float)diffSpeed / (1.0F + (float)diffSpeed * VariableConstant.ATK_SPEED_COE);
      this.cdRedueceRate = Math.min(0.99F, cdRate);
      this.attackSpeed = (int)((double)this.getBasicPublicCD() * (1.0D - (double)this.cdRedueceRate));
      if (this.attackSpeed != oldSpeed) {
         Iterator var6 = this.skillMap.values().iterator();

         while(var6.hasNext()) {
            Skill skill = (Skill)var6.next();
            skill.init();
            if (StatChange.isSendStat(this.getOwner())) {
               UpdateSkill.sendToClient((Player)this.getOwner(), skill);
            }
         }

      }
   }

   public boolean hasSkill(int skillID) {
      return this.skillMap.containsKey(skillID);
   }

   public Skill getSkill(int skillID) {
      return (Skill)this.skillMap.get(skillID);
   }

   public Skill getAutoSkill() {
      return this.autoSkill;
   }

   public void setAutoSkill(Skill autoSkill) {
      this.autoSkill = autoSkill;
   }

   public Skill getCommonSkill() {
      return this.commonSkill;
   }

   public void setCommonSkill(Skill commonSkill) {
      this.commonSkill = commonSkill;
   }

   public SkillAdditionalProperty getProperty(int skillID) {
      return (SkillAdditionalProperty)this.properties.get(skillID);
   }

   public HashMap getSkillMap() {
      return this.skillMap;
   }

   public Creature getOwner() {
      return this.owner;
   }

   public int getAttackSpeed() {
      return this.attackSpeed;
   }

   public float getCdRedueceRate() {
      return this.cdRedueceRate;
   }
}
