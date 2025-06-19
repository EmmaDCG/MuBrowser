package com.mu.game.model.item.box;

import com.mu.game.model.item.box.magic.MagicAtom;
import com.mu.game.model.item.box.magic.MagicManager;
import com.mu.game.model.item.box.magic.market.MagicMarketGoods;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.weight.WeightElement;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class BoxManager {
   private static HashMap boxItemMap = new HashMap();

   public static void init(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      initBoxItem(sheet);
      MagicAtom.initMagicAtom(wb.getSheet(2));
      MagicManager.init(wb.getSheet(3));
      MagicMarketGoods.init(wb.getSheet(5));
   }

   private static void initBoxItem(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int boxID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String weightStr = Tools.getCellValue(sheet.getCell("B" + i));
         String rateStr = Tools.getCellValue(sheet.getCell("C" + i));
         String rateCountStr = Tools.getCellValue(sheet.getCell("D" + i));
         String openNeedItemStr = Tools.getCellValue(sheet.getCell("E" + i));
         int ingot = Tools.getCellIntValue(sheet.getCell("G" + i));
         boolean firecracker = Tools.getCellIntValue(sheet.getCell("H" + i)) == 1;
         boolean tip = Tools.getCellFloatValue(sheet.getCell("I" + i)) == 1.0F;
         String des = sheet.getName() + "第" + i + "行-";
         WeightElement weightItems = createWeightElement(weightStr, des);
         List rateItems = null;
         int rateCount = 0;
         if (rateStr != null && rateStr.trim().length() > 0) {
            rateItems = new ArrayList();
            String[] splits = rateStr.split(";");
            String[] var19 = splits;
            int var18 = splits.length;

            for(int var17 = 0; var17 < var18; ++var17) {
               String spString = var19[var17];
               BoxItemAtom atom = BoxItemAtom.createAtom(des + "-概率物品 ", spString);
               atom.setMinWeight(1);
               rateItems.add(atom);
            }

            rateCount = Integer.parseInt(rateCountStr);
         }

         int openNeedItemId = -1;
         int openNeedCount = 0;
         if (openNeedItemStr != null && openNeedItemStr.trim().length() > 0) {
            openNeedItemId = Integer.parseInt(openNeedItemStr);
            openNeedCount = Tools.getCellIntValue(sheet.getCell("F" + i));
         }

         BoxItem boxItem = new BoxItem(boxID);
         boxItem.setOpenNeedCount(openNeedCount);
         boxItem.setOpenNeedItemId(openNeedItemId);
         boxItem.setRateItems(rateItems);
         boxItem.setRateCount(rateCount);
         boxItem.setWeightItems(weightItems);
         boxItem.setIngot(ingot);
         boxItem.setFirecracker(firecracker);
         boxItem.setTip(tip);
         boxItem.check(des);
         boxItemMap.put(boxItem.getBoxID(), boxItem);
      }

   }

   public static WeightElement createWeightElement(String weightStr, String des) throws Exception {
      WeightElement weightItems = null;
      if (weightStr != null && weightStr.trim().length() > 0) {
         weightItems = new WeightElement();
         String[] splits = weightStr.split(";");
         String[] var7 = splits;
         int var6 = splits.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String spString = var7[var5];
            BoxItemAtom atom = BoxItemAtom.createAtom(des + "-权重物品 ", spString);
            weightItems.addAtom(atom);
         }

         weightItems.sortByWeight(des);
      }

      return weightItems;
   }

   public static BoxItem getBoxItem(int boxID) {
      return (BoxItem)boxItemMap.get(boxID);
   }

   public static boolean hasBoxItem(int boxID) {
      return boxItemMap.containsKey(boxID);
   }

   public static List getItemData(int boxID) {
      BoxItem boxItem = getBoxItem(boxID);
      return boxItem == null ? null : boxItem.getItemData();
   }

   public static int canOpenBox(Player player, int boxID) {
      BoxItem boxItem = getBoxItem(boxID);
      if (boxItem == null) {
         return 3040;
      } else if (boxItem.getOpenNeedItemId() != -1 && !player.getBackpack().hasEnoughItem(boxItem.getOpenNeedItemId(), boxItem.getOpenNeedCount())) {
         return 3001;
      } else {
         return boxItem.getIngot() > 0 && player.getIngot() < boxItem.getIngot() ? 1015 : 1;
      }
   }
}
