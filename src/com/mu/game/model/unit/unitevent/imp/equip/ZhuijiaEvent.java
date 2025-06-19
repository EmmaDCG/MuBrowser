package com.mu.game.model.unit.unitevent.imp.equip;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.unitevent.Event;
import com.mu.game.model.unit.unitevent.OperationEnum;
import com.mu.game.model.unit.unitevent.Status;
import com.mu.io.game.packet.imp.equip.ZhuijiaEquipment;

public class ZhuijiaEvent extends Event {
   private Item item = null;
   private boolean useIngot;
   private boolean defaultBind;

   public ZhuijiaEvent(Player owner, Item item, boolean useIngot, boolean defaultBind) {
      super(owner);
      this.item = item;
      this.useIngot = useIngot;
      this.defaultBind = defaultBind;
      this.checkrate = 1000;
   }

   public void work(long now) throws Exception {
      int[] results = ZhuijiaEquipment.canZhuijia((Player)this.getOwner(), this.item, this.useIngot);
      int result = results[0];
      if (result == 1) {
         ZhuijiaEquipment.doZhuijia((Player)this.getOwner(), this.item, results[1], this.defaultBind);
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
