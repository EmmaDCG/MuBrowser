package com.mu.game.model.pet;

import com.mu.game.model.stats.FinalModify;
import com.mu.game.model.stats.StatEnum;
import com.mu.game.model.stats.StatModifyPriority;
import com.mu.utils.CommonRegPattern;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;

public class PetPropertyData {
   private PetRank rank;
   private int level;
   private int monsterLevel;
   private int monsterStar;
   private LinkedHashMap propertyList = new LinkedHashMap();
   private int petZDL;

   public PetPropertyData(PetRank rank, int level) {
      this.rank = rank;
      this.level = level;
   }

   public PetRank getRank() {
      return this.rank;
   }

   public int getLevel() {
      return this.level;
   }

   public int getPetZDL() {
      return this.petZDL;
   }

   public void setPetZDL(int zdl) {
      this.petZDL = zdl;
   }

   public LinkedHashMap getPropertyList() {
      return this.propertyList;
   }

   public void setPropertyListStr(String str) {
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(str);

      while(m.find()) {
         int id = Integer.parseInt(m.group());
         m.find();
         int value = Integer.parseInt(m.group());
         m.find();
         int type = Integer.parseInt(m.group());
         FinalModify fm = new FinalModify(StatEnum.find(id), value, StatModifyPriority.fine(type));
         this.propertyList.put(fm.getStat(), fm);
      }

   }

   public int getMonsterLevel() {
      return this.monsterLevel;
   }

   public void setMonsterLevel(int monsterLevel) {
      this.monsterLevel = monsterLevel;
   }

   public int getMonsterStar() {
      return this.monsterStar;
   }

   public void setMonsterStar(int monsterStar) {
      this.monsterStar = monsterStar;
   }
}
