package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.io.game.packet.imp.skill.RequestPassiveSkill;

public class AddSkillPassiveConsume extends ItemAction {
   private int skillID;
   private int value;

   public AddSkillPassiveConsume(int skillID, int value) {
      this.skillID = skillID;
      this.value = value;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      Skill skill = player.getSkillManager().getSkill(this.getSkillID());
      int actualCount = calCanUseCount(this.getRemainUseCount(player, item.getModelID()), useNum, this.getValue(), Integer.MAX_VALUE - skill.getPassiveConsume());
      int value = actualCount * this.getValue();
      int result = player.getItemManager().deleteItem(item, actualCount, 20).getResult();
      if (result == 1) {
         skill.addPassiveConsume(value);
         RequestPassiveSkill.sendClient(player, skill);
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      Skill skill = player.getSkillManager().getSkill(this.getSkillID());
      return skill != null && skill.getLevel() >= 1 ? 1 : 8008;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
      if (!SkillModel.hasModel(this.skillID)) {
         throw new Exception(des + "-添加被动技能消耗  没有技能模板 " + this.skillID);
      } else if (this.value < 0) {
         throw new Exception(des + "-添加被动技能消耗 ,数值填写不正确");
      }
   }

   public int getSkillID() {
      return this.skillID;
   }

   public void setSkillID(int skillID) {
      this.skillID = skillID;
   }

   public int getValue() {
      return this.value;
   }

   public void setValue(int value) {
      this.value = value;
   }
}
