package com.mu.game.model.activity.imp.tx.bluevip;

import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.bluevip.BlueVip;
import com.mu.io.game.packet.WriteOnlyPacket;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Iterator;
import jxl.Sheet;

public class ActivityNormalBlueElement extends ActivityBlueVipElement {
   private int vipLevel;

   public ActivityNormalBlueElement(int id, ActivityBlueVip father) {
      super(id, father);
   }

   public int getVipLevel() {
      return this.vipLevel;
   }

   public void setVipLevel(int vipLevel) {
      this.vipLevel = vipLevel;
   }

   public void init(Sheet sheet, int index) throws Exception {
      int icon = Tools.getCellIntValue(sheet.getCell("B" + index));
      this.setIcon(icon);
      this.vipLevel = Tools.getCellIntValue(sheet.getCell("C" + index));
      String itemStr = Tools.getCellValue(sheet.getCell("D" + index));
      String[] pStr = itemStr.split("#");

      for(int i = 0; i < pStr.length; ++i) {
         String[] tmp = pStr[i].split(":");
         int profession = Integer.parseInt(tmp[0]);
         this.addItemList(profession, Tools.parseItemList(tmp[1]));
         this.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
      }

   }

   public void writeDetail(Player player, WriteOnlyPacket packet) throws Exception {
      packet.writeShort(this.getId());
      packet.writeShort(this.getIcon());
      packet.writeUTF(this.getTitle());
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
      BlueVip bv = player.getUser().getBlueVip();
      return bv.isVip() && bv.getBlue_vip_level() == this.vipLevel ? super.canReceive(player, notice) : false;
   }

   public synchronized boolean receive(Player player) {
      return false;
   }

   public boolean doReceive(Player player) {
      return false;
   }

   public int getReceiveStatus(Player player) {
      BlueVip bv = player.getUser().getBlueVip();
      if (bv.isVip() && bv.getBlue_vip_level() == this.vipLevel) {
         return this.receiveOverload(player) ? 2 : 1;
      } else {
         return 0;
      }
   }
}
