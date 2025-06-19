package com.mu.game.model.item.action.imp;

import com.mu.config.VariableConstant;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.service.EvilManager;

public class ReduceEvil extends ItemAction {
   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int actualCount = this.getActualCount(player, item.getModelID(), useNum);
      int result = player.getItemManager().deleteItem(item, actualCount, 20).getResult();
      if (result == 1) {
         player.changeEvil(player.getEvil() - VariableConstant.Pk_Evil_Every * actualCount);
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return player.getEvil() < 1 ? 3045 : 1;
   }

   private int getActualCount(Player player, int modelID, int wantCount) {
      if (wantCount <= 1) {
         return wantCount;
      } else {
         int count = Math.min(wantCount, this.getRemainUseCount(player, modelID));
         int evil = player.getEvil();
         evil = EvilManager.getShowEvil(evil);
         count = Math.min(count, evil);
         count = Math.max(1, count);
         return count;
      }
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
   }
}
