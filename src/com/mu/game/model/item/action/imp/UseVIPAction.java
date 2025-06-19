package com.mu.game.model.item.action.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;

public class UseVIPAction extends ItemAction {
   private int vipId;

   public UseVIPAction(int vipId) {
      this.vipId = vipId;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = player.getItemManager().deleteItem(item, 1, 20).getResult();
      if (result == 1) {
         player.getVIPManager().activationVIP(this.vipId);
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
