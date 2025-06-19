package com.mu.game.model.equip.rune;

import com.mu.game.model.stats.FinalModify;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RuneSet {
   private int modelID;
   private int count;
   private int effectMultiple = 0;
   private List stats = null;

   public RuneSet(int modelID) {
      this.modelID = modelID;
      this.stats = new ArrayList();
      this.stats.addAll(this.getModel().getStats());
   }

   public void addRune(int addValue) {
      this.count += Math.max(0, addValue);
      this.calStats();
   }

   public void removeRune(int reduceValue) {
      this.count -= reduceValue;
      this.count = Math.max(this.count, 0);
      this.calStats();
   }

   public RuneSetModel getModel() {
      return RuneSetModel.getModel(this.modelID);
   }

   public void calStats() {
      int tmpEffectMul = this.effectMultiple;
      int effectNum = this.count / this.getModel().getEffectCount();
      if (tmpEffectMul != effectNum) {
         this.stats.clear();
         Iterator var4 = this.getModel().getStats().iterator();

         while(var4.hasNext()) {
            FinalModify fm = (FinalModify)var4.next();
            FinalModify cfm = new FinalModify(fm.getStat(), fm.getValue() * effectNum, fm.getPriority());
            this.stats.add(cfm);
         }

         this.effectMultiple = effectNum;
      }
   }

   public boolean isEffect() {
      return this.effectMultiple > 0;
   }

   public void destroy() {
      this.stats.clear();
      this.stats = null;
   }

   public int getModelID() {
      return this.modelID;
   }

   public int getCount() {
      return this.count;
   }

   public int getEffectMultiple() {
      return this.effectMultiple;
   }

   public List getStats() {
      return this.stats;
   }
}
