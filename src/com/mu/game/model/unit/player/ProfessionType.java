package com.mu.game.model.unit.player;

import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class ProfessionType {
   private static HashMap typeMap = new HashMap();
   private int typeID;
   private String name;
   private String des;
   private int model3D;
   private List pros = null;

   public ProfessionType(int typeID, String name, String des, int model3D) {
      this.typeID = typeID;
      this.name = name;
      this.des = des;
      this.model3D = model3D;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int typeID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         String des = Tools.getCellValue(sheet.getCell("C" + i));
         int model3D = Tools.getCellIntValue(sheet.getCell("D" + i));
         String proStr = Tools.getCellValue(sheet.getCell("E" + i));
         List pros = StringTools.analyzeIntegerList(proStr, ",");
         if (pros == null || pros.size() < 1) {
            throw new Exception(sheet.getName() + "第" + i + "行" + ",初始数值不正确");
         }

         ProfessionType pt = new ProfessionType(typeID, name, des, model3D);
         pt.setPros(pros);
         typeMap.put(pt.getTypeID(), pt);
      }

   }

   public static boolean hasType(int professionType) {
      return typeMap.containsKey(professionType);
   }

   public static HashMap getTypeMap() {
      return typeMap;
   }

   public List getPros() {
      return this.pros;
   }

   public void setPros(List pros) {
      this.pros = pros;
   }

   public int getTypeID() {
      return this.typeID;
   }

   public String getName() {
      return this.name;
   }

   public String getDes() {
      return this.des;
   }

   public int getModel3D() {
      return this.model3D;
   }
}
