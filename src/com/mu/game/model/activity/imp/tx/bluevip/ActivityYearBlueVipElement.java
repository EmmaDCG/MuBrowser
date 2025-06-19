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

public class ActivityYearBlueVipElement extends ActivityBlueVipElement {
   public ActivityYearBlueVipElement(int id, ActivityBlueVip father) {
      super(id, father);
   }

   public void init(Sheet sheet, int index) throws Exception {
      int icon = Tools.getCellIntValue(sheet.getCell("B" + index));
      this.setIcon(icon);
      String itemStr = Tools.getCellValue(sheet.getCell("C" + index));
      String[] pStr = itemStr.split("#");

      for(int i = 0; i < pStr.length; ++i) {
         String[] tmp = pStr[i].split(":");
         int profession = Integer.parseInt(tmp[0]);
         this.addItemList(profession, Tools.parseItemList(tmp[1]));
         this.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
      }

      this.setDes(Tools.getCellValue(sheet.getCell("D" + index)));
   }

   public synchronized boolean receive(Player player) {
      return false;
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
      packet.writeUTF(this.getDes());
   }

   public boolean canReceive(Player player, boolean notice) {
      BlueVip bv = player.getUser().getBlueVip();
      return !bv.is_blue_year_vip() ? false : super.canReceive(player, notice);
   }

   public int getReceiveStatus(Player player) {
      BlueVip bv = player.getUser().getBlueVip();
      if (!bv.is_blue_year_vip()) {
         return 0;
      } else {
         return this.receiveOverload(player) ? 2 : 1;
      }
   }

   public boolean doReceive(Player player) {
      return false;
   }
}
