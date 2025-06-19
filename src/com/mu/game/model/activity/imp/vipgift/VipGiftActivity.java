package com.mu.game.model.activity.imp.vipgift;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Tools;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import jxl.Sheet;

public class VipGiftActivity extends Activity {
   public VipGiftActivity() {
      super(16);
   }

   public void init(Object obj) throws Exception {
      Sheet sheet = (Sheet)obj;
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int eid = Tools.getCellIntValue(sheet.getCell("A" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("B" + i));
         int type = Tools.getCellIntValue(sheet.getCell("C" + i));
         int value = Tools.getCellIntValue(sheet.getCell("D" + i));
         VipGiftElement ae = new VipGiftElement(eid, this);
         String[] pStr = itemStr.split("#");

         for(int j = 0; j < pStr.length; ++j) {
            String[] tmp = pStr[j].split(":");
            int profession = Integer.parseInt(tmp[0]);
            ae.addItemList(profession, Tools.parseItemList(tmp[1]));
            ae.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
         }

         ae.setNumerical(value);
         ae.setVipType(type);
         ae.setSort(Tools.getCellIntValue(sheet.getCell("E" + i)));
         this.addElement(ae, true);
      }

      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, this.getDuration());
      this.setCloseDate(cc.getTime());
      Date curDate = Calendar.getInstance().getTime();
      if (this.getOpenDate() != null && this.getOpenDate().after(curDate)) {
         SpecifiedTimeManager.schedule(this.getOpenTask(), this.getOpenDate());
      }

      if (this.getCloseDate() != null && this.getCloseDate().after(curDate)) {
         SpecifiedTimeManager.schedule(this.getCloseTask(), this.getCloseDate());
      }

   }

   public int getShellId() {
      return 12;
   }

   public int getActivityType() {
      return 12;
   }

   private int getLeftTime() {
      long now = System.currentTimeMillis();
      long end = this.getCloseDate().getTime();
      long left = end - now;
      return left < 0L ? 0 : (int)(left / 1000L);
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         ai.writeInt(this.getLeftTime());
         ai.writeBoolean(player.getVipShowLevel() > 0);
         ai.writeByte(this.getElementList().size());
         Iterator var4 = this.getElementList().iterator();

         while(var4.hasNext()) {
            ActivityElement element = (ActivityElement)var4.next();
            element.writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
