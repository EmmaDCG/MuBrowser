package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.buff.BuffManager;
import com.mu.game.model.unit.buff.model.BuffModel;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import java.util.List;

public class UseBuffEvent extends Event {
   private int buffID;
   private int level;
   private int count;

   public UseBuffEvent(Creature owner, int buffID, int level, Object... objs) {
      super(owner);
      this.buffID = buffID;
      this.level = level;
      if (objs != null && objs.length > 0) {
         this.count = ((Integer)objs[0]).intValue();
      }

   }

   public void work(long now) throws Exception {
      BuffModel model = BuffModel.getModel(this.buffID);
      BuffManager buffManager = ((Creature)this.getOwner()).getBuffManager();
      if (model != null && buffManager != null) {
         List addtions = null;
         buffManager.createAndStartBuff((Creature)this.getOwner(), this.buffID, this.level, true, 0L, (List)addtions);
         this.setEnd(true);
      } else {
         this.setEnd(true);
      }
   }

   public Status getStatus() {
      return Status.NONE;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
