package com.mu.game.model.item.other.expriedHandel.imp;

import com.mu.game.model.item.Item;
import com.mu.game.model.item.other.expriedHandel.ExpiredHandel;
import com.mu.game.model.unit.player.Player;

public class ExpiredHandNone extends ExpiredHandel {
   public ExpiredHandNone(int modelID, int type) {
      super(modelID, type);
   }

   public boolean doubleClickHandel(Player player, Item item) {
      return false;
   }

   public void instantCheckHandel(Player player, Item item, boolean firstEnterMap) {
   }

   public void initCheck() throws Exception {
   }
}
