package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.item.container.BackpackExpandData;
import com.mu.game.model.item.container.imp.Storage;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.storage.BackpackCount;
import com.mu.io.game.packet.imp.sys.SystemMessage;

public class ExpandBackpackPopup extends Popup {
   private int addedGrid;
   private long[] rewardAndNeed;

   public ExpandBackpackPopup(int id, int addedGrid, long[] rewardAndNeed) {
      super(id);
      this.addedGrid = addedGrid;
      this.rewardAndNeed = rewardAndNeed;
   }

   public String getTitle() {
      return MessageText.getText(4004);
   }

   public String getContent() {
      String s = MessageText.getText(4003);
      s = s.replaceAll("%s%", String.valueOf(this.rewardAndNeed[1]));
      s = s.replaceAll("%d%", String.valueOf(this.addedGrid));
      s = s.replaceAll("%f%", String.valueOf(this.rewardAndNeed[0]));
      return s;
   }

   public void dealLeftClick(Player player) {
      int targetGrid = player.getBackpack().getLimit() + this.addedGrid;
      openGrid(player, targetGrid);
   }

   public static void openGrid(Player player, int targetGrid) {
      Storage backpack = player.getBackpack();
      int result = BackpackExpandData.addPage(player, targetGrid);
      if (result == 1) {
         result = backpack.expansion(player, targetGrid - backpack.getLimit());
      }

      BackpackCount.sendToClient(player);
      if (result != 1) {
         SystemMessage.writeMessage(player, result);
      }

   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 2;
   }
}
