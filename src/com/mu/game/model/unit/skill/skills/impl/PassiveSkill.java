package com.mu.game.model.unit.skill.skills.impl;

import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatChange;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.game.model.stats.statId.StatIdCreator;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.model.BuffDynamicData;
import com.mu.game.model.unit.buff.model.BuffProps;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.levelData.SkillLevelData;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PassiveSkill extends Skill {
   public PassiveSkill(int skillID, int level, Creature owner) {
      super(skillID, level, owner);
   }

   public int preCastCheck(Point centPoint, Creature effected) {
      return 1;
   }

   protected void startEffect(Point centPoint, Creature effected) {
      BuffDynamicData dyData = BuffDynamicData.getDyData(this.getSkillID(), this.getLevel());
      if (dyData != null && dyData.getProps() != null) {
         List modifies = new ArrayList();
         Iterator var6 = dyData.getProps().iterator();

         while(var6.hasNext()) {
            BuffProps prop = (BuffProps)var6.next();
            FinalModify modify = new FinalModify(prop.getStat(), prop.getValue(), prop.getPriority());
            modifies.add(modify);
         }

         SkillLevelData levelData = SkillLevelData.getLevelData(this.getSkillID(), this.getLevel());
         if (levelData != null) {
            modifies.add(new FinalModify(StatEnum.DOMINEERING, levelData.getDomineering(), StatModifyPriority.ADD));
         }

         StatChange.addStat(this.getOwner(), StatIdCreator.createSkillStatId(this.getSkillID()), modifies, StatChange.isSendStat(this.getOwner()));
      }

   }

   public boolean isSprint() {
      return false;
   }
}
