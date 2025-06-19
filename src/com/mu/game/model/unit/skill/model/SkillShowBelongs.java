package com.mu.game.model.unit.skill.model;

import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public class SkillShowBelongs {
   private static HashMap belongs = new HashMap();
   public static final int Belongs_Other = 100;
   public static final int Belongs_Single = 101;
   public static final int Belongs_Multiple = 102;
   public static final int Belongs_Feature = 103;
   public static final int Belongs_Profession = 104;

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int id = Tools.getCellIntValue(sheet.getCell("A" + i));
         String name = Tools.getCellValue(sheet.getCell("B" + i));
         belongs.put(id, name);
      }

   }

   public static String getName(int belongsID) {
      String name = (String)belongs.get(belongsID);
      if (name == null) {
         name = "其他技能";
      }

      return name;
   }

   public static HashMap getBelongs() {
      return belongs;
   }
}
