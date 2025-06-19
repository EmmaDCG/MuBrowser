package com.mu.game.model.unit.trigger;

public class EventParam {
   private int type;
   private Object[] args;

   public EventParam(int type, Object[] args) {
      this.type = type;
      this.args = args;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public Object[] getArgs() {
      return this.args;
   }

   public void setArgs(Object[] args) {
      this.args = args;
   }
}
