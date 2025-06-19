package com.mu.game.model.equip.excellent;

import com.mu.game.model.service.StringTools;
import com.mu.game.model.stats.FinalModify;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;

public class ExcellentCountEffect {
   private static HashMap modelMap = new HashMap();
   private static List modelList = new ArrayList();
   private static String Active_Des = "已激活";
   private static String Inactive_Des = "未激活";
   private int count;
   private String name;
   private List stats;
   private String activeStr;
   private String inactiveStr;
   private List showCountList = new ArrayList();
   private int domineering = 0;

   public ExcellentCountEffect(int count, String name, List stats) {
      this.count = count;
      this.name = name;
      this.stats = stats;
      this.showCountList.add(count);
   }

   public static void init(Sheet sheet) throws Exception {
      Active_Des = Tools.getCellValue(sheet.getCell("B1"));
      Inactive_Des = Tools.getCellValue(sheet.getCell("C1"));
      int rows = sheet.getRows();
      int maxCount = 0;

      int size;
      int i;
      for(size = 3; size <= rows; ++size) {
         i = Tools.getCellIntValue(sheet.getCell("A" + size));
         String name = Tools.getCellValue(sheet.getCell("B" + size));
         String statStr = Tools.getCellValue(sheet.getCell("C" + size));
         List statList = StringTools.analyzeArrayAttris(statStr, ",");
         int domi = Tools.getCellIntValue(sheet.getCell("D" + size));
         if (i < 1 || statList.size() < 1) {
            throw new Exception(sheet.getName() + "-条数或者属性填写错误");
         }

         if (domi < 0) {
            throw new Exception(sheet.getName() + "-战斗力数值不正确");
         }

         ExcellentCountEffect model = new ExcellentCountEffect(i, name, statList);
         model.setDomineering(domi);
         modelMap.put(model.getCount(), model);
         if (maxCount < model.getCount()) {
            maxCount = model.getCount();
         }
      }

      if (modelMap.size() < 1) {
         throw new Exception(sheet.getName() + "-没有数据");
      } else {
         for(size = 1; size <= maxCount; ++size) {
            if (modelMap.containsKey(size)) {
               modelList.add(size);
            }
         }

         size = modelList.size();

         for(i = 0; i < modelList.size(); ++i) {
            ExcellentCountEffect model = (ExcellentCountEffect)modelMap.get(modelList.get(i));
            List nextCountList = model.getShowCountList();
            if (i < size - 1) {
               nextCountList.add((Integer)modelList.get(i + 1));
            }

            if (i < size - 2) {
               nextCountList.add((Integer)modelList.get(i + 2));
            }

            model.assemStr(true);
            model.assemStr(false);
         }

      }
   }

   private void assemStr(boolean active) {
      StringBuffer sb = new StringBuffer();
      String nameFont = active ? "#f:{7}" : "#f:{2}";
      String statPreFont = active ? "#f:{20}" : "";
      String statSuffixFont = active ? "#f" : "";
      String activeStr = active ? "#f:{7}" + Active_Des + "#f" : "#f:{19}" + Inactive_Des + "#f";
      sb.append(nameFont + this.name + "#f" + activeStr);
      sb.append("#b");

      for(int i = 0; i < this.stats.size(); ++i) {
         FinalModify modify = (FinalModify)this.stats.get(i);
         sb.append(statPreFont);
         sb.append(modify.getStat().getName() + "+" + modify.getShowValue() + modify.getSuffix());
         sb.append(statSuffixFont);
         if (i < this.stats.size() - 1) {
            sb.append("#b");
         }
      }

      if (active) {
         this.setActiveStr(sb.toString());
      } else {
         this.setInactiveStr(sb.toString());
      }

   }

   public static ExcellentCountEffect getActiveEffect(int count) {
      for(int i = modelList.size() - 1; i >= 0; --i) {
         int tmpCount = ((Integer)modelList.get(i)).intValue();
         if (tmpCount <= count) {
            return (ExcellentCountEffect)modelMap.get(tmpCount);
         }
      }

      return (ExcellentCountEffect)modelMap.get(modelList.get(0));
   }

   public static ExcellentCountEffect getEffect(int count) {
      return (ExcellentCountEffect)modelMap.get(count);
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List getStats() {
      return this.stats;
   }

   public List getShowCountList() {
      return this.showCountList;
   }

   public String getActiveStr() {
      return this.activeStr;
   }

   public void setActiveStr(String activeStr) {
      this.activeStr = activeStr;
   }

   public String getInactiveStr() {
      return this.inactiveStr;
   }

   public void setInactiveStr(String inactiveStr) {
      this.inactiveStr = inactiveStr;
   }
}
