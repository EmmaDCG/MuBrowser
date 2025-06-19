package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;

public class GetTitle extends ItemAction {
   private int titleID;

   public GetTitle(int titleID) {
      this.titleID = titleID;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = 1;
      boolean flag = player.getTitleManager().gainATitle(this.titleID);
      if (flag) {
         player.getItemManager().deleteItem(item, 1, 20);
      } else {
         result = 3054;
      }

      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      return 1;
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
   }
}
