package com.mu.game.model.unit.player.sevenDay;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemDataUnit;
import com.mu.game.model.item.ItemTools;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class SevenDayTreasureData extends ItemDataUnit {
   public static final int MaxNumber = 7;
   private static HashMap treasureMap = new HashMap();
   private static ArrayList showItems = new ArrayList();
   private int index = 0;

   public SevenDayTreasureData(int modelID, int count, boolean isBind, int index) {
      super(modelID, count, isBind);
      this.index = index;
   }

   public static void init(InputStream is) throws Exception {
      Workbook wb = Workbook.getWorkbook(is);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      int i;
      for(i = 2; i <= rows; ++i) {
         int index = i - 2;
         int modelID = Tools.getCellIntValue(sheet.getCell("A" + i));
         boolean isBind = Tools.getCellIntValue(sheet.getCell("B" + i)) == 1;
         int statID = Tools.getCellIntValue(sheet.getCell("C" + i));
         long expireTime = Tools.getCellLongValue(sheet.getCell("D" + i));
         int count = Tools.getCellIntValue(sheet.getCell("E" + i));
         int dayNumber = Tools.getCellIntValue(sheet.getCell("F" + i));
         SevenDayTreasureData dataUnit = new SevenDayTreasureData(modelID, count, isBind, index);
         dataUnit.setStatRuleID(statID);
         dataUnit.setExpireTime(expireTime);
         Item item = ItemTools.createItem(2, dataUnit);
         if (item == null) {
            throw new Exception(sheet.getName() + ",物品不存在，第 " + i + "行");
         }

         showItems.add(item);
         treasureMap.put(dayNumber, dataUnit);
      }

      for(i = 1; i <= 7; ++i) {
         if (!treasureMap.containsKey(i)) {
            throw new Exception("七日挖宝，第 " + i + "天数据不存在");
         }
      }

   }

   public static SevenDayTreasureData getSevenData(int dayNumber) {
      return (SevenDayTreasureData)treasureMap.get(dayNumber);
   }

   public static boolean hasItemData(int dayNumber) {
      return treasureMap.containsKey(dayNumber);
   }

   public static ArrayList getShowItems() {
      return showItems;
   }

   public int getIndex() {
      return this.index;
   }

   public int getShowIndex() {
      return this.getIndex();
   }

   public static Item getShowItem(int index) {
      return (Item)showItems.get(index);
   }

   public static HashMap getTreasureMap() {
      return treasureMap;
   }

   public ItemDataUnit getUnit() {
      ItemDataUnit data = new ItemDataUnit(this.getModelID(), this.getCount(), this.isBind());
      data.setSource(38);
      data.setStatRuleID(this.getStatRuleID());
      data.setExpireTime(this.getExpireTime());
      return data;
   }
}
