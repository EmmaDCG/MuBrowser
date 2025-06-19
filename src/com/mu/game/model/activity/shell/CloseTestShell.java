package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.test.ActivityTest;
import com.mu.game.model.activity.imp.test.ActivityTestElement;
import com.mu.utils.Tools;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

public class CloseTestShell extends ActivityShell {
   public int getMenuId() {
      return 17;
   }

   public int getShellId() {
      return 3;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      String name = Tools.getCellValue(sheet.getCell("B2"));
      int eid = Tools.getCellIntValue(sheet.getCell("C2"));
      String tmp = Tools.getCellValue(sheet.getCell("D2"));
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("E2")) != 1;
      int buffId = Tools.getCellIntValue(sheet.getCell("F2"));
      String start = Tools.getCellValue(sheet.getCell("G2"));
      int buffDuration = Tools.getCellIntValue(sheet.getCell("H2"));
      ActivityTest at = new ActivityTest();
      ActivityTestElement ae = new ActivityTestElement(eid, at);
      ae.setRewardList(Tools.parseItemList(tmp));
      ae.setUnitList(Tools.parseItemDataUnitList(tmp));
      at.setName(name);
      at.setSystemClose(systemClose);
      at.init((Object)null);
      at.addElement(ae, true);
      at.setBuffId(buffId);
      String[] starts = start.split(":");
      at.setStartHour(Integer.parseInt(starts[0]));
      at.setStartMinute(Integer.parseInt(starts[1]));
      at.setBuffDuration((long)(buffDuration * 60) * 1000L);
      ActivityManager.addActivity(at);
   }
}
