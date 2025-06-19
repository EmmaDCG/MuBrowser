package com.mu.game.model.equip.equipStat;

import com.mu.game.model.equip.excellent.ExcellentSort;
import com.mu.game.model.equip.lucky.LuckyCreationData;
import com.mu.game.model.equip.mosaic.MosaicCreationData;
import com.mu.game.model.equip.star.StarCreationData;
import com.mu.game.model.equip.zhuijia.ZhuijiaCreationData;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class EquipStatRule {
   private static HashMap rules = new HashMap();
   private int ruleID;
   private int excellentSortID;
   private int mosaicSortID;
   private int zhuijiaSortID;
   private int strengthID;
   private int luckySortID;

   public EquipStatRule(int ruleID, int excellentSortID, int mosaicSortID, int zhuijiaSortID, int strengthID) {
      this.ruleID = ruleID;
      this.excellentSortID = excellentSortID;
      this.mosaicSortID = mosaicSortID;
      this.zhuijiaSortID = zhuijiaSortID;
      this.strengthID = strengthID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int ruleID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int excellentSortID = Tools.getCellIntValue(sheet.getCell("B" + i));
         int mosaicSortID = Tools.getCellIntValue(sheet.getCell("C" + i));
         int zhuijiaSortID = Tools.getCellIntValue(sheet.getCell("D" + i));
         int strengthID = Tools.getCellIntValue(sheet.getCell("E" + i));
         int luckySortID = Tools.getCellIntValue(sheet.getCell("F" + i));
         if (!ExcellentSort.hasSortID(excellentSortID)) {
            throw new Exception(ruleID + " 卓越属性规则 ,没有相应的附加属性ID " + excellentSortID);
         }

         if (!MosaicCreationData.hasSortID(mosaicSortID)) {
            throw new Exception(ruleID + " 卓越属性规则 ,没有相应的镶嵌孔数属性ID " + mosaicSortID);
         }

         if (!ZhuijiaCreationData.hasSortID(zhuijiaSortID)) {
            throw new Exception(ruleID + " 卓越属性规则 ,没有相应的追加 ID " + zhuijiaSortID);
         }

         if (!StarCreationData.hasSortID(strengthID)) {
            throw new Exception(ruleID + " 卓越属性规则 ,没有相应的强化ID " + strengthID);
         }

         if (!LuckyCreationData.hasSortID(luckySortID)) {
            throw new Exception(ruleID + " 卓越属性规则 ,没有相应的幸运ID " + luckySortID);
         }

         EquipStatRule rule = new EquipStatRule(ruleID, excellentSortID, mosaicSortID, zhuijiaSortID, strengthID);
         rule.setLuckySortID(luckySortID);
         rules.put(ruleID, rule);
      }

   }

   public static EquipStatRule getRule(int ruleID) {
      return (EquipStatRule)rules.get(ruleID);
   }

   public int getLuckySortID() {
      return this.luckySortID;
   }

   public void setLuckySortID(int luckySortID) {
      this.luckySortID = luckySortID;
   }

   public int getRuleID() {
      return this.ruleID;
   }

   public void setRuleID(int ruleID) {
      this.ruleID = ruleID;
   }

   public int getExcellentSortID() {
      return this.excellentSortID;
   }

   public void setExcellentSortID(int excellentSortID) {
      this.excellentSortID = excellentSortID;
   }

   public int getMosaicSortID() {
      return this.mosaicSortID;
   }

   public void setMosaicSortID(int mosaicSortID) {
      this.mosaicSortID = mosaicSortID;
   }

   public int getZhuijiaSortID() {
      return this.zhuijiaSortID;
   }

   public void setZhuijiaSortID(int zhuijiaSortID) {
      this.zhuijiaSortID = zhuijiaSortID;
   }

   public int getStrengthID() {
      return this.strengthID;
   }

   public void setStrengthID(int strengthID) {
      this.strengthID = strengthID;
   }
}
