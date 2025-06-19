package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.yxlb.ActivityYxlb;
import com.mu.utils.Tools;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

public class YxlbShell extends ActivityShell {
   public int getMenuId() {
      return 25;
   }

   public int getShellId() {
      return 7;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      String name = Tools.getCellValue(sheet.getCell("B2"));
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("D2")) != 1;
      ActivityYxlb ab = new ActivityYxlb();
      ab.setName(name);
      ab.init(wb.getSheet(2));
      ab.setSystemClose(systemClose);
      ActivityManager.addActivity(ab);
   }
}
