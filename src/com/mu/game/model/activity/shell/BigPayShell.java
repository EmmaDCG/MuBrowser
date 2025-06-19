package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.bigpay.BigPayActivity;
import java.io.InputStream;
import jxl.Workbook;

public class BigPayShell extends ActivityShell {
   public int getMenuId() {
      return 29;
   }

   public int getShellId() {
      return 11;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      BigPayActivity ba = new BigPayActivity();
      ba.init(wb);
      ActivityManager.addActivity(ba);
   }
}
