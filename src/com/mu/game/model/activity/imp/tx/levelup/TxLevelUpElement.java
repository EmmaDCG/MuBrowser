package com.mu.game.model.activity.imp.tx.levelup;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityReceive;
import com.mu.io.game.packet.imp.item.GetItemStats;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TxLevelUpElement extends ActivityElement {
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public TxLevelUpElement(int id, TxLevelUpActivity father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeShort(this.getNumerical());
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

   public synchronized boolean receive(Player player) {
      boolean b = super.receive(player);

      try {
         ActivityReceive ar = new ActivityReceive();
         ar.writeShort(this.getId());
         ar.writeBoolean(b);
         ar.writeByte(this.getReceiveStatus(player));
         player.writePacket(ar);
         ar.destroy();
         ar = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return b;
   }

   public int getReceiveStatus(Player player) {
      if (player.getLevel() < this.getNumerical()) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }
}
