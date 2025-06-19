package com.mu.game.model.unit.skill.manager;

import com.mu.game.model.packet.SkillPacketService;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.skill.Skill;
import com.mu.game.model.unit.skill.SkillManager;
import com.mu.game.model.unit.skill.learnCondition.LearnCondition;
import com.mu.game.model.unit.skill.learnCondition.LearnManager;
import com.mu.game.model.unit.skill.learnConsume.LearnConsume;
import com.mu.game.model.unit.skill.model.SkillModel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SkillService {
   private static Logger logger = LoggerFactory.getLogger(SkillService.class);

   public static int learnSkill(Player player, int skillID, int level) {
      SkillModel model = SkillModel.getModel(skillID);
      if (model == null) {
         logger.debug("技能不存在 " + skillID);
         return 8007;
      } else {
         SkillManager manager = player.getSkillManager();
         int currentLevel = manager.getSkillLevel(skillID);
         if (currentLevel >= level) {
            return 8012;
         } else {
            int result = manager.learnSkill(skillID);
            if (result != 1) {
               return result;
            } else {
               Skill skill = manager.getSkill(skillID);
               skill.getLevel();
               SkillPacketService.noticeGatewayUpdateSkill(player, skill, 1);
               List consumes = LearnManager.getLearnConsume(skillID, level);
               if (consumes != null) {
                  Iterator var10 = consumes.iterator();

                  while(var10.hasNext()) {
                     LearnConsume consume = (LearnConsume)var10.next();
                     consume.consumed(player);
                  }
               }

               return result;
            }
         }
      }
   }

   public static int validateConditions(Player player, int skillID, int level) {
      SkillModel model = SkillModel.getModel(skillID);
      if (model == null) {
         return 8007;
      } else {
         Skill skill = player.getSkillManager().getSkill(skillID);
         if (skill == null && level > 0) {
            return 8007;
         } else {
            if (level > 1) {
               if (skill.getLevel() + 1 < level) {
                  return 8010;
               }

               if (!skill.isReachMaxPassiveConsume()) {
                  return 8043;
               }
            }

            if (skill != null && skill.getLevel() >= model.getMaxLevel()) {
               return 8011;
            } else {
               HashMap conditions = LearnManager.getLearnCondition(skillID, level);
               if (conditions != null) {
                  Iterator var8 = conditions.values().iterator();

                  while(var8.hasNext()) {
                     LearnCondition condition = (LearnCondition)var8.next();
                     int result = condition.verify(player);
                     if (result != 1) {
                        return result;
                     }
                  }
               }

               return 1;
            }
         }
      }
   }
}
