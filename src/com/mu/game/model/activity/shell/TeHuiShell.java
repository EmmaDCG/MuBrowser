package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.tehui.ActivityTeHui;
import com.mu.utils.Tools;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

public class TeHuiShell extends ActivityShell {
   public int getMenuId() {
      return 21;
   }

   public int getShellId() {
      return 4;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int duration = Tools.getCellIntValue(sheet.getCell("C2"));
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("D2")) != 1;
      ActivityTeHui activity = new ActivityTeHui();
      activity.setDuration(duration);
      activity.setSystemClose(systemClose);
      activity.init(wb.getSheet(2));
      ActivityManager.addActivity(activity);
   }
}
