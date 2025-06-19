package com.mu.game.model.luckyTurnTable.weight;

import com.mu.game.model.luckyTurnTable.LuckyTurnTableManager;
import com.mu.game.model.weight.WeightElement;
import java.util.HashSet;
import org.slf4j.Logger;

public class TurnTableElement extends WeightElement {
   public void parse(String weightStr, String key) throws Exception {
      if (weightStr != null && weightStr.length() >= 1) {
         String[] firstSplits = weightStr.split(";");
         HashSet statSet = new HashSet();
         String[] var8 = firstSplits;
         int var7 = firstSplits.length;

         for(int var6 = 0; var6 < var7; ++var6) {
            String s = var8[var6];
            String[] secondSplits = s.split(",");
            if (secondSplits.length < 2) {
               throw new Exception("字符串格式不对 ，关键字 = " + key);
            }

            int tableID = Integer.parseInt(secondSplits[0]);
            int weight = Integer.parseInt(secondSplits[1]);
            if (!LuckyTurnTableManager.hasLuckyTable(tableID)) {
               throw new Exception("没有相应的转盘ID ，关键字 = " + key);
            }

            if (statSet.contains(tableID)) {
               throw new Exception("幸运大转盘-转盘ID重复，属性字符串 =" + key);
            }

            TurnTableAtom atom = new TurnTableAtom(weight, tableID);
            this.addAtom(atom);
            statSet.add(tableID);
         }

         statSet.clear();
         statSet = null;
         this.sortByWeight(key);
         if (this.getAtoms().size() < 1 || !this.checkWeight((Logger)null, key)) {
            throw new Exception("没有数值 ，关键字 = " + key);
         }
      } else {
         throw new Exception("字符串为空,关键字 = " + key);
      }
   }

   public int getRndTableID() {
      TurnTableAtom atom = (TurnTableAtom)this.getRndAtom();
      return atom.getTableID();
   }
}
