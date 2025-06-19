package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.mall.PopText;
import com.mu.utils.Rnd;
import java.util.ArrayList;

public class PopTextEvent extends Event {
   private static final int rate = 10;

   public PopTextEvent(Creature owner) {
      super(owner);
      this.checkrate = 2000;
   }

   public void work(long now) throws Exception {
      int tmp = Rnd.get(1000);
      if (tmp < 10) {
         ArrayList list = ((Creature)this.getOwner()).getTemplate().getPopTextList();
         if (list != null && list.size() > 0) {
            String str = (String)list.get(Rnd.get(list.size()));
            PopText.pop((Creature)this.getOwner(), str);
         }
      }

   }

   public Status getStatus() {
      return Status.POPTEXT;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
