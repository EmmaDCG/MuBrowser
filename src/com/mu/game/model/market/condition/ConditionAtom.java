package com.mu.game.model.market.condition;

import com.mu.game.model.item.Item;
import com.mu.game.model.market.condition.imp.ConditionEquipSet;
import com.mu.game.model.market.condition.imp.ConditionExcellentCount;
import com.mu.game.model.market.condition.imp.ConditionIngoreDefPro;
import com.mu.game.model.market.condition.imp.ConditionLevel;
import com.mu.game.model.market.condition.imp.ConditionLucky;
import com.mu.game.model.market.condition.imp.ConditionNone;
import com.mu.utils.Tools;
import java.util.HashMap;
import jxl.Sheet;

public abstract class ConditionAtom {
   private static HashMap conditionMap = new HashMap();
   private int atomID;
   private String name;
   private int type;

   public ConditionAtom(int atomID, String name, int type) {
      this.atomID = atomID;
      this.type = type;
      this.name = name;
   }

   public abstract boolean check(Item var1);

   public static ConditionAtom getConditonAtom(int atomID) {
      return (ConditionAtom)conditionMap.get(atomID);
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int atomID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String atomName = Tools.getCellValue(sheet.getCell("B" + i));
         int type = Tools.getCellIntValue(sheet.getCell("C" + i));
         int value = Tools.getCellIntValue(sheet.getCell("D" + i));
         ConditionAtom atom = createAtom(atomID, atomName, type, value);
         if (atom != null) {
            conditionMap.put(atomID, atom);
         }
      }

   }

   public static ConditionAtom createAtom(int atomID, String atomName, int type, int value) throws Exception {
      ConditionAtom atom = null;
      switch(type) {
      case 1:
         atom = new ConditionLevel(atomID, atomName, type, value);
         break;
      case 2:
         atom = new ConditionLucky(atomID, atomName, type);
         break;
      case 3:
         atom = new ConditionIngoreDefPro(atomID, atomName, type);
         break;
      case 4:
         atom = new ConditionEquipSet(atomID, atomName, type);
         break;
      case 5:
         atom = new ConditionExcellentCount(atomID, atomName, type, value);
         break;
      case 6:
         atom = new ConditionNone(atomID, atomName, type);
      }

      if (atom == null) {
         throw new Exception("市场-条件-找不到匹配类型 type = " + type);
      } else {
         return (ConditionAtom)atom;
      }
   }

   public int getAtomID() {
      return this.atomID;
   }

   public void setAtomID(int atomID) {
      this.atomID = atomID;
   }

   public int getType() {
      return this.type;
   }

   public void setType(int type) {
      this.type = type;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }
}
