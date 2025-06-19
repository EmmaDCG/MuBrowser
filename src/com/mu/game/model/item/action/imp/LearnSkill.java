package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.manager.SkillService;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.io.game.packet.imp.skill.UpdateSkill;

public class LearnSkill extends ItemAction {
   private int skillID;

   public LearnSkill(int skillID) {
      this.skillID = skillID;
   }

   public int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = SkillService.learnSkill(player, this.skillID, 1);
      if (result == 1) {
         UpdateSkill.sendToClient(player, player.getSkillManager().getSkill(this.skillID));
         com.mu.io.game.packet.imp.skill.LearnSkill.afterLearn(player, player.getSkillManager().getSkill(this.skillID));
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      Skill skill = player.getSkillManager().getSkill(this.skillID);
      if (skill == null) {
         return 8007;
      } else {
         return skill.getLevel() > 0 ? 8037 : SkillService.validateConditions(player, this.skillID, 1);
      }
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public int getSkillID() {
      return this.skillID;
   }

   public void initCheck(String des) throws Exception {
      if (!SkillModel.hasModel(this.skillID)) {
         throw new Exception(des + ": 激活技能，技能模板不存在 ");
      }
   }

   public boolean obtainFunctionTip(Player player, ItemModel model) {
      if (model.getProfession().size() > 0 && !model.getProfession().contains(player.getProfessionID())) {
         return false;
      } else {
         return player.getSkillManager().getSkillLevel(this.skillID) <= 0;
      }
   }
}
