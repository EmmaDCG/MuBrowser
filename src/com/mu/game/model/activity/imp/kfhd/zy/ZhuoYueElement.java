package com.mu.game.model.activity.imp.kfhd.zy;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ZhuoYueElement extends ActivityElement {
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public ZhuoYueElement(int id, ZhuoYueActivity father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeByte(this.getNumerical());
      packet.writeShort(this.hasServerLimit() ? this.getServerLeft() : -1);
      ArrayList list = this.getItemList(player.getProType());
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
         return player.getEquipment().getTotalExcellentCount() < this.getNumerical() ? false : super.canReceive(player, notice);
      }
   }

   public ArrayList getItemList(int pro) {
      return (ArrayList)this.itemMap.get(pro);
   }

   public ArrayList getItemDataList(int pro) {
      return (ArrayList)this.unitMap.get(pro);
   }

   public void addItemList(int pro, ArrayList list) {
      this.itemMap.put(pro, list);
   }

   public void addItemUnitList(int pro, ArrayList list) {
      this.unitMap.put(pro, list);
   }

   public ArrayList getItemUnitList(Player player) {
      int pro = player.getProType();
      return this.getItemDataList(pro);
   }

   public int getReceiveType() {
      return 1;
   }

   public int getReceiveStatus(Player player) {
      if (this.receiveOverload(player)) {
         return 2;
      } else {
         return player.getEquipment().getTotalExcellentCount() < this.getNumerical() ? 0 : 1;
      }
   }
}
