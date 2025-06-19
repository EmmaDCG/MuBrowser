package com.mu.game.model.item.action.imp;

import com.mu.game.model.fo.FunctionOpenManager;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.action.ItemAction;
import com.mu.game.model.item.model.ItemModel;
import com.mu.game.model.unit.player.Player;
import com.mu.io.game.packet.imp.sys.OpenPanel;

public class OpenFunctionPanel extends ItemAction {
   private int functionID;
   private int bigPanelID;
   private int smalPanelID;

   public OpenFunctionPanel(int functionID, int bigPanelID, int smalPanelID) {
      this.functionID = functionID;
      this.bigPanelID = bigPanelID;
      this.smalPanelID = smalPanelID;
   }

   protected int doAction(Player player, Item item, int useNum, boolean definite, int wantedSlot) {
      int result = 1;
      OpenPanel.open(player, this.bigPanelID, this.smalPanelID);
      return result;
   }

   public int privyCondition(Player player, Item item, int useNum, boolean definite) {
      if (this.functionID == -1) {
         return 1;
      } else {
         return !FunctionOpenManager.isOpen(player, this.functionID) ? 1035 : 1;
      }
   }

   public void useWhenObtaining(Player player, ItemModel model, int count, boolean isBind) {
   }

   public void initCheck(String des) throws Exception {
   }
}
