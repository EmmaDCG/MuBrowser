package com.mu.game.model.unit.skill.learnCondition;

import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.skill.condition.ConditionManager;
import com.mu.game.model.unit.skill.learnConsume.ItemLearnConsume;
import com.mu.game.model.unit.skill.learnConsume.LearnConsume;
import com.mu.game.model.unit.skill.learnConsume.MoneyLearnConsume;
import com.mu.game.model.unit.skill.model.SkillModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LearnManager {
   private static Logger logger = LoggerFactory.getLogger(LearnManager.class);
   private static HashMap learnConditions = new HashMap();
   private static HashMap learnConsumes = new HashMap();

   public static void addLearnCondition(int skillID, int level, LearnCondition condition) {
      if (condition != null) {
         int key = ConditionManager.createKey(skillID, level);
         HashMap conditions = (HashMap)learnConditions.get(key);
         if (conditions == null) {
            conditions = new HashMap();
            learnConditions.put(key, conditions);
         }

         conditions.put(condition.getType(), condition);
      }
   }

   public static void addLearnConsume(int skillID, int level, LearnConsume consume) {
      if (consume != null) {
         int key = ConditionManager.createKey(skillID, level);
         List consumeList = (List)learnConsumes.get(key);
         if (consumeList == null) {
            consumeList = new ArrayList();
            learnConsumes.put(key, consumeList);
         }

         ((List)consumeList).add(consume);
      }
   }

   public static HashMap getLearnCondition(int skillID, int level) {
      int key = ConditionManager.createKey(skillID, level);
      return (HashMap)learnConditions.get(key);
   }

   public static List getLearnConsume(int skillID, int level) {
      int key = ConditionManager.createKey(skillID, level);
      return (List)learnConsumes.get(key);
   }

   public static LearnCondition createLearnCondition(int type, Object... objs) throws Exception {
      LearnCondition condition = null;
      int fs;
      switch(type) {
      case 1:
         condition = new MoneyCondition(((Integer)objs[0]).intValue());
         break;
      case 2:
         fs = ((Integer)objs[0]).intValue();
         int count = ((Integer)objs[1]).intValue();
         condition = new ItemCondition(fs, count);
         break;
      case 3:
         String fstr = (String)objs[0];
         HashMap frontSkills = StringTools.analyzeIntegerMap(fstr, ",");
         Iterator var6 = frontSkills.keySet().iterator();

         while(var6.hasNext()) {
            fs = ((Integer)var6.next()).intValue();
            if (SkillModel.getModel(fs) == null) {
               logger.error("前置技能 不存在  字符串 = ", fstr);
            }
         }

         condition = new FrontSkillCodition(frontSkills);
         break;
      case 4:
         condition = new UserLevelCondition(((Integer)objs[0]).intValue());
      }

      return (LearnCondition)condition;
   }

   public static LearnConsume createConsume(int type, Object... objs) {
      LearnConsume consume = null;
      switch(type) {
      case 1:
         consume = new MoneyLearnConsume(((Integer)objs[0]).intValue());
         break;
      case 2:
         int itemID = ((Integer)objs[0]).intValue();
         int count = ((Integer)objs[1]).intValue();
         consume = new ItemLearnConsume(itemID, count);
      }

      return (LearnConsume)consume;
   }
}
