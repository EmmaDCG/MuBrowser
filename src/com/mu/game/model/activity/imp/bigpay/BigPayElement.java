package com.mu.game.model.activity.imp.bigpay;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.PayInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class BigPayElement extends ActivityElement {
   private int ingot = 50;
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public BigPayElement(int id, BigPayActivity father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeInt(this.ingot);
      int canReceiveTimes = this.getCanReceiveTimes(player);
      packet.writeShort(this.getLeftChance(player));
      ArrayList list = this.getItemList(player.getProType());
      packet.writeByte(list.size());
      Iterator var6 = list.iterator();

      while(var6.hasNext()) {
         Item item = (Item)var6.next();
         GetItemStats.writeItem(item, packet);
      }

      packet.writeByte(this.getReceiveStatus(player, canReceiveTimes));
   }

   public int getLeftChance(Player player) {
      int tmp = this.getMaxReceiveTimes() - this.getReceiveTimes(player);
      return tmp < 0 ? 0 : tmp;
   }

   public int getIncidentallyNumber(Player player) {
      return this.getLeftChance(player);
   }

   public int getCanReceiveTimes(Player player) {
      int num = 0;
      long begin = ((BigPayActivity)this.getFather()).getOpenDate().getTime();
      long end = ((BigPayActivity)this.getFather()).getCloseDate().getTime();
      CopyOnWriteArrayList list = player.getUser().getPayList();
      Iterator var9 = list.iterator();

      while(var9.hasNext()) {
         PayInfo info = (PayInfo)var9.next();
         long payTime = info.getPayTime();
         if (payTime >= begin && payTime <= end) {
            num += info.getIngot() / this.ingot;
         }
      }

      int maxNum = Math.min(num, this.getMaxReceiveTimes());
      int tmp = maxNum - this.getReceiveTimes(player);
      return tmp < 0 ? 0 : tmp;
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

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public int getReceiveType() {
      return 3;
   }

   public boolean canReceive(Player player, boolean notice) {
      if (!((BigPayActivity)this.getFather()).isOpen()) {
         return false;
      } else {
         return this.getReceiveStatus(player) == 1;
      }
   }

   public int getReceiveStatus(Player player) {
      int canReceiveTimes = this.getCanReceiveTimes(player);
      if (canReceiveTimes > 0) {
         return 1;
      } else {
         return this.receiveOverload(player) ? 2 : 0;
      }
   }

   public int getReceiveStatus(Player player, int canReceiveTimes) {
      if (canReceiveTimes > 0) {
         return 1;
      } else {
         return this.receiveOverload(player) ? 2 : 0;
      }
   }
}
