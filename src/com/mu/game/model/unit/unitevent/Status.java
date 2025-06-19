package com.mu.game.model.unit.unitevent;

public enum Status {
   NONE(0),
   ATTACK(1),
   MOVE(2),
   DEATH(3),
   USESKILL(4),
   AI(14),
   WAITREFRESH(15),
   CountDown(16),
   Hangset(17),
   Recovery(18),
   AutoAssSkill(19),
   Sprint(20),
   RobotAi(21),
   TRANSACTION(100),
   HEARTBEAT(101),
   ONLINECOUNT(102),
   TASK(103),
   Action(104),
   VIP(105),
   SAFEREVIVAL(106),
   PET(108),
   POPTEXT(109),
   LEAVEGEAR(110),
   AutoUseItem(111);

   private int identify;

   private Status(int identify) {
      this.identify = identify;
   }

   public int getIdentify() {
      return this.identify;
   }

   public void setIdentify(int identify) {
      this.identify = identify;
   }
}
