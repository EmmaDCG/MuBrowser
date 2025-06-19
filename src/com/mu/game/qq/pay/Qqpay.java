package com.mu.game.qq.pay;

import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.TreeMap;
import jxl.Sheet;
import jxl.Workbook;

public class Qqpay {
   private static TreeMap payMap = new TreeMap();
   private static int minId = -1;
   private static String functionName = "GameAPI.BuyBox.show";
   private static String title = "购买钻石";
   private static String imgUrl;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("B" + i));
         int icon = Tools.getCellIntValue(sheet.getCell("C" + i));
         int originalPrice = Tools.getCellIntValue(sheet.getCell("D" + i));
         int blueVipProce = Tools.getCellIntValue(sheet.getCell("E" + i));
         String des = Tools.getCellValue(sheet.getCell("F" + i));
         QqPayElement pe = new QqPayElement();
         pe.setBlueVipProce(blueVipProce);
         pe.setIcon(icon);
         pe.setId(id);
         pe.setIngot(ingot);
         pe.setOriginalPrice(originalPrice);
         pe.setDes(des);
         payMap.put(id, pe);
         if (minId == -1) {
            minId = id;
         } else if (id < minId) {
            minId = id;
         }
      }

      Sheet sheet2 = wb.getSheet(2);
      functionName = Tools.getCellValue(sheet2.getCell("A2"));
      title = Tools.getCellValue(sheet2.getCell("B2"));
      imgUrl = Tools.getCellValue(sheet2.getCell("C2"));
   }

   public static TreeMap getPayMap() {
      return payMap;
   }

   public static QqPayElement getPayElement(int id) {
      QqPayElement pe = (QqPayElement)payMap.get(id);
      return pe == null ? (QqPayElement)payMap.get(minId) : pe;
   }

   public static String getFunctionName() {
      return functionName;
   }

   public static String getTitle() {
      return title;
   }

   public static String getImgUrl() {
      return imgUrl;
   }
}
