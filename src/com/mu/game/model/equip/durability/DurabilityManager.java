package com.mu.game.model.equip.durability;

import com.mu.config.Constant;
import com.mu.game.model.item.Item;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class DurabilityManager {
   private static HashMap typeDatas = new HashMap();
   private static HashMap qualityDatas = new HashMap();
   private static HashMap levelDatas = new HashMap();
   private static int Repair_BasicNumber = 1000;
   private static int consumeRate = 50000;
   private static int intevalTime = 10000;
   public static final int DiePercent = 10000;
   public static final int BeAttackedDura = 1;
   public static final int AlertPercent_Yellow = 30000;
   public static final int AlertPercent_Red = 0;
   public static final int AlertType_None = 1;
   public static final int AlertType_Yellow = 2;
   public static final int AlertType_Red = 3;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet typeSheet = wb.getSheet(1);
      initType(typeSheet);
      Sheet qualitySheet = wb.getSheet(2);
      initQuality(qualitySheet);
      initLevel(wb.getSheet(3));
      Sheet otherSheet = wb.getSheet(4);
      initOther(otherSheet);
   }

   private static void initType(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int itemType = Tools.getCellIntValue(sheet.getCell("A" + i));
         float parameter = Tools.getCellFloatValue(sheet.getCell("B" + i));
         int treatmentType = Tools.getCellIntValue(sheet.getCell("C" + i));
         DurabilityData data = new DurabilityData(itemType, parameter, treatmentType);
         typeDatas.put(itemType, data);
      }

   }

   private static void initQuality(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int quality = Tools.getCellIntValue(sheet.getCell("A" + i));
         float parameter = Tools.getCellFloatValue(sheet.getCell("B" + i));
         qualityDatas.put(quality, parameter);
      }

   }

   private static void initLevel(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int level = Tools.getCellIntValue(sheet.getCell("A" + i));
         float parameter = Tools.getCellFloatValue(sheet.getCell("B" + i));
         levelDatas.put(level, parameter);
      }

   }

   private static void initOther(Sheet sheet) throws Exception {
      consumeRate = Tools.getCellIntValue(sheet.getCell("B1"));
      intevalTime = Tools.getCellIntValue(sheet.getCell("B2"));
      Repair_BasicNumber = Tools.getCellIntValue(sheet.getCell("B3"));
   }

   public static boolean canRepair(int itemType) {
      return typeDatas.containsKey(itemType);
   }

   public static DurabilityData getDurabilityData(int itemType) {
      return (DurabilityData)typeDatas.get(itemType);
   }

   public static int getConsumeRate() {
      return consumeRate;
   }

   public static int getIntevalTime() {
      return intevalTime;
   }

   public static int getAlertType(int dura, int maxDura) {
      if (maxDura < 1) {
         return 3;
      } else if (dura < 1) {
         return 3;
      } else {
         int percent = Constant.getPercent(dura, maxDura);
         return percent < 30000 ? 2 : 1;
      }
   }

   public static boolean hasTypeData(int itemType) {
      return typeDatas.containsKey(itemType);
   }

   public static float getTypeParam(int itemType) {
      DurabilityData data = (DurabilityData)typeDatas.get(itemType);
      return data == null ? 0.0F : data.getParameter();
   }

   public static float getLevelParam(int level) {
      return levelDatas.containsKey(level) ? ((Float)levelDatas.get(level)).floatValue() : 1.0F;
   }

   public static float getQualityParam(int quality) {
      return qualityDatas.containsKey(quality) ? ((Float)qualityDatas.get(quality)).floatValue() : 1.0F;
   }

   public static int getRepairNeedMoney(Item item) {
      float money = (float)Repair_BasicNumber * getTypeParam(item.getItemType()) * getQualityParam(item.getQuality());
      money *= getLevelParam(item.getLevel());
      money = money * (float)(item.getMaxDurability() - item.getDurability()) / (float)item.getMaxDurability();
      return (int)money;
   }
}
