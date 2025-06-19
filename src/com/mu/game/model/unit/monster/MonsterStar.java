package com.mu.game.model.unit.monster;

import com.mu.game.model.drop.model.MonsterDrop;
import com.mu.game.model.stats.StatEnum;
import com.mu.utils.Tools;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jxl.Sheet;
import jxl.Workbook;

public class MonsterStar {
   private static final HashMap monsterStarMap = new HashMap();
   private int star;
   private int level;
   private int hp;
   private int mp;
   private int minAtt;
   private int maxAtt;
   private int def;
   private int hit;
   private int avd;
   private int hitAbsolute;
   private int avdtAbsolute;
   private HashMap otherStats = null;
   private int exp;
   private int showStar;
   private int rewardAp;
   private List drops = null;

   public static void initMonsterStar(InputStream in) throws Exception {
      Workbook wb = Workbook.getWorkbook(in);
      Sheet[] sheets = wb.getSheets();

      for(int i = 1; i < sheets.length; ++i) {
         Sheet sheet = sheets[i];
         int rows = sheet.getRows();

         for(int j = 2; j <= rows; ++j) {
            MonsterStar monsterStar = parseStar(sheet, j);
            HashMap map = (HashMap)monsterStarMap.get(monsterStar.getStar());
            if (map == null) {
               map = new HashMap();
               monsterStarMap.put(monsterStar.getStar(), map);
            }

            map.put(monsterStar.getLevel(), monsterStar);
         }
      }

   }

   public static MonsterStar getMonsterStar(int star, int level) {
      HashMap map = (HashMap)monsterStarMap.get(star);
      return map == null ? null : (MonsterStar)map.get(level);
   }

   public static MonsterStar parseStar(Sheet sheet, int index) throws Exception {
      MonsterStar monsterStar = new MonsterStar();
      monsterStar.setStar(Tools.getCellIntValue(sheet.getCell("A" + index)));
      monsterStar.setLevel(Tools.getCellIntValue(sheet.getCell("B" + index)));
      monsterStar.setHp(Tools.getCellIntValue(sheet.getCell("C" + index)));
      monsterStar.setMp(Tools.getCellIntValue(sheet.getCell("D" + index)));
      monsterStar.setMinAtt(Tools.getCellIntValue(sheet.getCell("E" + index)));
      monsterStar.setMaxAtt(Tools.getCellIntValue(sheet.getCell("F" + index)));
      monsterStar.setDef(Tools.getCellIntValue(sheet.getCell("G" + index)));
      monsterStar.addOtherStat(StatEnum.IGNORE_DEF, Tools.getCellIntValue(sheet.getCell("H" + index)));
      monsterStar.addOtherStat(StatEnum.DAM_IGNORE, Tools.getCellIntValue(sheet.getCell("I" + index)));
      monsterStar.addOtherStat(StatEnum.WEAPON_MIN_ATK, Tools.getCellIntValue(sheet.getCell("J" + index)));
      monsterStar.addOtherStat(StatEnum.WEAPON_MAX_ATK, Tools.getCellIntValue(sheet.getCell("K" + index)));
      monsterStar.setHit(Tools.getCellIntValue(sheet.getCell("L" + index)));
      monsterStar.setAvd(Tools.getCellIntValue(sheet.getCell("M" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_LUCKY_DAM, Tools.getCellIntValue(sheet.getCell("N" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_EXCELLENT_DAM, Tools.getCellIntValue(sheet.getCell("O" + index)));
      monsterStar.addOtherStat(StatEnum.SKILL_ATK, Tools.getCellIntValue(sheet.getCell("P" + index)));
      monsterStar.addOtherStat(StatEnum.DAM_REDUCE, Tools.getCellIntValue(sheet.getCell("Q" + index)));
      monsterStar.addOtherStat(StatEnum.DAM_STRENGTHEN, Tools.getCellIntValue(sheet.getCell("R" + index)));
      monsterStar.addOtherStat(StatEnum.DAM_ABSORB, Tools.getCellIntValue(sheet.getCell("S" + index)));
      monsterStar.addOtherStat(StatEnum.DAM_REFLECTION, Tools.getCellIntValue(sheet.getCell("T" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_LUCKY_RATE, Tools.getCellIntValue(sheet.getCell("U" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_EXCELLENT_RATE, Tools.getCellIntValue(sheet.getCell("V" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_FATAL_RATE, Tools.getCellIntValue(sheet.getCell("W" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_LUCKY_RES, Tools.getCellIntValue(sheet.getCell("X" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_EXCELLENT_RES, Tools.getCellIntValue(sheet.getCell("Y" + index)));
      monsterStar.addOtherStat(StatEnum.ATK_FATAL_RES, Tools.getCellIntValue(sheet.getCell("Z" + index)));
      if (monsterStar.getOtherStats().containsKey(StatEnum.DAM_ABSORB) && monsterStar.getOtherStats().containsKey(StatEnum.DAM_REDUCE)) {
         monsterStar.setHitAbsolute(Tools.getCellIntValue(sheet.getCell("AA" + index)));
         monsterStar.setAvdtAbsolute(Tools.getCellIntValue(sheet.getCell("AB" + index)));
         monsterStar.addOtherStat(StatEnum.RES_POISONING, Tools.getCellIntValue(sheet.getCell("AC" + index)));
         monsterStar.addOtherStat(StatEnum.RES_WIND, Tools.getCellIntValue(sheet.getCell("AD" + index)));
         monsterStar.addOtherStat(StatEnum.RES_RESURRENTION, Tools.getCellIntValue(sheet.getCell("AE" + index)));
         monsterStar.addOtherStat(StatEnum.RES_PARALYSIS, Tools.getCellIntValue(sheet.getCell("AF" + index)));
         monsterStar.addOtherStat(StatEnum.RES_FROST, Tools.getCellIntValue(sheet.getCell("AG" + index)));
         monsterStar.addOtherStat(StatEnum.DAM_FORCE, Tools.getCellIntValue(sheet.getCell("AI" + index)));
         monsterStar.setExp(Tools.getCellIntValue(sheet.getCell("AJ" + index)));
         monsterStar.setShowStar(Tools.getCellIntValue(sheet.getCell("AK" + index)));
         monsterStar.setRewardAp(Tools.getCellIntValue(sheet.getCell("AL" + index)));
         String loopStr = sheet.getCell("AM" + index).getContents();
         String worldDropStr = sheet.getCell("AN" + index).getContents();
         String specialStr = sheet.getCell("AO" + index).getContents();
         MonsterDrop drop;
         if (worldDropStr != null && worldDropStr.length() > 0) {
            drop = new MonsterDrop(Integer.parseInt(loopStr), worldDropStr, sheet.getName() + ",第" + index + "行");
            monsterStar.addMonsterDrop(drop);
         }

         if (specialStr != null && specialStr.length() > 0) {
            drop = new MonsterDrop(1, specialStr, sheet.getName() + ",第" + index + "行");
            drop.setType(2);
            monsterStar.addMonsterDrop(drop);
         }

         return monsterStar;
      } else {
         throw new Exception(sheet.getName() + "第" + index + "行 减伤和伤害吸收数值错误");
      }
   }

   public void addOtherStat(StatEnum stat, int value) {
      if (value != 0) {
         if (this.otherStats == null) {
            this.otherStats = new HashMap();
         }

         this.otherStats.put(stat, value);
      }
   }

   public HashMap getOtherStats() {
      return this.otherStats;
   }

   public void addMonsterDrop(MonsterDrop drop) {
      if (this.drops == null) {
         this.drops = new ArrayList();
      }

      this.drops.add(drop);
   }

   public List getDrops() {
      return this.drops;
   }

   public int getLevel() {
      return this.level;
   }

   public void setLevel(int level) {
      this.level = level;
   }

   public void setStar(int star) {
      this.star = star;
   }

   public int getStar() {
      return this.star;
   }

   public int getHp() {
      return this.hp;
   }

   public void setHp(int hp) {
      this.hp = hp;
   }

   public int getRewardAp() {
      return this.rewardAp;
   }

   public void setRewardAp(int rewardAp) {
      this.rewardAp = rewardAp;
   }

   public int getMp() {
      return this.mp;
   }

   public void setMp(int mp) {
      this.mp = mp;
   }

   public int getMinAtt() {
      return this.minAtt;
   }

   public void setMinAtt(int minAtt) {
      this.minAtt = minAtt;
   }

   public int getMaxAtt() {
      return this.maxAtt;
   }

   public void setMaxAtt(int maxAtt) {
      this.maxAtt = maxAtt;
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

   public int getHitAbsolute() {
      return this.hitAbsolute;
   }

   public void setHitAbsolute(int hitAbsolute) {
      this.hitAbsolute = hitAbsolute;
   }

   public int getAvdtAbsolute() {
      return this.avdtAbsolute;
   }

   public void setAvdtAbsolute(int avdtAbsolute) {
      this.avdtAbsolute = avdtAbsolute;
   }

   public int getExp() {
      return this.exp;
   }

   public void setExp(int exp) {
      this.exp = exp;
   }

   public int getShowStar() {
      return this.showStar;
   }

   public void setShowStar(int showStar) {
      this.showStar = showStar;
   }
}
