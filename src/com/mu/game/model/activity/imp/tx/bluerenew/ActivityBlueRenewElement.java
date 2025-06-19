package com.mu.game.model.activity.imp.tx.bluerenew;

import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import jxl.Sheet;

public class ActivityBlueRenewElement extends ActivityElement {
   private HashMap itemMap = new HashMap();
   private HashMap unitMap = new HashMap();

   public ActivityBlueRenewElement(int id, ActivityBlueRenew father) {
      super(id, father);
   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      ArrayList list = this.getItemList(player.getProType());
      packet.writeByte(list.size());
      Iterator var5 = list.iterator();

      while(var5.hasNext()) {
         Item item = (Item)var5.next();
         GetItemStats.writeItem(item, packet);
      }

   }

   public void init(Sheet sheet, int index) throws Exception {
      String itemStr = Tools.getCellValue(sheet.getCell("B" + index));
      String[] pStr = itemStr.split("#");

      for(int i = 0; i < pStr.length; ++i) {
         String[] tmp = pStr[i].split(":");
         int profession = Integer.parseInt(tmp[0]);
         this.addItemList(profession, Tools.parseItemList(tmp[1]));
         this.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
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

   public boolean doReceive(Player player) {
      return false;
   }

   public synchronized boolean receive(Player player) {
      return false;
   }

   public int getReceiveStatus(Player player) {
      if (!((ActivityBlueRenew)this.getFather()).hasPlayer(player.getID())) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }

   public int getReceiveType() {
      return 3;
   }
}
