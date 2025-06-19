package com.mu.game.model.unit.skill.condition;

import com.mu.game.model.unit.skill.consume.AGConsume;
import com.mu.game.model.unit.skill.consume.Consume;
import com.mu.game.model.unit.skill.consume.HpConsume;
import com.mu.game.model.unit.skill.consume.MpConsume;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConditionManager {
   private static HashMap useConditions = new HashMap();
   private static HashMap consumes = new HashMap();

   public static int createKey(int skillID, int level) {
      return skillID * 10000 + level;
   }

   public static void addUseCondition(int skillID, int level, Condition condition) {
      if (condition != null) {
         int key = createKey(skillID, level);
         HashMap conditions = (HashMap)useConditions.get(key);
         if (conditions == null) {
            conditions = new HashMap();
            useConditions.put(key, conditions);
         }

         conditions.put(condition.getType(), condition);
      }
   }

   public static void addConsume(int skillID, int level, Consume consume) {
      if (consume != null) {
         int key = createKey(skillID, level);
         List cons = (List)consumes.get(key);
         if (cons == null) {
            cons = new ArrayList();
            consumes.put(key, cons);
         }

         ((List)cons).add(consume);
      }
   }

   public static HashMap getUseCondition(int skillID, int level) {
      int key = createKey(skillID, level);
      if (useConditions.containsKey(key)) {
         return (HashMap)useConditions.get(key);
      } else {
         key = createKey(skillID, 1);
         return (HashMap)useConditions.get(key);
      }
   }

   public static List getConsume(int skillID, int level) {
      int key = createKey(skillID, level);
      return (List)consumes.get(key);
   }

   public static Condition createCondition(int type, Object... objs) throws Exception {
      Condition condition = null;
      switch(type) {
      case 1:
         condition = new HpCondition(((Integer)objs[0]).intValue());
         break;
      case 2:
         condition = new MpCondition(((Integer)objs[0]).intValue());
         break;
      case 3:
         condition = new AGCondition(((Integer)objs[0]).intValue());
      case 4:
      case 6:
      default:
         break;
      case 5:
         condition = new StatusCondition(((Integer)objs[0]).intValue());
         break;
      case 7:
         condition = new SiegeCondition();
      }

      return (Condition)condition;
   }

   public static Consume createConsume(int type, Object... objs) {
      Consume consume = null;
      switch(type) {
      case 1:
         consume = new HpConsume(((Integer)objs[0]).intValue());
         break;
      case 2:
         consume = new MpConsume(((Integer)objs[0]).intValue());
         break;
      case 3:
         consume = new AGConsume(((Integer)objs[0]).intValue());
      }

      return (Consume)consume;
   }
}
