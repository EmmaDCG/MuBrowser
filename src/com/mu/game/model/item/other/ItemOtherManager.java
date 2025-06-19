package com.mu.game.model.item.other;

import com.mu.game.model.unit.player.tips.SystemFunctionTipConfig;
import java.io.InputStream;
import jxl.Workbook;

public class ItemOtherManager {
   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      SystemFunctionTipConfig.initTips(wb.getSheet(1));
      ExpiredItemManager.init(wb.getSheet(2));
      ExpiredItemManager.initAngelEquip(wb.getSheet(3));
   }
}
