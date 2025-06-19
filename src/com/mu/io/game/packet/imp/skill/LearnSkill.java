package com.mu.io.game.packet.imp.skill;

import com.mu.game.model.guide.arrow.ArrowGuideManager;
import com.mu.game.model.task.target.TargetType;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.manager.SkillService;
import com.mu.game.model.unit.skill.model.SkillModel;
import com.mu.io.game.packet.ReadAndWritePacket;
import com.mu.io.game.packet.imp.player.tips.SystemFunctionTip;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class LearnSkill extends ReadAndWritePacket {
   public LearnSkill(int code, byte[] readBuf) {
      super(code, readBuf);
   }

   public void process() throws Exception {
      Player player = this.getPlayer();
      int skillID = this.readInt();
      int level = 0;
      int result = learnSkill(player, skillID);
      if (result == 1) {
         level = player.getSkillManager().getSkillLevel(skillID);
      }

      this.writeInt(skillID);
      this.writeShort(level);
      this.writeBoolean(result == 1);
      player.writePacket(this);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public static int learnSkill(Player player, int skillID) {
      int currentLevel = player.getSkillManager().getSkillLevel(skillID);
      int level = currentLevel + 1;
      int result = SkillService.validateConditions(player, skillID, level);
      if (result == 1) {
         result = SkillService.learnSkill(player, skillID, level);
      }

      if (result == 1) {
         UpdateSkill.sendToClient(player, player.getSkillManager().getSkill(skillID));
         afterLearn(player, player.getSkillManager().getSkill(skillID));
      }

      return result;
   }

   public static void afterLearn(Player player, Skill skill) {
      if (skill.getModel().isPassive()) {
         SkillModel model = SkillModel.getModel(skill.getSkillID());
         if (model.getMaxLevel() != skill.getLevel()) {
            RequestPassiveSkill.sendClient(player, skill);
         }

      } else {
         if (skill.getLevel() == 1) {
            player.getGameHang().addSkillWhenLevelUp(skill.getSkillID());
            SystemFunctionTip.sendToClient(player, 3, skill.getSkillID());
            ArrowGuideManager.pushArrow(player, 3, (String)null);
         }

         player.getTaskManager().onEventCheckMoreSpecify(TargetType.MoreSpecifyType.SKILL);
      }
   }
}
