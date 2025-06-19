package com.mu.game.model.equip.compositenew.condition.imp;

import com.mu.game.model.equip.compositenew.condition.MaterialCondition;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemModel;
import java.util.HashSet;
import java.util.Iterator;

public class ProfessionCondition extends MaterialCondition {
   private HashSet professionSet = null;

   public ProfessionCondition(int conID, HashSet professionSet) {
      super(conID);
      this.professionSet = professionSet;
   }

   public boolean suit(Item item) {
      ItemModel model = item.getModel();
      if (model.getProfession().size() < 1) {
         return true;
      } else {
         Iterator var4 = model.getProfession().iterator();

         while(var4.hasNext()) {
            Integer proID = (Integer)var4.next();
            if (this.professionSet.contains(proID)) {
               return true;
            }
         }

         return false;
      }
   }
}
