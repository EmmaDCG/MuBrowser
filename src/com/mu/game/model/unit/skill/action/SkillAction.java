package com.mu.game.model.unit.skill.action;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.attack.AttackResult;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.skills.GroupSkill;
import com.mu.game.model.unit.skill.skills.SingleSkill;
import java.awt.Point;
import java.util.HashMap;

public abstract class SkillAction {
   public int type;

   public SkillAction(int type) {
      this.type = type;
   }

   public abstract int preCheck(Skill var1, Creature var2, Point var3);

   public abstract void singleAction(SingleSkill var1, Creature var2, AttackResult var3, Point var4);

   public abstract void groupAction(GroupSkill var1, HashMap var2, Point var3);

   public Creature useTargetInHang(Player owner, boolean assistedMode) {
      return null;
   }

   public abstract void initCheck(String var1) throws Exception;

   public int getType() {
      return this.type;
   }
}
