package com.mu.game.model.equip.excellent;

import com.mu.game.model.equip.equipStat.EquipStatCreation;
import com.mu.game.model.equip.weight.CreationElement;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class ExcellentSort extends EquipStatCreation {
   private static HashMap sorts = new HashMap();
   private int dataID;

   public ExcellentSort(int sortID, String des, int dataID) {
      super(sortID);
      this.setDes(des);
      this.setDataID(dataID);
   }

   public static void init(String excelName, Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int sortID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String des = Tools.getCellValue(sheet.getCell("B" + i));
         String weightStr = Tools.getCellValue(sheet.getCell("C" + i));
         CreationElement element = new CreationElement();
         element.parse(weightStr, sheet.getName() + "，第 " + i + " 行");
         int dataID = Tools.getCellIntValue(sheet.getCell("D" + i));
         if (!ExcellentCreationData.hasDataID(dataID)) {
            throw new Exception(excelName + sheet.getName() + " 不存在 ，dataID = " + dataID);
         }

         ExcellentSort sort = new ExcellentSort(sortID, des, dataID);
         sort.setElement(element);
         sorts.put(sortID, sort);
      }

   }

   public static ExcellentSort getExcellentSort(int sortID) {
      return (ExcellentSort)sorts.get(sortID);
   }

   public static boolean hasSortID(int sortID) {
      return sorts.containsKey(sortID);
   }

   public int getDataID() {
      return this.dataID;
   }

   public void setDataID(int dataID) {
      this.dataID = dataID;
   }
}
