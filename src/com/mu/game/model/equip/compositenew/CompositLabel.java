package com.mu.game.model.equip.compositenew;

import com.mu.game.model.equip.compositenew.condition.MaterialConFactory;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class CompositLabel {
   private static HashMap labelNames = new HashMap();

   public static void addLabel(int labelID, String labelName) {
      labelNames.put(labelID, labelName);
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet labelSheet = wb.getSheet(1);
      initLabel(labelSheet);
      Sheet conditionSheet = wb.getSheet(2);
      MaterialConFactory.initCondition(conditionSheet);
      MaterialModel.init(wb.getSheet(3));
      CompositeRate.initLevelRate(wb.getSheet(5));
      CompositeRate.initStarRate(wb.getSheet(6));
      CompositeRate.initZhuijiaRate(wb.getSheet(7));
      CompositeRate.initExcellentRate(wb.getSheet(8));
      CompositeRate.initLuckyRate(wb.getSheet(9));
      CompositeRate.initSetRate(wb.getSheet(10));
      CompositeModel.initBroadcast(wb.getSheet(11));
      CompositeModel.initModel(wb.getSheet(12));
      CompositeModel.initModel(wb.getSheet(13));
      CompositeModel.initModel(wb.getSheet(14));
      CompositeModel.initModel(wb.getSheet(15));
      CompositeGuideModel.init(wb.getSheet(16));
   }

   public static void initLabel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int labelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         labelNames.put(labelID, name);
      }

   }

   public static boolean hasLabel(int labelID) {
      return labelNames.containsKey(labelID);
   }

   public static String getLableName(int labelID) {
      return labelNames.containsKey(labelID) ? (String)labelNames.get(labelID) : "";
   }
}
