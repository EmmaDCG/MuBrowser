package com.mu.game.model.unit.trigger.action.model;

import com.mu.game.model.stats.StatEnum;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class TriggerStats {
   private static HashMap stats = new HashMap();
   private StatEnum stat;
   private boolean special;
   private int modelID;

   public TriggerStats(StatEnum stat, boolean special, int modelID) {
      this.stat = stat;
      this.special = special;
      this.modelID = modelID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int statId = Tools.getCellIntValue(sheet.getCell("A" + i));
         boolean special = Tools.getCellIntValue(sheet.getCell("B" + i)) == 1;
         int modelID = Tools.getCellIntValue(sheet.getCell("C" + i));
         StatEnum stat = StatEnum.find(statId);
         if (stat == StatEnum.None) {
            throw new Exception("触发 -- 属性不存在， 第" + i + "行");
         }

         if (special && TriggerModel.getModel(modelID) == null) {
            throw new Exception("触发 -- 属性 - ID模板不存在 ,第 " + i + ",行");
         }

         TriggerStats ts = new TriggerStats(stat, special, modelID);
         stats.put(stat, ts);
      }

   }

   public static boolean isTriggerStat(StatEnum stat) {
      return stats.containsKey(stat);
   }

   public static TriggerStats getTriggerStat(StatEnum stat) {
      return (TriggerStats)stats.get(stat);
   }

   public static boolean isSpecialTrigger(StatEnum stat) {
      return !stats.containsKey(stat) ? false : ((TriggerStats)stats.get(stat)).isSpecial();
   }

   public StatEnum getStat() {
      return this.stat;
   }

   public void setStat(StatEnum stat) {
      this.stat = stat;
   }

   public boolean isSpecial() {
      return this.special;
   }

   public void setSpecial(boolean special) {
      this.special = special;
   }

   public int getModelID() {
      return this.modelID;
   }

   public void setModelID(int modelID) {
      this.modelID = modelID;
   }
}
