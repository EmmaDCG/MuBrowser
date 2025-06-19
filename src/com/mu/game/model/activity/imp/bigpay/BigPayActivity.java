package com.mu.game.model.activity.imp.bigpay;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.PayInfo;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class BigPayActivity extends Activity {
   public BigPayActivity() {
      super(13);
   }

   public void init(Object obj) throws Exception {
      Workbook wb = (Workbook)obj;
      Sheet sheet = wb.getSheet(1);
      this.setSystemClose(Tools.getCellIntValue(sheet.getCell("D2")) != 1);
      int hour = Tools.getCellIntValue(sheet.getCell("C2"));
      Calendar oc = Calendar.getInstance();
      oc.setTime(Global.getOpenServerTiem());
      this.setOpenDate(oc.getTime());
      Calendar cc = Calendar.getInstance();
      cc.setTime(Global.getOpenServerTiem());
      cc.add(11, hour);
      this.setCloseDate(cc.getTime());
      Date curDate = Calendar.getInstance().getTime();
      if (this.getOpenDate() != null && this.getOpenDate().after(curDate)) {
         SpecifiedTimeManager.schedule(this.getOpenTask(), this.getOpenDate());
      }

      if (this.getCloseDate() != null && this.getCloseDate().after(curDate)) {
         SpecifiedTimeManager.schedule(this.getCloseTask(), this.getCloseDate());
      }

      this.initElement(wb.getSheet(2));
   }

   private void initElement(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String itemStr = Tools.getCellValue(sheet.getCell("B" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("C" + i));
         int times = Tools.getCellIntValue(sheet.getCell("D" + i));
         BigPayElement be = new BigPayElement(id, this);
         be.setIngot(ingot);
         be.setMaxReceiveTimes(times);
         String[] pStr = itemStr.split("#");

         for(int j = 0; j < pStr.length; ++j) {
            String[] tmp = pStr[j].split(":");
            int profession = Integer.parseInt(tmp[0]);
            be.addItemList(profession, Tools.parseItemList(tmp[1]));
            be.addItemUnitList(profession, Tools.parseItemDataUnitList(tmp[1]));
         }

         this.addElement(be, true);
      }

   }

   public int getShellId() {
      return 11;
   }

   public int getActivityType() {
      return 11;
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         PayInfo info = player.getUser().getLastPay();
         if (info != null && info.getPayTime() >= this.getOpenDate().getTime() && info.getPayTime() <= this.getCloseDate().getTime()) {
            ai.writeInt(info.getIngot());
         } else {
            ai.writeInt(0);
         }

         ai.writeUTF(Time.getTimeStr(this.getCloseDate().getTime()));
         ArrayList list = this.getElementList();
         ai.writeByte(list.size());
         Iterator var6 = list.iterator();

         while(var6.hasNext()) {
            ActivityElement element = (ActivityElement)var6.next();
            element.writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var7) {
         var7.printStackTrace();
      }

   }
}
