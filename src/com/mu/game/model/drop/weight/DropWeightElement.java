package com.mu.game.model.drop.weight;

import com.mu.game.model.drop.model.MDropAtom;
import com.mu.utils.Rnd;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class DropWeightElement {
   private long maxWeight;
   private List atoms = new ArrayList();

   public boolean checkWeight() {
      return this.atoms.size() <= 0 || this.maxWeight >= 1L;
   }

   public int getRndIndex() {
      long rnd = Rnd.get(1L, this.maxWeight);
      int first = this.atoms.size() - 1;

      for(int i = first; i >= 0; --i) {
         MDropAtom atom = (MDropAtom)this.atoms.get(i);
         if (atom.getMinWeight() <= rnd && rnd <= atom.getMaxWeight()) {
            return i;
         }
      }

      return first;
   }

   public MDropAtom getRndAtom() {
      long rnd = Rnd.get(1L, this.maxWeight);
      int first = this.atoms.size() - 1;

      for(int i = first; i >= 0; --i) {
         MDropAtom atom = (MDropAtom)this.atoms.get(i);
         if (atom.getMinWeight() <= rnd && rnd <= atom.getMaxWeight()) {
            return atom;
         }
      }

      return (MDropAtom)this.atoms.get(first);
   }

   public void sortByWeight(String des) {
      long minWeight = 1L;
      Collections.sort(this.atoms);
      long tmpMaxWeight = minWeight;

      MDropAtom atom;
      for(Iterator var7 = this.atoms.iterator(); var7.hasNext(); tmpMaxWeight = atom.getMaxWeight()) {
         atom = (MDropAtom)var7.next();
         atom.setMinWeight(minWeight);
         atom.setMaxWeight(atom.getMinWeight() + atom.getWeight() - 1L);
         minWeight += atom.getWeight();
      }

      this.maxWeight = tmpMaxWeight;
      Collections.sort(this.atoms);
   }

   public void addAtom(MDropAtom atom) {
      this.atoms.add(atom);
   }

   public long getMaxWeight() {
      return this.maxWeight;
   }

   public void setMaxWeight(int maxWeight) {
      this.maxWeight = (long)maxWeight;
   }

   public List getAtoms() {
      return this.atoms;
   }
}
