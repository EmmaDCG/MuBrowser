package com.mu.game.model.unit.skill.model;

import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import jxl.Sheet;

public class ProfessionSkills {
   private static HashMap skillIDs = new HashMap();
   private static HashMap commonSkills = new HashMap();
   private static HashMap professionCommonSkill = new HashMap();

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int profession = Tools.getCellIntValue(sheet.getCell("A" + i));
         String skillStr = sheet.getCell("B" + i).getContents();
         int commonSkilID = Tools.getCellIntValue(sheet.getCell("C" + i));
         HashSet skillSets = StringTools.analyzeIntegerHashset(skillStr, ",");
         boolean flag = true;
         if (skillSets != null && skillSets.size() >= 1) {
            Iterator var9 = skillSets.iterator();

            while(var9.hasNext()) {
               int skillID = ((Integer)var9.next()).intValue();
               if (!SkillModel.hasModel(skillID)) {
                  flag = false;
                  break;
               }
            }
         } else {
            flag = false;
         }

         if (!SkillModel.hasModel(commonSkilID)) {
            flag = false;
         }

         if (!flag) {
            throw new Exception("职业 " + profession + " 的技能为空");
         }

         skillSets.add(commonSkilID);
         skillIDs.put(profession, skillSets);
         commonSkills.put(commonSkilID, Integer.valueOf(1));
         professionCommonSkill.put(profession, commonSkilID);
      }

   }

   public static HashSet getProfessionSkill(int profession) {
      return (HashSet)skillIDs.get(profession);
   }

   public static boolean isCommonSkill(int SkillID) {
      return commonSkills.containsKey(SkillID);
   }

   public static int getCommonSkill(int profession) {
      return professionCommonSkill.containsKey(profession) ? ((Integer)professionCommonSkill.get(profession)).intValue() : -1;
   }
}
