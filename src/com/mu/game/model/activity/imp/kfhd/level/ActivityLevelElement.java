package com.mu.game.model.activity.imp.kfhd.level;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityLevelElement extends ActivityElement {
   public ActivityLevelElement(int id, ActivityLevel father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeShort(this.getNumerical());
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
      return player.getLevel() < this.getNumerical() ? false : super.canReceive(player, notice);
   }

   public int getReceiveStatus(Player player) {
      if (player.getLevel() < this.getNumerical()) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }
}
