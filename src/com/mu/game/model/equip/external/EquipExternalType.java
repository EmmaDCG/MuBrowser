package com.mu.game.model.equip.external;

import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.HashSet;
import jxl.Sheet;

public class EquipExternalType {
   private static HashSet allExeternalTypes = new HashSet();
   private static HashMap externalTypes = new HashMap();
   public static final int HEAD = 1;
   public static final int CLOTHES = 2;
   public static final int ELBOW = 3;
   public static final int TROUSERS = 4;
   public static final int FOOT = 5;
   public static final int WEAPON_L = 6;
   public static final int WEAPON_R = 7;
   public static final int WING = 8;
   public static final int RIDING = 9;
   public static final int Quiver = 10;
   public static final int Guardian = 12;
   public static final int ArmorEffect_Chest = 13;
   public static final int ArmorEffect_Foot = 14;
   private int profession;
   private int type;
   private int modelID;

   public EquipExternalType(int profession, int type, int modelID) {
      this.profession = profession;
      this.type = type;
      this.modelID = modelID;
   }

   private static int getKey(int profession, int type) {
      return profession * 1000 + type;
   }

   public static HashSet getAllExternalType() {
      return allExeternalTypes;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int profession = Tools.getCellIntValue(sheet.getCell("A" + i));
         int type = Tools.getCellIntValue(sheet.getCell("B" + i));
         int defaultModelID = Tools.getCellIntValue(sheet.getCell("C" + i));
         EquipExternalType eet = new EquipExternalType(profession, type, defaultModelID);
         externalTypes.put(getKey(profession, type), eet);
         allExeternalTypes.add(type);
      }

      if (allExeternalTypes.size() < 1) {
         throw new Exception("职业外形类型没有初始化。。。。");
      }
   }

   public static int getDefaulModelID(int profession, int type) {
      int key = getKey(profession, type);
      return externalTypes.containsKey(key) ? ((EquipExternalType)externalTypes.get(key)).getModelID() : 0;
   }

   public int getProfession() {
      return this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }
}
