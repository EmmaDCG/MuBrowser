package com.mu.game.model.equip.star;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.ItemTools;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.stats.StatEnum;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jxl.Sheet;

public class StarForgingData {
   private static HashMap forgingDatas = new HashMap();
   private static HashSet canForgingTypes = new HashSet();
   private static int CommonMaxStarLevel = 1;
   private static int HorseMaxStarLevel = 1;
   private int itemType;
   private int starLevel;
   private int rate;
   private int backLevel;
   private int backRate;
   private int protectIngot;
   private int money;
   private List needItems = null;
   private SortedMap luckyMap = null;
   private String des;
   private int popupID;
   private HashMap statDatas = null;
   private int domineering = 0;

   public StarForgingData(int itemType, int starLevel, int rate, int backLevel, int backRate, int protectIngot, int money, int popupID) {
      this.starLevel = starLevel;
      this.rate = rate;
      this.backLevel = backLevel;
      this.backRate = backRate;
      this.protectIngot = protectIngot;
      this.money = money;
      this.popupID = popupID;
   }

   private static int createKey(int itemType, int starLevel) {
      int key = itemType * 10000 + starLevel;
      return key;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int rate;
      for(int i = 2; i <= rows; ++i) {
         int itemType = Tools.getCellIntValue(sheet.getCell("A" + i));
         int starLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         rate = Tools.getCellIntValue(sheet.getCell("C" + i));
         int backLevel = Tools.getCellIntValue(sheet.getCell("D" + i));
         int backRate = Tools.getCellIntValue(sheet.getCell("E" + i));
         int protectIngot = Tools.getCellIntValue(sheet.getCell("F" + i));
         int money = Tools.getCellIntValue(sheet.getCell("G" + i));
         String needStr = Tools.getCellValue(sheet.getCell("H" + i));
         String luckStr = Tools.getCellValue(sheet.getCell("I" + i));
         String proStr = Tools.getCellValue(sheet.getCell("J" + i));
         String des = Tools.getCellValue(sheet.getCell("K" + i));
         int popupID = Tools.getCellIntValue(sheet.getCell("L" + i));
         int domineering = Tools.getCellIntValue(sheet.getCell("M" + i));
         if (rate <= 0) {
            throw new Exception(sheet.getName() + "-强化概率 ，不合格。第" + i + "行");
         }

         if (backLevel < 0 || backRate < 0) {
            throw new Exception(sheet.getName() + "-回退概率、回退等级、销毁概率 ，不合格。第" + i + "行");
         }

         if (protectIngot < 0) {
            throw new Exception(sheet.getName() + "-退回保护钻石数值不正确，第 " + i + " 行");
         }

         if (money <= 0) {
            throw new Exception(sheet.getName() + "-金币不合格。第" + i + "行");
         }

         List itemList = new ArrayList();
         String[] firstSplits = needStr.split(";");
         String[] var21 = firstSplits;
         int var20 = firstSplits.length;

         int itemID;
         for(int var19 = 0; var19 < var20; ++var19) {
            String fs = var21[var19];
            String[] secondSplits = fs.split(",");
            itemID = Integer.parseInt(secondSplits[0]);
            itemID = Integer.parseInt(secondSplits[1]);
            if (ItemModel.getModel(itemID) == null) {
               throw new Exception(sheet.getName() + ",物品不存在 " + i);
            }

            if (itemID < 1) {
               throw new Exception(sheet.getName() + ",物品数量不正确 " + i);
            }

            Item item = ItemTools.createItem(itemID, itemID, 2);
            itemList.add(item);
         }

         SortedMap luckyMap = new TreeMap();
         int var45;
         if (luckStr != null && luckStr.trim().length() > 0) {
            String[] luckySplits = luckStr.trim().split(",");
            String[] var47 = luckySplits;
            var45 = luckySplits.length;

            for(int var43 = 0; var43 < var45; ++var43) {
               String fs = var47[var43];
               itemID = Integer.parseInt(fs);
               ItemModel model = ItemModel.getModel(itemID);
               if (model == null) {
                  throw new Exception(sheet.getName() + "，幸运物品不存在，第" + i + "行");
               }

               if (model.getItemType() != 172 && model.getItemType() != 175) {
                  throw new Exception(sheet.getName() + "，幸运物品类型不正确，第" + i + "行");
               }

               Item item = ItemTools.createItem(itemID, 1, 2);
               luckyMap.put(itemID, item);
            }
         }

         HashMap starDatas = new HashMap();
         String[] starFirstSplits = proStr.split(";");
         String[] var48 = starFirstSplits;
         itemID = starFirstSplits.length;

         for(var45 = 0; var45 < itemID; ++var45) {
            String fs = var48[var45];
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
            starDatas.put(stat, starData);
         }

         setMaxStarLevel(starLevel, itemType);
         StarForgingData data = new StarForgingData(itemType, starLevel, rate, backLevel, backRate, protectIngot, money, popupID);
         data.setLuckyMap(luckyMap);
         data.setNeedItems(itemList);
         data.setDes(des);
         data.setStatDatas(starDatas);
         data.setDomineering(domineering);
         forgingDatas.put(createKey(itemType, starLevel), data);
         canForgingTypes.add(itemType);
      }

      Iterator var32 = canForgingTypes.iterator();

      while(var32.hasNext()) {
         Integer itemType = (Integer)var32.next();
         HashMap lastData = null;

         for(rate = 1; rate <= getMaxStarLevel(itemType.intValue()); ++rate) {
            StarForgingData forgingData = getForgingData(itemType.intValue(), rate);
            if (forgingData == null) {
               throw new Exception(sheet.getName() + " 数据不完整" + itemType + "," + rate);
            }

            if (rate != 1 && lastData != null) {
               HashMap tmpData = forgingData.getStatDatas();
               Iterator var37 = tmpData.values().iterator();

               while(var37.hasNext()) {
                  StatEnumData data = (StatEnumData)var37.next();
                  if (data.getValue() != ((StatEnumData)lastData.get(data.getStat())).getValue() + data.getIncremental()) {
                     throw new Exception(sheet.getName() + " itemType = " + itemType + "," + rate + " , 锻造越级数据不正确");
                  }
               }
            }

            lastData = forgingData.getStatDatas();
         }
      }

   }

   private static void setMaxStarLevel(int starLevel, int itemType) {
      if (itemType != 24) {
         if (CommonMaxStarLevel < starLevel) {
            CommonMaxStarLevel = starLevel;
         }
      } else if (HorseMaxStarLevel < starLevel) {
         HorseMaxStarLevel = starLevel;
      }

   }

   public static int getMaxStarLevel(int itemType) {
      return itemType == 24 ? HorseMaxStarLevel : CommonMaxStarLevel;
   }

   public static int getCommonMaxStraLevel() {
      return CommonMaxStarLevel;
   }

   public static int getHorseMaxStarLevel() {
      return HorseMaxStarLevel;
   }

   public static StarForgingData getForgingData(int itemType, int starLevel) {
      return (StarForgingData)forgingDatas.get(createKey(itemType, starLevel));
   }

   public static StarForgingData getNextLevelData(Item item) {
      return (StarForgingData)forgingDatas.get(createKey(item.getItemType(), item.getStarLevel() + 1));
   }

   public static int getCurrentValue(Item item, StatEnum stat) {
      StarForgingData data = getForgingData(item.getItemType(), item.getStarLevel());
      if (data == null) {
         return 0;
      } else {
         return data.getStatDatas().containsKey(stat) ? ((StatEnumData)data.getStatDatas().get(stat)).getValue() : 0;
      }
   }

   public static int getIncremental(Item item, StatEnum stat) {
      StarForgingData data = getForgingData(item.getItemType(), item.getStarLevel() + 1);
      if (data == null) {
         return 0;
      } else {
         return data.getStatDatas().containsKey(stat) ? ((StatEnumData)data.getStatDatas().get(stat)).getIncremental() : 0;
      }
   }

   public static int getDomineering(Item item) {
      StarForgingData data = getForgingData(item.getItemType(), item.getStarLevel());
      return data == null ? 0 : data.getDomineering();
   }

   public static boolean canForging(int itemType) {
      return canForgingTypes.contains(itemType);
   }

   public HashMap getNeedMap() {
      HashMap map = new HashMap();
      Iterator var3 = this.needItems.iterator();

      while(var3.hasNext()) {
         Item item = (Item)var3.next();
         map.put(item.getModelID(), item.getCount());
      }

      return map;
   }

   public boolean needToPopup() {
      return this.getPopupID() > 0;
   }

   public int getStarLevel() {
      return this.starLevel;
   }

   public void setStarLevel(int starLevel) {
      this.starLevel = starLevel;
   }

   public int getRate() {
      return this.rate;
   }

   public void setRate(int rate) {
      this.rate = rate;
   }

   public int getBackLevel() {
      return this.backLevel;
   }

   public void setBackLevel(int backLevel) {
      this.backLevel = backLevel;
   }

   public int getBackRate() {
      return this.backRate;
   }

   public void setBackRate(int backRate) {
      this.backRate = backRate;
   }

   public int getProtectIngot() {
      return this.protectIngot;
   }

   public void setProtectIngot(int protectIngot) {
      this.protectIngot = protectIngot;
   }

   public int getMoney() {
      return this.money;
   }

   public void setMoney(int money) {
      this.money = money;
   }

   public List getNeedItems() {
      return this.needItems;
   }

   public void setNeedItems(List needItems) {
      this.needItems = needItems;
   }

   public SortedMap getLuckyMap() {
      return this.luckyMap;
   }

   public void setLuckyMap(SortedMap luckyMap) {
      this.luckyMap = luckyMap;
   }

   public int getPopupID() {
      return this.popupID;
   }

   public void setPopupID(int popupID) {
      this.popupID = popupID;
   }

   public String getDes() {
      return this.des;
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public void setDes(String des) {
      this.des = des;
   }

   public HashMap getStatDatas() {
      return this.statDatas;
   }

   public void setStatDatas(HashMap statDatas) {
      this.statDatas = statDatas;
   }

   public int getItemType() {
      return this.itemType;
   }

   public void setItemType(int itemType) {
      this.itemType = itemType;
   }
}
