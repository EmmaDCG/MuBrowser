package com.mu.game.model.weight;

import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;

public class WeightElement {
   private int maxWeight;
   private List atoms = new ArrayList();

   public boolean checkWeight(Logger logger, String sign) {
      if (this.atoms.size() > 0 && this.maxWeight < 1) {
         if (logger != null) {
            logger.error(sign);
         }

         return false;
      } else {
         return true;
      }
   }

   public int getRndIndex() {
      int rnd = Rnd.get(1, this.maxWeight);
      int first = this.atoms.size() - 1;

      for(int i = first; i >= 0; --i) {
         WeightAtom atom = (WeightAtom)this.atoms.get(i);
         if (atom.getMinWeight() <= rnd && rnd <= atom.getMaxWeight()) {
            return i;
         }
      }

      return first;
   }

   public WeightAtom getRndAtom() {
      int rnd = Rnd.get(1, this.maxWeight);
      int first = this.atoms.size() - 1;

      for(int i = first; i >= 0; --i) {
         WeightAtom atom = (WeightAtom)this.atoms.get(i);
         if (atom.getMinWeight() <= rnd && rnd <= atom.getMaxWeight()) {
            return atom;
         }
      }

      return (WeightAtom)this.atoms.get(first);
   }

   public void sortByWeight(String des) throws Exception {
      int minWeight = 1;
      Collections.sort(this.atoms);
      int tmpMaxWeight = minWeight;

      WeightAtom atom;
      for(Iterator var5 = this.atoms.iterator(); var5.hasNext(); tmpMaxWeight = atom.getMaxWeight()) {
         atom = (WeightAtom)var5.next();
         atom.setMinWeight(minWeight);
         atom.setMaxWeight(atom.getMinWeight() + atom.getWeight() - 1);
         minWeight += atom.getWeight();
      }

      this.maxWeight = tmpMaxWeight;
      Collections.sort(this.atoms);
   }

   public void addAtom(WeightAtom atom) {
      this.atoms.add(atom);
   }

   public int getMaxWeight() {
      return this.maxWeight;
   }

   public void setMaxWeight(int maxWeight) {
      this.maxWeight = maxWeight;
   }

   public List getAtoms() {
      return this.atoms;
   }
}
