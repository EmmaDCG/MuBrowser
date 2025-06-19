package com.mu.game.model.pet;

import com.mu.game.model.stats.StatEnum;
import com.mu.utils.CommonRegPattern;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public class PetAttributeData {
   private int id;
   private StatEnum stat;
   private String name;
   private int openRank;
   private String openDesc;
   private List levelList;

   public PetAttributeData(int id, StatEnum stat, String name) {
      this.id = id;
      this.stat = stat;
      this.name = name;
      this.levelList = new ArrayList();
   }

   public StatEnum getStat() {
      return this.stat;
   }

   public String getName() {
      return this.name;
   }

   public List getLevelList() {
      return this.levelList;
   }

   public void addLevelStr(int level, int limit, PetItemData item, String attributeStr, int zdl) {
      PetAttributeData.Level lv = new PetAttributeData.Level();
      lv.level = level;
      lv.limit = limit;
      lv.expendItem = item;
      lv.zdl = zdl;
      Matcher m = CommonRegPattern.PATTERN_INT.matcher(attributeStr);
      m.find();
      lv.attributeValue = Integer.parseInt(m.group());
      m.find();
      lv.attributeType = Integer.parseInt(m.group());
      lv.attributeStr = lv.attributeType == 1 ? String.valueOf(lv.attributeValue) : (double)lv.attributeValue / 1000.0D + "%";
      if (!this.levelList.isEmpty()) {
         this.getLastLevel().next = lv;
      }

      this.levelList.add(lv);
   }

   public int getOpenRank() {
      return this.openRank;
   }

   public void setOpenRank(int openRank) {
      this.openRank = openRank;
   }

   public String getOpenDesc() {
      return this.openDesc;
   }

   public void setOpenDesc(String openDesc) {
      this.openDesc = openDesc;
   }

   public PetAttributeData.Level getLastLevel() {
      return (PetAttributeData.Level)this.levelList.get(this.levelList.size() - 1);
   }

   public PetAttributeData.Level getFirstLevel() {
      return (PetAttributeData.Level)this.levelList.get(0);
   }

   public PetAttributeData.Level getLevel(int level) {
      try {
         return (PetAttributeData.Level)this.levelList.get(level - this.getFirstLevel().getLevel());
      } catch (Exception var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public int getId() {
      return this.id;
   }

   public class Level {
      private int level;
      private int zdl;
      private int limit;
      private int attributeValue;
      private int attributeType;
      private PetItemData expendItem;
      private String attributeStr;
      private PetAttributeData.Level next;

      public int getLevel() {
         return this.level;
      }

      public int getLimit() {
         return this.limit;
      }

      public PetItemData getExpendItem() {
         return this.expendItem;
      }

      public int getAttributeValue() {
         return this.attributeValue;
      }

      public int getAttributeType() {
         return this.attributeType;
      }

      public PetAttributeData.Level getNext() {
         return this.next;
      }

      public String getAttributeStr() {
         return this.attributeStr;
      }

      public int getZDL() {
         return this.zdl;
      }
   }
}
