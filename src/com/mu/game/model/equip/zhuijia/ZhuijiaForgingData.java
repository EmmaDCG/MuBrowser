package com.mu.game.model.equip.zhuijia;

import com.mu.game.model.equip.excellent.ExcellentCountEffect;
import com.mu.game.model.equip.forging.ForgingRuleDes;
import com.mu.game.model.equip.horseFusion.HorseFusionData;
import com.mu.game.model.equip.rune.RuneForgingData;
import com.mu.game.model.equip.rune.RuneInheritData;
import com.mu.game.model.equip.rune.RuneSetModel;
import com.mu.game.model.equip.star.StarForgingData;
import com.mu.game.model.equip.star.StarInheritData;
import com.mu.game.model.equip.star.StarSetModel;
import com.mu.game.model.equip.star.StatEnumData;
import com.mu.game.model.equip.upgrade.UpgradeData;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.ItemModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class ZhuijiaForgingData {
   private static HashMap datas = new HashMap();
   private static HashSet canForgingTypes = new HashSet();
   private static int maxLevel = 1;
   private int itemType;
   private int level;
   private int rate;
   private int backRate;
   private int itemID;
   private int count;
   private int ingot;
   private int money;
   private Item needShowItem = null;
   private HashMap statDatas = null;
   private int domineering = 0;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet strengthForgingSheet = wb.getSheet(1);
      StarForgingData.init(strengthForgingSheet);
      Sheet zhuijiaForgingSheet = wb.getSheet(2);
      init(zhuijiaForgingSheet);
      Sheet runeForgingSheet = wb.getSheet(3);
      RuneForgingData.init(runeForgingSheet);
      RuneSetModel.init(wb.getSheet(4));
      RuneInheritData.init(wb.getSheet(5));
      Sheet inheritSheet = wb.getSheet(6);
      StarInheritData.init(inheritSheet);
      ForgingRuleDes.init(wb.getSheet(7));
      ForgingRuleDes.initSuffix(wb.getSheet(8));
      StarSetModel.init(wb.getSheet(9));
      ExcellentCountEffect.init(wb.getSheet(10));
      HorseFusionData.init(wb.getSheet(11));
      UpgradeData.init(wb.getSheet(12));
   }

   public ZhuijiaForgingData(int itemType, int level, int rate, int backRate, int itemID, int count, int ingot, int money) {
      this.itemType = itemType;
      this.level = level;
      this.rate = rate;
      this.backRate = backRate;
      this.itemID = itemID;
      this.count = count;
      this.ingot = ingot;
      this.money = money;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int itemType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int level = Tools.getCellIntValue(sheet.getCell("B" + i));
         i = Tools.getCellIntValue(sheet.getCell("C" + i));
         int backRate = Tools.getCellIntValue(sheet.getCell("D" + i));
         int itemID = Tools.getCellIntValue(sheet.getCell("E" + i));
         int count = Tools.getCellIntValue(sheet.getCell("F" + i));
         int money = Tools.getCellIntValue(sheet.getCell("G" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("H" + i));
         String proStr = Tools.getCellValue(sheet.getCell("I" + i));
         int domineering = Tools.getCellIntValue(sheet.getCell("J" + i));
         if (ItemModel.getModel(itemID) == null) {
            throw new Exception("锻造-追加 物品不存在 " + itemID + ", 等级 = " + level);
         }

         if (count < 1) {
            throw new Exception("锻造-追加 物品数量不正确" + count + ", 等级 = " + level);
         }

         if (ingot < 1) {
            throw new Exception("锻造-追加 补充钻石数量不正确" + ingot + ", 等级 = " + level);
         }

         HashMap statDatas = new HashMap();
         String[] starFirstSplits = proStr.split(";");
         String[] var18 = starFirstSplits;
         int var17 = starFirstSplits.length;

         for(int var16 = 0; var16 < var17; ++var16) {
            String fs = var18[var16];
            String[] secondSplits = fs.split(",");
            int statID = Integer.parseInt(secondSplits[0]);
            int value = Integer.parseInt(secondSplits[1]);
            int incremental = Integer.parseInt(secondSplits[2]);
            StatEnum stat = StatEnum.find(statID);
            if (stat == StatEnum.None) {
               throw new Exception(sheet.getName() + ",属性ID不存在 ，第" + i);
            }

            if (value < 1) {
               throw new Exception(sheet.getName() + ",属性值不正确，第 " + i);
            }

            if (incremental < 1) {
               throw new Exception(sheet.getName() + ",属性增量不正确，第 " + i);
            }

            StatEnumData starData = new StatEnumData(stat, value, incremental);
            statDatas.put(stat, starData);
         }

         ZhuijiaForgingData data = new ZhuijiaForgingData(itemType, level, i, backRate, itemID, count, ingot, money);
         Item needShowItem = ItemTools.createItem(itemID, count, 2);
         data.setNeedShowItem(needShowItem);
         data.setStatDatas(statDatas);
         data.setDomineering(domineering);
         datas.put(createKey(itemType, level), data);
         canForgingTypes.add(itemType);
         if (level > maxLevel) {
            maxLevel = level;
         }
      }

      Iterator var26 = canForgingTypes.iterator();

      while(var26.hasNext()) {
         Integer itemType = (Integer)var26.next();
         HashMap lastData = null;

         for(int i = 1; i <= maxLevel; ++i) {
            ZhuijiaForgingData forgingData = getData(itemType.intValue(), i);
            if (forgingData == null) {
               throw new Exception(sheet.getName() + " 数据不完整" + itemType + "," + i);
            }

            if (i != 1 && lastData != null) {
               Iterator var30 = forgingData.getStatDatas().values().iterator();

               while(var30.hasNext()) {
                  StatEnumData data = (StatEnumData)var30.next();
                  if (data.getValue() != data.getIncremental() + ((StatEnumData)lastData.get(data.getStat())).getValue()) {
                     throw new Exception(sheet.getName() + "itemType =" + itemType + "," + i + " 追加越级数据不正确");
                  }
               }
            }

            lastData = forgingData.getStatDatas();
         }
      }

   }

   private static int createKey(int itemType, int level) {
      int key = itemType * 10000 + level;
      return key;
   }

   private static ZhuijiaForgingData getData(int itemType, int level) {
      return (ZhuijiaForgingData)datas.get(createKey(itemType, level));
   }

   public static ZhuijiaForgingData getCurrentData(Item item) {
      return getData(item.getItemType(), item.getZhuijiaLevel());
   }

   public static ZhuijiaForgingData getNextLevelData(Item item) {
      return getData(item.getItemType(), item.getZhuijiaLevel() + 1);
   }

   public static int getMaxLevel() {
      return maxLevel;
   }

   public static void addZhjStatToList(int itemType, int level, List zhjList) {
      ZhuijiaForgingData zhuijiaData = getData(itemType, level);
      if (zhuijiaData != null && zhuijiaData.getStatDatas() != null) {
         Iterator var5 = zhuijiaData.getStatDatas().values().iterator();

         while(var5.hasNext()) {
            StatEnumData data = (StatEnumData)var5.next();
            ItemModify modify = new ItemModify(data.getStat(), data.getValue(), StatModifyPriority.ADD, 1);
            zhjList.add(modify);
         }
      }

   }

   public static List getZhuijiaItemModify(int itemType, int zhuijiaLevel) {
      List fms = null;
      ZhuijiaForgingData zhuijiaData = getData(itemType, zhuijiaLevel);
      ItemModify fm;
      if (zhuijiaData != null && zhuijiaData.getStatDatas() != null) {
         for(Iterator var5 = zhuijiaData.getStatDatas().values().iterator(); var5.hasNext(); fms.add(fm)) {
            StatEnumData data = (StatEnumData)var5.next();
            fm = new ItemModify(data.getStat(), data.getValue(), StatModifyPriority.ADD, 1);
            if (fms == null) {
               fms = new ArrayList();
            }
         }
      }

      return fms;
   }

   public static void addZhjToFinalModify(int itemType, int zhuijiaLevel, List fmList) {
      ZhuijiaForgingData zhuijiaData = getData(itemType, zhuijiaLevel);
      if (zhuijiaData != null && zhuijiaData.getStatDatas() != null) {
         Iterator var5 = zhuijiaData.getStatDatas().values().iterator();

         while(var5.hasNext()) {
            StatEnumData data = (StatEnumData)var5.next();
            FinalModify fm = new FinalModify(data.getStat(), data.getValue(), StatModifyPriority.ADD);
            fmList.add(fm);
         }
      }

   }

   public static int getIncremental(Item item, StatEnum stat) {
      ZhuijiaForgingData data = getData(item.getItemType(), item.getZhuijiaLevel() + 1);
      if (data == null) {
         return 0;
      } else {
         return data.getStatDatas().containsKey(stat) ? ((StatEnumData)data.getStatDatas().get(stat)).getIncremental() : 0;
      }
   }

   public static int getCurrentValue(Item item, StatEnum stat) {
      ZhuijiaForgingData data = getCurrentData(item);
      if (data == null) {
         return 0;
      } else {
         return data.getStatDatas().containsKey(stat) ? ((StatEnumData)data.getStatDatas().get(stat)).getValue() : 0;
      }
   }

   public static int getDomeneering(Item item) {
      ZhuijiaForgingData data = getCurrentData(item);
      return data == null ? 0 : data.getDomineering();
   }

   public static boolean hasZhuijia(int itemType) {
      return canForgingTypes.contains(itemType);
   }

   public HashMap getItemMap() {
      HashMap map = new HashMap();
      map.put(this.itemID, this.count);
      return map;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getRate() {
      return this.rate;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getBackRate() {
      return this.backRate;
   }

   public void setBackRate(int backRate) {
      this.backRate = backRate;
   }

   public int getItemID() {
      return this.itemID;
   }

   public void setItemID(int itemID) {
      this.itemID = itemID;
   }

   public int getCount() {
      return this.count;
   }

   public void setCount(int count) {
      this.count = count;
   }

   public int getIngot() {
      return this.ingot;
   }

   public void setIngot(int ingot) {
      this.ingot = ingot;
   }

   public Item getNeedShowItem() {
      return this.needShowItem;
   }

   public void setNeedShowItem(Item needShowItem) {
      this.needShowItem = needShowItem;
   }

   public int getItemType() {
      return this.itemType;
   }

   public void setItemType(int itemType) {
      this.itemType = itemType;
   }

   public HashMap getStatDatas() {
      return this.statDatas;
   }

   public void setStatDatas(HashMap statDatas) {
      this.statDatas = statDatas;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }
}
