package com.mu.game.model.equip.excellent;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class ExcellentCreationData {
   private static HashMap datas = new HashMap();
   private int dataID;
   private int itemType;
   private ExcellentElement element = null;

   public static void init(String excelName, Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int dataID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int itemType = Tools.getCellIntValue(sheet.getCell("B" + i));
         String weightStr = Tools.getCellValue(sheet.getCell("C" + i));
         ExcellentElement element = new ExcellentElement();
         element.parse(weightStr, excelName + sheet.getName() + "，第 " + i + " 行");
         ExcellentCreationData data = new ExcellentCreationData();
         data.setElement(element);
         data.setItemType(itemType);
         data.setDataID(dataID);
         addData(data);
      }

   }

   private static void addData(ExcellentCreationData data) {
      HashMap eData = (HashMap)datas.get(data.getDataID());
      if (eData == null) {
         eData = new HashMap();
         datas.put(data.getDataID(), eData);
      }

      eData.put(data.getItemType(), data);
   }

   public static ExcellentCreationData getData(int dataID, int itemType) {
      HashMap eData = (HashMap)datas.get(dataID);
      return eData == null ? null : (ExcellentCreationData)eData.get(itemType);
   }

   public static boolean hasDataID(int dataID) {
      return datas.containsKey(dataID);
   }

   public int getItemType() {
      return this.itemType;
   }

   public void setItemType(int itemType) {
      this.itemType = itemType;
   }

   public ExcellentElement getElement() {
      return this.element;
   }

   public void setElement(ExcellentElement element) {
      this.element = element;
   }

   public int getDataID() {
      return this.dataID;
   }

   public void setDataID(int dataID) {
      this.dataID = dataID;
   }
}
