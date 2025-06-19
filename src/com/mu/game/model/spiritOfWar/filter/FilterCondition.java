package com.mu.game.model.spiritOfWar.filter;

import com.mu.game.model.item.Item;
import com.mu.game.model.spiritOfWar.filter.imp.FilterEquipSet;
import com.mu.game.model.spiritOfWar.filter.imp.FilterExcellentCount;
import com.mu.game.model.spiritOfWar.filter.imp.FilterLevel;
import com.mu.game.model.spiritOfWar.filter.imp.FilterStarLevel;
import com.mu.game.model.spiritOfWar.filter.imp.FilterStoneSocekt;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public abstract class FilterCondition {
   public static final int Filter_EquipSet = 1;
   public static final int Filter_EquipLevel = 2;
   public static final int Filter_EquipStarLevel = 3;
   public static final int Filter_ExcellentCount = 4;
   public static final int Filter_EquipSocket = 5;
   private static HashMap filterMap = new HashMap();
   private static FilterExcellentCount defaultConditon = null;
   private int id;
   private int type;
   private String name;

   public FilterCondition(int id, int type, String name) {
      this.id = id;
      this.type = type;
      this.name = name;
   }

   public abstract boolean filter(Item var1);

   public static void init(Sheet sheet, String des) throws Exception {
      defaultConditon = new FilterExcellentCount(0, 4, "默认", 1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         int type = Tools.getCellIntValue(sheet.getCell("C" + i));
         int value = Tools.getCellIntValue(sheet.getCell("D" + i));
         FilterCondition fc = createFilter(id, type, name, value);
         if (fc == null) {
            throw new Exception(des + "-筛选条件-找不到类型,第 " + i + "行");
         }

         filterMap.put(id, fc);
      }

   }

   public static FilterCondition createFilter(int id, int type, String name, int value) throws Exception {
      FilterCondition filter = null;
      switch(type) {
      case 1:
         filter = new FilterEquipSet(id, type, name, value);
         break;
      case 2:
         filter = new FilterLevel(id, type, name, value);
         break;
      case 3:
         filter = new FilterStarLevel(id, type, name, value);
         break;
      case 4:
         filter = new FilterExcellentCount(id, type, name, value);
         break;
      case 5:
         filter = new FilterStoneSocekt(id, type, name, value);
      }

      return (FilterCondition)filter;
   }

   public static FilterCondition getfilterCondition(int id) {
      return (FilterCondition)filterMap.get(id);
   }

   public static FilterCondition getDefaultConditon() {
      return defaultConditon;
   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
