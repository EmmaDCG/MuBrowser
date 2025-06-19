package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.material.Material;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class RefreshEvent extends Event {
   private long begin = System.currentTimeMillis();

   public RefreshEvent(Material owner) {
      super(owner);
      this.checkrate = 500;
   }

   public Status getStatus() {
      return Status.WAITREFRESH;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }

   public void work(long now) throws Exception {
      if (now - this.begin >= (long)((Material)this.getOwner()).getRefreshTime()) {
         ((Material)this.getOwner()).refresh();
         ((Material)this.getOwner()).idle();
      }

   }
}
