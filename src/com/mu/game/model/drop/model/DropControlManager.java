package com.mu.game.model.drop.model;

import com.mu.game.model.task.TaskConfigManager;
import com.mu.game.model.unit.player.Player;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

public class DropControlManager {
   private static HashMap dropMap = new HashMap();
   private static HashMap rateControlMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet controlSheet = wb.getSheet(1);
      init(controlSheet);
      initControlGroup(wb.getSheet(2));
   }

   public static void initRateControl(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int dropID = Tools.getCellIntValue(sheet.getCell("A" + i));
         rateControlMap.put(dropID, Integer.valueOf(1));
      }

   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();
      int i = 2;

      while(i <= rows) {
         int controlID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int type = Tools.getCellIntValue(sheet.getCell("B" + i));
         int targetID = Tools.getCellIntValue(sheet.getCell("C" + i));
         String value = "";
         Cell cell = sheet.getCell("D" + i);
         if (cell.getContents() != null) {
            value = cell.getContents().trim();
         }

         switch(type) {
         case 2:
            if (TaskConfigManager.getData(targetID) == null) {
               throw new Exception("dropControl - task ID is null,controlID = " + controlID);
            }
         default:
            DropControl dc = DropControl.createControl(controlID, type, targetID, value);
            if (dc == null) {
               throw new Exception("掉落控制不存在 ，掉落ID = " + controlID);
            }

            DropControl.getControls().put(dc.getControlID(), dc);
            ++i;
         }
      }

   }

   public static void initControlGroup(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int controlID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String value = Tools.getCellValue(sheet.getCell("B" + i));
         if (value.trim().length() < 1) {
            throw new Exception(sheet.getName() + "第" + i + "行数据不合法");
         }

         String[] splits = value.split(",");
         List dcList = new ArrayList();
         String[] var10 = splits;
         int var9 = splits.length;

         for(int var8 = 0; var8 < var9; ++var8) {
            String s = var10[var8];
            int controlModelID = Integer.parseInt(s);
            DropControl dc = DropControl.getControl(controlModelID);
            if (dc == null) {
               throw new Exception(sheet.getName() + "第" + i + "行 控制模板不存在");
            }

            dcList.add(dc);
         }

         dropMap.put(controlID, dcList);
      }

   }

   public static boolean canDrop(int controlID, Player player, int templateID) {
      if (controlID == -1) {
         return true;
      } else {
         List dcList = getDropList(controlID);
         if (dcList == null) {
            return true;
         } else {
            Iterator var5 = dcList.iterator();

            while(var5.hasNext()) {
               DropControl dc = (DropControl)var5.next();
               if (!dc.checkDrop(player, templateID)) {
                  return false;
               }
            }

            return true;
         }
      }
   }

   public static int getDropCount(Player player, int controlID, int itemModelID) {
      if (controlID == -1) {
         return 2147483646;
      } else {
         List dcList = getDropList(controlID);
         if (dcList == null) {
            return 2147483646;
         } else {
            int max = 2147483646;

            DropControl dc;
            for(Iterator var6 = dcList.iterator(); var6.hasNext(); max = Math.min(max, dc.getCanDropCount(player, itemModelID))) {
               dc = (DropControl)var6.next();
            }

            return max;
         }
      }
   }

   public static boolean isInRateControl(int dropID) {
      return rateControlMap.containsKey(dropID);
   }

   public static List getDropList(int controlID) {
      return (List)dropMap.get(controlID);
   }

   public static HashMap getDropMap() {
      return dropMap;
   }
}
