package com.mu.game.model.activity.imp.kfhd.euip;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityEquipElement extends ActivityElement {
   private String des;

   public ActivityEquipElement(int id, ActivityEquip father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeUTF(this.getDes());
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
      if (this.receiveOverload(player)) {
         return false;
      } else {
         return !player.getEquipment().hasASetByLevel(this.getNumerical()) ? false : super.canReceive(player, notice);
      }
   }

   public int getReceiveType() {
      return 1;
   }

   public int getReceiveStatus(Player player) {
      if (this.receiveOverload(player)) {
         return 2;
      } else {
         return !player.getEquipment().hasASetByLevel(this.getNumerical()) ? 0 : 1;
      }
   }

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }
}
