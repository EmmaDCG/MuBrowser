package com.mu.game.model.activity.imp.tehui;

import com.mu.db.log.IngotChangeType;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.PlayerManager;
import com.mu.game.model.unit.player.popup.imp.TeHuiPopup;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.activity.ActivityReceive;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.io.game.packet.imp.player.pop.ShowPopup;
import com.mu.io.game.packet.imp.sys.SystemMessage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class ActivityTeHuiElement extends ActivityElement {
   private int ingot = 1;
   private String des = "";
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public ActivityTeHuiElement(int id, ActivityTeHui father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeUTF(this.getDes());
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

   public String getDes() {
      return this.des;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public synchronized boolean receive(Player player) {
      boolean b = super.receive(player);
      if (b) {
         PlayerManager.reduceIngot(player, this.getIngot(), IngotChangeType.TeHui, this.getId() + "(" + this.getName() + ")");
      }

      try {
         ActivityReceive ar = new ActivityReceive();
         ar.writeShort(this.getId());
         ar.writeBoolean(b);
         ar.writeByte(this.getReceiveStatus(player));
         ar.writeInt(this.getIncidentallyNumber(player));
         player.writePacket(ar);
         ar.destroy();
         ar = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return b;
   }

   public boolean canReceive(Player player, boolean notice) {
      boolean b = super.canReceive(player, notice);
      if (b && player.getIngot() < this.getIngot() && notice) {
         SystemMessage.writeMessage(player, 1015);
         b = false;
      }

      return b;
   }

   public int getReceiveType() {
      return 3;
   }

   public boolean doReceive(Player player) {
      TeHuiPopup pop = new TeHuiPopup(player.createPopupID(), this.getId());
      ShowPopup.open(player, pop);
      return true;
   }

   public int getReceiveStatus(Player player) {
      return this.receiveOverload(player) ? 2 : 1;
   }
}
