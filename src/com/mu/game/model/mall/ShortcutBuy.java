package com.mu.game.model.mall;

import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class ShortcutBuy {
   private static HashMap shortcutMap = new HashMap();
   public static final int Shortcut_ClearPK = 1;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         int modelId = Tools.getCellIntValue(sheet.getCell("B" + i));
         shortcutMap.put(id, modelId);
      }

      ShortcutBuyPanel.init(wb.getSheet(2));
   }

   public static int getModel(int id) {
      return ((Integer)shortcutMap.get(id)).intValue();
   }

   public static boolean hasShortcutBuy(int id) {
      return shortcutMap.containsKey(id);
   }
}
