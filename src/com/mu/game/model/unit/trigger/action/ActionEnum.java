package com.mu.game.model.unit.trigger.action;

import java.util.HashMap;

public enum ActionEnum {
   None(0),
   Kill_Monster_Recover_HP(1),
   Kill_Monster_Recover_Mp(5),
   Kill_Monster_Recover_AG(15),
   Attack_Recover_Hp(21),
   Attack_Target_DeBuff(81),
   Attack_Traget_ReduceHP(85),
   Attack_EndBuff(91),
   Move_EndBuff(95),
   UseSkill_EndBuff(101),
   PK_EndBuff(105),
   Attack_BossEndBuff(111),
   BeAttack_Fanzhen(115);

   private static HashMap enums = new HashMap();
   private int id;

   public static void init() {
      ActionEnum[] var3;
      int var2 = (var3 = values()).length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ActionEnum ae = var3[var1];
         enums.put(ae.getId(), ae);
      }

   }

   public static ActionEnum find(int id) {
      return enums.containsKey(id) ? (ActionEnum)enums.get(id) : None;
   }

   private ActionEnum(int id) {
      this.id = id;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }
}
