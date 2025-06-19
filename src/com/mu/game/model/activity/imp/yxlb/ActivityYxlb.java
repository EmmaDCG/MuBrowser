package com.mu.game.model.activity.imp.yxlb;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.item.Item;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.io.game.packet.imp.item.GetItemStats;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import jxl.Sheet;

public class ActivityYxlb extends Activity {
   public ActivityYxlb() {
      super(9);
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int eid = Tools.getCellIntValue(sheet.getCell("B2"));
      String itemStr = Tools.getCellValue(sheet.getCell("C2"));
      ActivityYxlbElement ae = new ActivityYxlbElement(eid, this);
      String[] pStr = itemStr.split("#");

      for(int i = 0; i < pStr.length; ++i) {
         String[] tmp = pStr[i].split(":");
         int profession = Integer.parseInt(tmp[0]);
         ae.addItemList(profession, Tools.parseItemList(tmp[1]));
         ae.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
      }

      this.addElement(ae, true);
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, 200000);
      this.setCloseDate(cc.getTime());
   }

   public int getShellId() {
      return 7;
   }

   public int getActivityType() {
      return 7;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         int pro = player.getProType();
         ActivityYxlbElement element = (ActivityYxlbElement)this.getElementList().get(0);
         ai.writeShort(element.getId());
         ArrayList list = element.getItemList(pro);
         ai.writeByte(list.size());
         Iterator var7 = list.iterator();

         while(var7.hasNext()) {
            Item item = (Item)var7.next();
            GetItemStats.writeItem(item, ai);
         }

         ai.writeByte(element.getReceiveStatus(player));
         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var8) {
         var8.printStackTrace();
      }

   }
}
