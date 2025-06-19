package com.mu.game.model.unit.unitevent;

import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import jxl.Sheet;

public class OperationLimitManager {
   public static final int LimitType_Buff = 1;
   public static final int LimitType_Status = 2;
   private static HashMap limits = new HashMap();

   public static List getOperation(int serverStatusID) {
      return (List)limits.get(serverStatusID);
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int serverStatusID = Tools.getCellIntValue(sheet.getCell("A" + i));
         int canntMove = Tools.getCellIntValue(sheet.getCell("B" + i));
         int canntUseSkill = Tools.getCellIntValue(sheet.getCell("C" + i));
         int canntUseItem = Tools.getCellIntValue(sheet.getCell("D" + i));
         List list = new ArrayList();
         if (canntMove == 1) {
            list.add(OperationEnum.MOVE);
         }

         if (canntUseItem == 1) {
            list.add(OperationEnum.USEITEM);
         }

         if (canntUseSkill == 1) {
            list.add(OperationEnum.USESKILL);
         }

         if (list.size() >= 1) {
            limits.put(serverStatusID, list);
         }
      }

   }

   public static void addOperationLimit(Event event) {
      HashSet limitSet = event.getLimitOperations();
   }
}
