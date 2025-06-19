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

public class ActivityLevelBlueElement extends ActivityBlueVipElement {
   private int level;

   public ActivityLevelBlueElement(int id, ActivityBlueVip father) {
      super(id, father);
   }

   public void init(Sheet sheet, int index) throws Exception {
      this.level = Tools.getCellIntValue(sheet.getCell("B" + index));
      this.setTitle(Tools.getCellValue(sheet.getCell("C" + index)));
      String itemStr = Tools.getCellValue(sheet.getCell("D" + index));
      String[] pStr = itemStr.split("#");

      for(int i = 0; i < pStr.length; ++i) {
         String[] tmp = pStr[i].split(":");
         int profession = Integer.parseInt(tmp[0]);
         this.addItemList(profession, Tools.parseItemList(tmp[1]));
         this.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
      }

   }

   public int getReceiveType() {
      return 3;
   }

   public boolean canReceive(Player player, boolean notice) {
      BlueVip bv = player.getUser().getBlueVip();
      return bv.isVip() && player.getLevel() >= this.level ? super.canReceive(player, notice) : false;
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

   public boolean doReceive(Player player) {
      boolean b = super.doReceive(player);
      if (b) {
         ((ActivityBlueVip)this.getFather()).writeDetail(player);
      }

      return b;
   }

   public int getReceiveStatus(Player player) {
      BlueVip bv = player.getUser().getBlueVip();
      if (bv.isVip() && player.getLevel() >= this.level) {
         return this.receiveOverload(player) ? 2 : 1;
      } else {
         return 0;
      }
   }
}
