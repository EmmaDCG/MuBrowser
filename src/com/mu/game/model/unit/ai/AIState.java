package com.mu.game.model.unit.ai;

public enum AIState {
   AS_STAND("站立"),
   AS_GOHOME("回家"),
   AS_PURSUE("追击"),
   AS_MOVE("移动"),
   AS_ATTACK("攻击"),
   AS_BEATTACK("被攻击"),
   AS_DEATH("死亡");

   private String name;

   private AIState(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }
}
