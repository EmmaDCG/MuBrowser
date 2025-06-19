package com.mu.game.model.properties;

import com.mu.game.model.stats.StatEnum;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class CreatureNaturalStats {
   private static HashMap basicStats = new HashMap();
   private static HashMap playerNatualStats = new HashMap();

   public static void initPlayerNaturalStats(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int statID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int value = Tools.getCellIntValue(sheet.getCell("B" + i));
         StatEnum stat = StatEnum.find(statID);
         if (stat == StatEnum.None) {
            throw new Exception("生物固有属性--属性ID不存在 ,第" + i + "行");
         }

         if (value != 0) {
            playerNatualStats.put(stat, value);
         }
      }

      if (!playerNatualStats.containsKey(StatEnum.DAM_ABSORB) || !playerNatualStats.containsKey(StatEnum.DAM_REDUCE)) {
         throw new Exception(sheet.getName() + " - 没有填写 减伤和伤害吸收");
      }
   }

   public static HashMap getBasicStats() {
      return basicStats;
   }

   public static HashMap getPlayerNatualStats() {
      return playerNatualStats;
   }
}
