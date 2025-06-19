package com.mu.game.model.equip.compositenew.condition;

import com.mu.game.model.equip.compositenew.condition.imp.EquipSetCondition;
import com.mu.game.model.equip.compositenew.condition.imp.ExcellentEquipCondition;
import com.mu.game.model.equip.compositenew.condition.imp.ProfessionCondition;
import com.mu.game.model.equip.compositenew.condition.imp.SocketCondition;
import com.mu.game.model.equip.compositenew.condition.imp.StarLevelCondition;
import com.mu.game.model.equip.compositenew.condition.imp.ZhuijiaCondition;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.HashSet;
import jxl.Sheet;

public class MaterialConFactory {
   public static final int ConditionType_Profession = 1;
   public static final int ConditionType_StarLevel = 2;
   public static final int ConditionType_ZhuijiaLevel = 3;
   public static final int ConditionType_EquipSet = 4;
   public static final int ConditionType_ExcellentEquip = 5;
   public static final int ConditionType_Socket = 6;
   private static HashMap conditionMap = new HashMap();

   public static void initCondition(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int conID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int conTypeID = Tools.getCellIntValue(sheet.getCell("B" + i));
         String value = Tools.getCellValue(sheet.getCell("C" + i));
         MaterialCondition condition = createCondition(conID, conTypeID, value);
         if (condition == null) {
            throw new Exception(sheet.getName() + "--合成条件类型有错，第 " + i + "行");
         }

         conditionMap.put(condition.getConID(), condition);
      }

   }

   private static MaterialCondition createCondition(int conID, int conTypeID, String value) throws Exception {
      switch(conTypeID) {
      case 1:
         return createProfession(conID, value);
      case 2:
         return createStarLevel(conID, value);
      case 3:
         return createZhuijiaLevel(conID, value);
      case 4:
         return new EquipSetCondition(conID);
      case 5:
         return createExcellentEquip(conID, value);
      case 6:
         return new SocketCondition(conID);
      default:
         return null;
      }
   }

   private static MaterialCondition createExcellentEquip(int conID, String value) throws Exception {
      int excellentCount = Integer.parseInt(value);
      if (excellentCount < 1) {
         throw new Exception("合成-材料条件集合 ，填写错误 " + conID);
      } else {
         return new ExcellentEquipCondition(conID, excellentCount);
      }
   }

   private static MaterialCondition createProfession(int conID, String value) throws Exception {
      String[] splits = value.split(",");
      HashSet proSet = new HashSet();
      String[] var7 = splits;
      int var6 = splits.length;

      for(int var5 = 0; var5 < var6; ++var5) {
         String proStr = var7[var5];
         Integer proID = Integer.parseInt(proStr);
         if (Profession.getProfession(proID.intValue()) == null) {
            throw new Exception("合成-材料条件集合 ，填写错误 " + conID);
         }

         proSet.add(proID);
      }

      if (proSet.size() < 1) {
         throw new Exception("合成-材料条件集合 ，填写错误 " + conID);
      } else {
         return new ProfessionCondition(conID, proSet);
      }
   }

   private static MaterialCondition createStarLevel(int conID, String value) throws Exception {
      int starLevel = Integer.parseInt(value);
      if (starLevel < 1) {
         throw new Exception("合成-材料条件集合 ，填写错误 " + conID);
      } else {
         return new StarLevelCondition(conID, starLevel);
      }
   }

   private static MaterialCondition createZhuijiaLevel(int conID, String value) throws Exception {
      int zhuijiaLevel = Integer.parseInt(value);
      if (zhuijiaLevel < 1) {
         throw new Exception("合成-材料条件集合 ，填写错误 " + conID);
      } else {
         return new ZhuijiaCondition(conID, zhuijiaLevel);
      }
   }

   public static MaterialCondition getCondition(int conID) {
      return (MaterialCondition)conditionMap.get(conID);
   }
}
