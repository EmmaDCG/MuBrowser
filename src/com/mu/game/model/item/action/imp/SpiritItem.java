package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;

public class SpiritItem extends ItemAction {
   int rank = 1;

   public SpiritItem(int level) {
      this.rank = level;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int maxCount = player.getSpiritManager().getCanUseCount(item.getModelID());
      useNum = Math.min(useNum, maxCount);
      int result = player.getItemManager().deleteItem(item, useNum, 20).getResult();
      if (result == 1) {
         player.getSpiritManager().useItem(item.getModelID(), useNum);
      }

      return 1;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return player.getSpiritManager().canUseItem(item.getModelID(), this.rank);
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
      if (this.rank < 1) {
         throw new Exception(des + ",战魂所需等阶不正确");
      }
   }
}
