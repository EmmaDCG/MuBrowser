package com.mu.game.model.spiritOfWar;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.service.StringTools;
import com.mu.game.model.spiritOfWar.filter.FilterCondition;
import com.mu.game.model.spiritOfWar.filter.FilterGroup;
import com.mu.game.model.spiritOfWar.model.RefineItem;
import com.mu.game.model.spiritOfWar.model.SpiritModel;
import com.mu.game.model.spiritOfWar.model.SpiritRankModel;
import com.mu.game.model.spiritOfWar.refine.RefineData;
import com.mu.game.model.stats.SpiritModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jxl.Sheet;
import jxl.Workbook;

public class SpiritTools {
   public static int RankMaxLevel = 10;
   public static final int MaxerRefineCount = 12;
   public static int maxIngotRefineCount = 10;
   private static HashMap modelMap = new HashMap();
   private static HashMap refineIngotMap = new HashMap();
   private static SortedMap itemMap = new TreeMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      SpiritRankModel.init(wb.getSheet(1));
      Sheet modelSheet = wb.getSheet(2);
      initModel(modelSheet);
      initIngotRefine(wb.getSheet(3));
      RefineData.initType(wb.getSheet(4));
      RefineData.initLevel(wb.getSheet(5));
      RefineData.initStats(wb.getSheet(6));
      RefineData.initSocket(wb.getSheet(7));
      RefineData.initStarLevel(wb.getSheet(8));
      RefineData.initZhuijia(wb.getSheet(9));
      RefineData.initRune(wb.getSheet(10));
      RefineData.initExcellentCount(wb.getSheet(11));
      RefineData.initSet(wb.getSheet(12));
      RefineData.initLucky(wb.getSheet(13));
      FilterCondition.init(wb.getSheet(14), "战魂");
      FilterGroup.init(wb.getSheet(15));
      initRefineItem(wb.getSheet(16));
   }

   private static void initModel(Sheet sheet) throws Exception {
      int rankLevel = Tools.getCellIntValue(sheet.getCell("B1"));
      if (rankLevel < 1) {
         throw new Exception("战魂每个等阶包含的等级数错误");
      } else {
         RankMaxLevel = rankLevel;
         int rows = sheet.getRows();

         int i;
         int rank;
         for(i = 3; i <= rows; ++i) {
            rank = Tools.getCellIntValue(sheet.getCell("A" + i));
            int level = Tools.getCellIntValue(sheet.getCell("B" + i));
            long needExp = Tools.getCellLongValue(sheet.getCell("C" + i));
            String statStr = Tools.getCellValue(sheet.getCell("D" + i));
            int domineering = Tools.getCellIntValue(sheet.getCell("E" + i));
            int addDomineering = Tools.getCellIntValue(sheet.getCell("F" + i));
            String itemCountStr = Tools.getCellValue(sheet.getCell("G" + i));
            int ingotExp = Tools.getCellIntValue(sheet.getCell("H" + i));
            if (needExp < 1L || domineering < 0 || ingotExp < 0 || addDomineering < 0) {
               throw new Exception(sheet.getName() + " - 数据不正常，第" + i + "行");
            }

            List modifies = new ArrayList();
            String[] splits = statStr.split(";");
            String[] var18 = splits;
            int key = splits.length;

            for(int var16 = 0; var16 < key; ++var16) {
               String s = var18[var16];
               String[] secSplits = s.split(",");
               if (secSplits.length < 4) {
                  throw new Exception(sheet.getName() + " - 属性不正确，第" + i);
               }

               StatEnum stat = StatEnum.find(Integer.parseInt(secSplits[0]));
               int value = Integer.parseInt(secSplits[1]);
               StatModifyPriority priority = StatModifyPriority.fine(Integer.parseInt(secSplits[2]));
               int increaseValue = Integer.parseInt(secSplits[3]);
               if (stat == StatEnum.None || value < 0 || increaseValue < 0) {
                  throw new Exception(sheet.getName() + "-属性配置不正确，第" + i);
               }

               SpiritModify modify = new SpiritModify(stat, value, priority, increaseValue);
               modifies.add(modify);
            }

            if (modifies.size() < 1) {
               throw new Exception(sheet.getName() + " - 没有填写属性，或者属性值不正确，第" + i);
            }

            HashMap itemCount = StringTools.analyzeIntegerMap(itemCountStr, ",");
            Iterator var28 = itemCount.values().iterator();

            while(var28.hasNext()) {
               Integer value = (Integer)var28.next();
               if (value.intValue() < 0) {
                  throw new Exception(sheet.getName() + "- 道具限制填写有误，第" + i);
               }
            }

            SpiritModel model = new SpiritModel(rank, level, needExp);
            model.setDomineering(domineering);
            model.setIngotExp(ingotExp);
            model.setItemCount(itemCount);
            model.setStats(modifies);
            model.setAddDomineering(addDomineering);
            key = createKey(rank, level);
            modelMap.put(key, model);
         }

         for(i = 1; i <= SpiritRankModel.maxRank; ++i) {
            for(rank = 1; rank <= RankMaxLevel; ++rank) {
               if (getModel(i, rank) == null) {
                  throw new Exception(sheet.getName() + "-第(" + i + "," + rank + ")级数据不存在");
               }
            }
         }

      }
   }

   public static int createKey(int rank, int level) {
      return rank * 10000 + level;
   }

   private static void initIngotRefine(Sheet sheet) throws Exception {
      int maxCount = 1;
      int rows = sheet.getRows();

      int i;
      for(i = 3; i <= rows; ++i) {
         int count = Tools.getCellIntValue(sheet.getCell("A" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("B" + i));
         int bindIngot = Tools.getCellIntValue(sheet.getCell("C" + i));
         if (count < 1 || ingot < 1 || bindIngot < 1) {
            throw new Exception(sheet.getName() + "-钻石炼化数据不正常 ，第" + i);
         }

         if (maxCount < count) {
            maxCount = count;
         }

         int[] ingots = new int[]{ingot, bindIngot};
         refineIngotMap.put(count, ingots);
      }

      for(i = 1; i <= maxCount; ++i) {
         if (!refineIngotMap.containsKey(i)) {
            throw new Exception(sheet.getName() + ",钻石炼化数据不对，第" + i + "次数据不存在");
         }
      }

      maxIngotRefineCount = maxCount;
   }

   public static void initRefineItem(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int itemId = Tools.getCellIntValue(sheet.getCell("A" + i));
         String statStr = Tools.getCellValue(sheet.getCell("B" + i));
         int domineering = Tools.getCellIntValue(sheet.getCell("C" + i));
         List modifies = StringTools.analyzeArrayAttris(statStr, ",");
         if (modifies == null || modifies.size() < 1) {
            throw new Exception("战魂 -" + sheet.getName() + "-没有合适的属性，第" + i);
         }

         Item showItem = ItemTools.createItem(itemId, 1, 2);
         if (showItem == null) {
            throw new Exception("战魂 -" + sheet.getName() + "-道具不存在，第" + i);
         }

         if (domineering < 1) {
            throw new Exception("战魂 -" + sheet.getName() + "-战斗力不正确，第" + i);
         }

         RefineItem item = new RefineItem(itemId, domineering);
         item.setStatList(modifies);
         item.setShowItem(showItem);
         itemMap.put(itemId, item);
      }

   }

   public static SpiritModel getModel(int rank, int level) {
      int key = createKey(rank, level);
      return (SpiritModel)modelMap.get(key);
   }

   public static long needExp(int rank, int level) {
      return getModel(rank, level).getNeedExp();
   }

   public static int[] getRefineIngot(int count) {
      if (count > maxIngotRefineCount) {
         count = maxIngotRefineCount;
      }

      return (int[])refineIngotMap.get(count);
   }

   public static RefineItem getRefineItem(int itemID) {
      return (RefineItem)itemMap.get(itemID);
   }

   public static SortedMap getItemMap() {
      return itemMap;
   }

   public static int canRefine(Item item) {
      if (!item.isEquipment()) {
         return 23305;
      } else if (!RefineData.canRefine(item.getItemType())) {
         return 23305;
      } else {
         int excelletCount = item.getBonusStatSize(3);
         return excelletCount < 1 ? 23306 : 1;
      }
   }

   public static boolean filterItem(Item item, List conditionList) {
      if (canRefine(item) != 1) {
         return false;
      } else {
         Iterator var3 = conditionList.iterator();

         while(var3.hasNext()) {
            FilterCondition condition = (FilterCondition)var3.next();
            if (!condition.filter(item)) {
               return false;
            }
         }

         return true;
      }
   }
}
