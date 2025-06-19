package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.daypay.DayPayActivity;
import java.io.InputStream;
import jxl.Workbook;

public class DayPayShell extends ActivityShell {
   public int getMenuId() {
      return 34;
   }

   public int getShellId() {
      return 13;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      DayPayActivity da = new DayPayActivity();
      da.init(wb);
      ActivityManager.addActivity(da);
   }
}
