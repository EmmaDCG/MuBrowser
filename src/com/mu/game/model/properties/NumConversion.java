package com.mu.game.model.properties;

import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatList2Client;
import com.mu.game.model.unit.Creature;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import jxl.Sheet;

public class NumConversion {
   private static HashMap conversions = new HashMap();
   private HashMap firstAffectMaps = new HashMap();
   private HashMap secondAffectMaps = new HashMap();
   private int proID;
   private float strToMinAtk;
   private float strToMaxAtk;
   private float dexToMinAtk;
   private float dexToMaxAtk;
   private float intToMinAtk;
   private float intToMaxAtk;
   private float strToHit;
   private float dexToAvd;
   private float conToMaxHp;
   private float intToSkillAtk;
   private float conToDefStrength;
   private float intToMp;
   private float dexToAtkSpeed;
   private float strToDamStrength;

   public NumConversion(float strToMinAtk, float strToMaxAtk, float dexToMinAtk, float dexToMaxAtk, float intToMinAtk, float intToMaxAtk, float strToHit, float dexToAvd, float conToMaxHp, float intToSkillAtk, float intToMp, float dexToAtkSpeed, float strToDamStrength, float conToDefStrength) throws Exception {
      this.strToMinAtk = strToMinAtk;
      this.strToMaxAtk = strToMaxAtk;
      this.dexToMinAtk = dexToMinAtk;
      this.dexToMaxAtk = dexToMaxAtk;
      this.intToMinAtk = intToMinAtk;
      this.intToMaxAtk = intToMaxAtk;
      this.strToHit = strToHit;
      this.dexToAvd = dexToAvd;
      this.conToMaxHp = conToMaxHp;
      this.intToSkillAtk = intToSkillAtk;
      this.intToMp = intToMp;
      this.dexToAtkSpeed = dexToAtkSpeed;
      this.strToDamStrength = strToDamStrength;
      this.conToDefStrength = conToDefStrength;
      this.addAffectStats(StatEnum.STR, strToMinAtk, StatEnum.ATK_MIN);
      this.addAffectStats(StatEnum.STR, strToMaxAtk, StatEnum.ATK_MAX);
      this.addAffectStats(StatEnum.DEX, dexToMinAtk, StatEnum.ATK_MIN);
      this.addAffectStats(StatEnum.DEX, dexToMaxAtk, StatEnum.ATK_MAX);
      this.addAffectStats(StatEnum.INT, intToMinAtk, StatEnum.ATK_MIN);
      this.addAffectStats(StatEnum.INT, intToMaxAtk, StatEnum.ATK_MAX);
      this.addAffectStats(StatEnum.STR, strToHit, StatEnum.HIT);
      this.addAffectStats(StatEnum.DEX, dexToAvd, StatEnum.AVD);
      this.addAffectStats(StatEnum.CON, conToMaxHp, StatEnum.MAX_HP);
      this.addAffectStats(StatEnum.INT, intToSkillAtk, StatEnum.SKILL_ATK);
      this.addAffectStats(StatEnum.INT, intToMp, StatEnum.MAX_MP);
      this.addAffectStats(StatEnum.DEX, dexToAtkSpeed, StatEnum.ATK_SPEED);
      this.addAffectStats(StatEnum.STR, strToDamStrength, StatEnum.DAM_STRENGTHEN);
      this.addAffectStats(StatEnum.CON, conToDefStrength, StatEnum.DEF_STRENGTH);
   }

   public void addAffectStats(StatEnum firstStat, float value, StatEnum secondStat) throws Exception {
      if (value > 0.0F) {
         List firstMaps = StatList2Client.getFirstLevelSatList();
         if (!firstMaps.contains(firstStat)) {
            throw new Exception("属性转换-第一级属性配置错误");
         } else {
            SortedMap fmaps = (SortedMap)this.firstAffectMaps.get(firstStat);
            if (fmaps == null) {
               fmaps = new TreeMap();
               this.firstAffectMaps.put(firstStat, fmaps);
            }

            ((SortedMap)fmaps).put(secondStat, value);
            HashMap smaps = (HashMap)this.secondAffectMaps.get(secondStat);
            if (smaps == null) {
               smaps = new HashMap();
               this.secondAffectMaps.put(secondStat, smaps);
            }

            smaps.put(firstStat, value);
         }
      }
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int proID = Tools.getCellIntValue(sheet.getCell("A" + i));
         float strToMinAtk = Tools.getCellFloatValue(sheet.getCell("B" + i)) / 100000.0F;
         float strToMaxAtk = Tools.getCellFloatValue(sheet.getCell("C" + i)) / 100000.0F;
         float dexToMinAtk = Tools.getCellFloatValue(sheet.getCell("D" + i)) / 100000.0F;
         float dexToMaxAtk = Tools.getCellFloatValue(sheet.getCell("E" + i)) / 100000.0F;
         float intToMinAtk = Tools.getCellFloatValue(sheet.getCell("F" + i)) / 100000.0F;
         float intToMaxAtk = Tools.getCellFloatValue(sheet.getCell("G" + i)) / 100000.0F;
         float strToHit = Tools.getCellFloatValue(sheet.getCell("H" + i)) / 100000.0F;
         float dexToAvd = Tools.getCellFloatValue(sheet.getCell("I" + i)) / 100000.0F;
         float conToMaxHp = Tools.getCellFloatValue(sheet.getCell("J" + i)) / 100000.0F;
         float intToSkillAtk = Tools.getCellFloatValue(sheet.getCell("K" + i));
         float conToDefStrength = Tools.getCellFloatValue(sheet.getCell("L" + i));
         float intToMp = Tools.getCellFloatValue(sheet.getCell("M" + i)) / 100000.0F;
         float dexToAtkSpeed = Tools.getCellFloatValue(sheet.getCell("N" + i)) / 100000.0F;
         float strToDamStrength = Tools.getCellFloatValue(sheet.getCell("O" + i));
         NumConversion conversion = new NumConversion(strToMinAtk, strToMaxAtk, dexToMinAtk, dexToMaxAtk, intToMinAtk, intToMaxAtk, strToHit, dexToAvd, conToMaxHp, intToSkillAtk, intToMp, dexToAtkSpeed, strToDamStrength, conToDefStrength);
         conversions.put(proID, conversion);
      }

      HashMap pros = Profession.getProfessions();
      Iterator var21 = pros.keySet().iterator();

      while(var21.hasNext()) {
         Integer proID = (Integer)var21.next();
         if (getNumConversion(proID.intValue()) == null) {
            throw new Exception("战斗属性转换 - 职业不足,职业ID = " + proID);
         }
      }

   }

   public static NumConversion getNumConversion(int proID) {
      return (NumConversion)conversions.get(proID);
   }

   public static void replaceSecondLevelStats(Creature creature, ConcurrentHashMap baseMap, int str, int dex, int intell, int con) {
      NumConversion conversion = getNumConversion(creature.getProfessionID());
      if (conversion != null) {
         HashMap firstLevelStats = new HashMap();
         firstLevelStats.put(StatEnum.STR, str);
         firstLevelStats.put(StatEnum.DEX, dex);
         firstLevelStats.put(StatEnum.CON, con);
         firstLevelStats.put(StatEnum.INT, intell);
         Iterator it = conversion.getSecondAffectMaps().entrySet().iterator();

         while(it.hasNext()) {
            Entry entry = (Entry)it.next();
            StatEnum key = (StatEnum)entry.getKey();
            int value = baseMap.containsKey(key) ? ((Integer)baseMap.get(key)).intValue() : 0;
            value += conversion.getConverNum(key, firstLevelStats);
            baseMap.put(key, value);
         }

         firstLevelStats.clear();
         firstLevelStats = null;
      }
   }

   private int getConverNum(StatEnum stat, HashMap firstLevelStats) {
      HashMap sMaps = (HashMap)this.secondAffectMaps.get(stat);
      if (sMaps == null) {
         return 0;
      } else {
         double value = 0.0D;

         Entry entry;
         for(Iterator it = sMaps.entrySet().iterator(); it.hasNext(); value += (double)(firstLevelStats.containsKey(entry.getKey()) ? (float)((Integer)firstLevelStats.get(entry.getKey())).intValue() * ((Float)entry.getValue()).floatValue() : 0.0F)) {
            entry = (Entry)it.next();
         }

         return (int)value;
      }
   }

   public int getProID() {
      return this.proID;
   }

   public void setProID(int proID) {
      this.proID = proID;
   }

   public float getStrToMinAtk() {
      return this.strToMinAtk;
   }

   public void setStrToMinAtk(float strToMinAtk) {
      this.strToMinAtk = strToMinAtk;
   }

   public float getStrToMaxAtk() {
      return this.strToMaxAtk;
   }

   public void setStrToMaxAtk(float strToMaxAtk) {
      this.strToMaxAtk = strToMaxAtk;
   }

   public float getDexToMinAtk() {
      return this.dexToMinAtk;
   }

   public void setDexToMinAtk(float dexToMinAtk) {
      this.dexToMinAtk = dexToMinAtk;
   }

   public float getDexToMaxAtk() {
      return this.dexToMaxAtk;
   }

   public void setDexToMaxAtk(float dexToMaxAtk) {
      this.dexToMaxAtk = dexToMaxAtk;
   }

   public float getIntToMinAtk() {
      return this.intToMinAtk;
   }

   public void setIntToMinAtk(float intToMinAtk) {
      this.intToMinAtk = intToMinAtk;
   }

   public float getIntToMaxAtk() {
      return this.intToMaxAtk;
   }

   public void setIntToMaxAtk(float intToMaxAtk) {
      this.intToMaxAtk = intToMaxAtk;
   }

   public float getStrToHit() {
      return this.strToHit;
   }

   public void setStrToHit(float strToHit) {
      this.strToHit = strToHit;
   }

   public float getDexToAvd() {
      return this.dexToAvd;
   }

   public void setDexToAvd(float dexToAvd) {
      this.dexToAvd = dexToAvd;
   }

   public float getConToMaxHp() {
      return this.conToMaxHp;
   }

   public void setConToMaxHp(float conToMaxHp) {
      this.conToMaxHp = conToMaxHp;
   }

   public float getIntToMp() {
      return this.intToMp;
   }

   public void setIntToMp(float intToMp) {
      this.intToMp = intToMp;
   }

   public float getDexToAtkSpeed() {
      return this.dexToAtkSpeed;
   }

   public void setDexToAtkSpeed(float dexToAtkSpeed) {
      this.dexToAtkSpeed = dexToAtkSpeed;
   }

   public float getIntToSkillAtk() {
      return this.intToSkillAtk;
   }

   public void setIntToSkillAtk(float intToSkillAtk) {
      this.intToSkillAtk = intToSkillAtk;
   }

   public float getStrToDamStrength() {
      return this.strToDamStrength;
   }

   public void setStrToDamStrength(float strToDamStrength) {
      this.strToDamStrength = strToDamStrength;
   }

   public float getConToDefStrength() {
      return this.conToDefStrength;
   }

   public void setConToDefStrength(float conToDefStrength) {
      this.conToDefStrength = conToDefStrength;
   }

   public HashMap getFirstAffectMaps() {
      return this.firstAffectMaps;
   }

   public HashMap getSecondAffectMaps() {
      return this.secondAffectMaps;
   }

   public static HashMap getMaybeChangeStatValue(Creature owner) {
      return new HashMap();
   }

   public static void checkDomiChange_1(HashMap maybeChangeStats, Creature owner, int statId) {
   }

   public static void checkDomiChange_2(HashMap maybeChangeStats, Creature owner, int statId) {
   }

   public static int getDomineering(CreatureProperties properties) {
      return 1;
   }
}
