package com.mu.game.model.properties.levelData;

import com.mu.config.VariableConstant;
import com.mu.game.model.unit.player.Profession;
import com.mu.game.model.unit.player.ProfessionType;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.HashMap;
import jxl.Sheet;
import jxl.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerLevelData {
   private static Logger logger = LoggerFactory.getLogger(PlayerLevelData.class);
   public static final int Default_HpRecover = 100;
   public static final int Default_HpRecWKillMon = 10;
   public static final int Default_MpRecover = 10;
   public static final int Default_MpRecWKillMon = 2;
   public static final int Default_SDRecover = 10;
   public static final int Default_AGRecover = 10;
   public static final int Default_APRecover = 10;
   private static HashMap datas = new HashMap();
   private static HashMap upgradeExps = new HashMap();
   private static HashMap expReductionMap = new HashMap();
   private int level;
   private int profession;
   private int str;
   private int dex;
   private int con;
   private int intell;
   private int maxHp;
   private int maxMp;
   private int maxSD;
   private int maxAP;
   private int hpRecover;
   private int mpRecover;
   private int SDRecover;
   private int AGRecover;
   private int APRecover;
   private int minAtk;
   private int maxAtk;
   private int def;
   private int hit;
   private int avd;
   private int domineering = 0;

   public static void initExpReduction(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet sheet = wb.getSheet(1);
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int lv = Tools.getCellIntValue(sheet.getCell("A" + i));
         int rate = Tools.getCellIntValue(sheet.getCell("B" + i));
         if (rate < 1 || rate > 1000) {
            throw new Exception("检验衰减数值错误");
         }

         expReductionMap.put(lv, rate);
      }

   }

   public static int getRate(int lv) {
      Integer rate = (Integer)expReductionMap.get(lv);
      return rate == null ? 1000 : rate.intValue();
   }

   public static void initLevelData(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet[] sheets = wb.getSheets();
      ProfessionType.init(sheets[1]);
      Profession.init(sheets[2]);
      Profession.initTransferPotentials(sheets[3]);

      int i;
      int j;
      PlayerLevelData levelData;
      for(i = 4; i < sheets.length - 1; ++i) {
         Sheet sheet = sheets[i];
         i = sheet.getRows();

         for(j = 2; j <= i; ++j) {
            levelData = new PlayerLevelData();
            levelData.setLevel(Tools.getCellIntValue(sheet.getCell("A" + j)));
            levelData.setProfession(Tools.getCellIntValue(sheet.getCell("B" + j)));
            levelData.setStr(Tools.getCellIntValue(sheet.getCell("C" + j)));
            levelData.setDex(Tools.getCellIntValue(sheet.getCell("D" + j)));
            levelData.setIntell(Tools.getCellIntValue(sheet.getCell("E" + j)));
            levelData.setCon(Tools.getCellIntValue(sheet.getCell("F" + j)));
            levelData.setMaxHp(Tools.getCellIntValue(sheet.getCell("G" + j)));
            levelData.setMaxMp(Tools.getCellIntValue(sheet.getCell("H" + j)));
            levelData.setMinAtk(Tools.getCellIntValue(sheet.getCell("I" + j)));
            levelData.setMaxAtk(Tools.getCellIntValue(sheet.getCell("J" + j)));
            levelData.setDef(Tools.getCellIntValue(sheet.getCell("K" + j)));
            levelData.setHit(Tools.getCellIntValue(sheet.getCell("L" + j)));
            levelData.setAvd(Tools.getCellIntValue(sheet.getCell("M" + j)));
            levelData.setMaxSD(Tools.getCellIntValue(sheet.getCell("N" + j)));
            levelData.setHpRecover(Tools.getCellIntValue(sheet.getCell("O" + j)));
            levelData.setMpRecover(Tools.getCellIntValue(sheet.getCell("P" + j)));
            levelData.setSDRecover(Tools.getCellIntValue(sheet.getCell("Q" + j)));
            levelData.setMaxAP(Tools.getCellIntValue(sheet.getCell("R" + j)));
            levelData.setDomineering(Tools.getCellIntValue(sheet.getCell("S" + j)));
            addLevelData(levelData);
         }
      }

      Sheet sheet = sheets[sheets.length - 1];
      int rows = sheet.getRows();

      for(i = 2; i <= rows; ++i) {
         j = Tools.getCellIntValue(sheet.getCell("A" + i));
         long exp = Tools.getCellLongValue(sheet.getCell("B" + i));
         addExpData(j, exp);
      }

      for(i = 1; i <= VariableConstant.Max_Level_2Exp; ++i) {
         for(j = 0; j < 2; ++j) {
            levelData = getLevelData(j, i);
            if (levelData == null) {
               logger.error("人物等级数据缺失 ,门派 = {},等级= {}", j, i);
               break;
            }
         }

         if (!upgradeExps.containsKey(i)) {
            logger.error("人物升级数据缺失,等级={}", i);
         }
      }

   }

   public static int getKey(int level, int party) {
      return (party + 1) * 10000 + level;
   }

   public static void addLevelData(PlayerLevelData data) {
      int key = getKey(data.getLevel(), data.getProfession());
      datas.put(key, data);
   }

   public static void addExpData(int level, long exp) {
      upgradeExps.put(level, exp);
      if (level > VariableConstant.Max_Level_2Exp) {
         VariableConstant.Max_Level_2Exp = level;
      }

   }

   public static long getNeedExp(int level) {
      return upgradeExps.containsKey(level) ? ((Long)upgradeExps.get(level)).longValue() : 9223372036854775806L;
   }

   public static PlayerLevelData getLevelData(int party, int level) {
      int key = getKey(level, party);
      return (PlayerLevelData)datas.get(key);
   }

   public int getDomineering() {
      return this.domineering;
   }

   public void setDomineering(int domineering) {
      this.domineering = domineering;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public int getStr() {
      return this.str;
   }

   public void setStr(int str) {
      this.str = str;
   }

   public int getDex() {
      return this.dex;
   }

   public void setDex(int dex) {
      this.dex = dex;
   }

   public int getCon() {
      return this.con;
   }

   public void setCon(int con) {
      this.con = con;
   }

   public int getIntell() {
      return this.intell;
   }

   public void setIntell(int intell) {
      this.intell = intell;
   }

   public int getProfession() {
      return this.profession;
   }

   public void setProfession(int profession) {
      this.profession = profession;
   }

   public int getMaxHp() {
      return this.maxHp;
   }

   public void setMaxHp(int maxHp) {
      this.maxHp = maxHp;
   }

   public int getMaxMp() {
      return this.maxMp;
   }

   public void setMaxMp(int maxMp) {
      this.maxMp = maxMp;
   }

   public int getMaxSD() {
      return this.maxSD;
   }

   public void setMaxSD(int maxSD) {
      this.maxSD = maxSD;
   }

   public int getMaxAP() {
      return this.maxAP;
   }

   public void setMaxAP(int maxAP) {
      this.maxAP = maxAP;
   }

   public int getHpRecover() {
      return this.hpRecover;
   }

   public void setHpRecover(int hpRecover) {
      this.hpRecover = hpRecover;
   }

   public int getMpRecover() {
      return this.mpRecover;
   }

   public void setMpRecover(int mpRecover) {
      this.mpRecover = mpRecover;
   }

   public int getSDRecover() {
      return this.SDRecover;
   }

   public void setSDRecover(int sDRecover) {
      this.SDRecover = sDRecover;
   }

   public int getAGRecover() {
      return this.AGRecover;
   }

   public void setAGRecover(int aGRecover) {
      this.AGRecover = aGRecover;
   }

   public int getAPRecover() {
      return this.APRecover;
   }

   public void setAPRecover(int aPRecover) {
      this.APRecover = aPRecover;
   }

   public int getMinAtk() {
      return this.minAtk;
   }

   public void setMinAtk(int minAtk) {
      this.minAtk = minAtk;
   }

   public int getMaxAtk() {
      return this.maxAtk;
   }

   public void setMaxAtk(int maxAtk) {
      this.maxAtk = maxAtk;
   }

   public int getDef() {
      return this.def;
   }

   public void setDef(int def) {
      this.def = def;
   }

   public int getHit() {
      return this.hit;
   }

   public void setHit(int hit) {
      this.hit = hit;
   }

   public int getAvd() {
      return this.avd;
   }

   public void setAvd(int avd) {
      this.avd = avd;
   }
}
