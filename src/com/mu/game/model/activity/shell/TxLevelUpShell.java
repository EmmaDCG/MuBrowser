package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.tx.levelup.TxLevelUpActivity;
import com.mu.utils.Tools;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

public class TxLevelUpShell extends ActivityShell {
   public int getMenuId() {
      return 23;
   }

   public int getShellId() {
      return 6;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("A2")) != 1;
      String name = Tools.getCellValue(sheet.getCell("B2"));
      TxLevelUpActivity activity = new TxLevelUpActivity();
      activity.setSystemClose(systemClose);
      activity.setName(name);
      activity.init(wb.getSheet(2));
      ActivityManager.addActivity(activity);
   }
}
