package com.mu.game.model.activity.imp.vipgift;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.vip.VIPData;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VipGiftElement extends ActivityElement implements Comparable {
   private static final int Vip_BaiYin = 1;
   private static final int Vip_ZuanShi = 2;
   private static final int Vip_Level = 3;
   private static final int Vip_HuanJin = 4;
   private int vipType = 1;
   private int sort = 1;
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public VipGiftElement(int id, VipGiftActivity father) {
      super(id, father);
   }

   public int getSort() {
      return this.sort;
   }

   public void setSort(int sort) {
      this.sort = sort;
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeBoolean(this.getVipType() == 1);
      packet.writeBoolean(this.getVipType() == 4);
      packet.writeBoolean(this.getVipType() == 2);
      packet.writeByte(this.getNumerical());
      ArrayList list = this.getItemList(player.getProType());
      packet.writeByte(list.size());
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         GetItemStats.writeItem(item, packet);
      }

      packet.writeByte(this.getReceiveStatus(player));
   }

   public void addItemList(int pro, ArrayList list) {
      this.itemMap.put(pro, list);
   }

   public void addItemUnitList(int pro, ArrayList list) {
      this.unitMap.put(pro, list);
   }

   public ArrayList getItemList(int pro) {
      return (ArrayList)this.itemMap.get(pro);
   }

   public ArrayList getItemDataList(int pro) {
      return (ArrayList)this.unitMap.get(pro);
   }

   public ArrayList getItemUnitList(Player player) {
      int pro = player.getProType();
      return this.getItemDataList(pro);
   }

   public int getVipType() {
      return this.vipType;
   }

   public void setVipType(int vipType) {
      this.vipType = vipType;
   }

   public boolean canReceive(Player player, boolean notice) {
      boolean b = super.canReceive(player, notice);
      if (b) {
         b = this.getReceiveStatus(player) == 1;
      }

      return b;
   }

   public int getReceiveType() {
      return 1;
   }

   public int getReceiveStatus(Player player) {
      int vipShowLevel = player.getVipShowLevel();
      if (vipShowLevel < 1) {
         return 0;
      } else if (this.receiveOverload(player)) {
         return 2;
      } else {
         VIPData vd = player.getVIPManager().getData();
         if (vd == null) {
            return 0;
         } else if (this.vipType == 1) {
            return vd.getId() >= 1 && vd.getId() <= 3 ? 1 : 0;
         } else if (this.vipType == 2) {
            return player.getVIPManager().getData().getId() == 1 ? 1 : 0;
         } else if (this.vipType == 4) {
            return player.getVIPManager().getData().getId() <= 2 ? 1 : 0;
         } else {
            return vipShowLevel >= this.getNumerical() ? 1 : 0;
         }
      }
   }

   public int compareTo(VipGiftElement o) {
      return this.sort - o.sort;
   }

   @Override
   public int compareTo(Object o) {
      return 0;
   }
}
