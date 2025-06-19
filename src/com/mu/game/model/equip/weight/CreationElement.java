package com.mu.game.model.equip.weight;

import com.mu.game.model.weight.WeightElement;
import java.util.Iterator;
import org.slf4j.Logger;

public class CreationElement extends WeightElement {
   private int bestValue;

   public void parse(String weightStr, String key) throws Exception {
      if (weightStr != null && weightStr.length() >= 1) {
         String[] firstSplits = weightStr.split(";");
         String[] var7 = firstSplits;
         int var6 = firstSplits.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            String s = var7[var5];
            String[] secondSplits = s.split(",");
            if (secondSplits.length < 2) {
               throw new Exception("字符串格式不对 ，关键字 = " + key);
            }

            int value = Integer.parseInt(secondSplits[0]);
            int weight = Integer.parseInt(secondSplits[1]);
            CreationAtom atom = new CreationAtom(value, weight);
            this.getAtoms().add(atom);
         }

         if (this.getAtoms().size() < 1) {
            throw new Exception("没有数值 ，关键字 = " + key);
         } else {
            this.sortByWeight(key);
            if (!this.checkWeight((Logger)null, key)) {
               throw new Exception(key + "，权重不对");
            } else {
               Iterator var13 = this.getAtoms().iterator();

               while(var13.hasNext()) {
                  CreationAtom atom = (CreationAtom)var13.next();
                  if (atom.getValue() > this.bestValue) {
                     this.bestValue = atom.getValue();
                  }
               }

            }
         }
      } else {
         throw new Exception("字符串为空,关键字 = " + key);
      }
   }

   public int getRndValue() {
      CreationAtom atom = (CreationAtom)this.getRndAtom();
      return atom.getValue();
   }

   public int getBestValue() {
      return this.bestValue;
   }
}
