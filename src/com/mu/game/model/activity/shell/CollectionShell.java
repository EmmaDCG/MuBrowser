package com.mu.game.model.activity.shell;

import com.mu.game.model.activity.ActivityManager;
import com.mu.game.model.activity.ActivityShell;
import com.mu.game.model.activity.imp.collection.CollectionActivity;
import com.mu.game.model.activity.imp.collection.CollectionElement;
import com.mu.utils.Tools;
import java.io.InputStream;
import jxl.Sheet;
import jxl.Workbook;

public class CollectionShell extends ActivityShell {
   public int getMenuId() {
      return 19;
   }

   public int getShellId() {
      return 127;
   }

   public void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      String name = Tools.getCellValue(sheet.getCell("B2"));
      int eid = Tools.getCellIntValue(sheet.getCell("C2"));
      String tmp = Tools.getCellValue(sheet.getCell("D2"));
      boolean systemClose = Tools.getCellIntValue(sheet.getCell("E2")) != 1;
      CollectionActivity at = new CollectionActivity();
      CollectionElement ae = new CollectionElement(eid, at);
      ae.setRewardList(Tools.parseItemList(tmp));
      ae.setUnitList(Tools.parseItemDataUnitList(tmp));
      at.setName(name);
      at.setSystemClose(systemClose);
      at.init((Object)null);
      at.addElement(ae, true);
      ActivityManager.addActivity(at);
   }
}
