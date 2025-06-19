package com.mu.game.model.unit.skill.action;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.unit.skill.action.impl.S_Buff_Action;
import com.mu.game.model.unit.skill.action.impl.S_SprintAction;
import com.mu.game.model.unit.skill.action.impl.S_TeleportAction;
import com.mu.game.model.unit.skill.action.impl.S_Treat_Action;

public class SkillActionFactory {
   public static final int Action_Buff = 1;
   public static final int Action_Teleport = 2;
   public static final int Action_Treat = 3;
   public static final int Action_Sprint = 4;
   public static final int Action_ContinuousAttack = 5;

   public static SkillAction createAction(String typeStr, String value) throws Exception {
      if (typeStr != null && typeStr.length() >= 1) {
         int type = Integer.parseInt(typeStr);
         switch(type) {
         case 1:
            return getBuffAction(type, value);
         case 2:
            return getTeleportAction(type, value);
         case 3:
            return getTreatAction(type, value);
         case 4:
            return getSprintAction(type, value);
         default:
            System.err.println("没有获得相应的SkillAction " + type);
            return null;
         }
      } else {
         return null;
      }
   }

   private static S_Buff_Action getBuffAction(int type, String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 3) {
         throw new Exception("技能Action - 添加buff type=" + type + ",value=" + value);
      } else {
         int buffID = Integer.parseInt(splits[0]);
         int buffLevel = Integer.parseInt(splits[1]);
         int levelBySkill = Integer.parseInt(splits[2]);
         S_Buff_Action action = new S_Buff_Action(type, buffID, buffLevel, levelBySkill == 1);
         return action;
      }
   }

   private static S_TeleportAction getTeleportAction(int type, String value) throws Exception {
      return new S_TeleportAction(type);
   }

   private static S_SprintAction getSprintAction(int type, String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 3) {
         throw new Exception("技能Action - 冲锋 type=" + type + ",value=" + value);
      } else {
         int buffID = Integer.parseInt(splits[0]);
         int buffLevel = Integer.parseInt(splits[1]);
         int levelBySkill = Integer.parseInt(splits[2]);
         S_SprintAction action = new S_SprintAction(type, buffID, buffLevel, levelBySkill == 1);
         return action;
      }
   }

   private static S_Treat_Action getTreatAction(int type, String value) throws Exception {
      String[] splits = value.split(",");
      if (splits.length < 4) {
         throw new Exception("技能Action - 治疗 type=" + type + ",value=" + value + ",数据有错");
      } else {
         int hpValue = Integer.parseInt(splits[0]);
         boolean percent = Integer.parseInt(splits[1]) == 1;
         StatEnum stat = StatEnum.find(Integer.parseInt(splits[2]));
         int statAddition = Integer.parseInt(splits[3]);
         if (hpValue < 1) {
            throw new Exception("技能Action - 治疗,数值有错");
         } else {
            S_Treat_Action action = new S_Treat_Action(type, hpValue, percent, stat, statAddition);
            return action;
         }
      }
   }
}
