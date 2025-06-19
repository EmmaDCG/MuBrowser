package com.mu.game.model.shop;

import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import jxl.Sheet;
import jxl.Workbook;

public class ShopConfigure {
   public static final int GoodGroupType_Repurchage = 0;
   public static final int GoodGroupType_Common = 1;
   public static final int ShopId_Vip = 10000;
   public static final int ShopType_Common = 1;
   public static final int ShopType_Aran = 2;
   public static final int ShopType_Vip = 3;
   private static HashMap labels = new HashMap();
   private static HashMap shopTypeNames = new HashMap();
   private static HashMap allShopItems = new HashMap();
   private static HashMap goodsMap = new HashMap();

   private static void initLabelNames(Sheet sheet) {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int labelId = Tools.getCellIntValue(sheet.getCell("A" + i));
         String labelName = sheet.getCell("B" + i).getContents();
         if (labelName == null) {
            labelName = "";
         }

         int labelSort = Tools.getCellIntValue(sheet.getCell("C" + i));
         labels.put(labelId, new GoodsLabel(labelId, labelName, labelSort));
      }

   }

   private static void initShopTypeNames(Sheet sheet) {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int shopType = Tools.getCellIntValue(sheet.getCell("A" + i));
         String shopTypeName = sheet.getCell("B" + i).getContents();
         if (shopTypeName == null) {
            shopTypeName = "";
         }

         shopTypeNames.put(shopType, shopTypeName);
      }

   }

   public static String getLalelName(int labelId) {
      GoodsLabel gl = (GoodsLabel)labels.get(labelId);
      return gl == null ? "普通商品" : gl.getLabelName();
   }

   public static int getLabelSort(int labelID) {
      GoodsLabel gl = (GoodsLabel)labels.get(labelID);
      return gl == null ? 1 : gl.getLabelSort();
   }

   public static String getShopTypeName(int shopType) {
      String name = (String)shopTypeNames.get(shopType);
      if (name == null) {
         name = "普通商店";
      }

      return name;
   }

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet[] sheets = wb.getSheets();
      Sheet labelSheet = sheets[1];
      initLabelNames(labelSheet);
      Sheet shopTypeSheet = sheets[2];
      initShopTypeNames(shopTypeSheet);

      for(int i = 3; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            Goods goods = new Goods();
            int shopId = Tools.getCellIntValue(sheet.getCell("B" + j));
            int label = Tools.getCellIntValue(sheet.getCell("C" + j));
            goods.setLabel(label);
            goods.setModelID(Tools.getCellIntValue(sheet.getCell("D" + j)));
            goods.setPrice(Tools.getCellIntValue(sheet.getCell("E" + j)));
            goods.setMoneyType(Tools.getCellIntValue(sheet.getCell("F" + j)));
            int ruleID = Tools.getCellIntValue(sheet.getCell("G" + j));
            goods.setStatRuleID(ruleID);
            boolean flag = goods.entity();
            if (!flag) {
               throw new Exception("ShopConfigure " + i);
            }

            addGoods(shopId, label, goods);
         }
      }

   }

   private static void addGoods(int shopId, int label, Goods goods) {
      SortedMap goodMap = (SortedMap)allShopItems.get(shopId);
      if (goodMap == null) {
         goodMap = new TreeMap();
         allShopItems.put(shopId, goodMap);
      }

      List goodList = (List)((SortedMap)goodMap).get(label);
      if (goodList == null) {
         goodList = new ArrayList();
         ((SortedMap)goodMap).put(label, goodList);
      }

      ((List)goodList).add(goods);
      goodsMap.put(goods.getGoodsID(), goods);
   }

   public static SortedMap getShopItems(int shopId) {
      return (SortedMap)allShopItems.get(shopId);
   }

   public static Goods getGoods(long goodsID) {
      return (Goods)goodsMap.get(goodsID);
   }
}
