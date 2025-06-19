package com.mu.game.model.unit.unitevent;

public enum OperationEnum {
   IDLE(100),
   NONE(0),
   MOVE(1),
   ATTACK(2),
   USEITEM(3),
   USESKILL(5),
   GATHENING(6),
   COUNTDOWN(7),
   TRANSACTION(8),
   CONVEY(9),
   Sprint(10);

   private int identify;

   private OperationEnum(int identify) {
      this.identify = identify;
   }

   public int getIdentify() {
      return this.identify;
   }

   public void setIdentify(int identify) {
      this.identify = identify;
   }
}
