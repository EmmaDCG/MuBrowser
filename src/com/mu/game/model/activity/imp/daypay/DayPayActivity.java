package com.mu.game.model.activity.imp.daypay;

import com.mu.config.Global;
import com.mu.game.model.activity.Activity;
import com.mu.game.model.activity.ActivityElement;
import com.mu.game.model.unit.player.Player;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.io.game.packet.imp.activity.ActivityInfo;
import com.mu.utils.Time;
import com.mu.utils.Tools;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import jxl.Sheet;
import jxl.Workbook;

public class DayPayActivity extends Activity {
   private int maxDay = 0;

   public DayPayActivity() {
      super(17);
   }

   public void init(Object obj) throws Exception {
      Workbook wb = (Workbook)obj;
      Date curDate = Calendar.getInstance().getTime();
      Sheet sheet = wb.getSheet(1);
      String name = Tools.getCellValue(sheet.getCell("B2"));
      int dayDelay = Tools.getCellIntValue(sheet.getCell("C2"));
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("D2")) != 1;
      this.setName(name);
      this.setSystemClose(systemClose);
      Calendar openCal = Calendar.getInstance();
      openCal.setTime(Global.getOpenServerTiem());
      openCal.set(11, 0);
      openCal.set(12, 0);
      openCal.set(13, 0);
      openCal.add(6, dayDelay);
      this.setOpenDate(openCal.getTime());
      Sheet elementSheet = wb.getSheet(2);
      int elementRows = elementSheet.getRows();

      int rewardRows;
      for(int i = 2; i <= elementRows; ++i) {
         rewardRows = Tools.getCellIntValue(elementSheet.getCell("A" + i));
         i = Tools.getCellIntValue(elementSheet.getCell("B" + i));
         DayPayElement de = new DayPayElement(rewardRows, this);
         de.setIngot(i);
         this.addElement(de, true);
      }

      Sheet rewardSheet = wb.getSheet(3);
      rewardRows = rewardSheet.getRows();

      for(int i = 2; i <= rewardRows; ++i) {
         int day = Tools.getCellIntValue(rewardSheet.getCell("A" + i));
         int eid1 = Tools.getCellIntValue(rewardSheet.getCell("B" + i));
         String re1 = Tools.getCellValue(rewardSheet.getCell("C" + i));
         int eid2 = Tools.getCellIntValue(rewardSheet.getCell("D" + i));
         String re2 = Tools.getCellValue(rewardSheet.getCell("E" + i));
         int eid3 = Tools.getCellIntValue(rewardSheet.getCell("F" + i));
         String re3 = Tools.getCellValue(rewardSheet.getCell("G" + i));
         if (day > this.maxDay) {
            this.maxDay = day;
         }

         DayPayElement de1 = (DayPayElement)this.getElement(eid1);
         de1.initReward(day, re1);
         DayPayElement de2 = (DayPayElement)this.getElement(eid2);
         de2.initReward(day, re2);
         DayPayElement de3 = (DayPayElement)this.getElement(eid3);
         de3.initReward(day, re3);
      }

      Calendar closeCal = Calendar.getInstance();
      closeCal.setTime(this.getOpenDate());
      closeCal.set(11, 23);
      closeCal.set(12, 59);
      closeCal.set(13, 59);
      closeCal.add(6, this.maxDay);
      this.setCloseDate(closeCal.getTime());
      if (this.getOpenDate() != null && this.getOpenDate().after(curDate)) {
         SpecifiedTimeManager.schedule(this.getOpenTask(), this.getOpenDate());
      }

      if (this.getCloseDate() != null && this.getCloseDate().after(curDate)) {
         SpecifiedTimeManager.schedule(this.getCloseTask(), this.getCloseDate());
      }

   }

   public int getShellId() {
      return 13;
   }

   public int getActivityType() {
      return 13;
   }

   public int getCurrentDay() {
      long openTime = this.getOpenDate().getTime();
      long now = System.currentTimeMillis();
      return (int)((now - openTime) / 1000L / 3600L / 24L);
   }

   public void writeDetail(Player player) {
      ActivityInfo ai = new ActivityInfo();

      try {
         ai.writeByte(this.getId());
         ai.writeInt(player.getUser().getPay(Time.getDayLong()));
         ai.writeByte(this.getElementList().size());
         Iterator var4 = this.getElementList().iterator();

         while(var4.hasNext()) {
            ActivityElement ae = (ActivityElement)var4.next();
            ae.writeDetail(player, ai);
         }

         player.writePacket(ai);
         ai.destroy();
         ai = null;
      } catch (Exception var5) {
         var5.printStackTrace();
      }

   }
}
