package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.firstpay.FirstPay;
import com.mu.game.task.specified.SpecifiedTimeManager;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import jxl.Sheet;
import jxl.Workbook;

public class FirstPayShell extends ActivityShell {
   public int getMenuId() {
      return 16;
   }

   public int getShellId() {
      return 1;
   }

   public void init(InputStream in) throws Exception {
      Date curDate = Calendar.getInstance().getTime();
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      String name = Tools.getCellValue(sheet.getCell("B2"));
      int sort = Tools.getCellIntValue(sheet.getCell("F2"));
      String tmp = Tools.getCellValue(sheet.getCell("G2"));
      int total = Tools.getCellIntValue(sheet.getCell("H2"));
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("I2")) != 1;
      FirstPay fp = new FirstPay();
      String[] ms = tmp.split(";");

      for(int i = 0; i < ms.length; ++i) {
         String[] model = ms[i].split(",");
         int pro = Integer.parseInt(model[0]);
         int[] mo = new int[]{Integer.parseInt(model[1]), Integer.parseInt(model[2])};
         fp.addModle(pro, mo);
      }

      fp.setSort(sort);
      fp.setName(name);
      fp.init(wb.getSheet(2));
      fp.setTotalReward(total);
      fp.setSystemClose(systemClose);
      ActivityManager.addActivity(fp);
      if (fp.getOpenDate() != null && fp.getOpenDate().after(curDate)) {
         SpecifiedTimeManager.schedule(fp.getOpenTask(), fp.getOpenDate());
      }

      if (fp.getCloseDate() != null && fp.getCloseDate().after(curDate)) {
         SpecifiedTimeManager.schedule(fp.getCloseTask(), fp.getCloseDate());
      }

   }
}
