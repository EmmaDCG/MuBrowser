package com.mu.game.model.unit.player.hang;

import com.mu.config.VariableConstant;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.shop.Goods;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class HangConfig {
   private static HashMap hpDrugMap = new HashMap();
   private static HashMap mpDrugMap = new HashMap();
   public static int DrugBuyNumber = 200;
   private static HashMap lowTicketMap = new HashMap();
   private static HashMap noSaleEquipType = new HashMap();
   private static HashMap noSaleModelMap = new HashMap();

   public static void init(Sheet sheet) throws Exception {
      String lowTicketStr = Tools.getCellValue(sheet.getCell("B1"));
      String noSaleEquipStr = Tools.getCellValue(sheet.getCell("B2"));
      String noSaleModelStr = Tools.getCellValue(sheet.getCell("B3"));
      String[] splits = lowTicketStr.split(",");
      String[] var8 = splits;
      int type = splits.length;

      String ss;
      int i;
      for(i = 0; i < type; ++i) {
         ss = var8[i];
         lowTicketMap.put(Integer.parseInt(ss), Integer.valueOf(1));
      }

      splits = noSaleEquipStr.split(",");
      var8 = splits;
      type = splits.length;

      for(i = 0; i < type; ++i) {
         ss = var8[i];
         noSaleEquipType.put(Integer.parseInt(ss), Integer.valueOf(1));
      }

      splits = noSaleModelStr.split(",");
      var8 = splits;
      type = splits.length;

      for(i = 0; i < type; ++i) {
         ss = var8[i];
         noSaleModelMap.put(Integer.parseInt(ss), Integer.valueOf(1));
      }

      int rows = sheet.getRows();
      i = 5;

      while(i <= rows) {
         type = Tools.getCellIntValue(sheet.getCell("A" + i));
         int minLevel = Tools.getCellIntValue(sheet.getCell("B" + i));
         int maxLevel = Tools.getCellIntValue(sheet.getCell("C" + i));
         int modelID = Tools.getCellIntValue(sheet.getCell("D" + i));
         int price = Tools.getCellIntValue(sheet.getCell("E" + i));
         if (minLevel >= 1 && maxLevel <= VariableConstant.Max_Level_2Exp) {
            if (price < 1) {
               throw new Exception("挂机配置金钱出错,第" + i + "行");
            }

            ItemModel model = ItemModel.getModel(modelID);
            if (model != null && (model.getItemType() == 31 || model.getItemType() == 32)) {
               for(int level = minLevel; level <= maxLevel; ++level) {
                  if (type == 1 && hpDrugMap.containsKey(level) || type == 2 && mpDrugMap.containsKey(level)) {
                     throw new Exception("挂机配置，等级重复，第" + i + "行");
                  }
               }

               Goods goods = new Goods();
               goods.setModelID(modelID);
               goods.setMoneyType(1);
               goods.setPrice(price);
               int level;
               if (type == 1) {
                  for(level = minLevel; level <= maxLevel; ++level) {
                     hpDrugMap.put(level, goods);
                  }
               } else {
                  for(level = minLevel; level <= maxLevel; ++level) {
                     mpDrugMap.put(level, goods);
                  }
               }

               ++i;
               continue;
            }

            throw new Exception("挂机配置物品出错，第" + i + "行");
         }

         throw new Exception("挂机配置等级出错,第" + i + "行");
      }

      for(i = 1; i <= VariableConstant.Max_Level_2Exp; ++i) {
         if (!hpDrugMap.containsKey(i) || !mpDrugMap.containsKey(i)) {
            throw new Exception("挂机配置，等级配置不足");
         }
      }

   }

   public static Goods getHpDrug(int level) {
      return (Goods)hpDrugMap.get(level);
   }

   public static Goods getMpDrug(int level) {
      return (Goods)mpDrugMap.get(level);
   }

   public static boolean isLowTicket(int modelID) {
      return lowTicketMap.containsKey(modelID);
   }

   public static boolean isNoSaleEquipType(int itemType) {
      return noSaleEquipType.containsKey(itemType);
   }

   public static boolean isNoSaleModel(int modelID) {
      return noSaleModelMap.containsKey(modelID);
   }
}
