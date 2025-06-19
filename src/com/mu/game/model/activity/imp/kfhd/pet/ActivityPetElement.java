package com.mu.game.model.activity.imp.kfhd.pet;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.pet.PlayerPetManager;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityPetElement extends ActivityElement {
   public ActivityPetElement(int id, ActivityPet father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeByte(this.getNumerical());
      packet.writeShort(this.hasServerLimit() ? this.getServerLeft() : -1);
      ArrayList list = this.getRewardList();
      packet.writeByte(list.size());
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         GetItemStats.writeItem(item, packet);
      }

      packet.writeByte(this.getReceiveStatus(player));
   }

   public boolean canReceive(Player player, boolean notice) {
      PlayerPetManager pm = player.getPetManager();
      return pm.isOpen() && pm.getRank().getRank() >= this.getNumerical() ? super.canReceive(player, notice) : false;
   }

   public int getReceiveStatus(Player player) {
      PlayerPetManager pm = player.getPetManager();
      if (pm.isOpen() && pm.getRank().getRank() >= this.getNumerical()) {
         return this.receiveOverload(player) ? 2 : 1;
      } else {
         return 0;
      }
   }
}
