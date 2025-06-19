package com.mu.game.model.item.model;

import com.mu.db.log.IngotChangeType;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;

public class ItemSource {
   private static HashMap sources = new HashMap();
   public static final int Source_Drop = 2;
   public static final int Source_Compo = 3;
   public static final int Source_Transaction = 4;
   public static final int Source_Task = 5;
   public static final int Source_shop = 7;
   public static final int Source_Gang = 8;
   public static final int Source_Arena = 11;
   public static final int Source_Split = 12;
   public static final int Source_XunBao = 13;
   public static final int Source_Discard = 14;
   public static final int Source_Stack = 15;
   public static final int Source_Position = 16;
   public static final int Source_ContainerChange = 17;
   public static final int Source_SellToNpc = 18;
   public static final int Source_Repurchase = 19;
   public static final int Source_UseItem = 20;
   public static final int Source_Forging = 21;
   public static final int Source_Collect = 22;
   public static final int Source_Redeem = 23;
   public static final int Source_StoneConver = 24;
   public static final int Source_Durability = 25;
   public static final int Source_Wing = 26;
   public static final int Source_Mail = 27;
   public static final int Source_OpenBox = 28;
   public static final int Source_PET = 29;
   public static final int Source_Market = 30;
   public static final int Source_Dungeon = 31;
   public static final int Source_GM = 32;
   public static final int Source_MALL = 33;
   public static final int Source_Expired = 34;
   public static final int Source_SHIELD = 35;
   public static final int Source_MagicItem = 36;
   public static final int Source_OpenBlueVip = 37;
   public static final int Source_SevenDay = 38;
   public static final int Source_GangDonate = 39;
   public static final int Source_SpiriteRefine = 40;
   public static final int Source_TanXian = 41;
   public static final int Source_Hallow = 42;
   public static final int Source_TanxianChest = 43;
   public static final int Source_Other = 100;

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      init(wb.getSheet(1));
      IngotChangeType.init(wb.getSheet(2));
   }

   private static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int sourceID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String des = Tools.getCellValue(sheet.getCell("B" + i));
         sources.put(sourceID, des);
      }

   }

   public static String getSourceName(int sourceID) {
      return sources.containsKey(sourceID) ? (String)sources.get(sourceID) : "other";
   }
}
