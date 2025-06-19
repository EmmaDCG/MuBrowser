package com.mu.game.model.unit.unitevent.imp;

import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;

public class PetRiseEvent extends Event {
   private boolean autoBuy;
   private boolean autoRise;

   public PetRiseEvent(Player owner, boolean autoBuy, boolean autoRise) {
      super(owner);
      this.checkrate = 1000;
      this.autoBuy = autoBuy;
      this.autoRise = autoRise;
   }

   public void work(long now) throws Exception {
      Player player = (Player)this.getOwner();
      PlayerPetManager ppm = null;
      if (player != null && (ppm = player.getPetManager()) != null) {
         if (!ppm.riseOperate(this.autoBuy, this.autoRise) || !this.autoRise) {
            this.setEnd(true);
         }

      } else {
         this.setEnd(false);
      }
   }

   public Status getStatus() {
      return Status.PET;
   }

   public OperationEnum getOperationEnum() {
      return OperationEnum.NONE;
   }
}
