package com.mu.game.model.unit.talent;

import com.mu.game.model.service.StringTools;
import com.mu.game.model.unit.player.Profession;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;

public class TalentModel {
   private static HashMap talents = new HashMap();
   private int proID;
   private List stats;

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int professionID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String statStr = Tools.getCellValue(sheet.getCell("B" + i));
         List stats = StringTools.analyzeArrayAttris(statStr, ",");
         if (stats.size() < 1) {
            throw new Exception("战斗--天赋 -- 属性不足  职业ID = " + professionID);
         }

         TalentModel tm = new TalentModel(professionID);
         tm.setStats(stats);
         talents.put(professionID, tm);
      }

      HashMap pros = Profession.getProfessions();
      Iterator var9 = pros.keySet().iterator();

      while(var9.hasNext()) {
         Integer proID = (Integer)var9.next();
         if (proID.intValue() != 100) {
            TalentModel model = getTalent(proID.intValue());
            if (model == null) {
               throw new Exception("战斗--天赋 -- 没有相应的天赋属性 ，职业ID = " + proID);
            }
         }
      }

   }

   public static TalentModel getTalent(int professionID) {
      return (TalentModel)talents.get(professionID);
   }

   public TalentModel(int proID) {
      this.proID = proID;
   }

   public int getProID() {
      return this.proID;
   }

   public void setProID(int proID) {
      this.proID = proID;
   }

   public List getStats() {
      return this.stats;
   }

   public void setStats(List stats) {
      this.stats = stats;
   }
}
