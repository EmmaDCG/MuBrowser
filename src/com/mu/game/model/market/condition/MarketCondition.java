package com.mu.game.model.market.condition;

import com.mu.game.model.service.StringTools;
import com.mu.utils.Tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import jxl.Sheet;

public class MarketCondition {
   private static HashMap conditonMap = new HashMap();
   private int conditionID;
   private List conAtoms = new ArrayList();

   public MarketCondition(int conditionID) {
      this.conditionID = conditionID;
   }

   public static void init(Sheet sheet) throws Exception {
      int rows = sheet.getRows();

      for(int i = 2; i <= rows; ++i) {
         int conditionID = Tools.getCellIntValue(sheet.getCell("A" + i));
         String conStr = Tools.getCellValue(sheet.getCell("B" + i));
         List atomList = StringTools.analyzeIntegerList(conStr, ",");
         MarketCondition marketCons = new MarketCondition(conditionID);
         Iterator var8 = atomList.iterator();

         while(var8.hasNext()) {
            Integer atomID = (Integer)var8.next();
            ConditionAtom atom = ConditionAtom.getConditonAtom(atomID.intValue());
            if (atom == null) {
               throw new Exception("市场 - 条件集合 - 没有找到条件ID {" + atomID + "},第" + i + "行");
            }

            marketCons.getConAtoms().add(atom);
         }

         conditonMap.put(conditionID, marketCons);
      }

   }

   public static MarketCondition getCondition(int conditionID) {
      return (MarketCondition)conditonMap.get(conditionID);
   }

   public ConditionAtom getAtom(int index) {
      return index >= 0 && index < this.getConAtoms().size() ? (ConditionAtom)this.getConAtoms().get(index) : null;
   }

   public int getConditionID() {
      return this.conditionID;
   }

   public void setConditionID(int conditionID) {
      this.conditionID = conditionID;
   }

   public List getConAtoms() {
      return this.conAtoms;
   }

   public void setConAtoms(List conAtoms) {
      this.conAtoms = conAtoms;
   }

   public static HashMap getConditonMap() {
      return conditonMap;
   }
}
