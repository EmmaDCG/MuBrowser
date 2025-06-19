package com.mu.game.model.equip.excellent;

import com.mu.game.model.equip.equipStat.EquipStat;
import com.mu.game.model.weight.WeightElement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExcellentElement extends WeightElement {
   private static Logger logger = LoggerFactory.getLogger(ExcellentElement.class);

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

            int equipStatID = Integer.parseInt(secondSplits[0]);
            int weight = Integer.parseInt(secondSplits[1]);
            if (!EquipStat.hasEquipStat(equipStatID)) {
               throw new Exception("没有想要装备属性 ，关键字 = " + key);
            }

            if (statSet.contains(equipStatID)) {
               throw new Exception("装备属性规则 - 卓越属性池- 属性重复，属性字符串 =" + key);
            }

            ExcellentAtom atom = new ExcellentAtom(equipStatID, weight);
            this.addAtom(atom);
            statSet.add(equipStatID);
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

   public SortedMap getExcellentStats(int count) {
      SortedMap modifies = new TreeMap();
      EquipStat stat;
      if (count >= this.getAtoms().size()) {
         Iterator var4 = this.getAtoms().iterator();

         while(var4.hasNext()) {
            ExcellentAtom atom = (ExcellentAtom)var4.next();
            stat = atom.createEquipStat();
            modifies.put(stat.getId(), stat);
         }
      } else {
         int num = 0;

         while(count > 0) {
            ++num;
            ExcellentAtom atom = (ExcellentAtom)this.getRndAtom();
            if (num >= 100) {
               logger.debug("卓越属性挑选出错  ExcellentElement");
               break;
            }

            if (!modifies.containsKey(atom.getEquipStatID())) {
               --count;
               stat = atom.createEquipStat();
               modifies.put(stat.getId(), stat);
            }
         }
      }

      return modifies;
   }
}
