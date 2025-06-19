package com.mu.game.model.activity.imp.daypay;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class DayPayElement extends ActivityElement {
   private int ingot;
   private HashMap dayItemMap = new HashMap();
   private HashMap dayUnitMap = new HashMap();

   public DayPayElement(int id, DayPayActivity father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      ArrayList list = this.getItemList(player);
      packet.writeShort(this.getId());
      packet.writeInt(this.getIngot());
      packet.writeByte(list.size());
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         GetItemStats.writeItem(item, packet);
      }

      packet.writeByte(this.getReceiveStatus(player));
   }

   public int getIngot() {
      return this.ingot;
   }

   public void initReward(int day, String str) throws Exception {
      String[] pStr = str.split("#");
      HashMap itemMap = new HashMap();
      HashMap unitMap = new HashMap();
      this.dayItemMap.put(day, itemMap);
      this.dayUnitMap.put(day, unitMap);

      for(int j = 0; j < pStr.length; ++j) {
         String[] tmp = pStr[j].split(":");
         int profession = Integer.parseInt(tmp[0]);
         itemMap.put(profession, Tools.parseItemList(tmp[1]));
         unitMap.put(profession, Tools.parseItemDataUnitList(tmp[1]));
      }

   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public ArrayList getItemUnitList(Player player) {
      int day = ((DayPayActivity)this.getFather()).getCurrentDay();
      HashMap map = (HashMap)this.dayUnitMap.get(day);
      return (ArrayList)map.get(player.getProType());
   }

   public ArrayList getItemList(Player player) {
      int day = ((DayPayActivity)this.getFather()).getCurrentDay();
      HashMap map = (HashMap)this.dayItemMap.get(day);
      return (ArrayList)map.get(player.getProType());
   }

   public boolean canReceive(Player player, boolean notice) {
      if (!((DayPayActivity)this.getFather()).isOpen()) {
         return false;
      } else {
         return this.getReceiveStatus(player) == 1;
      }
   }

   public int getReceiveType() {
      return 4;
   }

   public int getReceiveStatus(Player player) {
      int todayPay = player.getUser().getPay(Time.getDayLong());
      if (todayPay < this.ingot) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }
}
