package com.mu.game.model.equip.newStone;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.weight.WeightElement;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class StoneDataManager {
   public static final int MaxCount = 3;
   public static String SocketOpenDes = "未镶嵌";
   private static HashMap stoneStatMap = new HashMap();
   private static HashMap canForgingItemTypes = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      initStoneStat(wb.getSheet(1));
      initCanMosaicType(wb.getSheet(2));
      StoneSet.init(wb.getSheet(3));
   }

   public static void initStoneStat(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String statIDStr = Tools.getCellValue(sheet.getCell("B" + i));
         List statList = new ArrayList();
         String[] Splits = statIDStr.split(";");
         WeightElement element = null;
         String[] var11 = Splits;
         int var10 = Splits.length;

         for(int var9 = 0; var9 < var10; ++var9) {
            String ss = var11[var9];
            StoneStatAtom atom = StoneStatAtom.createStoneStarAtom(statList, sheet.getName() + " 第 " + i + " 行 - ", ss);
            if (element == null) {
               element = new WeightElement();
            }

            element.addAtom(atom);
         }

         if (element == null) {
            throw new Exception(sheet.getName() + " - 没有填写宝石属性 , 第  " + i + " 行");
         }

         element.sortByWeight(sheet.getName() + "，第 " + i + " 行 - ");
         statList.clear();
         statList = null;
         stoneStatMap.put(modelID, element);
      }

   }

   public static void initCanMosaicType(Sheet sheet) throws Exception {
      SocketOpenDes = Tools.getCellValue(sheet.getCell("B1"));
      int rows = sheet.getRows();

      for(int i = 3; i <= rows; ++i) {
         int itemType = Tools.getCellIntValue(sheet.getCell("A" + i));
         String canMosaicTypeStr = Tools.getCellValue(sheet.getCell("B" + i));
         HashSet canMosaicTypeSet = StringTools.analyzeIntegerHashset(canMosaicTypeStr, ",");
         if (canMosaicTypeSet.size() < 1) {
            throw new Exception(sheet.getName() + "-可镶嵌的宝石种类没有填写,第 " + i + "行");
         }

         canForgingItemTypes.put(itemType, canMosaicTypeSet);
      }

   }

   public static boolean canMosaicByType(int itemType) {
      return canForgingItemTypes.containsKey(itemType);
   }

   public static HashSet getMosaicStoneType(int itemType) {
      return (HashSet)canForgingItemTypes.get(itemType);
   }

   public static int getRndEquipStatID(int modelID) {
      WeightElement we = (WeightElement)stoneStatMap.get(modelID);
      if (we == null) {
         return -1;
      } else {
         StoneStatAtom atom = (StoneStatAtom)we.getRndAtom();
         return atom.getStatID();
      }
   }

   public static List getStoneStats(int modelID) {
      if (!stoneStatMap.containsKey(modelID)) {
         return null;
      } else {
         WeightElement we = (WeightElement)stoneStatMap.get(modelID);
         return we.getAtoms();
      }
   }

   public static boolean hasStoneStat(int modelID) {
      return stoneStatMap.containsKey(modelID);
   }

   public static String assemStoneStat(EquipStat equipStat) {
      StringBuffer sb = new StringBuffer();
      sb.append("#F{e=2}" + equipStat.getStat().getName() + "#F");
      sb.append("#F{e=7}+" + equipStat.getShowValue() + equipStat.getSuffix() + "#F");
      return sb.toString();
   }

   public static int getMaxCount() {
      return 3;
   }
}
