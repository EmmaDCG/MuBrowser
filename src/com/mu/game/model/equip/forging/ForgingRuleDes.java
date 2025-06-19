package com.mu.game.model.equip.forging;

import com.mu.game.model.item.Item;
import com.mu.io.game.packet.imp.skill.AddSkill;
import com.mu.utils.Tools;
import java.math.BigDecimal;
import java.util.HashMap;
import jxl.Sheet;

public class ForgingRuleDes {
   private static final int Number_hundredMillion = 100000000;
   private static final int Number_tenMillion = 10000000;
   private static final int Number_tenThousand = 10000;
   private static String Number_hundredMillion_Str = "亿";
   private static String Number_tenMillion_Str = "千万";
   private static String Number_tenThousand_Str = "万";
   public static String StrengthRuleDes = "";
   public static String RunehRuleDes = "";
   public static String InheriteRuleDes = "";
   public static String ZhujiaRuleDes = "";
   public static String StoneMosaicRuleDes = "";
   public static String StoneConverRuleDes = "";
   public static String CompositeRuleDes = "";
   public static String UpdateDes = "";
   public static String Suffix_Master = "大师级";
   public static String Suffix_Lucky = "幸运";
   public static String Suffix_Stats = "属性";
   public static HashMap Prefix_Excellents = new HashMap();

   public static void init(Sheet sheet) throws Exception {
      StrengthRuleDes = Tools.getCellValue(sheet.getCell("B1"));
      RunehRuleDes = Tools.getCellValue(sheet.getCell("B2"));
      InheriteRuleDes = Tools.getCellValue(sheet.getCell("B3"));
      ZhujiaRuleDes = Tools.getCellValue(sheet.getCell("B4"));
      StoneMosaicRuleDes = Tools.getCellValue(sheet.getCell("B5"));
      StoneConverRuleDes = Tools.getCellValue(sheet.getCell("B6"));
      CompositeRuleDes = Tools.getCellValue(sheet.getCell("B7"));
      UpdateDes = Tools.getCellValue(sheet.getCell("B8"));
   }

   public static void initSuffix(Sheet sheet) throws Exception {
      Number_hundredMillion_Str = Tools.getCellValue(sheet.getCell("A2"));
      Number_tenMillion_Str = Tools.getCellValue(sheet.getCell("B2"));
      Number_tenThousand_Str = Tools.getCellValue(sheet.getCell("C2"));
      Suffix_Lucky = Tools.getCellValue(sheet.getCell("D2"));
      Suffix_Stats = Tools.getCellValue(sheet.getCell("E2"));
      Suffix_Master = Tools.getCellValue(sheet.getCell("B3"));
      int rows = sheet.getRows();

      for(int i = 4; i <= rows; ++i) {
         int count = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         Prefix_Excellents.put(count, name);
      }

   }

   private static String getNumName(Item item) {
      BigDecimal bg = null;
      double number = (double)item.getCount();
      String suffix = "";
      if (number >= 1.0E8D) {
         bg = new BigDecimal(number / 1.0E8D);
         suffix = Number_hundredMillion_Str;
      } else if (number >= 1.0E7D) {
         bg = new BigDecimal(number / 1.0E7D);
         suffix = Number_tenMillion_Str;
      } else if (number >= 10000.0D) {
         bg = new BigDecimal(number / 10000.0D);
         suffix = Number_tenThousand_Str;
      } else {
         bg = new BigDecimal(number);
      }

      number = bg.setScale(1, 1).doubleValue();
      return AddSkill.getDyString(number) + suffix;
   }

   public static String assemItemShowName(Item item) {
      if (!item.isEquipment()) {
         return item.getModel().isNumbericType() ? item.getModel().getName() : item.getModel().getName();
      } else {
         int excellentSize = item.getBonusStatSize(3);
         String name = getPrefixName(excellentSize);
         if (name.length() > 0) {
            name = name + " ";
         }

         name = name + item.getModel().getName();
         int masterSize = item.getBonusStatSize(5);
         if (masterSize > 0) {
            name = name + "(" + Suffix_Master + ")";
         }

         if (item.getStarLevel() > 0) {
            name = name + " +" + item.getStarLevel();
         }

         return name;
      }
   }

   public static String getDropItemShowName(Item item) {
      String name = item.getName();
      if (!item.isEquipment()) {
         return name;
      } else {
         if (item.getZhuijiaLevel() > 0) {
            name = name + " +" + Suffix_Stats;
         }

         if (item.getBonusStatSize(2) > 0) {
            name = name + " +" + Suffix_Lucky;
         }

         return name;
      }
   }

   public static String getPrefixName(int excellentSize) {
      return Prefix_Excellents.containsKey(excellentSize) ? (String)Prefix_Excellents.get(excellentSize) : "";
   }
}
