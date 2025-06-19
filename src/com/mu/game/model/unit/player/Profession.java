package com.mu.game.model.unit.player;

import com.mu.config.VariableConstant;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;

public class Profession {
   public static final int ProfessionID_Other = 100;
   public static final int Type_Other = 10;
   public static final int Type_Warrior = 0;
   public static final int Type_Wielder = 1;
   public static final int Type_Archer = 2;
   public static final int[] proTypes = new int[]{0, 1, 2};
   public static int professionMaxLevel = 2;
   private int proID;
   private int proType;
   private int proLevel;
   private String proName;
   private int potential;
   private int header;
   private int gender = 0;
   private int loginImg = -1;
   private static HashMap professions = new HashMap();
   private static HashMap typeLevelToID = new HashMap();
   private static HashMap typeIDs = new HashMap();
   private static HashMap transferPotentials = new HashMap();

   public Profession(int proID, int proType, int proLevel, String proName, int potential, int header, int gender, int loginImg) {
      this.proID = proID;
      this.proType = proType;
      this.proLevel = proLevel;
      this.proName = proName;
      this.potential = potential;
      this.header = header;
      this.gender = gender;
      this.loginImg = loginImg;
   }

   private static int createKey(int proType, int proLevel) {
      int key = (proType + 1) * 1000 + proLevel + 1;
      return key;
   }

   public static void initTransferPotentials(Sheet sheet) throws Exception {
      int minLevelToAddPotential = Tools.getCellIntValue(sheet.getCell("B1"));
      if (minLevelToAddPotential < 1) {
         throw new Exception("角色升级表 - " + sheet.getName() + "-最小增加自由点数等级填写错误");
      } else {
         VariableConstant.MinLevelToAddPotential = minLevelToAddPotential;
      }
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      int proType;
      int proID;
      for(proType = 2; proType <= rows; ++proType) {
         proID = Tools.getCellIntValue(sheet.getCell("A" + proType));
         proType = Tools.getCellIntValue(sheet.getCell("B" + proType));
         int proLevel = Tools.getCellIntValue(sheet.getCell("C" + proType));
         String proName = Tools.getCellValue(sheet.getCell("D" + proType));
         int potential = Tools.getCellIntValue(sheet.getCell("E" + proType));
         int header = Tools.getCellIntValue(sheet.getCell("F" + proType));
         int gender = Tools.getCellIntValue(sheet.getCell("G" + proType));
         int li = Tools.getCellIntValue(sheet.getCell("H" + proType));
         Profession pro = new Profession(proID, proType, proLevel, proName, potential, header, gender, li);
         professions.put(proID, pro);
         if (proLevel > professionMaxLevel) {
            professionMaxLevel = proLevel;
         }

         int key = createKey(proType, proLevel);
         typeLevelToID.put(key, proID);
         List typeIDList = (List)typeIDs.get(proType);
         if (typeIDList == null) {
            typeIDList = new ArrayList();
            typeIDs.put(proType, typeIDList);
         }

         ((List)typeIDList).add(proID);
      }

      int[] var14 = proTypes;
      proType = proTypes.length;

      for(proID = 0; proID < proType; ++proID) {
         proType = var14[proID];

         for(int i = 0; i <= professionMaxLevel; ++i) {
            boolean has = false;
            Iterator var18 = professions.values().iterator();

            while(var18.hasNext()) {
               Profession pro = (Profession)var18.next();
               if (pro.getProType() == proType && pro.getProLevel() == i) {
                  has = true;
                  break;
               }
            }

            if (!has) {
               throw new Exception("职业配置出错 -- 职业数据不完整 ，职业=" + proType);
            }
         }
      }

   }

   public static HashMap getProfessions() {
      return professions;
   }

   public static int getProID(int proType, int proLevel) {
      int key = createKey(proType, proLevel);
      return ((Integer)typeLevelToID.get(key)).intValue();
   }

   public static int getPotentailByProID(int professionID) {
      return professions.containsKey(professionID) ? ((Profession)professions.get(professionID)).getPotential() : 0;
   }

   public static List getIDList(int proType) {
      return (List)typeIDs.get(proType);
   }

   public static int getProfessionID(int proID) {
      Profession profession = (Profession)professions.get(proID);
      return profession != null ? profession.getProType() : -1;
   }

   public static Profession getProfession(int proID) {
      return (Profession)professions.get(proID);
   }

   public static int getTransferPotential(int level) {
      return transferPotentials.containsKey(level) ? ((Integer)transferPotentials.get(level)).intValue() : 0;
   }

   public int getProID() {
      return this.proID;
   }

   public void setProID(int proID) {
      this.proID = proID;
   }

   public int getProType() {
      return this.proType;
   }

   public void setProType(int proType) {
      this.proType = proType;
   }

   public int getProLevel() {
      return this.proLevel;
   }

   public void setProLevel(int proLevel) {
      this.proLevel = proLevel;
   }

   public String getProName() {
      return this.proName;
   }

   public void setProName(String proName) {
      this.proName = proName;
   }

   public int getPotential() {
      return this.potential;
   }

   public void setPotential(int potential) {
      this.potential = potential;
   }

   public int getHeader() {
      return this.header;
   }

   public int getGender() {
      return this.gender;
   }

   public int getLoginImg() {
      return this.loginImg;
   }
}
