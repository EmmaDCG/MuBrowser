package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.composite.CompositeItem;
import java.util.HashMap;

public class CompositePopup extends Popup {
   private HashMap chooseMaterial = null;
   private HashMap chooseItem = null;
   private int comID;

   public CompositePopup(int id, int comID, HashMap chooseMaterial, HashMap chooseItem) {
      super(id);
      this.chooseMaterial = chooseMaterial;
      this.chooseItem = chooseItem;
      this.comID = comID;
   }

   public String getTitle() {
      return MessageText.getText(4026);
   }

   public String getContent() {
      return MessageText.getText(4027);
   }

   public void dealLeftClick(Player player) {
      CompositeItem.doComposite(player, this.comID, this.chooseMaterial, this.chooseItem, true);
   }

   public void dealRightClick(Player player) {
      CompositeItem.sendToClient(player, this.comID, 4025, -1);
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 28;
   }
}
