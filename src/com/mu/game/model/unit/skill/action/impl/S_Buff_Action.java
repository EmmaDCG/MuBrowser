package com.mu.game.model.unit.skill.action.impl;

import com.mu.game.model.team.Team;
import com.mu.game.model.team.Teammate;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.buff.model.BuffDynamicData;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.action.SkillAction;
import com.mu.game.model.unit.skill.skills.GroupSkill;
import com.mu.game.model.unit.skill.skills.SingleSkill;
import com.mu.utils.geom.MathUtil;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class S_Buff_Action extends SkillAction {
   private int buffID;
   private int buffLevel;
   private boolean levelBySkill;

   public S_Buff_Action(int type, int buffID, int buffLevel, boolean levelBySkill) {
      super(type);
      this.buffID = buffID;
      this.buffLevel = buffLevel;
      this.levelBySkill = levelBySkill;
   }

   public int getActualLevel(Skill skill) {
      return this.levelBySkill ? skill.getLevel() : this.buffLevel;
   }

   public void singleAction(SingleSkill skill, Creature effected, AttackResult result, Point centPoint) {
      this.createBuff(skill, effected, result);
   }

   public void groupAction(GroupSkill skill, HashMap results, Point centPoint) {
      Iterator it = results.entrySet().iterator();

      while(it.hasNext()) {
         Entry entry = (Entry)it.next();
         Creature effected = (Creature)entry.getKey();
         if (!effected.isDie()) {
            this.createBuff(skill, effected, (AttackResult)entry.getValue());
         }
      }

   }

   private void createBuff(Skill skill, Creature effected, AttackResult result) {
      if (skill.isDeBenefiesSkill()) {
         if (result == null) {
            return;
         }

         switch(skill.getModel().getType()) {
         case 3:
         case 4:
            break;
         default:
            if (result.getActualDamage() == 0) {
               return;
            }
         }
      }

      effected.getBuffManager().createAndStartBuff(skill.getOwner(), this.buffID, this.getActualLevel(skill), true, 0L, (List)null);
   }

   public int preCheck(Skill skill, Creature effected, Point specifyPoint) {
      return 1;
   }

   public Creature useTargetInHang(Player owner, boolean assistedMode) {
      Team team = owner.getCurrentTeam();
      if (assistedMode && team != null) {
         int distance = 5000;
         Creature target = null;
         Iterator var7 = team.getMateList().iterator();

         while(var7.hasNext()) {
            Teammate mate = (Teammate)var7.next();
            Player p = mate.getPlayer();
            if (p != null && p.getMap().equals(owner.getMap()) && !p.getBuffManager().hasBuff(this.buffID)) {
               int tmpDistance = MathUtil.getDistance(p.getPosition(), owner.getPosition());
               if (tmpDistance <= 5000 && distance > tmpDistance) {
                  distance = tmpDistance;
                  target = p;
               }
            }
         }

         return target;
      } else {
         return !owner.getBuffManager().hasBuff(this.buffID) ? owner : null;
      }
   }

   public int getBuffID() {
      return this.buffID;
   }

   public int getBuffLevel() {
      return this.buffLevel;
   }

   public boolean isLevelBySkill() {
      return this.levelBySkill;
   }

   public void initCheck(String des) throws Exception {
      if (BuffModel.getModel(this.buffID) == null || BuffDynamicData.getDyData(this.buffID, this.buffLevel) == null) {
         throw new Exception(des + "-获得buff-buff不存在或者buff动态数据不存在");
      }
   }
}
