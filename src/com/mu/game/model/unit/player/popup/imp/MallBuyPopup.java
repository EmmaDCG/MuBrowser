package com.mu.game.model.unit.player.popup.imp;

import com.mu.config.MessageText;
import com.mu.game.model.item.Item;
import com.mu.game.model.item.model.ItemColor;
import com.mu.game.model.mall.MallItemData;
import com.mu.game.model.unit.player.Player;
import com.mu.game.model.unit.player.popup.Popup;
import com.mu.io.game.packet.imp.mall.MallShowItem;

public class MallBuyPopup extends Popup {
   private MallItemData data;
   private int count;

   public MallBuyPopup(int id, MallItemData data, int count) {
      super(id);
      this.data = data;
      this.count = count;
   }

   public String getTitle() {
      return "";
   }

   public String getContent() {
      Item item = this.data.getItem();
      return MessageText.getText(23010).replace("%s%", "#F{c=" + ItemColor.find(item.getQuality()).getColor() + "}" + item.getName() + "#F");
   }

   public void dealLeftClick(Player player) {
      MallShowItem.show(player, this.data.getItem().getID(), this.count);
   }

   public void dealRightClick(Player player) {
   }

   public boolean isShowAgain(Player player) {
      return false;
   }

   public int getType() {
      return 24;
   }
}
